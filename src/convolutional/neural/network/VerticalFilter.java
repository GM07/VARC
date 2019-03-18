package convolutional.neural.network;

import math.Matrix;

/**
 * filtre vertical pour detecter les contours verticaux d'une image
 * @author Simon Daze
 */
public class VerticalFilter extends Filter{

	private static final long serialVersionUID = 843250495998652002L;

	/**
	 * Constructeur d'un filtre vertical
	 */
	public VerticalFilter() {
		super(3,3);
		double[][] filter = {
				{1,0,-1},
				{2,0,-2},
				{1,0,-1}
		};
		this.setMat(filter);




	}

	/**
	 * Convolution avec ce filtre
	 */
	public Matrix convolution(Matrix m) {
		return super.convolution(m);
	}
}


