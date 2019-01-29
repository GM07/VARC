package math;

public class MathTools {
	
	/**
	 * Returns the index of the highest number in an array
	 * @param a : array
	 * @return
	 */
	public static int getHighestIndex(double[] a) {
		int index = 0;
		
		for(int i = 1; i < a.length; i++) {
			if (a[index] < a[i]) {
				index = i;
			}
		}
		
		return index;
	}
	
	/**
	 * @param inputs : input array
	 * @return
	 */
	public static double[][] getAsTwoDimensionalArray(double[] inputs) {
		double[][] in = new double[inputs.length][1];

		for(int i = 0; i < in.length; i++) {

			for(int j = 0; j < in[i].length; j++) {

				in[i][j] = inputs[i];
			}
		}

		return in;
	}
}
