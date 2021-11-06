package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Variable declaration
 *
 * @author gl49
 * @date 19/01/2021
 */

public abstract class AbstractDeclParam extends Tree{


    /**
     * Implements non-terminal "decl_param" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to
     *      the synthetized parameters
     */
	
    protected abstract void verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;
    
    /**
     * Implements non-terminal "decl_param" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @return the type of the parameter
     */
    
    protected abstract Type verifyType(DecacCompiler compiler) throws ContextualError;

	protected abstract void codeGenParam(DecacCompiler compiler);

}
