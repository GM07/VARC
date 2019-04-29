package functions;

/**
 * Interface que les fonctions d'activation doivent implementees
 * @author Simon Daze
 */
public interface ActivationFunctionsInterface {

    /**
     * Methode qui donne la valeur f(x) pour un certain x
     * @param x valeur
     * @return y de la fonction
     */
    double getValue(double x);

    /**
     * Methode qui retourne la derivee de la fonction
     * @return fonction derivee
     */
    DerivativeFunctions getDerivative();

}
