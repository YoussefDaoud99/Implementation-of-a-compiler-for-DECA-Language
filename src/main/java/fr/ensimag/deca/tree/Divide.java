package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl49
 * @date 01/01/2021
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }


	@Override
	void calcOperationArith(DecacCompiler compiler, DVal op1, GPRegister op2) {
		
		if (this.getType().isFloat()) {
			if (!compiler.getCompilerOptions().getNoCheck()) {
				compiler.addInstruction(new LOAD(op1, Register.getR(GestionMemoire.nouveauRegistre(compiler))));
				compiler.addInstruction(new CMP(new ImmediateFloat(0), Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true))));
				compiler.addInstruction(new BEQ(new Label("DivisionByZeroException")));
			}
			compiler.addInstruction(new DIV(op1, op2));
			
		}
		else {
			assert(this.getType().isInt());
			if (!compiler.getCompilerOptions().getNoCheck()) {
				compiler.addInstruction(new LOAD(op1, Register.getR(GestionMemoire.nouveauRegistre(compiler))));
				compiler.addInstruction(new CMP(0, Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true))));
				compiler.addInstruction(new BEQ(new Label("DivisionByZeroException")));
			}
			compiler.addInstruction(new QUO(op1, op2));
		}
		
	}

}
