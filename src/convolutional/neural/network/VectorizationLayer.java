package convolutional.neural.network;

import math.Matrix;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe qui permet de prendre des valeurs en 3D (
 */
public class VectorizationLayer extends CNNLayer {

    // Variable qui retient la taille des matrices en entree -> Toutes les matrices ont la meme taille
    private int inputSize;

    // Variable qui retient le nombre de matrices avant la vectorisation
    private int numberOfMatrices;

    public VectorizationLayer(Matrix[] inputs, int inputSize) {
        this.inputs = inputs;
        this.numberOfMatrices = inputs.length;
        this.inputSize = inputSize;

        outputs = new Matrix[1];

        outputs[0] = new Matrix(inputs.length * inputSize * inputSize, 1);
    }

    /**
     * Methode qui linearise les sorties
     * @return le vecteur contenant les outputs
     */
    public Matrix[] operation() {

        outputs[0] = new Matrix(inputs.length * inputSize * inputSize, 1);

        for(int mat = 0; mat < inputs.length; mat++) {

            // On parcoure chaque matrices d'entree

            for(int i = 0; i < inputSize; i++) {
                for(int j = 0; j < inputSize; j++) {

                    outputs[0].setElement(mat * inputSize * inputSize + j * inputSize + i, 0, inputs[mat].getElement(i, j));
                }
            }
        }

        //System.out.println("AHH " + outputs[0].getMatrixSize());

        return outputs;


        /*
        int linearizedMatrixSize = 0;
        Matrix linearizedOutputs;
        ArrayList<Double> arrayOutput  = new ArrayList<Double>();
        for (int f = 0 ; f < outputs.length; f++ ) {
            for (int i = 0 ; i < outputs[f].getROWS(); i++) {
                for (int j = 0 ; j < outputs[f].getCOLS(); j++) {
                    arrayOutput.add(outputs[f].getElement(i, j));
                    linearizedMatrixSize += 1;
                }
            }
        }
        linearizedOutputs = new Matrix(1,linearizedMatrixSize);
        for(int i = 0; i < linearizedMatrixSize ; i++) {
            linearizedOutputs.setElement(0, i, arrayOutput.get(i));
        }

        Matrix[] mat = new Matrix[1];
        mat[0] = linearizedOutputs;
        outputs = mat;
        return mat;
        */
    }

    /**
     * Methode qui transforme une ligne d'erreur en tableau de matrices d'erreur en fonction de la taille d'entree
     * Voir explications scientifiques pour plus de details
     * @return tableau de matrice contenant l'erreur de la couche
     */
    public Matrix[] backpropagation(Matrix[] target) {

        Matrix[] error = new Matrix[numberOfMatrices];

        for(int mat = 0; mat < error.length; mat++) {

            Matrix a = new Matrix(inputSize, inputSize);

            for(int i = mat * inputSize * inputSize; i < (mat + 1) * inputSize * inputSize; i++) {

                int col = ((i % (inputSize * inputSize)) / inputSize);
                int row = ((i % (inputSize * inputSize)) % inputSize);

                a.setElement(row, col, target[0].getElement(i, 0));
            }

            error[mat] = a;
        }

        return error;

    }

    /**
     * Methode qui teste les methodes de la couche
     * @param args
     */
    public static void main(String[] args) {

        Matrix[] i = new Matrix[3];

        i[0] = new Matrix(3, 3, 1);
        i[1] = new Matrix(3, 3, 2);
        i[2] = new Matrix(3, 3, 3);

        Matrix[] j = new Matrix[1];
        j[0] = new Matrix(3 * 3 * 3, 1, 0.5);

        VectorizationLayer vl = new VectorizationLayer(i, 3);

        vl.operation();

        System.out.println(vl);
        System.out.println(Arrays.toString(vl.backpropagation(j)));
    }
}
