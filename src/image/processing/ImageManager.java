package image.processing;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

	public static void main(String[] args) {

		try {
			BufferedImage b = ImageIO.read(new File("C:\\Users\\mehga\\Desktop\\green.jpg"));

			BufferedImage imageSquared = ImageManager.getSquaredImage(b, 64);
			double[] imageRGB = ImageManager.convertRGB(imageSquared);

			System.out.println("AAHH");
			for(int i = 0; i < 64 * 64; i++) {
				System.out.println(i + " : " + imageRGB[i * 3] + ", " + imageRGB[i * 3 + 1] + ", " + imageRGB[i * 3 + 2]);

			}

		} catch(IOException e) {
			e.printStackTrace();
		}
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
}

