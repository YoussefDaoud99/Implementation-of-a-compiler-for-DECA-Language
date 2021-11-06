package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl49
 * @date 01/01/2021
 */
public class DeclVar extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(DeclVar.class);

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    public AbstractIdentifier getVarName() {
        return this.varName;
    }
    
    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	Type t = type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Illegal start of expression: void type inaccessible ! (3.17)", this.getLocation());
        }
        ExpDefinition definition = new VariableDefinition(t, varName.getLocation());
        varName.setDefinition(definition);
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        try {
            localEnv.declare(varName.getName(), definition);
            
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable is already defined ! (3.17)", varName.getLocation());
        }
		definition.setOperand(GestionMemoire.allocRegGB());
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
    	type.decompile(s);
        s.print(" ");
    	varName.decompile(s);
        s.print(" ");
    	initialization.decompile(s);
    	s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
    
    @Override
    public void codeGenInst(DecacCompiler compiler) {
    	GestionMemoire.incrementeVarPosition();
    	this.getVarName().getExpDefinition().setOperand(new RegisterOffset(GestionMemoire.getVariablePosition(), Register.GB));
    	//GestionMemoire.incrementeVarPosition();
    	initialization.codeGenInst(compiler, this);
 
	
    }
    	
    	
}

