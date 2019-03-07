package convolutional.neural.network;

import math.Matrix;

/**
 * Filtre horizontal pour detecte les contours
 * @author Simon Daze
 *
 */
public class HorizontalFilter extends Filter {

	


	/**
	 * 
	 */
	private static final long serialVersionUID = 6538563290814677755L;

	public HorizontalFilter() {
		super(3,3);
		double[][] filter = {
				{1,2,1},
				{0,0,0},
				{-1,-2,-1}
		};
		this.setMat(filter);

		


	}

	/**
	 * convolution avec ce filtre
	 */
	public Matrix convolution(Matrix m) {
		return super.convolution(m);
	}

}