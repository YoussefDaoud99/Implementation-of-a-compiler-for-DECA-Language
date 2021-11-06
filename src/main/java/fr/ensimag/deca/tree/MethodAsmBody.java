package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;

/**
 * @author gl49
 * @date 19/01/2021
 */

public class MethodAsmBody extends AbstractMethodBody{

	private StringLiteral asm;
	
	public MethodAsmBody(StringLiteral asm) {
        Validate.notNull(asm);
        this.asm = asm;
    }
	
	@Override
    protected boolean isAsm() {
        return true;
    }
	
	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
        asm.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
        asm.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
        asm.iterChildren(f);
	}

	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExpParam, ClassDefinition classDef,
			Type methodType) throws ContextualError{
		// TODO Auto-generated method stub
		Type asmType = asm.verifyExpr(compiler, envExpParam, classDef);
        asm.setType(asmType);
	}

	@Override
	public void codeGenBody(DecacCompiler compiler) {
        compiler.add(new InlinePortion("\t" + asm.getValue()));
	}

}
