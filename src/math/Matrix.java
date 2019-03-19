package math;

import java.io.Serializable;

import functions.ActivationFunctions;

/**
 * Classe utilisee pour representer des matrices
 * @author Gaya Mehenni
 * @author Simon Daze
 */

public class Matrix implements Serializable {


	private static final long serialVersionUID = -404749125068503936L;
	private double[][] mat;
	private int ROWS, COLS;

	/**
	 * Constructeur sans parametre pour la sauvegarde du fichier
	 */
	public Matrix() {}

	/**
	 * Constructeur ou les dimensions sont passees en parametres
	 * @param rows le nombre de lignes
	 * @param cols le nombre de colonnes
	 */
	public Matrix(int rows, int cols) {
		mat = new double[rows][cols];
		this.ROWS = rows;
		this.COLS = cols;

	}

	/**
	 * Constructeur a l'aide du matrice
	 * @param mat la matrice a copiee
	 */
	public Matrix(double[][] mat) {
		this.mat = mat;
		this.ROWS = mat.length;
		this.COLS = mat[0].length;
	}

	/**
	 * Constructeur avec dimensions pour une valeur specifique
	 * @param rows le nombre de lignes
	 * @param cols le nombre de colonnes
	 * @param value la valeur a laquelle la matrice est initialisee
	 */ 
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
	 * modifie la matrice a une certaines valeurs
	 * @param value la valeur a laquelle on initie la matrice
	 */
	public void setValue(double value) {
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				mat[i][j] = value;
			}
		}
	}

	/**
	 * Intialise la matrice a des valeurs par defauts ( de base entre -1 et 1)
	 */
	public void initWithRandomValues() {
		this.initWithRandomValues(-1, 1);
	}

	/**
	 * Intialise la matrice a des valeurs entre 2 bornes
	 * @param	lowerBound la borne inferieure
	 * @param	upperBound la borne superieure
	 */
	public void initWithRandomValues(double lowerBound, double upperBound) {
		for(int i = 0; i < ROWS; i++) {

			for(int j = 0; j < COLS; j++) {

				mat[i][j] = (Math.random() * (upperBound - lowerBound)) + lowerBound;
			}
		}
	}

	/**
	 * Addition de matrices
	 * @param	m	la matrice a additioner
	 */
	public void add(Matrix m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] += m.getMat()[i][j];
			}
		}
	}

	/**
	 * Addition de matrices
	 * @param	m	un tableau de doubles a ajouter a la matrice
	 */
	public void add(double[][] m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] += m[i][j];
			}
		}
	}

	/**
	 * Addition de matrices
	 * @param	m	la premiere matrice
	 * @param	m2	la deuxieme matrice
	 * @return		la somme des 2 matrices
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
	 * Soustraction de matrices
	 * @param	m	la premiere matrice
	 * @param	m2	la deuxieme matrice
	 * @return		la difference entre les 2 matrices
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
	 * Soustraction de matrices
	 * @param	m	la matrice a soustraire
	 */
	public void subtract(Matrix m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] -= m.getMat()[i][j];
			}
		}
	}

	/**
	 * Soustraction de matrices
	 * @param	m	un tableau de doubles a soustraire a la matrice
	 */
	public void subtract(double[][] m) {
		for(int i = 0; i < mat.length; i++) {

			for(int j = 0; j < mat[i].length; j++) {

				mat[i][j] -= m[i][j];
			}
		}
	}

	/**
	 * Multiplication matricielle
	 * @param	m	la matrice qui multiplie la matrice (this.multiply(m))
	 * @return 		Le produit matriciel 
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
	 * produit matriciel d'Hadamard 
	 * @param m la matrice a multiplie
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

	/**
	 * produit matriciel d'une matrice avec elle meme
	 * @return la matrice au carre
	 */
	public Matrix getSquaredMatrix(){
		return this.product(this);
	}

	/**
	 * Multiplie tous les elements de la matrice par un scalaire
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
	 * Fais la somme des elements de la matrice
	 * @return la somme de tous les elements de la matrice
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
	 * mofifier la valeur d'un element a la position (i,j)
	 * @param i la ligne 
	 * @param j la colonne 
	 * @param value la valeur a laquelle on modifie la valeur
	 */
	public void setElement(int i, int j, double value) {
		mat[i][j] = value;
	}

	/**
	 * retourne l'element de la position (i,j)
	 * @param i la ligne de l'element
	 * @param j la colonne de l'element
	 * @return l'element en (i,j)
	 */
	public double getElement(int i, int j) {
		return mat[i][j];
	}

	/**
	 * Applique une fonction a tous les elements de la matrice
	 * @param function la fonction a appliquer
	 * @return une nouvelle matrice activee par la fonction 
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
	 * Applique la derivee d'une fonction a tous les elements de la matrice
	 * @param function la fonction 
	 * @return une nouvelle matrice affectee par la derive de la fonction
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
	 * Transpose la matrice
	 * @return la matrice transposee
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
	 * Println d'une matrice
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
	 * Ajout d'un padding a une matrice (contours de 0)
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

	/**
	 * Retourne la taille de la matrice
	 * @return la taille de la matrice
	 */
	public String getMatrixSize() {
		return "" + ROWS + "x" + COLS;
	}

	/**
	 * Retourne la matrice sous forme de tableau a 2 dimensions
	 * @return		la matrice a sous forme de tableaux a 2 dimensions
	 */
	public double[][] getMat() {
		return mat;
	}

	/**
	 * Modifie la matrice
	 * @param mat un tableau a 2 dimensions
	 */
	public void setMat(double[][] mat) {
		this.mat = mat;
	}

	/**
	 * Modifie la matrice 
	 * @param m		une autre matrice
	 */
	public void setMat(Matrix m) {
		this.mat = m.getMat();
	}

	/**
	 * Change le nombre de rangee de la matrice
	 * @param ROWS 
	 */
	public void setROWS(int ROWS) {
		this.ROWS = ROWS;
	}

	/**
	 * Retourne le nombre de lignes de la matrice
	 * @return		le nombre de lignes de la matrice
	 */
	public int getROWS() {
		return ROWS;
	}

	/**
	 * Change le nombre de colonnes de la matrice
	 * @param COLS
	 */
	public void setCOLS(int COLS) {
		this.COLS = COLS;
	}

	/**
	 * Retourne le nombre de colonnes de la matrice
	 * @return		Nombre de colonnes de la matrice
	 */
	public int getCOLS() {
		return COLS;
	}

	/**
	 * Methode qui retourne la valeur maximale dans une matrice
	 * @return la valeur maximale dans la matrice
	 */
	//Auteur : Simon Daze
	public double getMaxValue() {
		double maxValue = (Double.MAX_VALUE * -1) -1, currentValue;
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

	/**
	 * Test de methodes sur les matrices
	 * @param args
	 */
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



}
