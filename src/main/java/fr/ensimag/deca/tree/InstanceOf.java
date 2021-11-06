package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class InstanceOf extends AbstractExpr {
	
	private AbstractExpr expression;
	private AbstractIdentifier type;
	private ClassType className;
	
	public InstanceOf(AbstractExpr expression, AbstractIdentifier type) {
		Validate.notNull(expression);
        Validate.notNull(type);
		this.expression = expression;
		this.type = type;
	}
	
	public AbstractExpr getExpression() {
		return expression;
	}
	
	public AbstractIdentifier getType1() {
		return type;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
//		return null;
		Type expressionType = expression.verifyExpr(compiler, localEnv, currentClass);
		Type typeType = type.verifyType(compiler);
		className = typeType.asClassType("Instanceof is used only on classes ! (3.42)", type.getLocation());
		if ( ((expressionType == null ) || (expressionType.isClass())) && typeType.isClass() )
		{
			Type booleanType = new BooleanType(compiler.getSymbolTable().create("boolean"));
			this.setType(booleanType);
	        return booleanType;
		}
		else
		{
			throw new ContextualError ( "InstanceOf is not correctly used ! (3.40)",this.getLocation());
		}
	}

	@Override
	public void decompile(IndentPrintStream s) {
		getExpression().decompile();
		s.print("instanceof");
		getType1().decompile();
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		expression.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		expression.iter(f);
        type.iter(f);
	}
    
	@Override
    protected void codeGenInst(DecacCompiler compiler) {
        int i=compiler.getGestionLabel().getNbrInstanceOf();
        compiler.getGestionLabel().incrNbrInstanceOf();
        Label succes = new Label("instanceof.success." + i);
        Label fin = new Label("instanceof.end." + i);
       
        
        expression.codeGenInst(compiler);
        GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));        


        Label retry = new Label("ancestry.loop." + i);
        Label end = new Label("ancestry.end." + i);
        compiler.addInstruction(new CMP(new NullOperand(), register));
        compiler.addInstruction(new BEQ(end));
        compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R0));
        compiler.addLabel(retry);
        compiler.addInstruction(new CMP(Register.R0, register));

        compiler.addInstruction(new BEQ(succes));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, register), register));
        compiler.addInstruction(new BEQ(end));
        compiler.addInstruction(new BRA(retry));
        compiler.addLabel(end);
        
        compiler.addInstruction(new LOAD(0, Register.getR(GestionMemoire.nouveauRegistre(compiler))));
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(succes);
        compiler.addInstruction(new LOAD(1, Register.getR(GestionMemoire.nouveauRegistre(compiler))));
        compiler.addLabel(fin);
    }
	
}



