package aaplication;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 * Fenetre d'aide et d'explocation du fonctionnement de l'application
 * @author Simon Daze
 *
 */
public class HelpWindow extends JFrame {
	

	//panels
	JPanel contentPane;
	
	//constantes
	private final int LARGEUR_PRINCIPALE = 900;
	private final int HAUTEUR_PRINCIPALE = 600;
	private final int OFFSET = 25;
	
	//Etiquettes
	JLabel lblEtapes;
	JLabel lblChargerReseau;
	JLabel lblChoixCharger;
	JLabel lblChoixImage;
	JLabel lblChoixEntrainer;
	JLabel lblTester;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HelpWindow frame = new HelpWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public HelpWindow() {
		setTitle("Aide");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(300,300,LARGEUR_PRINCIPALE,HAUTEUR_PRINCIPALE);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
         
        ImageAvecDefilement panAide = new ImageAvecDefilement();
       
    
         
        panAide.setBounds(35, 11, 800, 541);
        contentPane.add(panAide);
        panAide.setFichierImage("file:///C:/Users/simon/git/25carai/res/fenetreAideProg.jpg");
		
		
		
		
		
		
	}
	
	
}
