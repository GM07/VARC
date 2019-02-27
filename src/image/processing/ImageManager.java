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

public class ImageManager {

	/**
	 * Method that converts an image into a one dimensional array ([r1, g1, b1, r2, g2, ...)
	 * @param img : image (BufferedImage)
	 * @return
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
	 * Squares up an image
	 * @param img
	 * @param imageSize
	 * @return
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
	 * Function that gets the r, g and b values of a pixel from an image
	 * @param img : image
	 * @param x : x coordinate
	 * @param y : y coordinate
	 * @return
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

	public static double[] getAverageColorFromMiddle(BufferedImage img) {

		double[] rgbValues = new double[3];
		double middleX = img.getWidth()/2d;
		double middleY = img.getHeight()/2d;
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

