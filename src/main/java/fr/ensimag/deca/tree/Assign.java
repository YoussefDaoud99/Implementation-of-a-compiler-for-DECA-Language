package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl49
 * @date 01/01/2021
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
        ClassDefinition currentClass) throws ContextualError {
    	//throw new UnsupportedOperationException("not yet implemented");
    	Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType));
        this.setType(leftType);
        return leftType;
    }
    

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	super.getRightOperand().codeGenInst(compiler);
    	DAddr Adresse ;
    	if(getLeftOperand().isSelection()) {
        	Adresse = getLeftOperand().getAdrSel();
    	}else {
    		Adresse = getLeftOperand().getExpDefinition().getOperand();
    	}
    	compiler.addInstruction(new STORE(Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false)), Adresse));
    	GestionMemoire.decrPosDernierRegistreUtilise();
        /*super.getRightOperand().codeGenInst(compiler);
        Identifier i = (Identifier) super.getLeftOperand();
        i.getExpDefinition().getOperand();
        GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        compiler.addInstruction(new STORE(register, i.getExpDefinition().getOperand()));
        GestionMemoire.decrPosDernierRegistreUtilise();*/
    	
    	
        }
    



    @Override
    protected String getOperatorName() {
        return "=";
    }

}
