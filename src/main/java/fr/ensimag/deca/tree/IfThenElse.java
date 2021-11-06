package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl49
 * @date 01/01/2021
 */
public class IfThenElse extends AbstractInst {
    
	private static int ifNbr = 0;
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public ListInst getElseBranch() {
        return this.elseBranch;
    }

    public void setElseBranch(ListInst elseBranch) {
        this.elseBranch = elseBranch;
    }
    
    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	condition.verifyCondition(compiler, localEnv, currentClass);
    	thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    	elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        ifNbr ++;
    	Label end = new Label("end" + ifNbr);
        Label elseInst = new Label("elseInst" + ifNbr);
        condition.codeGenInst(compiler);
    	GPRegister conditionReg = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        compiler.addInstruction(new CMP(0, conditionReg));
        compiler.addInstruction(new BEQ(elseInst));
        thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(end));
        compiler.add(new Line(elseInst));
        elseBranch.codeGenListInst(compiler);  
        compiler.addLabel(end);
       }

    @Override
    public void decompile(IndentPrintStream s) {
    	s.print( "if (");
    	condition.decompile(s);
    	s.println(") {");
    	s.indent();
    	thenBranch.decompile(s);
    	s.unindent();
    	s.print("}");
    	if (!elseBranch.isEmpty()) {
    	s.print("else ");
    	
    		if (elseBranch.getList().get(0) instanceof IfThenElse) {
    			elseBranch.getList().get(0).decompile(s);
    		}
    		else {
        		s.println("{");
        		s.indent();
        		elseBranch.decompile(s);
        		s.unindent();
            	s.println("}");
        	}
    	}
    	else {
    		s.println();
    	}
   
    	
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
