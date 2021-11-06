package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl49
 * @date 01/01/2021
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
//        throw new UnsupportedOperationException("not yet implemented");
   	 	 this.setLocation(this.getOperand().getLocation());
         Type floatType = new FloatType((compiler.getSymbolTable().create("float")));
         return floatType;
    }
    
    @Override
    public void codeGenInst(DecacCompiler compiler) {
    	this.getOperand().codeGenInst(compiler);
        GPRegister convReg = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        compiler.addInstruction(new FLOAT(convReg, convReg));
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
