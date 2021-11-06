package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class GestionExceptions {
	
	
	
	public static void addExceptions(DecacCompiler compiler){
        
		compiler.addLabel(new Label("DivisionByZeroException"));
        compiler.addInstruction(new WSTR("DivisionByZeroException"));
        compiler.addInstruction(new ERROR());
        
        if (!compiler.getCompilerOptions().getNoCheck()) {
            compiler.addLabel(new Label("IOException"));
            compiler.addInstruction(new WSTR("IOException"));
            compiler.addInstruction(new ERROR());
        	
        	compiler.addLabel(new Label("StackOverFlowException"));
            compiler.addInstruction(new WSTR("StackOverflowException"));
            compiler.addInstruction(new ERROR());

            compiler.addLabel(new Label("OverFlowException"));
            compiler.addInstruction(new WSTR("OverFlowException"));
            compiler.addInstruction(new ERROR());
        }
        compiler.addLabel(new Label("HeapOverFlowException"));
        compiler.addInstruction(new WSTR("HeapOverFlowException"));
        compiler.addInstruction(new ERROR());
    }
}
