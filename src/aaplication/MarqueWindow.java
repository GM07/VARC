package aaplication;

import java.awt.Color;
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
	
	public MarqueWindow() {
		setTitle("Informations sur la marque");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(300,300,LARGEUR_PRINCIPALE,HAUTEUR_PRINCIPALE);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
	}
}
