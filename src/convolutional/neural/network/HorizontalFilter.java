package convolutional.neural.network;

import java.io.Serializable;

import math.Matrix;

/**
 * Filtre horizontal pour detecte les contours
 * @author Simon Daze
 *
 */
public class HorizontalFilter extends Filter implements Serializable {

	
	private static final long serialVersionUID = 6538563290814677755L;

	/**
	 * Constructeur d'un filtre horizontal
	 */
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