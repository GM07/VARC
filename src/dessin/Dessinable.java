package dessin;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Interface que doit prendre un objet dessinable
 * @author Gaya Mehenni
 */
public interface Dessinable {

	/**
	 * Methode qui dessine un objet
	 * @param g2d contexte graphique
	 */
	public void dessiner(Graphics2D g2d);

}
