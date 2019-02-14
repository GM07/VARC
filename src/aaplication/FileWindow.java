package aaplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Fenetre qui permet de chercher un fichier a lire
 * @author Gaya Mehenni
 *
 */
public class FileWindow extends JFrame {

	private JPanel contentPane;
	private JFileChooser fileChooser;
	private String path;

	public FileWindow() {

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(0, 0, 600, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		setContentPane(contentPane);

		fileChooser = new JFileChooser();
		fileChooser.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					path = fileChooser.getSelectedFile().getAbsolutePath();
				} catch(NullPointerException n) {
					path = "";
				}
				setVisible(false);

			}
		});
		fileChooser.setBounds(0, 0, 400, 400);
		contentPane.add(fileChooser);
	}

	/**
	 * Returns the path of the selected file
	 * @return
	 */
	public String getPath() {
		return path;
	}

}
