package dessin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import aaplication.App25CarAiLRIMa;
import image.processing.ImageManager;

/**
 * Classe de la scene de dessin du reseau de neurones
 * @author Gaya Mehenni
 * @author Simon Daze
 */
public class DessinNeuralNetwork extends JPanel implements Runnable{

	private DrawableNeuralNetwork neuralNetwork;

	private boolean enCours = false;

	// Configuration du reseau de neurone
	private final int[] configuation = {4, 5, 5, 4};

	/**
	 * Constructeur d'un dessin de neural network
	 */
	public DessinNeuralNetwork(){
		setBackground(Color.WHITE);

		neuralNetwork = new DrawableNeuralNetwork(2 * App25CarAiLRIMa.OFFSET, 2 * App25CarAiLRIMa.OFFSET,
				App25CarAiLRIMa.LARGEUR_PANEL_SECONDAIRE - 4 * App25CarAiLRIMa.OFFSET, App25CarAiLRIMa.OFFSET * 14 - 4 * App25CarAiLRIMa.OFFSET,
				configuation);
	}

	/**
	 * Methode qui dessine le reseau
	 * @param g contexte graphique
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		neuralNetwork.dessiner(g2d);
	}

	/**
	 * Methode qui anime le panel et qui contient la boucle d'animation
	 */
	public void run() {

		while(enCours) {

			neuralNetwork.changeNeurons();
			neuralNetwork.changeWeights();

			repaint();

			try {
				Thread.sleep(150);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Methode qui demarre l'application
	 */
	public void demarrer() {

		Thread thread = new Thread(this);
		thread.start();
		enCours = true;
	}

	/**
	 * Methode qui arrete l'animation
	 */
	public void stop() {
		enCours = false;
	}

}
