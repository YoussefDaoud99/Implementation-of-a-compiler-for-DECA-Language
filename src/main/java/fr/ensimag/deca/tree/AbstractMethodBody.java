package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public abstract class AbstractMethodBody extends Tree{

	protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExpParam,
			ClassDefinition classDef, Type methodType) throws ContextualError;

	protected boolean isAsm() {
        return false;
    }
	public abstract void codeGenBody(DecacCompiler compiler);

}
