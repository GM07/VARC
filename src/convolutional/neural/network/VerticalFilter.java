package convolutional.neural.network;

import math.Matrix;

public class VerticalFilter extends Filter{

	public VerticalFilter() {
		super(3,3);
		double[][] filter = {
				{1,0,-1},
				{2,0,-2},
				{1,0,-1}
		};
		this.setMat(filter);




	}

	public Matrix convolution(Matrix m) {
		return super.convolution(m);
	}
}


