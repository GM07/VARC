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
 * @author Simon Daze
 *
 */
public class DessinNeuralNetwork extends JPanel implements Runnable{

	private final int DISTANCE_ENTRE_NEURONES = 50;
	private final Color COULEUR_INPUT_LAYER_NEURONES = Color.blue;
	private final Color COULEUR_HIDDEN_LAYER_NEURONES = Color.white;
	private final Color COULEUR_OUTPUT_LAYER_NEURONES = Color.red;
	private DrawableNeuralNetwork neuralNetwork;

	private boolean enCours = false;

	private final int[] configuation = {4, 5, 5, 4};

	public DessinNeuralNetwork(){
		setBackground(Color.WHITE);

		neuralNetwork = new DrawableNeuralNetwork(2 * App25CarAiLRIMa.OFFSET, 2 * App25CarAiLRIMa.OFFSET,
				App25CarAiLRIMa.LARGEUR_PANEL_SECONDAIRE - 4 * App25CarAiLRIMa.OFFSET, App25CarAiLRIMa.OFFSET * 14 - 4 * App25CarAiLRIMa.OFFSET,
				configuation);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		neuralNetwork.dessiner(g2d);
	}

	public void run() {

		while(enCours) {

			neuralNetwork.changeNeurons();
			neuralNetwork.changeWeights();

			repaint();

			try {
				Thread.sleep(75);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void demarrer() {

		Thread thread = new Thread(this);
		thread.start();
		enCours = true;
	}

}
