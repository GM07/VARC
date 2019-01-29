package convolutional.neural.network;

import math.Matrix;

public class Filter extends Matrix {

	public Filter(int rows, int cols) {
		super(rows, cols);
	}


	public Matrix convolution(Matrix m) {

		int FINALROWS = m.getROWS() - this.getROWS() + 1;
		int FINALCOLS = m.getCOLS() - this.getCOLS() + 1;
		Matrix finalMatrix = new Matrix(FINALROWS, FINALCOLS);
		for(int ofX = 0; ofX < FINALROWS; ofX++) {
			for(int ofY = 0; ofY < FINALROWS; ofY++) {

				double[][] matInput = new double[this.getROWS()][this.getCOLS()];
				for(int i = 0; i < this.getROWS(); i++) {
					for(int j = 0; j < this.getCOLS(); j++) {

						matInput[i][j] = m.getMat()[ofX + i][ofY + j];
					}
				}

				Matrix inputMatrix = new Matrix(matInput);
				finalMatrix.setElement(ofX, ofY, inputMatrix.product(this).sumOfElements());

			}
		}

		return finalMatrix;
	}



}
