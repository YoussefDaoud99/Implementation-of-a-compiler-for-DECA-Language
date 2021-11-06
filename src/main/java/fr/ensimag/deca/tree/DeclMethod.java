package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionLabel;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;

/**
 * @author gl49
 * @date 18/01/2021
 */
public class DeclMethod extends AbstractDeclMethod{
	
    final private AbstractIdentifier methodName;
    final private ListDeclParam param;
    final private AbstractMethodBody methodBody;
	final private AbstractIdentifier returnType;
	
	public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName, ListDeclParam param, AbstractMethodBody
            methodBody) {
        Validate.notNull(returnType);
        Validate.notNull(methodName);
        this.returnType = returnType;
        this.methodName = methodName;
        this.param = param;
        this.methodBody = methodBody;
    }
	@Override
	protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition superDef, ClassDefinition classDef)
			throws ContextualError {
		// TODO Auto-generated method stub
        Type methodType = returnType.verifyType(compiler);
        Signature sigClass = param.createSignature(compiler);
        if (classDef.getNumberOfMethods() == 0) {
            classDef.setNumberOfMethods(superDef.getNumberOfMethods());
        }
        int indice = classDef.getNumberOfMethods()+1;
        if (superDef.getMembers().getFilsEnvironment().containsKey(methodName.getName())) {
        	if (!superDef.getMembers().getFilsEnvironment().get(methodName.getName()).isMethod()) {
        		throw new ContextualError("Method name is used for a field !", this.getLocation());
        	}
            MethodDefinition superMethodDef = (MethodDefinition) superDef.getMembers().getFilsEnvironment().get(methodName.getName());  
            indice = superMethodDef.getIndex();
            if (!superMethodDef.getSignature().sameSignature(sigClass))
                throw new ContextualError("Redefined method must have the same signature of the inherited method ! (2.7)", this.getLocation());
            if (methodType.isClass()) {
                ClassType returnClass = (ClassType) methodType;
                ClassType superClassType = superMethodDef.getType().asClassType("Redefined method type must be a subtype of the inherited method type ! (2.7)", this.getLocation());
                if (!returnClass.isSubClassOf(superClassType)) {
                    throw new ContextualError("Redefined method type must be a subtype of the inherited method type ! (2.7)", this.getLocation());
                }
            } else {
                if (!methodType.sameType(superMethodDef.getType())) {
                    throw new ContextualError("Redefined method type must be a subtype of the inherited method type ! (2.7)", this.getLocation());
                }
            }
        } else {
            classDef.incNumberOfMethods();
        }
        MethodDefinition methodDef = new MethodDefinition(methodType, this.getLocation(), sigClass,
                indice);
        try {
            classDef.getMembers().declare(methodName.getName(),methodDef);
        } catch (EnvironmentExp.DoubleDefException e){
            throw new ContextualError("Identifier already used ! (2.6)", methodName.getLocation());
        }
        // decorating the tree
        methodName.setDefinition(methodDef);
        returnType.setType(methodType);
	}

	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, ClassDefinition classDef)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type methodType = returnType.getType();
        EnvironmentExp envExpParam = param.verifyListDeclParam(compiler, envExp);
        methodBody.verifyMethodBody(compiler, envExpParam, classDef, methodType);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		returnType.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        param.decompile(s);
        s.print(") ");
        if (methodBody.isAsm()) {
            s.println("asm (");
        } else {
            s.println("{");
        }
        s.indent();
        methodBody.decompile(s);
        s.unindent();
        if (methodBody.isAsm()) {
            s.println(");");
        } else {
            s.println("}");
        }
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		returnType.iterChildren(f);
        methodName.iterChildren(f);
        param.iterChildren(f);
        methodBody.iterChildren(f);	
	}
	@Override
	protected void codeGenMethod(DecacCompiler compiler) {
        Label lbl = new Label(this.getMethodName().getMethodDefinition().getLabel().toString().replace("code", "fin")); 
        GestionLabel.setReturnLabel(lbl);
		compiler.addLabel(this.getMethodName().getMethodDefinition().getLabel());
        // Stock registers
		// PUSH register
        for (int i = 2; i <= GestionMemoire.getMaxRegistersUtilise() ; i++)
            compiler.addInstruction(new PUSH(Register.getR(i)));

        // Parameters and method body
        GestionMemoire.setParamPos(-2);;
        for (AbstractDeclParam i : param.getList()) {
            i.codeGenParam(compiler);
        }
        methodBody.codeGenBody(compiler);
        compiler.addInstruction(new BRA(lbl));
        compiler.addLabel(lbl);
        GestionLabel.setReturnLabel(lbl);

        // restore registers
        //POP register
        for (int i=GestionMemoire.getMaxRegistersUtilise() ; i>=2 ; i--)
            compiler.addInstruction(new POP(Register.getR(i)));
        compiler.addInstruction(new RTS());
    }
		
	
	public AbstractIdentifier getMethodName() {
		return methodName;
	}


}
