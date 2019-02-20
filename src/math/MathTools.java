package math;

import java.io.Serializable;
import java.util.Arrays;

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

	/**
	 * Transforme un 2D array en 1D array
	 * @param arr
	 * @return
	 */
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

	public static double[] mapArray(double[] in, double current1, double current2, double target1, double target2) {

		double[] out = new double[in.length];
		for(int i = 0 ;i < in.length; i++) {
			out[i] = target1 + (target2 - target1) * ((in[i] - current1) / (current2 - current1));
		}

		return out;
	}
}
