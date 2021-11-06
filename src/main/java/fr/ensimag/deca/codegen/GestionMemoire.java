package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

public class GestionMemoire {
	
	private static Label objetctEqualsLabel = new Label("code.Object.equals");
	private static int variablePosition = 1;
	private static int positionDernierRegistreUtilise = 1;
	private static int nombreMaxRegistres = 15;
	private static int nombrePush = 0;
	private static int maxStackUtilise = 0;
    private static int maxRegistersUtilise = 0;
    private static int nbrDeclVar = 2;
    private static int paramPos = -2;
    private static int regGB = 1;
    private static int regLB = 1;
    
    public static DAddr allocRegGB() {
    	regGB++;
    	return new RegisterOffset(regGB, Register.GB);
    }
    public static DAddr allocRegLB() {
    	regLB++;
    	return new RegisterOffset(regLB, Register.LB);
    }
    public static void incrRegGB(int i) {
    	regGB += i;
    }
    public static int getRegGB() {
    	return regGB;
    }


    
	public GestionMemoire() {
		
	}
	
	public static int getNbrDeclVar() {
		return nbrDeclVar;
	}
	
	public static int getMaxStackUtilise() {
        return maxStackUtilise;
    }
	
	private static void updateStackUtilise() {
	        if (maxStackUtilise < nombrePush)
	        	maxStackUtilise = nombrePush;
	    }
	private static void updateMaxRegisterUtilise() {
	        if (maxRegistersUtilise < positionDernierRegistreUtilise)
	        	maxRegistersUtilise = positionDernierRegistreUtilise;
	 }

	public static int incrementeVarPosition() {
		variablePosition++;
		return variablePosition;
	}
	
	public static int getVariablePosition() {
		return variablePosition;
	}


	public static void setVariablePosition(int variablePosition) {
		GestionMemoire.variablePosition = variablePosition;
	}

	public static int getPositionDernierRegistreUtilise(DecacCompiler compiler, boolean decremente) {
			if (decremente) {
				if (nombrePush > 0) {
					
					nombrePush --;
					compiler.addInstruction(new POP(Register.R0));
			        return 0;
					
		        } else {
		        	
		        	assert positionDernierRegistreUtilise > -1;
		        	positionDernierRegistreUtilise --;
		        	return positionDernierRegistreUtilise + 1;
		        }
			}else {
				return positionDernierRegistreUtilise;
			}
		
	}
	public static void decrPosDernierRegistreUtilise() {
		positionDernierRegistreUtilise --; 
	}

	public static int nouveauRegistre(DecacCompiler compiler) {
		
		if (positionDernierRegistreUtilise < nombreMaxRegistres) {
			positionDernierRegistreUtilise ++ ;
		}
		
		else {
			compiler.addInstruction(new PUSH(Register.getR(positionDernierRegistreUtilise)));
			nombrePush ++;
		}
		return positionDernierRegistreUtilise;	
	}
	
	
	
	public static void setPositionDernierRegistreUtilise(int positionDernierRegistreUtilise) {
		GestionMemoire.positionDernierRegistreUtilise = positionDernierRegistreUtilise;
	}

	public static int getMaxRegistersUtilise() {
		return maxRegistersUtilise;
	}

	public static int getParamPosSuiv() {
		paramPos --;
		return paramPos;
	}

	public static void setParamPos(int paramPos) {
		GestionMemoire.paramPos = paramPos;
	}
	
	public static void setNombreMaxRegistres(int numRegistres) {
		nombreMaxRegistres = numRegistres;
	}
	
}
