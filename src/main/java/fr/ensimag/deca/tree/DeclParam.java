package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class DeclParam extends AbstractDeclParam{
	/**
	 * @author gl49
	 * @date 19/01/2021
	 */
	private AbstractIdentifier paramType;
    private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier paramType, AbstractIdentifier paramName) {
        Validate.notNull(paramType);
        Validate.notNull(paramName);
        this.paramType = paramType;
        this.paramName = paramName;
    }

	@Override
	protected void verifyDeclParam(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
		// TODO Auto-generated method stub
		Type parametreType = this.paramType.verifyType(compiler);
        ParamDefinition paramDef = new ParamDefinition(parametreType, this.getLocation());
        paramName.setDefinition(paramDef);
        try {
            localEnv.declare(paramName.getName(), paramDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Parameter is already used ! (3.13)", paramName.getLocation());
        }
	}

	@Override
	protected Type verifyType(DecacCompiler compiler) throws ContextualError {
		// TODO Auto-generated method stub
//		return null;
		Type parametreType = this.paramType.verifyType(compiler);
        if (parametreType.isVoid())
            throw new ContextualError("Parameter type can't be void ! (2.9)", this.getLocation());
        return parametreType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		paramType.decompile(s);
        s.print(" ");
        paramName.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		paramType.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		paramType.iterChildren(f);
        paramName.iterChildren(f);
	}

	@Override
	protected void codeGenParam(DecacCompiler compiler) {
		paramName.getExpDefinition().setOperand(new RegisterOffset(GestionMemoire.getParamPosSuiv(), Register.LB));
	}
    
}
