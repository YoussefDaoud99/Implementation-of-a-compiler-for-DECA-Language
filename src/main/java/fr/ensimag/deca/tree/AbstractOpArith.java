package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl49
 * @date 01/01/2021
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");

        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getLeftOperand().setType(leftType);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getRightOperand().setType(rightType);
        if (!(leftType.isFloat() || leftType.isInt())) {
            throw new ContextualError("Types of arithmetic operations must be float and int ! (3.33)", this.getLocation());
        }
        if (!leftType.sameType(rightType)) {
        	if (leftType.isFloat() && rightType.isInt()) {
                AbstractExpr convFloat = new ConvFloat(this.getRightOperand());
                rightType = convFloat.verifyExpr(compiler, localEnv, currentClass);
                convFloat.setType(rightType);
                this.setRightOperand(convFloat);
            } else if (leftType.isInt() && rightType.isFloat()) {
                AbstractExpr convFloat = new ConvFloat(this.getLeftOperand());
                leftType = convFloat.verifyExpr(compiler, localEnv, currentClass);
                convFloat.setType(leftType);
                this.setLeftOperand(convFloat);
            } else {
            	throw new ContextualError("Incompatible type for arithmetic operation ! (3.33)", this.getLocation());
            }
        }
        return leftType;
    }
    abstract void calcOperationArith(DecacCompiler compiler, DVal op1, GPRegister op2);
    
    @Override
    public void codeGenPrint(DecacCompiler compiler) {
        this.codeGenInst(compiler);
        GPRegister registreRes = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true));
        compiler.addInstruction(new LOAD(registreRes, Register.R1));       
        if (getType().isFloat()) {
            compiler.addInstruction(new WFLOAT());
        }else if (getType().isInt()) {
            compiler.addInstruction(new WINT());
        }
    }    
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	this.getLeftOperand().codeGenInst(compiler);
        this.getRightOperand().codeGenInst(compiler);
        
        GPRegister debugRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        GPRegister rightRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true));      
        GPRegister leftRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        
        if (debugRegister.getNumber() == rightRegister.getNumber()) {
        	calcOperationArith(compiler, rightRegister, leftRegister);
        }else {
        	calcOperationArith(compiler, leftRegister, rightRegister);
        	compiler.addInstruction(new LOAD(rightRegister, debugRegister));
        }
    }
}
