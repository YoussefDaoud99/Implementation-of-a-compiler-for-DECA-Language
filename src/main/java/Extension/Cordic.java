package Extension;

/**
 * La classe utilisée pour appliquer la méthode de Cordic
 * et un test associé
 */

public class Cordic {


		  static float MAX_VALUE = Float.MAX_VALUE;
		  static float MIN_VALUE = Float.MIN_VALUE;
		  // float MAX_VALUE = power((float)(2),127); pour deca
		  // float MIN_VALUE = power((float)(2),-149);
		  static float Pi = 3.14159265358979323846264338327950288419716939937510582f;
		  static float Pi_2 = 1.57079632679489661923132169163975144209858469968755291f;
		  static float Pi_4 = 0.7853981633974483f;
		  static float C1 = 1.5f;
		  static float C2 = 7.079632679E-2f;
		  static float C3 = 4.8966E-12f;
		  static float C4 = 1.923E-17f;
		  static float C5 = 1.32169E-21f;
		  static float C6 = 1.63975144E-27f;
		  static float C7 = 2.098584E-36f;
		  static float C8 = 6.99687553E-43f;
      private final static int iterations = 30;
      private final static float K = 0.60725293510314f;



	    /**
	     * Recupère la valeur de sin(theta) de la méthode cosAndsin(theta) et on utilise Cody and Wait
	     * @param theta La valeur d'entrée
	     * @return la valeur approchée de sin(theta)
	     */
	    public static float sin(float theta) {

	    	  int k = Math.round((theta / (C1+C2+C3+C4+C5+C6+C7+C8)));

			  float r = ((((((((theta-k*C1)-k*C2)-k*C3))-k*C4)-k*C5)-k*C6)-k*C7)-k*C8;


			  if (theta > Pi_4 || theta < -Pi_4) {
				  if (k % 4 == 0) {
					  return sin(r);
				  }
				  else if (k % 4 == 1) {
					  return cos(r);
				  }

				  else if (k % 4 == 2) {
					  return -sin(r);
				  }

				  else {
					  return -cos(r);
				  }

			  }
	        return sinAndCos(theta)[1];
	    }

	    /**
	     * Recupère la valeur de cos(theta) de la méthode cosAndsin(theta) et on utilise Cody and Wait
	     * @param theta La valeur d'entrée
	     * @return la valeur approchée de cos(theta)
	     */
	    public static float cos(float theta) {

	    	int k = Math.round((theta / (C1+C2+C3+C4+C5+C6+C7+C8)));

			  float r = ((((((((theta-k*C1)-k*C2)-k*C3))-k*C4)-k*C5)-k*C6)-k*C7)-k*C8;



			  if (theta > Pi_4 || theta < -Pi_4) {
				  if (k % 4 == 0) {
					  return cos(r);
				  }
				  else if (k % 4 == 1) {
					  return -sin(r);
				  }

				  else if (k % 4 == 2) {
					  return -cos(r);
				  }

				  else {
					  return sin(r);
				  }

			  }
	        return sinAndCos(theta)[0];
	    }

	    /**
	     * l'application de l'algorithme de cordic
	     * @param theta La valeur d'entrée
	     * @return la valeur approchée de cos(theta) et sin(theta)
	     */
	    public static float[] sinAndCos(float theta) {

	    	//On crée d'abord la liste des arctan(2^-i) dont on aura besoin
	        float[] tableAtan = new float[iterations];
	        for (int i=0; i<iterations; i++) {
	            tableAtan[i] = (float) Tools.atan((float) 1 / Tools.power(2, i));
	        }
	        // On commence à appliquer l'algorithme de Cordic
	        float cosAppro = K;
	        float sinAppro = 0f;
	        float theta1 = theta;
	        float t = 1f;
	        for (int i=0; i<iterations; i++) {
	            float sigma;
	        	if (theta1 >= 0) {
	            	sigma = 1;
	            }
	            else {
	            	sigma = -1;
	            }
	            float xk = cosAppro - sigma*sinAppro*t;
	            float yk = sinAppro + sigma*cosAppro*t;
	            float zk = theta1 - sigma*tableAtan[i];
	            cosAppro = xk;
	            sinAppro= yk;
	            theta1 = zk;
	            t *= 1/2f;
	        }
	        float[] result = {cosAppro, sinAppro};
	        return result;
	    }


	    /**
	     * Test de l'algorithme de Cordic
	     * @param args Aruments d'entrée
	     */
	    public static void main(String[] args) {
			int nbr=0;
			int error=0;
			float error_total=0;
			float max_error=0;
			float pas =(float)Tools.power(2f,-15);
	        for (float x=0;x<Tools.Pi_4;x+=pas){
				nbr+=1;
				float java_value=(float)Math.cos(x);
				float our_value=Cordic.cos(x);
				float dif=(float)Math.abs(java_value-our_value);
				System.out.println(our_value);
				float diff =(dif/(float)Math.ulp(java_value));
				if ((float)diff > 2f) {
					error += 1;
					error_total += diff;
					if(diff>max_error) {max_error=diff;}
				}
		}

			System.out.println("finished");
			System.out.println(error);

			System.out.println("le pourcentage des erreurs rencotrees dans ce test fait "+ nbr + " fois est  "+((float)error/nbr)*100+"%");
			System.out.println("l'error moyen en united'ulp est "+ ((float)error_total)/error );
			System.out.println("l'error max en united'ulp est "+ max_error );

	        }
	    }
