package aaplication;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class App25CarAiLRIMa extends JFrame {
	//panels
	private JPanel contentPane;
	private JPanel panImage;
	private JPanel panInputNumerique;
	private JPanel panOutput;
	private ImageVisible imageVisible = new ImageVisible();

	//Constantes

	private final int OFFSET = 25 ;
	private final int LARGEUR_PRINCIPALE = 1500;
	private final int HAUTEUR_PRINCIPALE = 1000;
	private final int LARGEUR_PANEL_SECONDAIRE = LARGEUR_PRINCIPALE/3;
	private final int HAUTEUR_PANEL_SECONDAIRE = HAUTEUR_PRINCIPALE-8*OFFSET;

	//boutons
	private JButton btnChoixImage;
	private JButton btnTest;
	private JButton btnTrain;
	//labels
	private JLabel lblTitle;
	private JLabel lblEpoc;
	private JLabel lblOutputTitle;
	private JLabel lblOutputVoiture;
	private JLabel lblOutputMoto;


	//spinner
	private JSpinner spnEpoc;
	private JLabel lblOutputCamion;
	
	//menu item
	private JMenuItem mnItHelp;
	private JMenuItem mntmScientificExplanations;
	private JMenuItem mntmOptions;

	//Secondary windows
	JFrame helpWindow = new HelpWindow();
	JFrame scientificExplanationWindow = new ScientificExplanationWindow();
	JFrame fileWindow = new FileWindow();



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App25CarAiLRIMa frame = new App25CarAiLRIMa();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public App25CarAiLRIMa() {
		setTitle("CarAI-LRIMA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200,LARGEUR_PRINCIPALE, HAUTEUR_PRINCIPALE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnItHelp = new JMenuItem("Help");
		menuBar.add(mnItHelp);
		mnItHelp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				helpWindow.setVisible(true);  

			}
		});

		mntmScientificExplanations = new JMenuItem("Scientific Explanations");
		menuBar.add(mntmScientificExplanations);
		mntmScientificExplanations.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				scientificExplanationWindow.setVisible(true);

			}
		});

		mntmOptions = new JMenuItem("Options");
		menuBar.add(mntmOptions);
		mntmOptions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//fenetreOption.setVisible();

			}
		});

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panImage = new JPanel();
		panImage.setBounds(0,8*OFFSET,LARGEUR_PANEL_SECONDAIRE,HAUTEUR_PANEL_SECONDAIRE);
		panImage.setBackground(Color.gray);
		panImage.setLayout(null);
		contentPane.add(panImage);
		panImage.add(imageVisible);

		panInputNumerique = new JPanel();
		panInputNumerique.setBounds(LARGEUR_PANEL_SECONDAIRE, 8*OFFSET, LARGEUR_PANEL_SECONDAIRE, HAUTEUR_PANEL_SECONDAIRE);
		panInputNumerique.setBackground(Color.white);
		panInputNumerique.setLayout(null);
		contentPane.add(panInputNumerique);

		panOutput = new JPanel();
		panOutput.setBounds(2*LARGEUR_PANEL_SECONDAIRE, 8*OFFSET, LARGEUR_PANEL_SECONDAIRE, HAUTEUR_PANEL_SECONDAIRE);
		panOutput.setBackground(Color.gray);
		panOutput.setLayout(null);
		contentPane.add(panOutput);

		btnChoixImage = new JButton();
		btnChoixImage.setBounds(LARGEUR_PANEL_SECONDAIRE/2-4*OFFSET,0,8*OFFSET,2*OFFSET);
		btnChoixImage.setText(" image a tester");
		panImage.add(btnChoixImage);
		btnChoixImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fileWindow.setVisible(true);
			}
		});

		lblTitle =  new JLabel();
		lblTitle.setBounds(LARGEUR_PRINCIPALE/2-OFFSET/2, 2*OFFSET,4*OFFSET, 2*OFFSET);
		lblTitle.setText("CarAI");
		contentPane.add(lblTitle);

		btnTrain = new JButton();
		btnTrain.setBounds(LARGEUR_PANEL_SECONDAIRE/2-4*OFFSET, 12*OFFSET, 7*OFFSET, 2*OFFSET);
		btnTrain.setText("TRAIN");
		btnTrain.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//nn.train();			
			}
		});
		panInputNumerique.add(btnTrain);

		lblEpoc = new JLabel();
		lblEpoc.setBounds(LARGEUR_PANEL_SECONDAIRE/2-4*OFFSET, 15*OFFSET, 5*OFFSET, 2*OFFSET);
		lblEpoc.setText(" Nombre de EPOC:");
		panInputNumerique.add(lblEpoc);

		spnEpoc = new JSpinner();
		spnEpoc.setBounds(LARGEUR_PANEL_SECONDAIRE/2+OFFSET,15*OFFSET, 2*OFFSET,2*OFFSET);
		spnEpoc.setValue(10);
		panInputNumerique.add(spnEpoc);

		btnTest = new JButton();
		btnTest.setBounds(LARGEUR_PANEL_SECONDAIRE/2-4*OFFSET, 18*OFFSET, 7*OFFSET, 2*OFFSET);
		btnTest.setText(" Tester le reseau");
		btnTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//nn.getOutput();			
			}
		});
		panInputNumerique.add(btnTest);

		lblOutputTitle = new JLabel();
		lblOutputTitle.setBounds(LARGEUR_PANEL_SECONDAIRE/2-2*OFFSET, 0, 8*OFFSET, 2*OFFSET);
		lblOutputTitle.setText("Valeurs de sortie");
		panOutput.add(lblOutputTitle);

		lblOutputVoiture = new JLabel();
		lblOutputVoiture.setBounds(0 , 3*OFFSET, 8*OFFSET, 2*OFFSET );
		lblOutputVoiture.setText("Possibilite voiture :  "+ " %");
		panOutput.add(lblOutputVoiture);

		lblOutputMoto = new JLabel();
		lblOutputMoto.setBounds(0, 6*OFFSET, 8*OFFSET, 2*OFFSET);
		lblOutputMoto.setText("Possibilite moto : "+" %");
		panOutput.add(lblOutputMoto);

		lblOutputCamion = new JLabel();
		lblOutputCamion.setBounds(0, 9*OFFSET, 8*OFFSET, 2*OFFSET);
		lblOutputCamion.setText("Possibilite camion: "+"%");
		panOutput.add(lblOutputCamion);












	}
}
