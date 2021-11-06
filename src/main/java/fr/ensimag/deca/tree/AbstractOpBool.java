package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl49
 * @date 01/01/2021
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getLeftOperand().setType(leftType);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getRightOperand().setType(rightType);
        if (!(leftType.isBoolean() && rightType.isBoolean())) {
            throw new ContextualError("Binary boolean operation must be between booleans (3.33)", this.getLocation());
        }
        return leftType;
    }
    abstract void calcOperationBool(DecacCompiler compiler, DVal op1, GPRegister op2);
    

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	this.getLeftOperand().codeGenInst(compiler);
        GPRegister leftRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        this.getRightOperand().codeGenInst(compiler);
        GPRegister rightRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        calcOperationBool(compiler, leftRegister, rightRegister);
}
    
}


