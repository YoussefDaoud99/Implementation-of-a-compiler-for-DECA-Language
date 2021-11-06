package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl49
 * @date 01/01/2021
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	AbstractExpr exprRight = expression.verifyRValue(compiler, localEnv, currentClass, t);
        this.setExpression(exprRight);
    }


    @Override
    public void decompile(IndentPrintStream s) {
    	s.print(" = ");
        this.getExpression().decompile(s);
    	//throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
    
    @Override
    public void codeGenInst(DecacCompiler compiler, DeclVar var) {
    	
        this.getExpression().codeGenInst(compiler);
        //GestionMemoire.incrementeVarPosition();
        //System.out.println("here");
        GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true));
        compiler.addInstruction(new STORE(register, var.getVarName().getExpDefinition().getOperand()));
        //compiler.addInstruction(new STORE(register, var.getVarName().getVariableDefinition().getOperand()));
        //compiler.addInstruction(new STORE(register, new RegisterOffset(GestionMemoire.getVariablePosition(), Register.GB)));
    }

	@Override
	protected void codeGenDefaultInit(DecacCompiler compiler, Type type) {
		// TODO Auto-generated method stub
	}
	
	@Override
	protected void codeGenFieldInit(DecacCompiler compiler, Type type) {	
		expression.codeGenInst(compiler);
		//GestionMemoire.incrementeVarPosition();
		GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true));
	    compiler.addInstruction(new LOAD(register, Register.R0));

	    //GestionMemoire.decrPosDernierRegistreUtilise();


	}
    
    

}
