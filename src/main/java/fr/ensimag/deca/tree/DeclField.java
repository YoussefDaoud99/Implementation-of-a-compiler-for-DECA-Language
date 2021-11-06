package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
/**
 * @author gl49
 * @date 18/01/2021
 */
public class DeclField extends AbstractDeclField{
	
	private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier fieldName;
    private AbstractInitialization initialization;
    
    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.visibility = visibility;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    public AbstractInitialization getInitialization() {
    	return this.initialization;
    }
    
    public AbstractIdentifier getFieldName() {
    	return this.fieldName;
    }
    
	@Override
	protected void verifyDeclField(DecacCompiler compiler, ClassDefinition superDef, ClassDefinition classDef)
			throws ContextualError {
		// TODO Auto-generated method stub
		if (classDef.getNumberOfFields() == 0) {
            classDef.setNumberOfFields(superDef.getNumberOfFields());
        }
		// Verifying the FieldType
		Type fieldType = this.type.verifyType(compiler);
		if (fieldType.isVoid()) {
			throw new ContextualError("Field type must not be void ! (2.5)", this.getLocation());
		}
		// verifying the FieldName
        int indice = classDef.getNumberOfFields()+1;
		if (superDef.getMembers().getFilsEnvironment().containsKey(this.fieldName.getName())) {
            if (!superDef.getMembers().getFilsEnvironment().get(this.fieldName.getName()).isField()) {
                throw new ContextualError("Identifier already used for a method in super class (2.5)", this.getLocation());
            }
            FieldDefinition superFieldDef= (FieldDefinition) superDef.getMembers().getFilsEnvironment().get(fieldName.getName());
            indice = superFieldDef.getIndex();
        } else {
            classDef.incNumberOfFields();
        }
		FieldDefinition fieldDef = new FieldDefinition(fieldType, fieldName.getLocation(), visibility, classDef,
                indice);
        try {
            classDef.getMembers().declare(fieldName.getName(), fieldDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("field already declared ! (2.5)", fieldName.getLocation());
        }
		// Decorating the tree
        fieldName.setDefinition(fieldDef);
        type.setType(fieldType);
	}

	@Override
	protected void verifyFieldInit(DecacCompiler compiler, EnvironmentExp envExp, ClassDefinition classDef)
			throws ContextualError {
		// TODO Auto-generated method stub
		// Verifying the initialization
        Type fieldType = type.verifyType(compiler);
        initialization.verifyInitialization(compiler, fieldType, envExp, classDef);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		if (visibility == Visibility.PROTECTED) {
			s.print("protected");
			s.print(" ");
		}
		s.print(type.decompile());
		s.print(" ");
		s.print (getFieldName().decompile());
    	s.print (getInitialization().decompile());
    	s.print(";");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
//		visibility.prettyPrintChildren();
		type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
//		visibility.iterChildren(f);
		type.iterChildren(f);
        fieldName.iterChildren(f);
        initialization.iterChildren(f);
	}

	@Override
	protected void codeGenField(DecacCompiler compiler) {
		initialization.codeGenFieldInit(compiler, this.type.getType());
		
		// on met tout dans R1, on recupere de R0
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new STORE(Register.R0,
                new RegisterOffset(this.fieldName.getFieldDefinition().getIndex(), Register.R1)));
        this.fieldName.getFieldDefinition().setOperand(
                new RegisterOffset(this.fieldName.getFieldDefinition().getIndex(), 
                		Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false))));
        //this.fieldName.getExpDefinition().setOperand(
         //       new RegisterOffset(this.fieldName.getFieldDefinition().getIndex(), 
           //     		Register.getR(GestionMemoire.getPositionDernierRegistreUtilise(compiler, false))));

        
	}

}
