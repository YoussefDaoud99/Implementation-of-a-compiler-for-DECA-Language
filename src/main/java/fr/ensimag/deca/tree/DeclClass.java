package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GestionLabel;
import fr.ensimag.deca.codegen.GestionMemoire;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl49
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {

    private AbstractIdentifier className;
    private AbstractIdentifier superClass;
    private ListDeclFields fields;
    private ListDeclMethods methods;

    @Override
    public void decompile(IndentPrintStream s) {
//        s.print("class { ... A FAIRE ... }");
    	s.print("class ");
        className.decompile(s);
        s.print(" extends ");
        superClass.decompile(s);
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass, ListDeclFields fields, ListDeclMethods
            methods) {
        Validate.notNull(className);
        Validate.notNull(superClass);
        Validate.notNull(fields);
        Validate.notNull(methods);
        this.className = className;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;
    }
    
    public AbstractIdentifier getClassName() {
    	return this.className;
    }
    
    public AbstractIdentifier getSuperClass() {
    	return this.superClass;
    }
    
    public ListDeclFields getListDeclFields() {
    	return this.fields;
    }
    
    public ListDeclMethods getListDeclMethods() {
    	return this.methods;
    }
    
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	// Checking for contextual errors
    	if (!compiler.getEnvironementType().containsKey(this.superClass.getName()))
            throw new ContextualError("Super class must exist ! (1.3)", this.getLocation());
        Type superType = compiler.getEnvironementType().get(superClass.getName()).getType();
        if (!superType.isClass())
            throw new ContextualError("Superclass identifier must be a class identifier ! (1.3)", this.getLocation());

        if (compiler.getEnvironementType().containsKey(className.getName()))
            throw new ContextualError("Class name is already used ! (1.3)", this.getLocation());

        // Adding class to env_types
        ClassDefinition superDef = (ClassDefinition) compiler.getEnvironementType().get(superClass.getName());
        ClassType classType = new ClassType(className.getName(),className.getLocation(), superDef);
        compiler.getEnvironementType().put(className.getName(), classType.getDefinition());

        // Decorating the tree
        this.className.setDefinition(classType.getDefinition());
        this.className.setType(classType);
        this.superClass.setDefinition(superDef);
        this.superClass.setType(superType);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	ClassDefinition classDef = className.getClassDefinition();
        ClassDefinition superDef = superClass.getClassDefinition();
    	fields.verifyListDeclField(compiler, superDef, classDef);
        methods.verifyListDeclMethod(compiler, superDef, classDef);
    	GestionMemoire.incrRegGB(classDef.getNumberOfMethods()+1);

    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	ClassDefinition classDef = className.getClassDefinition();
        EnvironmentExp envExp = classDef.getMembers();
        fields.verifyListFieldInit(compiler, envExp, classDef);
        methods.verifyListMethodBody(compiler, envExp, classDef);
        className.setDefinition(classDef);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
//        throw new UnsupportedOperationException("Not yet supported");
    	className.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
//        throw new UnsupportedOperationException("Not yet supported");
    	className.iterChildren(f);
        superClass.iterChildren(f);
        fields.iterChildren(f);
        methods.iterChildren(f);
    }

	@Override
	protected void codeGenClass(DecacCompiler compiler) {
        GestionMemoire.incrementeVarPosition();
        this.getClassName().getClassDefinition().setAdrClass(GestionMemoire.getVariablePosition());
    	//compiler.addLabel(new Label("init."+className.getName().getName()));

        // get superClass address 
        if (superClass.getName().getName().equals("Object")) {
        	DAddr adresse = new RegisterOffset(1, Register.GB);
        	this.className.getClassDefinition().setOperand(adresse);
            compiler.addInstruction(new LEA(adresse, Register.R0));
        }else {
            compiler.addInstruction(new LEA(new RegisterOffset(this.getSuperClass().getClassDefinition().getAdrClass(), Register.GB), Register.R0));
        }
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(this.getClassName().getClassDefinition().getAdrClass(), Register.GB)));

        this.getClassName().getClassDefinition().getListMethodLabels().put(0, new Label("code.Object.equals"));

        if (superClass.getName().getName().equals("Object")) {
            getClassName().getClassDefinition().setRegisterFields(Register.getR(GestionMemoire.nouveauRegistre(compiler)));
        }else {
            getClassName().getClassDefinition().setRegisterFields(getSuperClass().getClassDefinition().getRegisterFields());
        }
        for (Map.Entry<Integer, Label> m: superClass.getClassDefinition().getListMethodLabels().entrySet()){
            getClassName().getClassDefinition().getListMethodLabels().put(m.getKey(), m.getValue());
        }
        
        // give each method its label
        for (AbstractDeclMethod m: this.getListDeclMethods().getList()){
            DeclMethod method = (DeclMethod) m;
            String classeName = this.className.getName().getName();
            GestionLabel.setMethodLabel(classeName, method);
            this.getClassName().getClassDefinition().getListMethodLabels().put(method.getMethodName().getMethodDefinition().getIndex(), method.getMethodName().getMethodDefinition().getLabel());
        }
        
        for (Map.Entry<Integer, Label> m: this.getClassName().getClassDefinition().getListMethodLabels().entrySet()){
            GestionMemoire.incrementeVarPosition();
            compiler.addInstruction(new LOAD(new LabelOperand(m.getValue()), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(GestionMemoire.getVariablePosition(), Register.GB)));
        }
	}

	@Override
	protected void codeGenFields(DecacCompiler compiler) {
		if (fields.getList().size() > 0)
            fields.codeGenListFields(compiler, this);
	}
	
	@Override
	protected void codeGenMethods(DecacCompiler compiler) {
		for (AbstractDeclMethod method : this.methods.getList()) {
            method.codeGenMethod(compiler);
        }
		
	}

	

}
