package convolutional.neural.network;

import math.Matrix;

public class HorizontalFilter extends Filter {

	


	public HorizontalFilter() {
		super(3,3);
		double[][] filter = {
				{1,2,1},
				{0,0,0},
				{-1,-2,-1}
		};
		this.setMat(filter);

		


	}

	public Matrix convolution(Matrix m) {
		return super.convolution(m);
	}

}