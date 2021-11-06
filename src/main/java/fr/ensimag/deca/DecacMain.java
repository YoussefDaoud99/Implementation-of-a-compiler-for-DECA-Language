package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl49
 * @date 01/01/2021
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
//        final long startTime = System.currentTimeMillis();
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        
        if (options.getPrintBanner()) {
        	System.out.println("GREAT 49 TEAM composé par : \n"
        			+ "Driss Ait Hammou \n"
        			+ "Hamza Ajja \n"
        			+ "Khalil Leachouri \n"
        			+ "Oussama Bentaibi \n"
        			+ "Youssef Daoud");
        	System.exit(0);
        }
        
        if (options.getSourceFiles().isEmpty()) {
        	
        	System.out.println("Veuillez insérer un ou plusieurs fichiers source .deca");
        	options.displayUsage();
        	System.exit(1);
        
        }
        
       
        if (options.getParallel()) {

        	
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
        	
        	//nombre de processeurs disponibles pour la machine virtuelle Java
        	int nbrProcRestants = Runtime.getRuntime().availableProcessors();
            ExecutorService executor = Executors.newFixedThreadPool(nbrProcRestants);
            
            //Linked List qui va contenir le retour de la méthode call
            List<Future<Object>> sorties = new LinkedList<Future<Object>>();
            
        	for (File source : options.getSourceFiles()) {        		
        		DecacCompiler compiler = new DecacCompiler(options, source);
        		
        		ThreadParallel thread = new ThreadParallel(compiler);
        		
        		sorties.add(executor.submit(thread));
        		}
        	
        	for(Future<Object> future : sorties){
                    try {
						if((Boolean) future.get()){
						    error = true;
               
						}
					} catch (InterruptedException e) {
						System.out.println("Cause of Exception : "
	                               + e.getCause()); 
					} catch (ExecutionException e) {
						System.out.println("Cause of Exception : "
	                               + e.getCause()); 
					}
        	}
        	executor.shutdown();
//        	final long endTime = System.currentTimeMillis();
//            System.out.println("Temps de compilation: "+ (endTime - startTime));
        }

        
        
         else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
//            final long endTime = System.currentTimeMillis();
//            System.out.println("Temps de compilation: "+ (endTime - startTime));
        }
        System.exit(error ? 1 : 0);
    }
}
