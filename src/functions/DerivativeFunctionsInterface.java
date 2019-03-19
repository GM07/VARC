package functions;

/**
 * Interface que les derivees des fonctions d'activation doivent implementees
 * @author Gaya Mehenni
 */
public interface DerivativeFunctionsInterface {

    /**
     * Methode qui donne la valeur f(x) pour un certain x
     * @param x valeur
     * @return y de la fonction
     */
    double getValue(double x);
}
