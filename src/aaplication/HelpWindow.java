package aaplication;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private int counterMax = 2;
	private int counter = 1;
	
	//buttons
	private JButton btnSuivant;
	private JButton btnPrecedant;

	
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
	/**
	 * Constructeur de la fenêtre
	 */
	
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

        panAide.setBounds(35, 11, 800, 500);
        contentPane.add(panAide);
        panAide.setFichierImage("fenetre aide prog-1.jpg");
		
        
        btnSuivant = new JButton();
		btnSuivant.setBounds(450, 500, 100, 50);
		btnSuivant.setText("Suivant");
		
		//Boutons pour changer de pages
		btnSuivant.addActionListener(actionPerformed -> {
           if(counter < counterMax) {
        	   counter += 1;
           }else {
        	   JOptionPane.showMessageDialog(null,"Pas d'autres fenetres d'explications");
           }
			
           switch( counter){
           case 1 :
        	   panAide.setFichierImage("fenetre aide prog-1.jpg");
           break;
           case 2:
        	   panAide.setFichierImage("fenetre aide prog-2.jpg");
           }
           panAide.repaint();
        });
		contentPane.add(btnSuivant);
		
		btnPrecedant =  new JButton();
		btnPrecedant.setBounds(350, 500, 100, 50);
		btnPrecedant.setText("Precedant");
		btnPrecedant.addActionListener(actionPerformed -> {
           if(counter > 1) {
        	   counter -= 1;
           }else {
        	   JOptionPane.showMessageDialog(null,"Pas d'autres fenetres d'explications");
           }
			
           switch( counter){
           case 1 :
        	   panAide.setFichierImage("fenetre aide prog-1.jpg");
           break;
           case 2:
        	   panAide.setFichierImage("fenetre aide prog-2.jpg");
           }
           panAide.repaint();
        });
		
		contentPane.add(btnPrecedant);
	}
	
	
}
