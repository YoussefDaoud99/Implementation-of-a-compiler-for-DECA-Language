package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations of methods (e.g. public void getName(){Method Body..}).
 * 
 * @author gl49
 * @date 18/01/2021
 */

public class ListDeclMethods extends TreeList<AbstractDeclMethod> {
    public void decompile(IndentPrintStream s) {
    	//Todo
    	for (AbstractDeclMethod method : getList()) {
            method.decompile(s);
            s.println();
        }
    }

    /**
     * Implements non-terminal "list_decl_methods" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains the "env_types" attribute
     * @param superDef 
     * 			corresponds to the ClassDefinition of the super class of our class.
     * @param classDef 
     *          corresponds to the ClassDefinition of our class.
     * @throws ContextualError 
     */  
    
	public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition superDef, ClassDefinition classDef) throws ContextualError {
		// TODO Auto-generated method stub
		for (AbstractDeclMethod method : getList()) {
            method.verifyDeclMethod(compiler, superDef, classDef);
        }
	}
	
	/**
     * Implements non-terminal "list_decl_methods" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param envvExp
     *  its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param classDef 
     *          corresponds to the ClassDefinition of our class.
	 * @throws ContextualError 
     */ 

	public void verifyListMethodBody(DecacCompiler compiler, EnvironmentExp envExp, ClassDefinition classDef) throws ContextualError {
		// TODO Auto-generated method stub
		for (AbstractDeclMethod method : getList()) {

            method.verifyMethodBody(compiler, envExp, classDef);
        }
	}

	

}
