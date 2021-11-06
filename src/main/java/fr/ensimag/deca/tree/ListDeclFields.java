package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;

/**
 * List of declarations of fields (e.g. private int x;).
 * 
 * @author gl49
 * @date 18/01/2021
 */

public class ListDeclFields extends TreeList<AbstractDeclField>{
    public void decompile(IndentPrintStream s) {
    	//TODO
    	for (AbstractDeclField field : getList()) {
            field.decompile(s);
            s.println();
        }
    }

    /**
     * Implements non-terminal "list_decl_fields" of [SyntaxeContextuelle] in pass 2
     * @param compiler contains the "env_types" attribute
     * @param superDef 
     * 			corresponds to the ClassDefinition of the super class of our class.
     * @param classDef 
     *          corresponds to the ClassDefinition of our class.
     */  
    
	public void verifyListDeclField(DecacCompiler compiler, ClassDefinition superDef
			, ClassDefinition classDef) throws ContextualError{
		// TODO Auto-generated method stub
		 for (AbstractDeclField field : getList()) {
	            field.verifyDeclField(compiler, superDef, classDef);
	        }
	}

	/**
     * Implements non-terminal "list_decl_fields" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param envvExp
     *  its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param classDef 
     *          corresponds to the ClassDefinition of our class.
     */  
	public void verifyListFieldInit(DecacCompiler compiler, EnvironmentExp envExp,
			ClassDefinition classDef) throws ContextualError{
		// TODO Auto-generated method stub
		for (AbstractDeclField field : getList()) {
            field.verifyFieldInit(compiler, envExp, classDef);
        }
	}

	public void codeGenListFields(DecacCompiler compiler, DeclClass laClasse) {
		//Label lb = new Label("init." + laClasse.getClassName().getName().getName());
		//compiler.addLabel(lb);
		
		// if our class has a superclass
		if (!laClasse.getSuperClass().getName().getName().equals("Object")) {
			
			// LOAD -2(LB), R0
            // PUSH R0
			// add laClass to the stack
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
            compiler.addInstruction(new PUSH(Register.R0));

            // initialization of parent
            compiler.addInstruction(new BSR(new Label("init." + laClasse.getSuperClass().getName())));
            
            //compiler.addInstruction(new SUBSP(1));
        }

		for (AbstractDeclField i : getList()) {
            i.codeGenField(compiler);
        }
        compiler.addInstruction(new RTS());		
	}

}
