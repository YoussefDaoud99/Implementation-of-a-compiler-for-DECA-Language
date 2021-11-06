package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl49
 * @date 01/01/2021
 */
public abstract class AbstractLValue extends AbstractExpr {
	public abstract ExpDefinition getExpDefinition();
	protected DAddr getAdrSel() {
		return null;
	}
	protected boolean isSelection() {
		return false;
	}
}
