package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionExceptions;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl49
 * @date 01/01/2021
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
//        throw new UnsupportedOperationException("not yet implemented");
        this.classes.verifyListClass(compiler);
        this.classes.verifyListClassMembers(compiler);
        this.classes.verifyListClassBody(compiler);
        this.main.verifyMain(compiler);
         LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) { 
    	
        Label label = new Label("code.Object.equals");

    	if (!classes.getList().isEmpty()) {
    	if(!compiler.getCompilerOptions().getNoCheck()){
    		compiler.addComment("overflow test");
            compiler.addInstruction(new BOV(new Label("StackOverFlowException")));
            compiler.addInstruction(new ADDSP(GestionMemoire.getRegGB()+1));
    	}
    	//Object variables Table
        compiler.addComment("Generation de la table des methodes");
        //compiler.addLabel(label);
        
        /*ADDSP/TSTO : update*/
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(GestionMemoire.getVariablePosition(), Register.GB)));
        
        /*ADDSP/TSTO : update*/
        compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(GestionMemoire.incrementeVarPosition(), Register.GB)));
    	
        //generation des classes
        compiler.addComment("generation des classes");
        for (AbstractDeclClass classe : classes.getList()){
            classe.codeGenClass(compiler);
        }
    	 }
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        
        if (main instanceof Main) {
            Main mainInit = (Main) main;
            //compiler.addComment("Main program:");
            //main.codeGenMain(compiler);
            //compiler.addInstruction(new HALT());

            int maxUsed = GestionMemoire.getMaxStackUtilise() + GestionMemoire.getMaxRegistersUtilise()
                    + mainInit.getDeclVariables().size();
            //compiler.addFirst(new ADDSP(GestionMemoire.getNbrDeclVar()));

            if (!compiler.getCompilerOptions().getNoCheck()) {
                compiler.addFirst(new BOV(new Label("StackOverFlowException")));
            }
            compiler.addFirst(new TSTO(maxUsed + GestionMemoire.getRegGB()+ 2));


        }
        if (!classes.getList().isEmpty()) {
            compiler.addLabel(label);
     compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), Register.R0));
     compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
     compiler.addInstruction(new CMP(Register.R0, Register.R1));
     compiler.addInstruction(new SEQ(Register.R0));
     compiler.addInstruction(new RTS());
     
      	
      }
        
        //fields and methods generation
        compiler.addComment("fields and methods generation");
        for (AbstractDeclClass classe : classes.getList()){
        	compiler.addLabel(new Label("init."+((DeclClass)classe).getClassName().getName()));
        	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            /*if (!classe.getClass().getSuperclass().equals("Object")) { //Only if super class is not Object
            	//compiler.getPile().incrTesto();
                compiler.addInstruction(new PUSH(Register.R1));
                compiler.addInstruction(new BSR(new Label("init."+((DeclClass)classe).getClassName().getName().getName())));
                compiler.addInstruction(new SUBSP(1));
                //compiler.getPile().decrTesto();
            }*/
        	classe.codeGenFields(compiler);
            compiler.addInstruction(new RTS());
        	classe.codeGenMethods(compiler);
        }


        GestionExceptions.addExceptions(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
