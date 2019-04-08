package image.processing;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import math.MathTools;

/**
 * Classe qui permet de manipuler des images pour les adapter au reseau de neurones
 * @author Gaya Mehenni
 */
public class ImageManager {

	/**
	 * Methode qui convertit une image en tableau de valeurs rgb
	 * @param img image
	 * @return tableau des valeurs rgb
	 * @throws IOException
	 */
	public static double[] convertRGB(BufferedImage img) throws IOException {
		BufferedImage bi = img;

		double values[][] = new double[bi.getWidth() * bi.getHeight()][3];

		for(int i = 0; i < bi.getWidth(); i++) {
			//System.out.println();
			for(int j = 0; j < bi.getHeight(); j++) {

				values[i * bi.getHeight() + j] = getPixelData(img, i, j);

			}
		}

		return MathTools.getAsOneDimension(values);
	}

	/**
	 * Methode qui convertit une image en tableau a 2 dimensions contenant les valeurs rgb de chaque pixel de l'image
	 * @param img image
	 * @return tableau a 2 dimensions
	 * @throws IOException
	 */
	public static double[][] convertRGB2D(BufferedImage img) throws IOException {
		double values[][] = new double[img.getWidth()][img.getHeight()];

		for(int i = 0; i < img.getWidth(); i++) {
			//System.out.println();
			for(int j = 0; j < img.getHeight(); j++) {

				double[] rgb = getPixelData(img, i, j);
				values[i][j] = (rgb[0] + rgb[1] + rgb[2])/3.0;

			}
		}

		return values;
	}

	/**
	 * Methode qui convertit une image en tableau des valeurs rgb, mais en ne prenant que la moyenne des trois valeurs (grey scale)
	 * @param img image
	 * @return tableau des valeurs
	 * @throws IOException
	 */
	public static double[] convertGreyValues(BufferedImage img) throws IOException {
		
		BufferedImage bi = img;

		double values[] = new double[bi.getWidth() * bi.getHeight()];

		for(int i = 0; i < bi.getWidth(); i++) {
			//System.out.println();
			for(int j = 0; j < bi.getHeight(); j++) {

				values[i * bi.getHeight() + j] = (getPixelData(img, i, j)[0] + getPixelData(img, i, j)[1] + getPixelData(img, i, j)[2]) / (3.0);

			}	
		}

		return values;
	}

	/**
	 * Convertit une image en image carre
	 * @param img image
	 * @param imageSize taille
	 * @return image carre
	 */
	public static BufferedImage getSquaredImage(BufferedImage img, int imageSize) {

		BufferedImage bimage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB_PRE);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, imageSize, imageSize, null);
		bGr.dispose();

		// Returns a buffered image
		return bimage;
	}

	/**
	 * Method qui retourne les valeurs rgb d'un pixel d'une image
	 * @param img image
	 * @param x position en x
	 * @param y position en y
	 * @return valeurs rgb
	 */
	public static double[] getPixelData(BufferedImage img, int x, int y) {
		int argb = img.getRGB(x, y);

		double rgb[] = new double[] {
				(double) ((argb >> 16) & 0xff), //red
				(double) ((argb >> 8) & 0xff), //green
				(double) ((argb) & 0xff)  //blue
		};

		//System.out.println("rgb: " + rgb[0] + " " + rgb[1] + " " + rgb[2]);
		return rgb;
	}

	/**
	 * Methode qui retourne la couleur moyenne d'une image
	 * Utilisee entre autre pour detecter la couleur d'une voiture
	 * @param img image a analyser
	 * @return tableau rgb de la couleur moyenne
	 */
	public static double[] getAverageColor(BufferedImage img) {

		double[] rgbValues = new double[3];

		//System.out.println(img.getWidth() + ", " + img.getHeight());
		for(int i = 0; i < img.getWidth(); i++) {
			for(int j = 0; j < img.getHeight(); j++) {

				double[] rgb = getPixelData(img, i, j);

				rgbValues[0] += rgb[0];
				rgbValues[1] += rgb[1];
				rgbValues[2] += rgb[2];

			}
		}

		//System.out.println(Arrays.toString(rgbValues) + " --> " + img.getWidth() + ", " + img.getHeight());
		for(int i = 0; i < rgbValues.length; i++) {
			rgbValues[i] = (int) (rgbValues[i] / ((double) (img.getWidth() * img.getHeight())));
		}

		//System.out.println(Arrays.toString(rgbValues));
		return rgbValues;

	}

	/**
	 * Methode qui retourne la couleur moyenne d'une image en mettant la priorite sur les pixels du milieu
	 * @param img image
	 * @return tableau des valeurs rgb moyennes
	 */
	public static double[] getAverageColorFromMiddle(BufferedImage img) {

		double[] rgbValues = new double[3];
		double middleX = img.getWidth()/2.0;
		double middleY = img.getHeight()/2.0;
		double maxValue = Math.sqrt(middleX * middleX + middleY * middleY);

		System.out.println(img.getWidth() + ", " + img.getHeight());
		for(int i = 0; i < img.getWidth(); i++) {
			for(int j = 0; j < img.getHeight(); j++) {

				double[] rgb = getPixelData(img, i, j);

				double dx = i - middleX;
				double dy = j - middleY;
				double dp = MathTools.mapValue(Math.sqrt(dx * dx + dy * dy), 0, maxValue, 0, 1);
				System.out.println(i + ", " + j + ", " + dp);

				rgbValues[0] += rgb[0] * 1d/dp;
				rgbValues[2] += rgb[2] * 1d/dp;// (1 - Math.pow(Math.E, -dp));
				rgbValues[1] += rgb[1] * 1d/dp;/// (1 - Math.pow(Math.E, -dp));

			}
		}

		for(int i = 0; i < rgbValues.length; i++) {
			rgbValues[i] = (int) (rgbValues[i] / ((double) (img.getWidth() * img.getHeight())));

			if (rgbValues[i] > 255) rgbValues[i] = 255;
			else if (rgbValues[i] < 0) rgbValues[i] = 0;
		}

		System.out.println(Arrays.toString(rgbValues));
		return rgbValues;
	}
	/**
	 * Detection de la couleur d'une image en donnant plus d'importance au pixel du centre
	 * @param img l'image
	 * @return la couleur moyenne u centre de l'image
	 */
	
	public static double[] getColorImageCenter(BufferedImage img) {
		double rgbValues[] = new double[3];
		double dx;
		double dy;
		double middleX = img.getWidth()/2.0;
		double middleY = img.getHeight()/2.0;
		double maxValue = Math.sqrt(middleX * middleX + middleY * middleY);
		double coef;
		
		for(int i = 0; i < img.getWidth(); i++) {
			for(int j = 0; j < img.getHeight(); j++) {
				
				dx = Math.abs(i - middleX);
				dy = Math.abs(j - middleY);
				coef = 1.20 *Math.abs(3 * maxValue - 2 * (Math.sqrt(dx * dx + dy * dy))) / (2 * maxValue);
				rgbValues[0] += ImageManager.getPixelData(img, i, j)[0]*coef;
				rgbValues[1] += ImageManager.getPixelData(img, i, j)[1]*coef;
				rgbValues[2] += ImageManager.getPixelData(img, i, j)[2]*coef;
				
			}
		}
		double totalRgb = rgbValues[0]  + rgbValues[1] + rgbValues[2];
		for(int i = 0 ; i < rgbValues.length; i++) {
			rgbValues[i] = (rgbValues[i] / (img.getHeight() * img.getWidth()) ) ;
			
			if (rgbValues[i] > 255) rgbValues[i] = 255;
			else if (rgbValues[i] < 0) rgbValues[i] = 0;
		}
		
		return rgbValues;
		
	}

	/**
	 * Methode qui retourne l'image du chemin entre en parametre
	 * @param path chemin de l'image
	 * @return l'image du chemin
	 */
	public static BufferedImage getImageFromPath(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new URL("file:///" + path));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erreur de lecture du fichier d'image");
		}

		return img;
	}
}

