package fr.ensimag.deca;

import java.util.concurrent.Callable;


public class ThreadParallel implements Callable<Object>{


		   private DecacCompiler compiler;

		   public ThreadParallel(DecacCompiler compiler) {
		      this.compiler = compiler;
		      }

		@Override
		public Object call() throws Exception {
			// TODO Auto-generated method stub
			return compiler.compile();
		}

}
