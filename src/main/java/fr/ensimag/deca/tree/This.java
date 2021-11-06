package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * This
 *
 * @author gl49
 * @date 19/01/2021
 */

public class This extends AbstractExpr{

    private boolean implicit;
    
    public This(boolean implicit) {
        Validate.notNull(implicit);
        this.implicit = implicit;
    }
    
    @Override
    boolean isImplicit() {
        return this.implicit;
    }
    
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
//		return null;
		if (currentClass == null) {
            throw new ContextualError("Cannot use 'this' in main ! (3.43)", getLocation());
        }
        ClassType thisType = currentClass.getType();
        this.setType(thisType);
        return thisType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("this");
		s.print(".");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		// Nothing to do
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		// Nothing to do
	}
	
	@Override 
    public void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB),
        		Register.getR(GestionMemoire.nouveauRegistre(compiler))));
	}

}
