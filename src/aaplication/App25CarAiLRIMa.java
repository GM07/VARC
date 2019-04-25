package aaplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

import algorithm.CNNAI;
import algorithm.CarAI;
import dessin.DessinNeuralNetwork;
import functions.SoftmaxFunction;
import image.processing.FileManager;
import image.processing.ImageManager;
import listeners.TrainingEvents;
import math.MathTools;

/**
 * Classe de la fenetre principale et de demarrage de l'application
 * Elle contient toutes les instances de composants Java ainsi que de plusieurs composants crees specialement pour l'application
 *
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
	private JButton btnConvolution;

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
	private JLabel lblOutputFinal;
	private JLabel lblOutputMarque;
	private JLabel lblColorVehicle;
	private JLabel lblColorText;
	private JLabel lblBarProgression;
	private JLabel lblLearningRate;
	private JLabel lblMarque;

	// Spinner
	private JSpinner spnEpoch;
	private JSpinner spnBatch;
	private JSpinner spnLearningRate;

	// Elements du menu
	private JMenuBar menuBar;

	private JMenu menuAide;
	private JMenuItem menuItemHelp;
	private JMenuItem menuItemScientificExplanations;

	private JMenu menuOptions;
	private JMenuItem menuItemSave;
	private JMenuItem menuItemLoad;
	private JMenuItem menuItemQuit;
	private JMenuItem menuItemLoadPreTrained;

	// Fenetre secondaire
	private HelpWindow helpWindow;
	private ScientificExplanationWindow scientificExplanationWindow;
	private FileWindow fileWindow;
	private MarqueWindow marqueWindow;
	private FileWindow datasetWindow;
	private FileWindow saveNetworkWindow;
	private FileWindow loadNetworkWindow;
	private ConvolutionWindow convolutionWindow;
    private WindowConsole console;
    private DessinNeuralNetwork nnDraw;

    private String path = "";
    private boolean training = true;
    private int numberOfTraining = 0;


    // Arraylist qui contient tous les JComponent
    private ArrayList<JComponent> elements = new ArrayList<>();

    // Autres variables de classes
    Color color = Color.GRAY;

    //Instance de la classe qui contient tous les algorithmes pour detecter les vehicules
    private CarAI carAI;

    private CNNAI cnnAI;

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
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 20));
        UIManager.put("MenuItem.font ", new Font("Arial", Font.BOLD, 20));
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 20));

        path = getClass().getClassLoader().getResource("network_saves/trained_neural_network.dat").getPath();

        // Creation de la fenetre
        setTitle("VARC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LARGEUR_PRINCIPALE, HAUTEUR_PRINCIPALE);
        setLocationRelativeTo(null);

        // Fenetres intermediaires
        helpWindow = new HelpWindow();
        scientificExplanationWindow = new ScientificExplanationWindow();
        marqueWindow = new MarqueWindow();
        fileWindow = new FileWindow();

        // Creation du panel de depart
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(210, 240, 255));

        URL urlLRIMa = getClass().getClassLoader().getResource("LRIMA.png");
        ImageIcon icon = new ImageIcon(urlLRIMa);
        JLabel btnL = new JLabel(icon);
        btnL.setBounds(OFFSET, 0, OFFSET * 6, OFFSET * 4);
        contentPane.add(btnL);

		lblTitle = new JLabel();
		lblTitle.setBounds(LARGEUR_PRINCIPALE/2 - OFFSET/2, OFFSET,4 * OFFSET, 2 * OFFSET);
		lblTitle.setText("VARC");
		contentPane.add(lblTitle);

		setUpMenu();

		setUpPanelGauche();

		setUpPanelMilieu();

		setUpPanelDroite();

		setFontOfElements();

        carAI = new CarAI((double) spnLearningRate.getValue(), (int) spnEpoch.getValue(), (int) spnBatch.getValue());
        carAI.setBar(progressBarTraining);
        carAI.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        carAI.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 10 * OFFSET / 2, HAUTEUR_PANEL_SECONDAIRE / 2 + 4 * OFFSET, 10 * OFFSET, 10 * OFFSET);
        carAI.addTrainingEvent(new TrainingEvents() {

            @Override
            public void trainingEnded() {
                btnTest.setEnabled(true);
                btnChoixImage.setEnabled(true);
                training = true;
                nnDraw.stop();
                numberOfTraining = 0;
                btnTrain.setText("Entrainer");
            }
        });

        panImage.add(carAI);

		cnnAI = new CNNAI(28);
		convolutionWindow = new ConvolutionWindow(cnnAI);

	}

    /**
     * Methode qui initialise tous les composants lies au menu
     */
    private void setUpMenu() {

        // Barre de menu
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menu des options
        menuOptions = new JMenu("Options");
        menuBar.add(menuOptions);

        saveNetworkWindow = new FileWindow();

        // Bouton qui sauvegarde le reseau dans le menu des options
        menuItemSave = new JMenuItem("Sauvegarder mon reseau");
        menuItemSave.addActionListener(actionPerformed -> {
            saveNetworkWindow.setVisible(true);
        });
        menuOptions.add(menuItemSave);

        saveNetworkWindow.addWindowListener(new WindowAdapter() {

            public void windowDeactivated(WindowEvent e) {

                try {
                    carAI.saveNetwork(saveNetworkWindow.getPath());
                    System.out.println("Le reseau a ete sauvegarde");
                } catch (NullPointerException n) {
                    System.out.println("Le reseau n'a pas pu etre sauvegarde, car il faut choisir une destination valide");
                }
            }
        });

        loadNetworkWindow = new FileWindow();

        // Bouton qui charge un reseau dans le menu des options
        menuItemLoad = new JMenuItem("Charger un reseau");
        menuItemLoad.addActionListener(actionPerformed -> {
            loadNetworkWindow.setVisible(true);
        });
        menuOptions.add(menuItemLoad);

        loadNetworkWindow.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDeactivated(WindowEvent e) {
                carAI.loadNetwork(loadNetworkWindow.getPath());
            }
        });

        //Bouton qui charge notre reseau preentrainer
        menuItemLoadPreTrained = new JMenuItem("Charger notre reseau");
        menuItemLoadPreTrained.addActionListener(actionPerformed -> {
            carAI.loadNetwork(path);
        });
        menuOptions.add(menuItemLoadPreTrained);

        // Bouton qui ferme la fenetre
        menuItemQuit = new JMenuItem("Quitter");
        menuItemQuit.addActionListener(actionPerformed -> {
            dispose();
            helpWindow.dispose();
            scientificExplanationWindow.dispose();

        });
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
        panImage.setBounds(0, 4 * OFFSET, LARGEUR_PANEL_SECONDAIRE, HAUTEUR_PANEL_SECONDAIRE);
        panImage.setBackground(new Color(220, 240, 255));
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
        btnChoixImage.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 4 * OFFSET, HAUTEUR_PANEL_SECONDAIRE / 2, 8 * OFFSET, 2 * OFFSET);
        btnChoixImage.setText(" Image a tester");
        panImage.add(btnChoixImage);
        btnChoixImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                fileWindow.setVisible(true);
            }
        });

        fileWindow.addWindowListener(new WindowAdapter() {

            public void windowDeactivated(WindowEvent e) {

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
        panInputNumerique.setBackground(new Color(220, 240, 255));
        panInputNumerique.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panInputNumerique.setLayout(null);
        contentPane.add(panInputNumerique);

        // Panel qui dessine une animation d'un reseau de neurone
        nnDraw = new DessinNeuralNetwork();
        nnDraw.setBounds(5, 5, LARGEUR_PANEL_SECONDAIRE - 10, OFFSET * 14 - 10);
        nnDraw.setBackground(panInputNumerique.getBackground());
        panInputNumerique.add(nnDraw);

		// Fenetre qui choisit le dataset
		datasetWindow = new FileWindow();
		datasetWindow.setVisible(false);
		datasetWindow.setMode("Folder");

		//Fenetre pour la console
		console = new WindowConsole();
		console.setVisible(false);

        // Bouton pour entrainer
        btnTrain = new JButton();
        btnTrain.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 8 * OFFSET / 2, 14 * OFFSET, 8 * OFFSET, 2 * OFFSET);
        btnTrain.setText("Entrainer");
        btnTrain.addActionListener(actionPerformed -> {

            if (training) {
                btnTrain.setText("Pause");
                btnChoixImage.setEnabled(false);
                btnTest.setEnabled(false);

                // Si c'est la premiere fois qu'on l'entraine, on veut afficher la fenetre qui permet d'indiquer le chemin d'acces de la banque d'image
                if (numberOfTraining == 0) {
                    datasetWindow.setVisible(true);
                } else {
                    nnDraw.demarrer();
                    carAI.demarrer();
                }
                numberOfTraining++;


            } else {
                btnChoixImage.setEnabled(true);
                btnTest.setEnabled(true);
                btnTrain.setText("Entrainer");
                carAI.pauseTraining();
                nnDraw.stop();
            }

            training = !training;

        });

        datasetWindow.addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				try {

                    FileManager.getFoldersFromFolder(datasetWindow.getPath());

					if (!datasetWindow.getPath().equals("")) {
						carAI.setTrainingPath(datasetWindow.getPath());
						console.setVisible(true);
						nnDraw.demarrer();
						carAI.demarrer();

					} else {
					    training = true;
					    numberOfTraining = 0;
                        btnChoixImage.setEnabled(true);
                        btnTest.setEnabled(true);
                        btnTrain.setText("Entrainer");
                    }
				} catch (NullPointerException n) {
				    training = true;
                    btnChoixImage.setEnabled(true);
                    btnTest.setEnabled(true);
                    btnTrain.setText("Entrainer");
				    numberOfTraining = 0;
					System.out.println("Veuillez selectionner un chemin d'acces");
				}
			}
		});

        panInputNumerique.add(btnTrain);

        // Label qui affiche le texte sur le nombre d'Epoch
        lblEpoch = new JLabel("Nombre d'epoch : ", SwingConstants.CENTER);
        lblEpoch.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 8 * OFFSET / 2, 16 * OFFSET, 8 * OFFSET, 2 * OFFSET);
        panInputNumerique.add(lblEpoch);

        // Spinner qui change le nombre d'Epoch
        spnEpoch = new JSpinner(new SpinnerNumberModel(500, 1, 10000, 1));
        spnEpoch.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 2 * OFFSET / 2, 18 * OFFSET, 2 * OFFSET, OFFSET);
        spnEpoch.addChangeListener(stateChanged -> {
            carAI.setNumberOfEpochs((int) spnEpoch.getValue());
        });
        panInputNumerique.add(spnEpoch);

        // Label qui affiche le texte sur la taille d'un seul batch lors de l'entrainement
        lblBatch = new JLabel("Taille d'un batch : ", SwingConstants.CENTER);
        lblBatch.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 8 * OFFSET / 2, 19 * OFFSET, 8 * OFFSET, 2 * OFFSET);
        panInputNumerique.add(lblBatch);

        // Spinner qui change la taille d'un seul batch
        spnBatch = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        spnBatch.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 2 * OFFSET / 2, 21 * OFFSET, 2 * OFFSET, 1 * OFFSET);
        spnBatch.addChangeListener(statedChanged -> {
            carAI.setBatchSize((int) spnBatch.getValue());
        });
        panInputNumerique.add(spnBatch);

        // Label qui affiche le texte sur le taux d'apprentissage
        lblLearningRate = new JLabel("Taux d'apprentissage : ");
        lblLearningRate.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 8 * OFFSET / 2, 22 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panInputNumerique.add(lblLearningRate);

        spnLearningRate = new JSpinner(new SpinnerNumberModel(0.3, 0.01, 10, 0.01));
        spnLearningRate.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 2 * OFFSET / 2, 24 * OFFSET, 2 * OFFSET, 1 * OFFSET);
        spnLearningRate.addChangeListener(stateChanged -> {
            //carAI.setLearningRate();
        });
        panInputNumerique.add(spnLearningRate);

        // Bouton pour tester le reseau de neurone
        btnTest = new JButton();
        btnTest.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 8 * OFFSET / 2, 26 * OFFSET, 8 * OFFSET, 2 * OFFSET);
        btnTest.setText("Tester le reseau");
        btnTest.addActionListener(actionPerformed -> {

			DecimalFormat df = new DecimalFormat("#.###");
			double[] out = carAI.testNetwork(imageVoiture.getImage());
			SoftmaxFunction sf = new SoftmaxFunction();
			out[0] = sf.getValue(out, 0);
			out[1] = sf.getValue(out, 1);
			out[2] = sf.getValue(out, 2);
			lblOutputVoiture.setText("Possibilite voiture : " + df.format(out[0] * 100) + "%");
			lblOutputMoto.setText("Possibilite moto : " + df.format(out[1] * 100) + "%");
			lblOutputCamion.setText("Possibilite camion : " + df.format(out[2] * 100) + "%");

            int index = MathTools.getHighestIndex(out);
            if (index == 0) {
                lblOutputFinal.setText("Decision du reseau : Voiture");
            } else if (index == 1) {
                lblOutputFinal.setText("Decision du reseau : Moto");
            } else {
                lblOutputFinal.setText("Decision du reseau : Camion");
            }

            double[] output = cnnAI.feedForward(imageVoiture.getImage()); 

			System.out.println(Arrays.toString(output));
			int i = MathTools.getHighestIndex(output);
			if (i == 0) {
				lblMarque.setText("Marque : BMW");
				marqueWindow.getMarque().setFichierImage("Marque- BMW-1.jpg");
			} else if (i == 1) {
				lblMarque.setText("Marque : Chevrolet");
				marqueWindow.getMarque().setFichierImage("Marque-chevrolet-1.jpg");
			} else {
				lblMarque.setText("Marque : Toyota");
				marqueWindow.getMarque().setFichierImage("Marque- toyota-1.jpg");
			}

            repaint();
            convolutionWindow.repaint();
            marqueWindow.repaint();
        });
        panInputNumerique.add(btnTest);

        // Label de la barre de progression
        lblBarProgression = new JLabel("Etat de l'entrainement", SwingConstants.CENTER);
        lblBarProgression.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 28 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panInputNumerique.add(lblBarProgression);

        // Barre de progression
        progressBarTraining = new JProgressBar(0, (int) spnEpoch.getValue());
        progressBarTraining.setStringPainted(true);
        progressBarTraining.setBounds(OFFSET, HAUTEUR_PANEL_SECONDAIRE - (int) (OFFSET * 1.8), LARGEUR_PANEL_SECONDAIRE - 2 * OFFSET, 1 * OFFSET);
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
        panOutput.setBackground(new Color(220, 240, 255));
        panOutput.setLayout(null);
        contentPane.add(panOutput);

        // Label qui affiche le texte du panel
        lblOutputTitle = new JLabel("Valeurs de sortie", SwingConstants.CENTER);
        lblOutputTitle.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 8 * OFFSET / 2, 0, 8 * OFFSET, 2 * OFFSET);
        panOutput.add(lblOutputTitle);

        // Label qui affiche la probabilite que ce soit une voiture
        lblOutputVoiture = new JLabel("Possibilite voiture :  " + " %", SwingConstants.CENTER);
        lblOutputVoiture.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 3 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblOutputVoiture);

        // Label qui affiche la probabilite que ce soit une moto
        lblOutputMoto = new JLabel("Possibilite moto : " + " %", SwingConstants.CENTER);
        lblOutputMoto.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 6 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblOutputMoto);

        // Label qui affiche la probabilite que ce soit un camion
        lblOutputCamion = new JLabel("Possibilite camion: " + "%", SwingConstants.CENTER);
        lblOutputCamion.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 9 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblOutputCamion);

        // Label qui affiche la marque identifiee
        lblOutputMarque = new JLabel(" Marque identifiee : ", SwingConstants.CENTER);
        lblOutputMarque.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 15 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblOutputMarque);

        // Bouton qui affiche la fenetre sur les informations sur la marque
        btnMarque = new JButton("Information sur la marque");
        btnMarque.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 17 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        btnMarque.addActionListener(actionPerformed -> {
            marqueWindow.setVisible(true);
        });

        panOutput.add(btnMarque);

        // Label qui affiche la probabilite que ce soit un camion
        lblOutputFinal = new JLabel("Decision du reseau: ", SwingConstants.CENTER);
        lblOutputFinal.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 12 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblOutputFinal);

        // Label qui affiche la couleur du vehicule
        lblColorVehicle = new JLabel();
        lblColorVehicle.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 23 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        lblColorVehicle.setBackground(color);
        lblColorVehicle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblColorVehicle.setOpaque(true);
        panOutput.add(lblColorVehicle);

        // Label qui affiche le texte sur la couleur du vehicule
        lblColorText = new JLabel("Couleur du vehicule : ", SwingConstants.CENTER);
        lblColorText.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 21 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblColorText);

        // Label qui affiche la marque de la voiture
        lblMarque = new JLabel("Marque : ", SwingConstants.CENTER);
        lblMarque.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 25 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        panOutput.add(lblMarque);

        btnConvolution = new JButton("Voir le reseau");
        btnConvolution.setBounds(LARGEUR_PANEL_SECONDAIRE / 2 - 12 * OFFSET / 2, 27 * OFFSET, 12 * OFFSET, 2 * OFFSET);
        btnConvolution.addActionListener(actionPerformed -> {
            convolutionWindow.setVisible(true);
            convolutionWindow.repaint();
        });
        panOutput.add(btnConvolution);
    }


    /**
     * Methode qui change le font de tous les composants presents dans le array list de la classe
     */
    private void setFontOfElements() {

        for (JComponent j : elements) {
            j.setFont(new Font("Dialog", Font.BOLD, 20));

        }
    }
}
