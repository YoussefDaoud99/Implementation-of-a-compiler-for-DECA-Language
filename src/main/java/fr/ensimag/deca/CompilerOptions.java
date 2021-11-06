package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl49
 * @date 01/01/2021
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public boolean getParse() {
    	return parse;
    }
    
	public boolean getVerification() {
		return verification;
	}
	
	public boolean getNoCheck() {
		return noCheck;
	}
	
	public boolean getRegisters() {
		return registers;
	}
	
	public boolean getWarnings() {
		return warnings;
	}
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private boolean registers = false;
    private int numRegisters = 15;
    private boolean warnings = false;
    private boolean parse = false;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
    	
    	if (args.length==0){
			throw new CLIException("Aucun parametre rentree");
		}
		else if(args.length==1 && args[0].equals("-b")){ 
			this.printBanner=true;
		}
    	
		else {
			int indiceArg = 0;
			while (indiceArg < args.length) {
				if (args[indiceArg].equals("-d")){
					debug ++;
					indiceArg ++;
				}
				else if (args[indiceArg].equals("-P")) {
					parallel = true;
					indiceArg ++;
				}
				
				else if (args[indiceArg].equals("-p")) {
					parse = true;
					indiceArg ++;
				}
				
				else if (args[indiceArg].equals("-v")) {
					verification = true;
					indiceArg ++;
					if (getParse()) {
						throw new CLIException("Les options -p et -v sont incompatibles.");
					}
				} 
					
				else if (args[indiceArg].equals("-n")) {
					noCheck = true;
					indiceArg ++;
				}	
				
				else if ((args[indiceArg]).equals("-r")){
					registers = true;
					indiceArg ++;
					numRegisters = Integer.parseInt(args[indiceArg]);	
				}
				
				else if (args[indiceArg].equals("-w")) {
					warnings = true;
					indiceArg ++;
				}
				
				else if (args[indiceArg].contains(".deca")) {
					sourceFiles.add(new File(args[indiceArg]));
					indiceArg ++;
				}
				
				else {
					indiceArg ++;
				}
			}
		}
    	
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {
        System.out.println("La syntaxe d’utilisation de l’exécutable decac est \n"
        		+ ":decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]");
        System.out.println("La définition de chaque option :\n"
        		+ "-b       (banner)       : affiche une bannière indiquant le nom de l’équipe\n"
        		+ "-p       (parse)        : arrête decac après l’étape de construction del’arbre,\n"
                + "                          et affiche la décompilation de ce dernier(i.e. s’il n’y a \n"
                +"                           qu’un fichier source àcompiler, la sortie doit être un \n"
                +"                           programmedeca syntaxiquement correct) \n"
                +"-v       (verification)  : arrête decac après l’étape de vérifications(ne produit aucune \n"
                + "                         sortie en l’absence d’erreur)\n"
                + "-n       (no check)     : supprime les tests à l’exécution spécifiés dansles points 11.1 \n"
                + "                          et 11.3 de la sémantique de Deca.\n"
                + "-r X     (registers)    : limite les registres banalisés disponibles àR0 ... R{X-1}, avec 4 <= X <= 16\n"
                + "-d       (debug)        : active les traces de debug. Répéter l’option plusieurs fois pour avoir plus detraces.\n"
                + "-P       (parallel)     : s’il y a plusieurs fichiers sources,lance la compilation des fichiers enparallèle \n"
                + "                          (pour accélérer la compilation)\n"
                + "-w       (warnings)     : affiche des messages d’avertissement (« warnings »)");
        
    }

	public int getNumRegisters() {
		return numRegisters;
	}


}
