package convolutional.neural.network;

import java.io.Serializable;

import math.Matrix;
/**
 * Les differents filtres qui seront apposes sur les images lors de leur passage dans le reseau
 * @author Simon Daze
 */
public class Filter extends Matrix implements Serializable{

	private static final long serialVersionUID = 601262895217910258L;

	/**
	 * Constructeur des filters
	 * @param rows : le nombre de ligne de la matrice
	 * @param cols : le nombre de colonnes de la matrice
	 */
	//Simon Daze
	public Filter(int rows, int cols) {
		super(rows, cols);
		this.initWithRandomValues(-1, 1);

	}

	/**
	 * Retourne la plus grande valeur dans une sous matrice de la taille du filter
	 * @return la matrice contenant toutes les valeurs maximales des sous-matrices nxn
	 * @param m = La matrice sur laquelle on applique le maxPooling
	 */
	
	public Matrix maxPool(Matrix m) {
		Matrix pooledMat = new Matrix(m.getROWS() - this.getROWS() + 1, m.getCOLS() - this.getROWS() + 1);
		for (int ofX = 0 ; ofX < m.getROWS() - this.getROWS() + 1; ofX++) {
			for(int ofY = 0; ofY < m.getCOLS() - this.getCOLS() + 1; ofY++) {
				double[][] matInput = new double[this.getROWS()][this.getCOLS()];
				for(int i = 0; i < this.getROWS() ; i++) {
					for(int j = 0 ; j < this.getCOLS() ; j++) {
						matInput[i][j] = m.getElement(ofX + i, ofY + j);
					}
				}
				Matrix inputMatrix = new Matrix(matInput);
				pooledMat.setElement(ofX, ofY, inputMatrix.getMaxValue());
			}

		}
		return pooledMat;

	}


	/**
	 * Retourne la valeur moyenne dans une sous matrice de la taille du filter
	 * @return la matrice contenant toutes les valeurs moyennes des sous-matrices nxn
	 * @param m = La matrice sur laquelle on applique le averagePooling
	 */
	
	public Matrix averagePool(Matrix m) {
		Matrix pooledMat = new Matrix(m.getROWS() - this.getROWS() + 1, m.getCOLS() - this.getROWS() + 1);
		for (int ofX = 0 ; ofX < m.getROWS() - this.getROWS() + 1; ofX++) {
			for(int ofY = 0; ofY < m.getCOLS() - this.getCOLS() + 1; ofY++) {
				double[][] matInput = new double[this.getROWS()][this.getCOLS()];
				for(int i = 0; i < this.getROWS() ; i++) {
					for(int j = 0 ; j < this.getCOLS() ; j++) {
						matInput[i][j] = m.getElement(ofX + i, ofY + j);
					}
				}
				Matrix inputMatrix = new Matrix(matInput);
				pooledMat.setElement(ofX, ofY, inputMatrix.getAverageValue());
			}

		}
		return pooledMat;
	}

	/**
	 * Test du pooling sur une layer 
	 * @param args
	 */

	public static void main(String[] args) {
		Filter f = new Filter(2,2);
		HorizontalFilter f2 = new HorizontalFilter();
		Matrix m = new Matrix(new double[][] 
				{{1,2,3,4},
			{2,3,4,5},
			{3,4,5,6},
			{6,7,8,9}
				});
		Matrix out = new Matrix();
		out = f2.convolution(m); 
		System.out.println(out);
		System.out.println("");
		System.out.println("Matrice avec le max Pooling");
		System.out.println(f.maxPool(out));
		
		
	}




}
