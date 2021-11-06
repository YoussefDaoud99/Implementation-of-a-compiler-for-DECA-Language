
package Extension;
import java.lang.Math ;


/**
 * Une classe de test pour les fonctions trigonometriques implémentées
 * @author ensimag
 *
 */
public class Test{

	
	/**
	 * test de la fonction ulp à l'aide de la fonction ulp de java
	 */
	public static void testUlp() {
		System.out.println("Testing now : ulp \n");
		float pas =(float)Tools.power(2f,-17);
		float test = Tools.power(2f,-23);
		System.out.println(Math.ulp(test));
		System.out.println(Tools.ulp(test));
		int nbr=0;
		int error=0;
		for (float x=0f;x<10000f;x+=pas){
			nbr+=1;
			 System.out.println(nbr);
			float dif=Math.abs((float)Math.ulp(x) - Tools.ulp(x));
			float diff =(dif/(float)Math.ulp(x));
			if ((float)diff > 1f) {
				error+=1;
			}
		}
		System.out.println("finished");
		System.out.println("le pourcentage des erreurs rencotrees dans ce test fait "+ nbr + " fois est " + ((float)error/nbr)+"%");
}
	
	/**
	 * Test de l'arctangente 
	 */
	public static void testAtan() {
		System.out.println("Testing now : atan \n");
		float pas =(float)Tools.power(2f,-17);
		int nbr=0;
		int error=0;
		float errorTotal=0;
		float maxError=0;
		for (float x=5f;x<=5.1f;x+=pas){
			nbr+=1;
			float javaValue=(float)Math.atan(x);
			float ourValue=Tools.atan(x);
			System.out.println("our value: " + ourValue);
			System.out.println("Java value: " + javaValue);
			float dif=(float)Math.abs(( javaValue - ourValue));
			float diff =(dif/(float) Math.ulp(((float) javaValue)));
			if ((float)diff>1f) {
				error+=1;
				errorTotal+=diff;
				if(diff>maxError) {maxError=diff;}
			}	
	}
		System.out.println("finished");
		System.out.println(error);

		System.out.println("le pourcentage des erreurs rencotrees dans ce test fait "+ nbr + " fois est  "+((float)error/nbr)*100+"%");
		System.out.println("l'error moyen en unit?d'ulp est "+ ((float)errorTotal)/error );
		System.out.println("l'error max en unit?d'ulp est "+ maxError );

	}
		
		/**
		 * Test de cosinus
		 */
		public static void testCos() {
		System.out.println("Testing now : cos \n");
		float pas =(float)Tools.power(2f,-15);
		System.out.println(Math.cos(Tools.Pi));
		System.out.println(Tools.cos(Tools.Pi));
		int nbr=0;
		int error=0;
		float errorTotal=0;
		float maxError=0;
		//0.9 =PI/4
		for (float x= 0; x<= Tools.Pi_4 ;x+=pas){
			nbr+=1;
			float javaValue = (float)Math.cos(x);
			float ourValue = Tools.cos(x);
			System.out.println("Our value " + ourValue);
			System.out.println("Java value " + javaValue);
			float dif=(float)Math.abs(javaValue-ourValue);
			float diff =(dif/(float)Math.ulp(javaValue));
			if ((float)diff>1f) {
				error+=1;
				errorTotal+=diff;
				if(diff>maxError) {maxError=diff;}
			}	
		if (diff>100f) {System.out.println(x);
			}
		}
		System.out.println("finished");
		System.out.println(error);
		System.out.println("le pourcentage des erreurs rencotrees dans ce test fait "+ nbr + " fois est  "+((float)error/nbr)*100+"%");
		System.out.println("l'error moyen en united'ulp est "+ ((float)errorTotal)/error );
		System.out.println("l'error max en united'ulp est "+ maxError );

	}

		/**
		 * Test de l'arcsinus
		 */
		public static void testAsin() {
			System.out.println("Testing now : asin \n");
			float pas =(float)Tools.power(2f,-16);
			float test =0;
			System.out.println(Math.asin(test));
			System.out.println(Tools.asin(test));
			int nbr=0;
			int error=0;
			float errorTotal=0;
			float maxError=0;
			for (float x=0.2f;x<0.8f;x+=pas){
				nbr+=1;
				float javaValue=(float)Math.asin(x);
				float ourValue= Tools.asin(x);
				float dif=(float)Math.abs(javaValue-ourValue);
				float diff =(dif/(float)Math.ulp(javaValue));
				if ((float)diff>2f) {
					error+=1;
					errorTotal+=diff;
					if(diff>maxError) {maxError=diff;}
				}	
			}
			System.out.println("finished");
			System.out.println(error);
	
			System.out.println("le pourcentage des erreurs rencotrees dans ce test fait "+ nbr + " fois est  "+((float)error/nbr)*100+"%");
			System.out.println("l'error moyen en united'ulp est "+ ((float)errorTotal)/error );
			System.out.println("l'error max en united'ulp est "+ maxError );

		}
		
		/**
		 * Test de sinus
		 */
		public static void testSin() {
			System.out.println("Testing now :  SIN \n");
			float pas =(float)Tools.power(2f,-15);
			float test =0;
			System.out.println(Math.sin(test));
			System.out.println(Tools.sin(test));
	
			int nbr=0;
			int error=0;
			float errorTotal=0;
			float maxError=0;
			for (float x = 0; x <= Tools.Pi_4;x+=pas){
				nbr+=1;
				float javaValue=(float)Math.sin(x);
				
				float ourValue = Tools.sin(x);
				System.out.println("Our value " + ourValue);
				System.out.println("Java value " + javaValue);
				float dif=(float)Math.abs(javaValue-ourValue);

				float diff =(dif/(float)Math.ulp(javaValue));
	
				if ((float)diff>2f) {
					error+=1;
					errorTotal+=diff;
					if(diff>maxError) {maxError=diff;}
				}	
				if (diff>100f) {System.out.println(x);
				System.out.println(diff);
				}
			}
			System.out.println("finished");
			System.out.println(error);
	
			System.out.println("le pourcentage des erreurs rencotrees dans ce test fait "+ nbr + " fois est  "+((float)error/nbr)*100+"%");
			System.out.println("l'error moyen en united'ulp est "+ ((float)errorTotal)/error );
			System.out.println("l'error max en united'ulp est "+ maxError );

	}

	public static void main(String args[]){
		//testUlp();
		//testAtan();
		testCos();
		//testAsin();
		//testSin();
	}

}
