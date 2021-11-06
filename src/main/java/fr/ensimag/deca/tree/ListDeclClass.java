package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl49
 * @date 01/01/2021
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
//        throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass classDecl : getList()) {
            classDecl.verifyClass(compiler);
        }
         LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	for (AbstractDeclClass classDecl : getList()) {
            classDecl.verifyClassMembers(compiler);
        }
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
//        throw new UnsupportedOperationException("not yet implemented");
    	for (AbstractDeclClass classDecl : getList()) {
            classDecl.verifyClassBody(compiler);
        }
    }
    public void codeGenListClass(DecacCompiler compiler) {
        for (AbstractDeclClass c : this.getList()){
            c.codeGenClass(compiler);
        }
    }

    public void codeGenListMethods(DecacCompiler compiler) {
        for (AbstractDeclClass c : this.getList()){
            c.codeGenMethods(compiler);
        }
    }



}
