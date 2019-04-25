package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import image.processing.ImageManager;
/**
 * Classe qui permet de lire et d'afficher l'image de la marque de la voiture dans une scene
 * @author Simon Daze
 *
 */
public class MarqueImage extends JPanel {


		private BufferedImage img = null;
		 
		/**
		 * Constructeur de la fenetre
		 */
	    public MarqueImage() {
	    	setBounds(100, 100, 300, 300);
	        setBackground(Color.BLACK);
	        readImg("marque1.jpg");
	 
	    }// fin constructeur
	 
	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        BufferedImage imageCarre = ImageManager.getSquaredImage(img, 150);
	        g2d.drawImage(imageCarre,0, 0, imageCarre.getWidth(null) , imageCarre.getHeight(null), null);
	    }
	 /**
	  * Methode qui permet de lire une image
	  * @param nomFichier Le nom du fichier d'image
	  */
	    private void readImg(String nomFichier) {
	        URL res = getClass().getClassLoader().getResource(nomFichier);
	        if (res == null) {
	            JOptionPane.showMessageDialog(null, "Fichier " + nomFichier + " introuvable!");
	        } else {
	            try {
	                img = ImageIO.read(res);
	            } catch (IOException e) {
	                System.out.println("Erreur de lecture du fichier d'image");
	            }
	        }
	    }
	    /**
	     * Methode qui permet de modifie l'image en memoire dans la classe
	     * @param image a mettre en propriete dans la classe
	     */
	    public void setImage(BufferedImage image) {
	    	this.img = image;
	    }

}
