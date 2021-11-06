package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * read...() statement.
 *
 * @author gl49
 * @date 01/01/2021
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }
 
    abstract void readFunction(DecacCompiler compiler);

    @Override
    public void codeGenInst(DecacCompiler compiler) {
    readFunction(compiler);
    GPRegister newRegister = Register.getR(GestionMemoire.nouveauRegistre(compiler));
    compiler.addInstruction(new LOAD(Register.R1, newRegister));
    }
}
