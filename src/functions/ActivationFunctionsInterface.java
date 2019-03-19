package functions;

public interface ActivationFunctionsInterface {

    /**
     * @param x valeur
     * @return y de la fonction
     */
    double getValue(double x);

    /**
     * Retourne la fonction derivee
     * @return
     */
    DerivativeFunctions getDerivative();

}
