package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Method declaration
 *
 * @author gl49
 * @date 18/01/2021
 */

public abstract class AbstractDeclMethod extends Tree{

	/**
     * Implements non-terminal "decl_method" of [SyntaxeContextuelle] in pass 2 & 3
     * @param compiler contains "env_types" attribute
     * @param envExp 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param superDef
     * 			corresponds to the "class" attribute of the super class of our class.
     * @param classDef
     *          corresponds to the "class" attribute (null in the main bloc).
     */ 

	protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition superDef,
			ClassDefinition classDef) throws ContextualError;

	protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp,
			ClassDefinition classDef) throws ContextualError;

    protected abstract void codeGenMethod(DecacCompiler compiler);
	
}
