package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
/**
 * New
 *
 * @author gl49
 * @date 19/01/2021
 */
public class New extends AbstractExpr{

    private AbstractIdentifier type;
    
    public New(AbstractIdentifier type) {
        Validate.notNull(type);
    	this.type = type;
    }
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
//		return null;
		Type newType = type.verifyType(compiler);
        type.setType(newType);
        return newType.asClassType("Instancied type must be a class ! (3.42)", type.getLocation());
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.print("new ");
        type.decompile(s);
        s.print("()");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
        type.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        type.iterChildren(f);
	}
	protected void codeGenInst(DecacCompiler compiler) {
		//if (type.getType().isClass()) {
			//compiler.addLabel(new Label("init." + this.type.getName().getName()) );
		//}
		GPRegister registerDest = Register.getR(GestionMemoire.nouveauRegistre(compiler));
        ClassDefinition classDef = type.getClassDefinition();
        DAddr addressClass = new RegisterOffset(classDef.getAdrClass(), Register.GB);
        // NEW size, registerDest
        compiler.addInstruction(
                new NEW(classDef.getNumberOfFields() + 1, registerDest));

        // On teste "the stack overflow"
        if (!compiler.getCompilerOptions().getNoCheck()) {
            // BOV label
            compiler.addInstruction(new BOV(new Label("HeapOverFlowException")));
        }
        
        //On Stocke le pointeur dans la table de méthodes dans le tas
        // LEA AdressClass, R0
        compiler.addInstruction(new LEA(addressClass, Register.R0));
        // STORE R0, 0(registerDest)
        compiler.addInstruction(
                new STORE(GPRegister.R0, new RegisterOffset(0, registerDest)));

        //On appelle la méthode d'initialisation
        // PUSH registerDest
        compiler.addInstruction(new PUSH(registerDest));
        // BSR init
        compiler.addInstruction(new BSR(new Label("init." + this.type.getName().getName())));
        
        // POP registerDest
        compiler.addInstruction(new POP(registerDest));
		/*GPRegister registerDest = Register.getR(GestionMemoire.nouveauRegistre(compiler));
		compiler.addInstruction(new NEW(type.getClassDefinition().getNumberOfFields()+1,Register.R1));
        compiler.addInstruction(new BOV(new Label("HeapOverFlowException")));
        compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(),Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,Register.R1)));
        compiler.addInstruction(new TSTO(3));
        compiler.addInstruction(new BOV(new Label("StackOverflowException")));
        compiler.addInstruction(new ADDSP(1));
        compiler.addInstruction(new STORE(Register.R1,new RegisterOffset(0,Register.SP)));
        compiler.addInstruction(new BSR(new Label("init."+type.getName().toString())));
        compiler.addInstruction(new SUBSP(1));
        compiler.addInstruction(new LOAD(Register.R1,registerDest));*/
		
		
		
	}


}
