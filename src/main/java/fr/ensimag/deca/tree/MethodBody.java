package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * @author gl49
 * @date 19/01/2021
 */
public class MethodBody extends AbstractMethodBody{

	 private ListDeclVar declVariables;
	 private ListInst insts;

	    public MethodBody(ListDeclVar declVariables, ListInst insts) {
	        Validate.notNull(declVariables);
	        Validate.notNull(insts);
	        this.declVariables = declVariables;
	        this.insts = insts;
	    }
	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		declVariables.decompile(s);
        insts.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		declVariables.iterChildren(f);
        insts.iterChildren(f);
	}
	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExpParam, ClassDefinition classDef,
			Type methodType) throws ContextualError{
		// TODO Auto-generated method stub
		declVariables.verifyListDeclVariable(compiler, envExpParam, classDef);
        insts.verifyListInst(compiler, envExpParam, classDef, methodType);
	}
	@Override
	public void codeGenBody(DecacCompiler compiler) {
	    this.declVariables.codeGenListVar(compiler);
	    this.insts.codeGenListInst(compiler);
	}

}
