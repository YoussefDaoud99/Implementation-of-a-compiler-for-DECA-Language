package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl49
 * @date 01/01/2021
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	Type exprType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(exprType.isInt() || exprType.isFloat())) {
            throw new ContextualError("Unary Minus is not defined for this type (3.38)", this.getLocation());
        }
        this.getOperand().setType(exprType);
        return exprType;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	this.getOperand().codeGenInst(compiler);
        GPRegister RegistreResultat = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        compiler.addInstruction(new OPP(RegistreResultat, RegistreResultat));
    }

}
