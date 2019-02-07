package image.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class FileManager {

	public static File getFolder(String path) {
		
		return null;
	}
	
	public static ArrayList<BufferedImage> getImagesFromFolder(String path) {
		
		File folder = new File(path);
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		
		for(File f : folder.listFiles()) {
			
			String fileName = f.getName();
			
			if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".png")) {
				
				try {
					BufferedImage image = ImageIO.read(new File(f.getAbsolutePath()));
					images.add(image);
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
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
	
	
	public static void main(String[] args) {
		
		String path = "D:\\Cegep\\Session_4\\IA Data\\mnist_png\\mnist_png\\training";
		
		ArrayList<String> f = getFoldersFromFolder(path);
		for(String s : f) System.out.println(s);
		
	}
}
