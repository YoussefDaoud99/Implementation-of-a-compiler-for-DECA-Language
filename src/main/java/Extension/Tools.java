
package Extension;
import java.lang.Math;

/**
 *
 * @author Equipe 49
 * La bibliothèque mathématique contenant des fonctions trigonometriques et d'autres fonctions mathématiques
 */
public class Tools{

	//les constantes utilisées dans la méthode de cody and waite .
		static float C1 = 1.5f;
		static float C2 = 7.079632679E-2f;
		static float C3 = 4.8966E-12f;
		static float C4 = 1.923E-17f;
		static float C5 = 1.32169E-21f;
		static float C6 = 1.63975144E-27f;
		static float C7 = 2.098584E-36f;
		static float C8 = 6.99687553E-43f;
		static float  Pi = 3.14159265358979323846264338327950288419716939937510582f;
		static float Pi_2 = 1.57079632679489661923132169163975144209858469968755291f;
		static float Pi_4 = 0.7853981633974483f;
		//Valeur maximale prise par les nombres flottants.
		static float MAX_VALUE = power((float)(2),127);
		//Valeur minimale prise par les nombres flottants.
		static float MIN_VALUE = power((float)(2),-149);


	/**
	 * Une méthode sin approximant la fonction mathématiques sinus
	 * On utilise les séries entières + la méthode de Horner + la méthode de Réduction : Cody and Wait
	 * @param x  la valeur d'entrée
	 * @return la valeur approchée de sin(x)
	 */
	  public static float sin(float x){

		  int iterations = 10;

			int k = Math.round(x/(Pi_2));
			float xReduced = (((((((((x-k*C1)-k*C2)-k*C3))-k*C4)-k*C5)-k*C6) - k*C7) - k*C8);

		  	float result = 1/(float) (factorial(2*iterations + 1));
		  	int n = iterations - 1;
		  	int i = 2*iterations;

			if (x < 0) {
			    return -sin(-x);
			}

			if (x > Pi_4) {

				if (k % 4 == 0) {
					return sin(xReduced);
				}
				else if (k % 4 == 1) {
					return cos(xReduced);
				}
			  	else if (k % 4 == 2) {
				  return -sin(xReduced);
		    }
			  else {
				  return -cos(xReduced);
		    }
		  }

		  while (i >= 0) {

			  if (i%2==0){
			   result = result*x + 0;
			  }
			  else{
			  	if (n % 2 == 0) {
			    result = result*x + 1 / (float) (factorial(i));
			    }
			   else {
			      result = result*x + -1/(float) (factorial(i));
			    }
			    n = n - 1;
			  }
			  i = i - 1;
			}
			return result;
		}

	  /**
	 * Une méthode cos approximant la fonction mathématiques cosinus
	 * On utilise les séries entières + la méthode de Horner + la méthode de Réduction : Cody and Wait
	 * @param x : la valeur d'entrée
	 * @return la valuer approchée de cos(x)
	   */

	  public static float cos(float x ){

		  if (x < 0) {
				return cos(-x);
			}

		  int iterations = 10;
		  int k = Math.round(x/(Pi_2));
		  float xReduced=(((((((((x-k*C1)-k*C2)-k*C3))-k*C4)-k*C5)-k*C6) - k*C7) - k*C8 );
		  float result = 1/(float) (factorial(2*iterations));
		  int n = iterations - 1;
		  int i = 2*iterations-1;
		  if (x > Pi_4) {
			  if (k % 4 == 0) {
				  return cos(xReduced);
			  }
			  else if (k % 4 == 1) {
				  return -sin(xReduced);
			  }
			  else if (k % 4 == 2) {
				  return -cos(xReduced);
			  }
			  else {
				  return sin(xReduced);
			  }
		  }

		  while (i >= 0) {

			  if (i%2==1){
			    result = result*x + 0;
			  }
			  else{
			    if (n % 2 == 0) {
			      result = result*x + 1/(float) (factorial(i));
			    }
			    else {
			      result = result*x + -1/(float) (factorial(i));
			    }
			    n = n - 1;
		  	}
		  	i = i - 1;
		 }
		return result;
	  }


	  /**
		 * Une méthode cosSansHorner approximant la fonction mathématiques cosinus
		 * On utilise juste les séries entières
		 * @param x : la valeur d'entrée
		 * @return la valeir approchée de cos(x)
		 */


	  public float cosSansHorner(float x){
		  int iterations = 10;
		  if (x<0f){
	      return cosSansHorner(-x);
		  }
		  float result=0;
		  for (int i=0;i<iterations;i++){
			  int j=2*i;
			  if (i%2==0){
				  result +=power(x,j)/(float)factorial(j);
			  }
			  else{
				  result -=power(x,j)/(float)factorial(j);
			  }
		  }
		  return result;
	  }

	  /**
	   * Une fonction approchant la valeur de arctan
	   * On utilise l'expression de arctan en séries entières + des relations
	   * trigonometriques qui nous rapproches de 0 où on a une bonne approximation
	   * @param x la valeur d'entrée
	   * @return la valeur approchée de arctan(x)
	   */
	  public static float atan(float x){
		  int iterations = 40;
		  if (x<0f){
			  return -atan(-x);
		  }
		  else if(x>1.3f){
			  return (Pi/2)-atan(1f/x);
		  }
		  if(x>0.7f && x<=1.3f){
			  return 2*atan(x/(1+sqrt(1+power(x,2))));
		  }
		  float result=0;
		  for (int i=0;i<iterations;i++){
			  int j=2*i+1;
			  if (i%2==0){
				  result +=power(x,j)/j;
			  }
			  else{
				  result -=power(x,j)/j;
			  }
		  }
    //on utilise la méthode de Horner + serie de Taylor
    // float h=0f;
    // for(int i=1;i<=iterations;i+=2){
    //   h=(1f/i)-x*x*h;
    // }
    // return x*h;
		  return result;
	  }

	  /**
	   * Une fonction approchant la valeur de arcsin
	   * On utilise la relation entre arcsinus et arctangente
	   * @param x la valeur d'entrée
	   * @return la valeur approchée de arcsin(x)
	   */
	  public static float asin(float x){
		  if(abs(x)>1){
			  System.out.println("la valeur de x doit être compris entre -1 et 1 !!");
			  return 0;
		  }
		  if(x<0){
			  return -asin(-x);
		  }
		  else{
			  return atan(x/(sqrt(1-power(x,2))));
		  }
    // if(x>0.4f){
    //   return Pi/2 - asin(sqrt(1-power(x,2)),iterations);
    // }
    // else{
    //   float result=0;
    //   for(int i=0;i<iterations;i++){
    //     float fact_frac=factorial(2*i)/(power(2f,2*i)*power(factorial(i),2)*(2*i+1));
    //     result+=power(x,2*i+1)*fact_frac;
    //   }
    //   return result;

    // }
	  }

	  /**
	   * Une fonction calculant la puissance
	   * @param x la valeur d'entrée
	   * @param y l'exposant
	   * @return x à la puissance y
	   */
	  public static float power(float x,int y){
		  if (x==0.0 && y!=0){
			  return 0;
		  }
		  if(y==0){
			  return 1;
		  }
		  else if(y<0){
			  return power(1/x,-y);
		  }
		  else if(y==1){
			  return x;
		  }
		  else if (y%2==0){
			  return power(x*x,y/2);
		  }
		  else{
			  return x*power(x*x,(y-1)/2);
		  }
	  }

	  /**
	   * Calcul du factoriel
	   * @param n un entier
	   * @return n factoriel
	   */
	  public static int factorial(int n){
		  int result = 1;
		  int i = n;
		  while(i > 0) {
		    result = result*i;
		    	i = i - 1;
		    }
		  return result;
    // int result =1;
    // for (int i=1;i<=n;i++){
    //   result =result*i;
    // }
    // return result;
	  }

	  /**
	   * Extraire  l'exposant d'un flottant
	   * @param x un flottant
	   * @return n l'exposant de cet flottant .
	   */
	  public static int exponent(float x){
		  int exponent = 0;
		  float y = x;
		  if (y < 0) {
			  return exponent(-y);
		  }
		  if (y < 1) {
			  while (y < 1) {
				  y = y * 2;
				  exponent = exponent -1;
			  }
			  return exponent;
		  }
		  else {
			  while (y >= 2) {
				  y = y/2;
				  exponent = exponent + 1;
			  }
			  return exponent;
		  }
	  }

	  /**
	   * Calcule l'ulp d'un flottant x à l'aide des valeurs retournées par la fonction ulp de java
	   * et la formule de calcul d'ulp expliqué dans la documentation jointe à cette extension
	   * @param x un flottant
	   * @return u flottant
	   */
	  public static float ulp(float x){
		  //on a ulp(-x)=ulp(x)
		  if (x<0){
			  return ulp(-x);
		  }
		  //la meme valeur retournee par la fonction ulp de java
		  else if(x==MIN_VALUE){
			  return MIN_VALUE;
		  }
		  else if(x==MAX_VALUE){
			  return power(2f,104);
		  }
		  else if (x==0f){
			  return MIN_VALUE;
		  }
		  else {
			  int exp = exponent(x);
			  return power(2f,exp-23);
		  }
	  }

	  /**
	   * une méthode calculant la valeur absolue de x
	   * @param x la valeur d'entrée
	   * @return la valeur absolue de x
	   */
	  public static float abs(float x){
		  if(x<=0){
			  return -x;
		  }
		  else{
			  return x;
		  }
	  }


	  /**
	   * Une méthode calculant la racine d'une valeur en utilisant la méthode de Newton
	   * @param x La valeur d'entrée
	   * @return la racine de x
	   */
	  public static float sqrt(float x) {
		  if (x<0) {
			  throw new IllegalArgumentException("Le nombre doit être positif");
		  }
		  else if (x == 0) {
			  return 0;
		  }
		  else{
			  float sqrtt=1;
			  int nbrIterations=0;
			  for(nbrIterations=0;nbrIterations<200000;nbrIterations++){
				  float temp =sqrtt;
				  sqrtt=0.5f*(temp+(x/temp));
			  }
			  return sqrtt;
		  }
	  }

}
