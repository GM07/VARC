package aaplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImageVisible extends JPanel{

	private Image img = null;
	 
    public ImageVisible() {
        setBackground(Color.green);
        lireImage("bob.JPG");
 
    }// fin constructeur
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
    }
 
    private void lireImage(String nomFichier) {
        URL fich = getClass().getClassLoader().getResource(nomFichier);
        if (fich == null) {
            JOptionPane.showMessageDialog(null, "Fichier " + nomFichier + " introuvable!");
        } else {
            try {
                img = ImageIO.read(fich);
            } catch (IOException e) {
                System.out.println("Erreur de lecture du fichier d'image");
            }
        }
    }
}
