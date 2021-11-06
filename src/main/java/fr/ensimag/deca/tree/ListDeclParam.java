package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations ((int x, float y)).
 * 
 * @author gl49
 * @date 18/01/2021
 */
public class ListDeclParam extends TreeList<AbstractDeclParam>{

	/**
     * Implements non-terminal "list_decl_param" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */  

	 public EnvironmentExp verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
//	        return null;
		 EnvironmentExp envExpParam = new EnvironmentExp(localEnv);
	     for (AbstractDeclParam param : getList()) {
	          param.verifyDeclParam(compiler, envExpParam);
	     }
	     return envExpParam;
	    }
	 
	 public Signature createSignature(DecacCompiler compiler) throws ContextualError {
//	        return null;
		 Signature signature = new Signature();
	        for (AbstractDeclParam param : getList())
	            signature.add(param.verifyType(compiler));
	        return signature;
	    }
	 @Override
	 public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		 for (AbstractDeclParam param : getList()) {
	            param.decompile(s);
	            if (!(getList().indexOf(param) == getList().size() - 1)) {
	                s.print(", ");
	            }
	        }
	 }

}
