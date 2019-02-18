package math;

import java.io.Serializable;

public class MathTools implements Serializable {
	
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
	
	public static double[] getAsOneDimension(double[][] arr) {
		double[] array = new double[arr.length * arr[0].length];
		for(int i = 0; i < arr.length; i ++) {
			
			for(int s = 0; s < arr[i].length; s ++) {
				
				array[(i * arr[0].length) + s] = arr[i][s];
			}
		}

		return array;
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
