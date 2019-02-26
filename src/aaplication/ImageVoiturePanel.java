package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import image.processing.ImageManager;

/**
 * Classe qui permet d'afficher l'image d'un vehicule
 * @author Simon Daze
 *
 */
public class ImageVoiturePanel extends JPanel{

	private BufferedImage img;
	private BufferedImage imageCarre;
	private BufferedImage defaultImage;
	 
    public ImageVoiturePanel() {

        defaultImage = readImg("defaultImage.png");

    }// fin constructeur
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, getWidth(), getHeight());
        if (img != null) {
            imageCarre = ImageManager.getSquaredImage(img, getWidth());
            g2d.drawImage(imageCarre, 0, 0, getWidth(), getHeight(), null);
        } else {
            imageCarre = ImageManager.getSquaredImage(defaultImage, getWidth());
            g2d.drawImage(imageCarre, 0, 0, getWidth(), getHeight(), null);
        }
    }

    /**
     * Methode de lecture de l'image
     * @param nomFichier le chemin vers image a lire
     */
    private BufferedImage readImg(String nomFichier) {
        BufferedImage i;
        URL res = getClass().getClassLoader().getResource(nomFichier);
        try {
            i = ImageIO.read(res);
            return i;
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier d'image");
        }

        return null;
    }

    /**
     * Methode qui permet de modifier l'image de la classe avec un chemin
     * @param path chemin d'acces de l'image
     */
    public void setImage(String path) {
        try {
            img = ImageIO.read(new URL("file:///" + path));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur de lecture du fichier d'image");
        }
    }

    /**
     * Methode qui retourne l'image affichee sur le panel
     * @return image du panel
     */
    public BufferedImage getImage() {
        return img;
    }

    /**
     * Methode qui permet de modifier l'image de la classe
     * @param image l'image que l'on veut afficher
     */
    public void setImage(BufferedImage image) {
    	this.img = image;
    }

    /**
     * Retourne une version carree de l'image qui est affichee
     * @return image carree de l'image affichee
     */
    public BufferedImage getImageCarre() {
        return imageCarre;
    }

}
