package aaplication;

import algorithm.CNNAI;
import convolutional.neural.network.CNN;

import javax.swing.*;
import java.awt.*;

/**
 * Classe qui affiche les etapes de convolution du reseau
 * @author Gaya Mehenni
 */
public class ConvolutionWindow extends JFrame {

    private JPanel contentPane;
    private CNNAI cnn;

    private final int sizeX = 600, sizeY = 600;

    /**
     * Constructeur avec un panel
     * @param cnn panel
     */
    public ConvolutionWindow(CNNAI cnn) {

        this.cnn = cnn;
        setUp();
    }

    /**
     * Methode qui initialise la fenetre avec ses composants
     */
    private void setUp(){
        setVisible(false);
        setTitle("Reseau de convolution");
        setBounds(0, 0, sizeX, sizeY);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBounds(0, 0, sizeX, sizeY);
        contentPane.setLayout(null);
        setContentPane(contentPane);

        cnn.setBackground(Color.BLACK);
        cnn.setBounds(0, 0, sizeX - 15, sizeY - 38);
        contentPane.add(cnn);

    }
}
