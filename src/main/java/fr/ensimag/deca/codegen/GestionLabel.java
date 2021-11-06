package fr.ensimag.deca.codegen;

import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.ima.pseudocode.Label;

public class GestionLabel {
	
	private int nbrCast = 0;
	private int nbrInstanceOf = 0;
	private int nbrReturn = 0;
	private static Label returnLabel;
	
	public static Label getReturnLabel() {
		return returnLabel;
	}
	public static void setReturnLabel(Label lbl) {
		returnLabel = lbl;
	}
	
	public GestionLabel(int nbrInstanceOf, int nbrReturn) {
		this.nbrInstanceOf = nbrInstanceOf;
		this.nbrReturn =nbrReturn;
	}
	
	
	public static void setMethodLabel(String mother, DeclMethod method){
		String meth = method.getMethodName().getName().getName();
        if (!(method.getMethodName().getMethodDefinition().getIsLabelSet())){
            Label label = new Label("code."+mother+"."+meth);
            method.getMethodName().getMethodDefinition().setLabel(label);
        }
        else if (meth.equals("equals")){
            Label label = new Label("code.Objet.equals");
            method.getMethodName().getMethodDefinition().setLabel(label);
        }
    }


	public int getNbrInstanceOf() {
		return nbrInstanceOf;
	}
	
	public void incrNbrInstanceOf() {
		nbrInstanceOf ++;
	}


	public int getNbrReturn() {
		return nbrReturn;
	}


	public void incrNbrReturn() {
		this.nbrReturn ++;
	}
	public int getNbrCast() {
		return nbrCast;
	}
	public void incrNbrCast() {
		nbrCast ++;
	}
}
