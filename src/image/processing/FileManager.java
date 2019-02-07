package image.processing;

import java.awt.image.BufferedImage;
import java.io.File;

public class FileManager {

	public static File getFolder(String path) {
		
		return null;
	}
	
	public static BufferedImage[] getImagesFromFolder(String path) {
		
		File folder = new File(path);
		
		for(File f : folder.listFiles()) {
			
			String fileName = f.getName();
			
			if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(".png")) {
				
				System.out.println(fileName);
			}
		}
		
		return null;
	}
	
	
	public static void main(String[] args) {
		
		
	}
}
