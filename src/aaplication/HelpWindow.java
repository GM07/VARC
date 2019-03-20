package aaplication;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
		setTitle("Aide sur l'application");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(300,300,LARGEUR_PRINCIPALE,HAUTEUR_PRINCIPALE);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblEtapes = new JLabel();
		lblEtapes.setBounds(LARGEUR_PRINCIPALE/2 -5*OFFSET, OFFSET, 10*OFFSET, 2*OFFSET);
		lblEtapes.setText(" Etapes de fonctionnement");
		contentPane.add(lblEtapes);
		
		lblChoixCharger = new JLabel();
		lblChoixCharger.setBounds(OFFSET, 2*OFFSET, 13*OFFSET, 2*OFFSET);
		lblChoixCharger.setText("Utiliser un reseau existant.");
		contentPane.add(lblChoixCharger);
		
		lblChoixImage = new JLabel();
		lblChoixImage.setBounds(OFFSET, 4*OFFSET, 14*OFFSET, 2*OFFSET);
		lblChoixImage.setText("2. Choisissez une image a l'aide du bouton image a tester.");
		contentPane.add(lblChoixImage);
		
		lblTester = new JLabel();
		lblTester.setBounds(OFFSET, 5*OFFSET, 14*OFFSET, 2*OFFSET);
		lblTester.setText("3. Si l'image vous convient, cliquez sur tester le reseau.");
		contentPane.add(lblTester);
		
		
		
		lblChargerReseau = new JLabel();
		lblChargerReseau.setBounds(OFFSET, 3*OFFSET, 13*OFFSET, 2*OFFSET);
		lblChargerReseau.setText("1. Chargez un reseau dans les options en haut a droite." );
		contentPane.add(lblChargerReseau);
		
		
		
	}
	
	
}
