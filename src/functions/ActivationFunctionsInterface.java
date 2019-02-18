package functions;

public interface ActivationFunctionsInterface {

    /**
     * @param	x	value
     * @return		output of the function
     */
    double getValue(double x);

    /**
     * @return		derivative of the function
     */
    DerivativeFunctions getDerivative();

    double getValueOfInverse(double x);
}
