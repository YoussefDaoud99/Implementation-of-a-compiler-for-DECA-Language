package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl49
 * @date 01/01/2021
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
        if (leftType.sameType(rightType)) {
            if (!leftType.isInt() && !leftType.isFloat() && (this instanceof AbstractOpIneq)) {
                throw new ContextualError("Inequality is not available for non-numbers ! (3.33)", getLocation());
            }
        } else {
        	if (leftType.isFloat() && rightType.isInt()) {
        		AbstractExpr convFloat = new ConvFloat(this.getRightOperand());
                rightType = convFloat.verifyExpr(compiler, localEnv, currentClass);
                convFloat.setType(rightType);
                this.setRightOperand(convFloat);
        	}
        	else if (leftType.isInt() && rightType.isFloat()) {
        		AbstractExpr convFloat = new ConvFloat(this.getLeftOperand());
                leftType = convFloat.verifyExpr(compiler, localEnv, currentClass);
                convFloat.setType(leftType);
                this.setLeftOperand(convFloat);
            } else {
                throw new ContextualError("Incompatible types for comparaison ! (3.33)", getLocation());
            }
        }
        Type booleanType = new BooleanType(compiler.getSymbolTable().create("boolean"));
        return booleanType;
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.getLeftOperand().codeGenInst(compiler);
        this.getRightOperand().codeGenInst(compiler);
        
        GPRegister debugRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        GPRegister rightRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true));      
        GPRegister leftRegister = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        if (debugRegister.getNumber() == rightRegister.getNumber()) {
        	compiler.addInstruction(new CMP(rightRegister, leftRegister));
        	this.ComparaisonFunction(compiler, leftRegister);
        }else {
        	compiler.addInstruction(new CMP(leftRegister, rightRegister));
        	this.ComparaisonFunction(compiler, debugRegister);
        }
    }
    abstract void ComparaisonFunction(DecacCompiler compiler, GPRegister r);
}
