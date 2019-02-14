package dessin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import image.processing.ImageManager;

/**
 * Classe de la scene de dessin du reseau de neurones
 * @author Simon Daze
 *
 */
public class NnDraw extends JPanel {

	private final int DISTANCE_ENTRE_NEURONES = 50;
	private final Color COULEUR_INPUT_LAYER_NEURONES = Color.blue;
	private final Color COULEUR_HIDDEN_LAYER_NEURONES = Color.white;
	private final Color COULEUR_OUTPUT_LAYER_NEURONES = Color.red;
	Neurones neurones = new Neurones();

	public NnDraw(){
		setBounds(100, 100, 300, 300);
		setBackground(Color.BLACK);
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		neurones.setColor(COULEUR_INPUT_LAYER_NEURONES);
		g2d.translate(25,0);
		for (int i = 0 ; i < 3 ; i++) {
			neurones.dessiner(g2d);
			g2d.translate(0, DISTANCE_ENTRE_NEURONES);
		}
		g2d.translate(DISTANCE_ENTRE_NEURONES,-(3 * DISTANCE_ENTRE_NEURONES));
		neurones.setColor(COULEUR_HIDDEN_LAYER_NEURONES);
		for (int j = 0 ; j < 2; j++) {
			g2d.translate(0, DISTANCE_ENTRE_NEURONES *0.75 );
			neurones.dessiner(g2d);
		}
		g2d.translate(DISTANCE_ENTRE_NEURONES, -1.5 * DISTANCE_ENTRE_NEURONES);
		neurones.setColor(COULEUR_OUTPUT_LAYER_NEURONES);
		for(int z = 0; z < 3 ; z++ ) {
			neurones.dessiner(g2d);
			g2d.translate(0, DISTANCE_ENTRE_NEURONES);
		}
	}


}
