package image.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class FileManager {
	
	public static ArrayList<BufferedImage> getImagesFromFolder(String path) {
		
		File folder = new File(path);
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(File f : folder.listFiles()) {

			BufferedImage image = getImageFromFile(f);
			if (image != null) images.add(image);
		}
		
		return images;
	}


	public static ArrayList<BufferedImage> getImagesFromFolder(String path, int numberOfImages) {
		File folder = new File(path);
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

		int max = folder.listFiles().length;
		if (max > numberOfImages) max = numberOfImages;

		for(int i = 0; i < max; i++) {

			File f = folder.listFiles()[i];

			BufferedImage image = getImageFromFile(f);
			if (image != null) images.add(image);

		}

		return images;
	}

	public static ArrayList<String> getFoldersFromFolder(String path) {
		
		File folder = new File(path);
		ArrayList<String> folders = new ArrayList<String>();
		
		for(File f: folder.listFiles()) {
			
			folders.add(f.getName());
		}
		
		return folders;
	}

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
