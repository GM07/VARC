package math;

import java.io.Serializable;

import functions.ActivationFunctions;

/**
 * @author Gaya Mehenni
 * @author Simon Daze
 */

public class Matrix implements Serializable {


	private static final long serialVersionUID = -404749125068503936L;
	private double[][] mat;
	private int ROWS, COLS;

	public Matrix() {}

	public Matrix(int rows, int cols) {
		mat = new double[rows][cols];
		this.ROWS = rows;
		this.COLS = cols;

	}

	public Matrix(double[][] mat) {
		this.mat = mat;
		this.ROWS = mat.length;
		this.COLS = mat[0].length;
	}

	public Matrix(int rows, int cols, double value) {
		mat = new double[rows][cols];
		this.ROWS = rows;
		this.COLS = cols;

		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				mat[i][j] = value;
			}
		}
	}

	/**
	 * Sets the value of each number in the matrix to a certain number
	 * @param value
	 */
	public void setValue(double value) {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				mat[i][j] = value;
			}
		}
	}

	/*
	 * Initialize with random values (default : between 0 and 1)
	 */
	public void initWithRandomValues() {
		this.initWithRandomValues(-1, 1);
	}

	/**
	 * Initialize with random values between two bounds
	 * @param	lowerBound
	 * @param	upperBound
	 */
	public void initWithRandomValues(double lowerBound, double upperBound) {
		for(int i = 0; i < ROWS; i++) {

			for(int j = 0; j < COLS; j++) {

				mat[i][j] = (Math.random() * (upperBound - lowerBound)) + lowerBound;
			}
		}
	}

	/**
	 * Matrix addition
	 * @param	m	matrix to add in argument
	 */
	public void add(Matrix m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] += m.getMat()[i][j];
			}
		}
	}

	/**
	 * Matrix addition
	 * @param	m	two dimensional array of doubles to add to this matrix
	 */
	public void add(double[][] m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] += m[i][j];
			}
		}
	}

	/**
	 * Matrix addition
	 * @param	m	first Matrix
	 * @param	m2	second Matrix
	 * @return		sum of the two matrices
	 */
	public static Matrix sum(Matrix m, Matrix m2) {

		Matrix tempM = new Matrix(m.getROWS(), m.getCOLS());

		if (m.getCOLS() == m2.getCOLS() && m.getROWS() == m.getROWS()) {

			for (int i = 0; i < m.getROWS(); i++) {

				for (int j = 0; j < m.getCOLS(); j++) {

					tempM.getMat()[i][j] = m.getMat()[i][j] + m2.getMat()[i][j];
				}
			}
		}

		return tempM;
	}

	/**
	 * Matrix subtraction
	 * @param	m	first Matrix
	 * @param	m2	second Matrix
	 * @return		difference of the two matrices
	 */
	public static Matrix subtract(Matrix m, Matrix m2) {

		Matrix tempM = new Matrix(m.getROWS(), m.getCOLS());

		if (m.getCOLS() == m2.getCOLS() && m.getROWS() == m.getROWS()) {

			for (int i = 0; i < m.getROWS(); i++) {

				for (int j = 0; j < m.getCOLS(); j++) {

					tempM.getMat()[i][j] = m.getMat()[i][j] - m2.getMat()[i][j];
				}
			}
		}

		return tempM;
	}


	/**
	 * Matrix subtraction
	 * @param	m	matrix to subtract
	 */
	public void subtract(Matrix m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] -= m.getMat()[i][j];
			}
		}
	}

	/**
	 * Matrix subtraction
	 * @param	m	two dimensional array of doubles to subtract to this matrix
	 */
	public void subtract(double[][] m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] -= m[i][j];
			}
		}
	}

	/**
	 * Matrix multiplication
	 * @param	m	the Matrix to multiply in argument (THIS * PARAM)
	 * @return 		the dot product of the two Matrices	
	 * 
	 */
	public Matrix multiply(Matrix m) {

		double[][] matr = new double[this.getROWS()][m.getCOLS()];

		if (COLS != m.getROWS()){
			System.out.println("CAN'T BE MULTIPLIED : " + getMatrixSize() + ", " + m.getMatrixSize());
			return null;
		}

		for (int i = 0; i < this.getROWS();i++) {

			for(int j = 0; j < m.getCOLS();j++) {

				double sum = 0;
				for(int k = 0; k < this.getCOLS();k++) {

					sum += this.mat[i][k] * m.getMat()[k][j];

				}
				matr[i][j]=sum;
			}
		}

		Matrix finalMatrix = new Matrix(matr);
		return finalMatrix;
	}

	/**
	 * Hadamard's product on matrices
	 * @param m
	 * @return
	 */
	public Matrix product(Matrix m) {

		if (m.ROWS == ROWS && m.COLS == COLS) {
			Matrix finalMatrix = new Matrix(this.ROWS, this.COLS);

			for(int i = 0; i < ROWS; i++) {

				for(int j = 0; j < COLS; j++) {

					finalMatrix.setElement(i, j, mat[i][j] * m.getElement(i, j));

				}
			}

			return finalMatrix;

		} else {
			System.out.println("Matrices does not have the same size (" + ROWS + "x" + COLS + ") vs (" + m.getROWS() + "x" + m.getCOLS() + ")");
			return null;
		}
	}

	public Matrix getSquaredMatrix(){
		return this.product(this);
	}

	/**
	 * Multiplies every element by a scalar
	 */
	public Matrix scalarProduct(double k) {
		Matrix m = new Matrix(ROWS, COLS);
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				m.setElement(i, j, mat[i][j] * k);
			}
		}
		return m;
	}

	/**
	 * Calculates the sum of every element in the matrix
	 * @return
	 */
	public double sumOfElements() {

		double sum = 0;
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				sum += mat[i][j];
			}
		}

		return sum;
	}

	/**
	 * Sets the element at the position i,j to a certain value
	 * @param i
	 * @param j
	 * @param value
	 */
	public void setElement(int i, int j, double value) {
		mat[i][j] = value;
	}

	/**
	 * Returns the element at the position i, j
	 * @param i
	 * @param j
	 * @return
	 */
	public double getElement(int i, int j) {
		return mat[i][j];
	}

	/**
	 * Apply a certain function to every element of the matrix
	 * @param function
	 * @return
	 */
	public Matrix applyFunction(ActivationFunctions function) {

		double[][] a = new double[ROWS][COLS];

		for(int i = 0; i < ROWS; i++) {

			for(int j = 0; j < COLS; j++) {

				a[i][j] = function.getValue(mat[i][j]);
			}
		}

		return new Matrix(a);
	}

	/**
	 * Apply the derivative of a certain function to every element of the matrix
	 * @param function
	 * @return
	 */
	public Matrix applyFunctionDerivative(ActivationFunctions function) {

		double[][] a = new double[ROWS][COLS];

		for(int i = 0; i < ROWS; i++) {

			for(int j = 0; j < COLS; j++) {

				a[i][j] = function.getDerivative().getValue(mat[i][j]);
			}
		}

		return new Matrix(a);
	}

	/**
	 * Transposes the matrix
	 * @return
	 */
	public Matrix transpose() {
		Matrix matTranspose = new Matrix(getCOLS(), getROWS());

		for(int i = 0; i < getCOLS(); i++) {
			for (int j = 0; j < getROWS(); j++) {
				matTranspose.setElement(i, j, this.getElement(j, i));
			}
		}
		return matTranspose;
	}

	/**
	 * Print the matrix
	 */
	@Override
	public String toString() {

		String chain="MATRIX ----- \n";
		for (int i=0; i<this.getROWS();i++) {
			for(int j=0;j<this.getCOLS();j++) {
				chain += mat[i][j] + "\t";

			}
			chain += "\n";
		}
		return chain;
	}

	/**
	 *  ajout d'un padding a une matrice (contours de 0)
	 * @param paddingSize le nombre de rangee + colonne de 0 qui entoure la matrice
	 * @return une nouvelle matrice entouree de couches de 0 
	 */
	public Matrix padding(int paddingSize) {
		double[][] m = new double[ROWS + paddingSize * 2][COLS + paddingSize * 2];


		for(int i = 0; i < m.length; i++) {

			for(int j = 0; j < m[i].length; j++) {

				if ((i >= paddingSize && i < m.length - paddingSize)
						&& ((j >= paddingSize && j < m[i].length - paddingSize))) {
					m[i][j] = mat[i - paddingSize][j - paddingSize];
				} else m[i][j] = 0;

			}
		}

		return new Matrix(m);
	}

	public String getMatrixSize() {
		return "" + ROWS + "x" + COLS;
	}

	/**
	 * Returns the Matrix
	 * @return		The matrix in a two dimensional array
	 */
	public double[][] getMat() {
		return mat;
	}

	/**
	 * Changes the matrix
	 * @param mat two dimensional array
	 */
	public void setMat(double[][] mat) {
		this.mat = mat;
	}

	/**
	 * Changes the matrix
	 * @param m		Matrix object
	 */
	public void setMat(Matrix m) {
		this.mat = m.getMat();
	}

	/**
	 * Returns the number of rows of the matrix
	 * @return		Number of rows of the matrix
	 */
	public int getROWS() {
		return ROWS;
	}

	/**
	 * Returns the number of columns of the matrix
	 * @return		Number of columns of the matrix
	 */
	public int getCOLS() {
		return COLS;
	}

	public static void main(String[] args) {
		Matrix m1 = new Matrix(new double[][]{
			{1, 2, 1},
			{0, 0, 0},
			{1, 2, 1}
		});

		Matrix m2 = new Matrix(new double[][]{
			{1, 0, 1},
			{2, 0, 2},
			{1, 0, 1}
		});

		System.out.println(m1.scalarProduct(3));
		System.out.println(m1.transpose());

	}
	
	public double getMaxValue() {
		double maxValue = 0 , currentValue;
		for(int i = 0 ; i < ROWS ; i++) {
			for (int j = 0 ; j < COLS ; j++) {
				currentValue = mat[i][j];
				if(currentValue > maxValue) {
					maxValue = currentValue;
				}
			}
		}
		return maxValue;
	}



}
