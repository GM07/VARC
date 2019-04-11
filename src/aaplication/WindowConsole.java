package aaplication;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class WindowConsole extends JFrame{



	//panels
	JPanel contentPane;

	//

	JTextArea console;
	//constantes
	private final int LARGEUR_PRINCIPALE = 900;
	private final int HAUTEUR_PRINCIPALE = 600;
	private final int OFFSET = 25;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowConsole frame = new WindowConsole();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WindowConsole() {
		setTitle("Training Infos");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(300,300,LARGEUR_PRINCIPALE,HAUTEUR_PRINCIPALE);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		console = new JTextArea();
		console.setBounds(OFFSET, OFFSET,LARGEUR_PRINCIPALE - 3 * OFFSET,HAUTEUR_PRINCIPALE - 3 * OFFSET);
		
		contentPane.add(console);

		OutputStream out = new OutputStream() {
			@
			Override
			public void write(int b) throws	IOException{
				
				console.append(String.valueOf((char) b));
			}
			@
			Override
			public void write(byte[] b,int off, int len) throws IOException{

				console.append ( new String(b, off,len));
			}
			@
			Override
			public void write(byte[] b) throws IOException{
				write(b, 0,b.length);
			}
		};
		
		System.setOut( new PrintStream(out,true) );
		System.setErr( new PrintStream(out, true) );
		

	}


	
}
