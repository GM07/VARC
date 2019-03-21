package image.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Classe qui facilite l'ouverture de fichiers et qui permet d'extraire les dossiers et les images dans un chemin d'acces
 * @author Gaya Mehenni
 */
public class FileManager {

	/**
	 * Methode qui retourne toutes les images (.png ou .jpg) dans un dossier
	 * @param path chemin d'acces du dossier
	 * @return tableau d'images
	 */
	public static ArrayList<BufferedImage> getImagesFromFolder(String path) {
		
		File folder = new File(path);
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(File f : folder.listFiles()) {

			BufferedImage image = getImageFromFile(f);
			if (image != null) images.add(image);
		}
		
		return images;
	}


	/**
	 * Methode qui retourne un certain nombre d'images (.png ou .jpg) dans un dossier
	 * @param path chemin d'acces du dossier
	 * @param numberOfImages nombre d'images desire
	 * @return tableau d'images
	 */
	public static ArrayList<BufferedImage> getImagesFromFolder(String path, int numberOfImages) {
		File folder = new File(path);
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

		int max = folder.listFiles().length;
		if (max > numberOfImages) max = numberOfImages;

		System.out.println("\t\t- Nombre d'images : " + max);

		for(int i = 0; i < max; i++) {

			if (i % 100 == 0)System.out.print(i + ", ");
			File f = folder.listFiles()[i];

			BufferedImage image = getImageFromFile(f);
			if (image != null) images.add(image);

		}

		return images;
	}

	/**
	 * Methode qui retourne tous les noms des dossiers presents dans un chemin d'acces
	 * @param path chemin d'acces
	 * @return tableau contenant les noms des dossiers
	 */
	public static ArrayList<String> getFoldersFromFolder(String path) {
		
		File folder = new File(path);
		ArrayList<String> folders = new ArrayList<String>();
		
		for(File f: folder.listFiles()) {
			
			folders.add(f.getName());
		}
		
		return folders;
	}

	/**
	 * Methode qui retourne l'image presente dans un fichier
	 * @param f fichier
	 * @return image
	 */
	private static BufferedImage getImageFromFile(File f) {
		String fileName = f.getName();

		if (fileName.substring(fileName.lastIndexOf(".")).equals(".png") || fileName.substring(fileName.lastIndexOf(".")).equals(".jpg")) {

			try {
				BufferedImage image = ImageIO.read(new File(f.getAbsolutePath()));
				return image;
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		return null;
	}



}
