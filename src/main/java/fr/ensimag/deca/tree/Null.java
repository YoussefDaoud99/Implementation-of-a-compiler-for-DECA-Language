package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Null
 *
 * @author gl49
 * @date 19/01/2021
 */
public class Null extends AbstractExpr {

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		Type nullType = new NullType((compiler.getSymbolTable().create("null")));
		return nullType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("null");

	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// Nothing to do

	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// Nothing to do
	}
	@Override
	protected void codeGenInst(DecacCompiler compiler) {
	   compiler.addInstruction(new LOAD(new NullOperand(), 
			   Register.getR(GestionMemoire.nouveauRegistre(compiler))));
	}
}
