package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionLabel;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Return
 * @author gl49
 * @date 19/01/2021
 */

public class Return extends AbstractInst{

    private AbstractExpr returnExpr;
    
    public Return(AbstractExpr returnedExpr) {
        Validate.notNull(returnedExpr);
        this.returnExpr = returnedExpr;
    }
    
    public void setReturnExpr(AbstractExpr returnedExpr) {
        Validate.notNull(returnedExpr);
        this.returnExpr = returnedExpr;
    }
    
	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		// TODO Auto-generated method stub
		AbstractExpr verifiedExpr = this.returnExpr.verifyRValue(compiler, localEnv, currentClass, returnType);
		if (verifiedExpr.getType().isVoid()) {
            throw new ContextualError("Void type cannot be returned ! (3.24)", getLocation());
        }
        this.setReturnExpr(verifiedExpr);
	}

	
	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		//compiler.getGestionLabel().incrNbrReturn();
        returnExpr.codeGenInst(compiler);
        assert GestionLabel.getReturnLabel() != null;
        compiler.addInstruction(new LOAD(Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true)), Register.R0));
        //compiler.addInstruction(new BRA(GestionLabel.getReturnLabel()));
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.print("return ");
        returnExpr.decompile(s);
        s.print(";");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
        returnExpr.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        returnExpr.iterChildren(f);
	}

}
