package image.processing;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

		BufferedImage bimage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);

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
				(double) ((argb >>  8) & 0xff), //green
				(double) ((argb      ) & 0xff)  //blue
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
	public static int[] getColorOfImage(BufferedImage img) {

		int[] rgbValues = new int[3];

		System.out.println(img.getWidth() + ", " + img.getHeight());
		for(int i = 0; i < img.getWidth(); i++) {
			for(int j = 0; j < img.getHeight(); j++) {

				int rgb = img.getRGB(i, j);
				int r = (rgb >> 16) & 0xff;
				int g = (rgb >> 8) & 0xff;
				int b = (rgb) & 0xff;

				//System.out.println(r + ", " + g + ", " + b);
				rgbValues[0] += r;
				rgbValues[1] += g;
				rgbValues[2] += b;

			}
		}

		for(int i = 0; i < rgbValues.length; i++) {
			rgbValues[i] = (int) (rgbValues[i] / (double) (img.getWidth() * img.getHeight()));
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

