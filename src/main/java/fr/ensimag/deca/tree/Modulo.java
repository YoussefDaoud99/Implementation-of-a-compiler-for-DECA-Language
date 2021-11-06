package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl49
 * @date 01/01/2021
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getLeftOperand().setType(leftType);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().setType(rightType);
        if (!(leftType.isInt() && rightType.isInt())) {
            throw new ContextualError("Modulo types must be int ! (3.33)", this.getLocation());
        }
        return leftType;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

	@Override
	void calcOperationArith(DecacCompiler compiler, DVal op1, GPRegister op2) {
		compiler.addInstruction(new REM(op1, op2));
	}

}
