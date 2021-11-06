package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * Cast
 *
 * @author gl49
 * @date 19/01/2021
 */

public class Cast extends AbstractExpr{
    private AbstractIdentifier type;
    private AbstractExpr operand;
	
    public Cast (AbstractIdentifier type, AbstractExpr operand) {
    	this.operand = operand;
        Validate.notNull(type);
        this.type = type;
    }
    
    @Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
//		return null;
    	Type castType = type.verifyType(compiler);
    	Type exprType = this.operand.verifyExpr(compiler, localEnv, currentClass);
    	this.setType(castType);
    	this.operand.setType(castType);
    	if (!compiler.getCompilerOptions().getNoCheck()) {
	    	if (exprType.isVoid()) {
	    		throw new ContextualError("Void type cannot be casted ! (3.39)", this.getLocation());
	    	} else if (!castType.sameType(exprType)) {
	    		if (castType.isClassOrNull() && exprType.isClassOrNull()) {
	    			// casts succed 100%
	       		 ClassType exprClassType = (ClassType) exprType; 
	             ClassType castClassType = (ClassType) castType;
	             if (!(castClassType.isSubClassOf(exprClassType) || exprClassType.isSubClassOf(castClassType))) {
	            	 throw new ContextualError("Incompatible types for cast ! (3.39)", this.getLocation());
	             }
	    		} else if (!((castType.isInt() && exprType.isFloat()) || (castType.isFloat() && exprType.isInt()))) {
	    			throw new ContextualError("Incompatible types for cast ! (3.39)", this.getLocation());
	    		}		
	    	}
    	}
        return castType;
	}
    /*@Override
    protected void codeGenInst(DecacCompiler compiler) {
        
    	operand.codeGenInst(compiler);
        GPRegister castReg = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));

        if (type.getType().isFloat()) {
            compiler.addInstruction(new FLOAT(castReg, castReg));
        }else if(type.getType().isFloat()){
            compiler.addInstruction(new INT(castReg, castReg));
        }else if(type.getType().isClass()){
            int i = compiler.getGestionLabel().getNbrCast();
        	compiler.getGestionLabel().incrNbrCast();
            Label caCast = new Label("success_cast" + i); // à la suite du else
            Label noCast = new Label("failed_cast" + i);
            Label endCast = new Label("end_cast" + i);
            operand.codeGenInst(compiler);
            GPRegister stockReg = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));

            compiler.addInstruction(new LOAD(new RegisterOffset(0, stockReg), stockReg));
            compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), castReg));
            compiler.addLabel(new Label("debut.instanceof" + i));
            compiler.addInstruction(new CMP(stockReg, castReg));
            compiler.addInstruction(new BEQ(caCast)); //test si egal
            compiler.addInstruction(new LOAD(new RegisterOffset(0, stockReg), stockReg)); // on descend
            compiler.addInstruction(new CMP(new NullOperand(), stockReg)); //si object instance
            compiler.addInstruction(new BNE(new Label("debut.instanceof" + i))); //non, on remonte
            //on test null
            operand.codeGenInst(compiler);
            GPRegister stockReg1 = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
            stockReg = stockReg1;
            compiler.addInstruction(new CMP(new NullOperand(), stockReg));
            compiler.addInstruction(new BNE(noCast));

            // Instructions
            compiler.addLabel(caCast);
            operand.codeGenInst(compiler);
            GPRegister castReg1 = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
            castReg = castReg1;
            compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), stockReg)); //classe, a forcément une adresse GB ou LB
            compiler.addInstruction(new STORE(stockReg, new RegisterOffset(0, castReg)));
            compiler.addInstruction(new BRA(endCast));
            compiler.addLabel(noCast);
            compiler.addInstruction(new WSTR("Erreur: bad cast from " + operand.getType().toString() + " to " + type.getName().toString()));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            compiler.addLabel(endCast);


            //compiler.getRegManager().setTableRegistre(backup);
            // Instructions
            compiler.addLabel(caCast);
            compiler.addInstruction(new STORE(castReg, type.getVariableDefinition().getOperand())); //classe, a forcément une adresse GB ou LB

            compiler.addLabel(noCast);
            compiler.addInstruction(new WSTR("Erreur: bad cast from " + operand.getType().toString() + " to " + type.getName().toString()));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }    
    }*/
    
    @Override
    public void codeGenInst(DecacCompiler compiler) {
    	Type castType = type.getType();
    	Type baseType = operand.getType();
        assert baseType != null;
    	operand.codeGenInst(compiler);

        GPRegister register = Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false));
        if(!castType.sameType(baseType)) {
        	if (castType.isFloat()) {
                compiler.addInstruction(new FLOAT(register, register));
            } else if (castType.isInt()) {
                compiler.addInstruction(new INT(register, register));
            } else {
            	compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), register));
            }
        }
      
    }
    


	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		s.print( "(");
		s.print(type.decompile() );
		s.print(") (");
		s.print(this.operand.decompile());
		s.print(")");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		type.prettyPrint(s, prefix, false);
		this.operand.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		type.iter(f);
		this.operand.iter(f);
	}


}