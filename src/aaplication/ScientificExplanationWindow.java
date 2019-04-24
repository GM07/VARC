package aaplication;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Fenetre secondaire des explications scientifiques
 * @author Simon Daze
 *
 */
public class ScientificExplanationWindow extends JFrame{
	
	//panels
		JPanel contentPane;
		
		//constants
		private final int LARGEUR_PRINCIPALE = 900;
		private final int HAUTEUR_PRINCIPALE = 600;
		private final int COUNTER_MAX= 5 ;
		private int counter = 1;
		
		//boutons
		private JButton btnSuivant;
		private JButton btnPrecedant;
		
		
		
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ScientificExplanationWindow frame = new ScientificExplanationWindow();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		public ScientificExplanationWindow() {
			setTitle("Scientific Explanation Window");
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setBounds(300,300,LARGEUR_PRINCIPALE,HAUTEUR_PRINCIPALE);
			
			
			contentPane = new JPanel();
			contentPane.setBackground(Color.WHITE);
			setContentPane(contentPane);
			contentPane.setLayout(null);
			
			ImageAvecDefilement panScience = new ImageAvecDefilement();
			panScience.setBounds(20, 20, 860, 450);
			contentPane.add(panScience);
			panScience.setFichierImage("Concepts scientifiques-1.jpg");
			
			//Boutons pour changer de pages
			btnSuivant = new JButton();
			btnSuivant.setBounds(450, 500, 100, 50);
			btnSuivant.setText("Suivant");
			btnSuivant.addActionListener(actionPerformed -> {
	           if(counter < COUNTER_MAX) {
	        	   counter += 1;
	           }
				
	           switch( counter){
	           case 1 :
	        	   panScience.setFichierImage("Concepts scientifiques-1.jpg");
	           break;
	           case 2:
	        	   panScience.setFichierImage("Concepts scientifiques-2.jpg");
	        	   btnPrecedant.setEnabled(true);
	        	   break;
	           case 3: 
	        	   panScience.setFichierImage("Concepts scientifiques-3.jpg");
	        	   break;
	           case 4: 
	        	   panScience.setFichierImage("Concepts scientifiques-4.jpg");
	        	   break;
	           case 5: 
	        	   panScience.setFichierImage("Concepts scientifiques-5.jpg");
	        	   btnSuivant.setEnabled(false);
	        	   break;
	           }
	           panScience.repaint();
	        });
			contentPane.add(btnSuivant);
			
			btnPrecedant =  new JButton();
			btnPrecedant.setBounds(350, 500, 100, 50);
			btnPrecedant.setText("Precedant");
			btnPrecedant.setEnabled(false);
			btnPrecedant.addActionListener(actionPerformed -> {
	           if(counter > 1) {
	        	   counter -= 1;
	           
	           }
				
	           switch( counter){
	           case 1 :
	        	   panScience.setFichierImage("Concepts scientifiques-1.jpg");
	        	   btnPrecedant.setEnabled(false);
	           break;
	           case 2:
	        	   panScience.setFichierImage("Concepts scientifiques-2.jpg");
	        	   break;
	           case 3: 
	        	   panScience.setFichierImage("Concepts scientifiques-3.jpg");
	        	   break;
	           case 4: 
	        	   panScience.setFichierImage("Concepts scientifiques-4.jpg");
	        	   btnSuivant.setEnabled(true);
	        	   break;
	           case 5: 
	        	   panScience.setFichierImage("Concepts scientifiques-5.jpg");
	        	   break;
	           }
	           
	           panScience.repaint();
	        });
			
			contentPane.add(btnPrecedant);
		}
		

}
