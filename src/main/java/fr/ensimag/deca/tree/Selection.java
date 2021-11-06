package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

/**
 * Selection
 * @author gl49
 * @date 20/01/2021
 */

public class Selection extends AbstractLValue{

	private AbstractExpr object;
    private AbstractIdentifier fieldIdent;

    public Selection(AbstractExpr object, AbstractIdentifier fieldIndent) {
        Validate.notNull(object);
        Validate.notNull(fieldIndent);
        this.object = object;
        this.fieldIdent = fieldIndent;
    }
    
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
//		return null;
		
		Type exprType = object.verifyExpr(compiler, localEnv, currentClass);
		object.setType(exprType);
		ClassType classType = exprType.asClassType("Selection LeftValue expression must be a class ! (3.65), (3.66)", object.getLocation());
		EnvironmentExp envExp2 = classType.getDefinition().getMembers();
        Type fieldType = fieldIdent.verifyExpr(compiler, envExp2, currentClass);
        FieldDefinition fieldDef = envExp2.get(fieldIdent.getName()).asFieldDefinition(
                "Selection right identifier must be a class field ! (3.70)", fieldIdent.getLocation());
        Visibility fieldVisbility = fieldDef.getVisibility();
        ClassType fieldClass = fieldDef.getContainingClass().getType();   
        if (fieldVisbility == Visibility.PROTECTED) {
        	if (currentClass == null) {
            	throw new ContextualError("Field is protected ! (3.66)", this.getLocation());
            }
        	if (!currentClass.getType().isSubClassOf(fieldClass)) {
                throw new ContextualError("Field is protected ! (3.66)", fieldIdent.getLocation());
            }
            if (!classType.isSubClassOf(currentClass.getType())) {
                throw new ContextualError("Expression type is not a subtype of the current class ! (3.66)", fieldIdent.getLocation());
            }
        }
        this.setType(fieldType);
        return fieldType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		object.decompile(s);
        s.print(".");
        fieldIdent.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		object.prettyPrint(s, prefix, false);
        fieldIdent.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		object.iterChildren(f);
        fieldIdent.iterChildren(f);
	}

	@Override
	public ExpDefinition getExpDefinition() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DAddr getAdrSel() {
		return fieldIdent.getExpDefinition().getOperand();
	}
	@Override
	public boolean isSelection() {
		return true;
	}
    @Override
    protected void codeGenInst(DecacCompiler compiler){
       
        object.codeGenInst(compiler);
        GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));

        compiler.addInstruction(new LOAD(new RegisterOffset(fieldIdent.getFieldDefinition().getIndex(),register),register));
       
    }
    
	@Override
	public void codeGenPrint(DecacCompiler compiler) {
		this.codeGenInst(compiler);
		
		compiler.addInstruction(new LOAD(Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, true)), Register.getR(1)));
    	//compiler.addInstruction(new LOAD(new RegisterOffset(GestionMemoire.getVariablePosition(), Register.GB), Register.R1));

        if (this.getType().isFloat()) {
            if (super.isHexaDecimal())
                compiler.addInstruction(new WFLOATX());
            else
                compiler.addInstruction(new WFLOAT());
        }
        
        else if (this.getType().isInt())
            compiler.addInstruction(new WINT());
        
        else if (this.getType().isBoolean())
            compiler.addInstruction(new WINT());
        
		
	}

}
