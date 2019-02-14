package dessin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

/**
 * Objet dessinable pour representer un neurone
 * @author Simon Daze
 *@param DIAMETRE : le diametre du neurone
 *@Param cercle : un objet Ellipse2D.double pour illustrer le  neurone
 *@param Color : la couleur du neurone
 */
public class Neurones implements Dessinable{
	private final int DIAMETRE = 25;
	private Ellipse2D.Double cercle;
	private Color color;
	
	/**
	 * Construteur du neurone : on definit la taille du cercle le representant
	 */
	public Neurones() {
		this.cercle = new Ellipse2D.Double(0,0,DIAMETRE,DIAMETRE) ;
	}

	@Override
	/**
	 * @param g2d: le contexte graphique
	 */
	public void dessiner(Graphics2D g2d) {
		 		
		 g2d.setColor(this.color);
		 g2d.fill(this.cercle);
	}
	/**
	 * 
	 * @param couleur : la couleur a donne au neurone
	 */

	public void setColor(Color couleur) {
		this.color = couleur;
		
	}

	@Override
	public void dessiner(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	

}
