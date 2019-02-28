package aaplication;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import algorithm.CarAI;
import dessin.DessinNeuralNetwork;
import image.processing.ImageManager;

/**
 * Classe de la fenetre principale et de demarrage de l'application
 * Elle contient toutes les instances de composants Java ainsi que de plusieurs composants crees specialement pour l'application
 * @author Simon Daze
 * @author Gaya Mehenni
 *
 */
public class App25CarAiLRIMa extends JFrame {

	// Panels
	private JPanel contentPane;
	private JPanel panImage;
	private JPanel panInputNumerique;
	private JPanel panOutput;
	private ImageVoiturePanel imageVoiture;

	// Constantes
	public static final int OFFSET = 25;
	public static final int LARGEUR_PRINCIPALE = 1200;
	public static final int HAUTEUR_PRINCIPALE = 1000;
	public static final int LARGEUR_PANEL_SECONDAIRE = (LARGEUR_PRINCIPALE - 15)/3;
	public static final int HAUTEUR_PANEL_SECONDAIRE = HAUTEUR_PRINCIPALE - 8*OFFSET;

	// Boutons
	private JButton btnChoixImage;
	private JButton btnTest;
	private JButton btnTrain;
	private JButton btnMarque;

	// Progress bar
	private JProgressBar progressBarTraining;

	// Etiquettes
	private JLabel lblTitle;
	private JLabel lblEpoch;
	private JLabel lblBatch;
	private JLabel lblOutputTitle;
	private JLabel lblOutputVoiture;
	private JLabel lblOutputMoto;
	private JLabel lblOutputCamion;
	private JLabel lblOutputMarque;
	private JLabel lblColorVehicle;
	private JLabel lblColorText;
	private JLabel lblBarProgression;
	private JLabel lblLearningRate;

	// Spinner
	private JSpinner spnEpoch;
	private JSpinner spnBatch;
	private JSpinner spnLearningRate;

	// Item de menu
	private JMenuBar menuBar;

	private JMenu menuAide;
	private JMenuItem menuItemHelp;
	private JMenuItem menuItemScientificExplanations;

	private JMenu menuOptions;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemLoad;
	private JMenuItem menuItemQuit;

	// Fenetre secondaire
	private HelpWindow helpWindow;
	private ScientificExplanationWindow scientificExplanationWindow;
	private FileWindow fileWindow;
	private MarqueWindow marqueWindow;

	// Arraylist qui contient tous les JComponent
	private ArrayList<JComponent> elements = new ArrayList<>();

	// Autres variables de classes
	Color color = Color.GRAY;

	/*
	 * Instance de la classe qui contient tous les algorithmes pour detecter les vehicules
	 */
	private CarAI carAI;

	/**
	 * Methode principale qui lance l'application
	 * @param args
	 */
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

	/**
	 * Constructeur de l'application
	 */
	public App25CarAiLRIMa() {

		// Permet de changer la taille des caracteres
		UIManager.put("Label.font",new Font("Courier", Font.BOLD, 20));
		UIManager.put("MenuItem.font ",new Font("Courier", Font.BOLD,20));
		UIManager.put("Button.font", new Font("Courier", Font.BOLD, 20));

		// Creation de la fenetre
		setTitle("CarAI-LRIMA");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(LARGEUR_PRINCIPALE, HAUTEUR_PRINCIPALE);
		setLocationRelativeTo(null);

		// Creation des fenetres intermediaires
		helpWindow = new HelpWindow();
		scientificExplanationWindow = new ScientificExplanationWindow();
		marqueWindow = new MarqueWindow();
		fileWindow = new FileWindow();

		// Creation du panel de depart
		contentPane = new JPanel();
		//contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitle = new JLabel();
		lblTitle.setBounds(LARGEUR_PRINCIPALE/2 - OFFSET/2, OFFSET,4 * OFFSET, 2 * OFFSET);
		lblTitle.setText("CarAI");
		contentPane.add(lblTitle);

		setUpMenu();

		setUpPanelGauche();

		setUpPanelMilieu();

		setUpPanelDroite();

		setFontOfElements();

		carAI = new CarAI((double) spnLearningRate.getValue(), (int) spnEpoch.getValue(), (int) spnBatch.getValue());
		carAI.setBar(progressBarTraining);
		carAI.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		carAI.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 10 * OFFSET/2, HAUTEUR_PANEL_SECONDAIRE/2 + 4 * OFFSET, 10 * OFFSET, 10 * OFFSET);
		panImage.add(carAI);
	}

	/**
	 * Methode qui initialise tous les composants lies au menu
	 */
	private void setUpMenu() {

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Menu des options
		menuOptions = new JMenu("Options");
		menuBar.add(menuOptions);

		// Bouton qui sauvegarde le reseau dans le menu des options
		menuItemSave = new JMenuItem("Sauvegarder mon reseau");
		menuOptions.add(menuItemSave);

		// Bouton qui charge un reseau dans le menu des options
		menuItemLoad = new JMenuItem("Charger un reseau");
		menuOptions.add(menuItemLoad);

		menuItemQuit = new JMenuItem("Quitter");
		menuOptions.add(menuItemQuit);

		// Menu d'aide
		menuAide = new JMenu("Aide");
		menuBar.add(menuAide);

		// Bouton d'aide sur l'application dans le menu d'aide
		menuItemHelp = new JMenuItem("Aide sur l'application");
		menuItemHelp.addActionListener(actionPerformed -> {
			helpWindow.setVisible(true);
		});
		menuAide.add(menuItemHelp);

		// Bouton d'explications scientifiques dans le menu d'aide
		menuItemScientificExplanations = new JMenuItem("Explications scientifiques");
		menuItemScientificExplanations.addActionListener(actionPerformed -> {
			scientificExplanationWindow.setVisible(true);
		});
		menuAide.add(menuItemScientificExplanations);

	}

	/**
	 * Methode qui initialise tous les composants du panel de gauche
	 */
	private void setUpPanelGauche() {

		// Panel de gauche avec image a tester
		panImage = new JPanel();
		panImage.setBounds(0,4 * OFFSET, LARGEUR_PANEL_SECONDAIRE, HAUTEUR_PANEL_SECONDAIRE);
		panImage.setBackground(Color.WHITE);
		panImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panImage.setLayout(null);
		contentPane.add(panImage);

		// Panel qui affiche l'image de la voiture
		imageVoiture = new ImageVoiturePanel();
		imageVoiture.setBackground(panImage.getBackground());
		imageVoiture.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		imageVoiture.setBounds(5, 5, LARGEUR_PANEL_SECONDAIRE - 10, LARGEUR_PANEL_SECONDAIRE - 10);
		panImage.add(imageVoiture);

		// Bouton pour selectionner l'image a tester
		btnChoixImage = new JButton();
		btnChoixImage.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 4 * OFFSET,HAUTEUR_PANEL_SECONDAIRE/2,8 * OFFSET,2 * OFFSET);
		btnChoixImage.setText(" Image a tester");
		panImage.add(btnChoixImage);
		btnChoixImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {

				fileWindow.setVisible(true);
			}
		});

		fileWindow.addWindowListener(new WindowAdapter() {

			public void windowDeactivated(WindowEvent e){

				try {
					imageVoiture.setImage(fileWindow.getPath());

					double[] rgb = ImageManager.getAverageColor(imageVoiture.getImage());
					Color c = new Color((int) rgb[0], (int) rgb[1], (int) rgb[2]);
					lblColorVehicle.setBackground(c);

					repaint();
				} catch (NullPointerException n) {
					System.out.println("Veuillez selectionner une image");
				}
			}
		});

	}

	/**
	 * Methode qui initialise tous les composants du panel du milieu
	 */
	private void setUpPanelMilieu() {

		// Panel du milieu avec les donnees d'entree
		panInputNumerique = new JPanel();
		panInputNumerique.setBounds(LARGEUR_PANEL_SECONDAIRE, 4 * OFFSET, LARGEUR_PANEL_SECONDAIRE, HAUTEUR_PANEL_SECONDAIRE);
		panInputNumerique.setBackground(Color.WHITE);
		panInputNumerique.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panInputNumerique.setLayout(null);
		contentPane.add(panInputNumerique);

		// Panel qui dessine une animation d'un reseau de neurone
		DessinNeuralNetwork nnDraw = new DessinNeuralNetwork();
		nnDraw.setBounds(5, 5, LARGEUR_PANEL_SECONDAIRE - 10, OFFSET * 14 - 10);
		nnDraw.setBackground(panInputNumerique.getBackground());
		panInputNumerique.add(nnDraw);

		// Bouton pour entrainer
		btnTrain = new JButton();
		btnTrain.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 8*OFFSET/2, 14*OFFSET, 8*OFFSET, 2 * OFFSET);
		btnTrain.setText("Entrainer");
		btnTrain.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				nnDraw.demarrer();
				carAI.demarrer();
			}
		});
		panInputNumerique.add(btnTrain);

		// Label qui affiche le texte sur le nombre d'Epoch
		lblEpoch = new JLabel("Nombre d'epoch : ", SwingConstants.CENTER);
		lblEpoch.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 8*OFFSET/2, 16*OFFSET, 8*OFFSET, 2*OFFSET);
		panInputNumerique.add(lblEpoch);

		// Spinner qui change le nombre d'Epoch
		spnEpoch = new JSpinner(new SpinnerNumberModel(500, 1, 10000, 1));
		spnEpoch.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 2*OFFSET/2,18*OFFSET, 2*OFFSET, OFFSET);
		spnEpoch.addChangeListener(stateChanged -> {
			carAI.setNumberOfEpochs((int) spnEpoch.getValue());
		});
		panInputNumerique.add(spnEpoch);

		// Label qui affiche le texte sur la taille d'un seul batch lors de l'entrainement
		lblBatch = new JLabel("Taille d'un batch : ", SwingConstants.CENTER);
		lblBatch.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 8 * OFFSET/2, 19 * OFFSET, 8 * OFFSET, 2 * OFFSET);
		panInputNumerique.add(lblBatch);

		// Spinner qui change la taille d'un seul batch
		spnBatch = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
		spnBatch.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 2 * OFFSET/2,21 * OFFSET, 2 * OFFSET,1 * OFFSET);
		spnBatch.addChangeListener(statedChanged -> {
			carAI.setBatchSize((int) spnBatch.getValue());
		});
		panInputNumerique.add(spnBatch);

		// Label qui affiche le texte sur le taux d'apprentissage
		lblLearningRate = new JLabel("Taux d'apprentissage : ");
		lblLearningRate.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 8 * OFFSET/2, 22 * OFFSET, 12 * OFFSET, 2 * OFFSET);
		panInputNumerique.add(lblLearningRate);

		spnLearningRate = new JSpinner(new SpinnerNumberModel(0.3, 0.01, 10, 0.01));
		spnLearningRate.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 2 * OFFSET/2,24 * OFFSET, 2 * OFFSET,1 * OFFSET);
		spnLearningRate.addChangeListener(stateChanged -> {
			//carAI.setLearningRate();
		});
		panInputNumerique.add(spnLearningRate);

		// Bouton pour tester le reseau de neurone
		btnTest = new JButton();
		btnTest.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 8*OFFSET/2, 26*OFFSET, 8*OFFSET, 2*OFFSET);
		btnTest.setText("Tester le reseau");
		btnTest.addActionListener(actionPerformed -> {

			DecimalFormat df = new DecimalFormat("#.###");
			double[] out = carAI.testNetwork(imageVoiture.getImage());
			lblOutputVoiture.setText("Possibilite voiture : " + df.format(out[0]) + "%");
			lblOutputMoto.setText("Possibilite moto : " + df.format(out[1]) + "%");
			lblOutputCamion.setText("Possibilite camion : " + df.format(out[2]) + "%");
		});
		panInputNumerique.add(btnTest);

		// Label de la barre de progression
		lblBarProgression = new JLabel("État de l'entrainement", SwingConstants.CENTER);
		lblBarProgression.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET/2, 28 * OFFSET, 12 * OFFSET, 2 * OFFSET);
		panInputNumerique.add(lblBarProgression);

		// Barre de progression
		progressBarTraining = new JProgressBar(0, (int) spnEpoch.getValue());
		progressBarTraining.setStringPainted(true);
		progressBarTraining.setBounds(1,  HAUTEUR_PANEL_SECONDAIRE - (int) (OFFSET * 1.5), LARGEUR_PANEL_SECONDAIRE - 2, 1 * OFFSET);
		panInputNumerique.add(progressBarTraining);

	}

	/**
	 * Methode qui initialise tous les composants du panel de droite
	 */
	private void setUpPanelDroite() {

		// Panel de droite avec les donnees de sortie
		panOutput = new JPanel();
		panOutput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panOutput.setBounds(2 * LARGEUR_PANEL_SECONDAIRE, 4 * OFFSET, LARGEUR_PANEL_SECONDAIRE - 1, HAUTEUR_PANEL_SECONDAIRE);
		panOutput.setBackground(Color.WHITE);
		panOutput.setLayout(null);
		contentPane.add(panOutput);

		// Label qui affiche le texte du panel
		lblOutputTitle = new JLabel("Valeurs de sortie", SwingConstants.CENTER);
		lblOutputTitle.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 8 * OFFSET / 2, 0, 8 * OFFSET, 2 * OFFSET);
		panOutput.add(lblOutputTitle);

		// Label qui affiche la probabilite que ce soit une voiture
		lblOutputVoiture = new JLabel("Possibilite voiture :  " + " %", SwingConstants.CENTER);
		lblOutputVoiture.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2, 3 * OFFSET, 12 * OFFSET, 2 * OFFSET );
		panOutput.add(lblOutputVoiture);

		// Label qui affiche la probabilite que ce soit une moto
		lblOutputMoto = new JLabel("Possibilite moto : " + " %", SwingConstants.CENTER);
		lblOutputMoto.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2, 6 * OFFSET, 12 * OFFSET, 2 * OFFSET);
		panOutput.add(lblOutputMoto);

		// Label qui affiche la probabilite que ce soit un camion
		lblOutputCamion = new JLabel("Possibilite camion: " + "%", SwingConstants.CENTER);
		lblOutputCamion.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2, 9 * OFFSET, 12 * OFFSET, 2 * OFFSET);
		panOutput.add(lblOutputCamion);

		// Label qui affiche la marque identifiee
		lblOutputMarque = new JLabel(" Marque identifiee : ", SwingConstants.CENTER);
		lblOutputMarque.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2, 15 * OFFSET , 12 * OFFSET, 2 * OFFSET);
		panOutput.add(lblOutputMarque);

		// Bouton qui affiche la fenetre sur les informations sur la marque
		btnMarque = new JButton("Information sur la marque");
		btnMarque.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2,17 * OFFSET, 12 * OFFSET, 2 * OFFSET);
		btnMarque.addActionListener(actionPerformed -> {
			marqueWindow.setVisible(true);
		});

		panOutput.add(btnMarque);

		// Label qui affiche la couleur du vehicule
		lblColorVehicle = new JLabel();
		lblColorVehicle.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2, 23 * OFFSET, 12 * OFFSET, 2 * OFFSET);
		lblColorVehicle.setBackground(color);
		lblColorVehicle.setOpaque(true);
		panOutput.add(lblColorVehicle);

		// Label qui affiche le texte sur la couleur du vehicule
		lblColorText = new JLabel("Couleur du vehicule : ", SwingConstants.CENTER);
		lblColorText.setBounds(LARGEUR_PANEL_SECONDAIRE/2 - 12 * OFFSET / 2, 21*OFFSET, 12 * OFFSET, 2 * OFFSET);
		panOutput.add(lblColorText);
	}


	/**
	 * Methode qui change le font de tous les composants presents dans le array list de la classe
	 */
	private void setFontOfElements() {

		for(JComponent j : elements) {
			j.setFont(new Font("Dialog", Font.BOLD, 20));

		}
	}
}
