package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.Label;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

public class MethodCall extends AbstractExpr{
	
	private AbstractExpr       object;
    private AbstractIdentifier methodName;
    private ListExpr           params;
    private int                stackNeeds;

    public MethodCall(AbstractExpr object, AbstractIdentifier methodName, ListExpr params) {
    	Validate.notNull(object);
        Validate.notNull(methodName);
        Validate.notNull(params);
        this.object = object;
        this.methodName = methodName;
        this.params = params;
		this.stackNeeds = 1 + params.size();
    }

//    public int getstackNeed() {
//    	return 1 + params.size();
//    }
    
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// To do
//		return null;
		Type exprType = object.verifyExpr(compiler, localEnv, currentClass);
        ClassType classType = exprType.asClassType("Object must be a class type ! (3.71)", object.getLocation());
        EnvironmentExp envExp2 = classType.getDefinition().getMembers();
        Definition def = envExp2.get(methodName.getName());
        if (def == null) {
            throw new ContextualError("Method is not defined (3.71)", methodName.getLocation());
        }
        MethodDefinition methodDef = def.asMethodDefinition("Identifier is not a method identifier ! (3.72)", methodName.getLocation());
        methodName.setDefinition(methodDef);
        Type methodReturn = methodDef.getType();
        Signature signature = methodDef.getSignature();
        Type sigType;
        if (signature.size() != params.size()) {
            throw new ContextualError("Argument number doesn't match with method signature ! (3.72)",
                    methodName.getLocation());
        }
        for (int i = 0; i < params.size(); i++) {
            sigType = signature.paramNumber(i);
            AbstractExpr newArg = params.getList().get(i).verifyRValue(compiler, localEnv, currentClass, sigType);
            params.set(i,  newArg);
        }
        this.setType(methodReturn);
        return methodReturn;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		object.decompile(s);
        if (!(object instanceof This) || !object.isImplicit()) {
            s.print(".");
        }
        methodName.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(")");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		object.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		object.iterChildren(f);
        methodName.iterChildren(f);
        params.iterChildren(f);
	}
	
/*	@Override
	protected void codeGenInst(DecacCompiler compiler) {
        // réserver l'espace de pile pour les paramètres
        compiler.addInstruction(new ADDSP(stackNeeds));
        // Object
        object.codeGenInst(compiler);
        compiler.addInstruction(new STORE(Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false)), 
        		new RegisterOffset(0, Register.SP)));
        GestionMemoire.decrPosDernierRegistreUtilise();
        // parametres
        int i = -1;
        for (AbstractExpr e : params.getList()) {
            e.codeGenInst(compiler);
            compiler.addInstruction(new STORE(Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false)), 
            		new RegisterOffset(i, Register.SP)));
            i--;
            GestionMemoire.decrPosDernierRegistreUtilise();
        }
        // load method
        
        // commentaire
        //compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.getR(GestionMemoire.nouveauRegistre(compiler))));
        compiler.addInstruction(new CMP(new NullOperand(), Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false))));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false))), 
        		Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false))));
        compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex()+1, 
        		Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false)))));

        compiler.addInstruction(new SUBSP(stackNeeds));//
        
        
        GPRegister register = Register.getR(GestionMemoire.nouveauRegistre(compiler));
        compiler.addInstruction(new LOAD(new RegisterOffset(0,Register.SP),register));
        compiler.addInstruction(new LOAD(new RegisterOffset(0,register),register));
        //System.out.println(methodName.getMethodDefinition().getIndex());
        compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex(),register)));
        compiler.addInstruction(new LOAD(Register.R0,register));
        compiler.addInstruction(new SUBSP(1+params.size()));
        //compiler.getRegManager().setTableRegistre(table);
        
    }*/
	@Override
    public void codeGenInst(DecacCompiler compiler){
		if(!compiler.getCompilerOptions().getNoCheck()){
			
			compiler.addInstruction(new TSTO(3+params.size()));
	        compiler.addInstruction(new BOV(new Label("StackOverflowException")));
	        compiler.addInstruction(new ADDSP(1+params.size()));
		}
        if(!params.isEmpty()){
            for(int j=params.size()-1; j>-1; j--){
            	params.getList().get(j).codeGenInst(compiler);
        		GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));

                compiler.addInstruction(new PUSH(register));
            }
        }
        object.codeGenInst(compiler); 
		GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));

        compiler.addInstruction(new PUSH(register));
        compiler.addInstruction(new LOAD(new RegisterOffset(0,register),register));
        //compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex(),register)));

        compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex() + 1,register)));
        compiler.addInstruction(new LOAD(Register.R0,register));
        compiler.addInstruction(new SUBSP(1+params.size()));
        }
	@Override
	public void codeGenPrint(DecacCompiler compiler) {
		this.codeGenInst(compiler);
		
		compiler.addInstruction(new LOAD(Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true)), Register.getR(1)));
    	//compiler.addInstruction(new LOAD(new RegisterOffset(GestionMemoire.getVariablePosition(), Register.GB), Register.R1));

        if (this.getType().isFloat()) {
            if (super.isHexaDecimal())
                compiler.addInstruction(new WFLOATX());
            else
                compiler.addInstruction(new WFLOAT());
        }
        
        else if (this.getType().isInt())
            compiler.addInstruction(new WINT());
        
        else if (this.getType().isBoolean())
            compiler.addInstruction(new WINT());
        
		
	}
	
}


