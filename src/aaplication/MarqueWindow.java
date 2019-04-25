package aaplication;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Fenetre secondaire des informations sur la marque
 * @author Simon Daze
 *
 */
public class MarqueWindow extends JFrame{
	//panels
	JPanel contentPane;
	
	//constants
	private final int LARGEUR_PRINCIPALE = 900;
	private final int HAUTEUR_PRINCIPALE = 600;
	private String marqueTxt = "Marque- BMW-1";

	private ImageAvecDefilement marque;
	
	
	/**
	 * Constructeur de la fenetre
	 */
	public MarqueWindow() {
		setTitle("Informations sur la marque");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(300,300,LARGEUR_PRINCIPALE,HAUTEUR_PRINCIPALE);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		 marque = new ImageAvecDefilement();
		
		marque.setBounds(50, 50, 800, 500);
		marque.setFichierImage( marqueTxt+".jpg");
		contentPane.add(marque);
		
		
		
		
	}
	
	/**
	 * Definie la marque dont on doit afficher l'image d'information
	 * @param marque : la marque a affichee
	 */
	public void setMarque(String marqueT) {
		this.marqueTxt = marqueT;
		marque.repaint();
	}
	
	/**
	 * retourne l'objet qui contient l'image des informarions sur les marques
	 * @return marque : l'objet sur les infos des marques
	 */
	public ImageAvecDefilement getMarque() {
		return this.marque;
	}
}
