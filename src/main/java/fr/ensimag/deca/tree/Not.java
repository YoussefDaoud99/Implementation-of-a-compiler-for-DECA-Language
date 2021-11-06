package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
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
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	Type exprType = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(exprType.isBoolean())) {
            throw new ContextualError("Unary not is not defined for this type (3.38)", this.getLocation());
        }
        this.getOperand().setType(exprType);
        return exprType;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	this.getOperand().codeGenInst(compiler);
        GPRegister RegistreResultat = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        compiler.addInstruction(new CMP(0, RegistreResultat));
        compiler.addInstruction(new SEQ(RegistreResultat));
    }
}
