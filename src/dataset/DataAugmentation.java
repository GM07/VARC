package dataset;

import image.processing.FileManager;
import image.processing.ImageManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe qui permet d'augmenter la quantite de donnees a analyser par les reseaux de neurons
 * @author Gaya Mehenni
 */
public class DataAugmentation {

    // Nombre de copies differentes d'une
    private static int numberOfCopies = 100;

    /**
     * Methode qui augmente le nombre d'image dans une banque d'image
     * Il faut que la banque d'image soit sous la forme (dossier d'image -> dossiers des labels -> images)
     * @param path chemin d'acces de la banque d'image
     */
    public static void increaseData(String path){

        ArrayList<String> folders = FileManager.getFoldersFromFolder(path);

        for(int folder = 0; folder < folders.size(); folder++) {

            ArrayList<BufferedImage> images = FileManager.getImagesFromFolder(path + "\\" + folders.get(folder));

            System.out.println(folders.get(folder));

            for(int image = images.size() - 1; image >= 0; image--) {

                System.out.println(image + " ");

                // On parcoure chaque image pour effectuer deux rotations
                BufferedImage img = ImageManager.getSquaredImage(images.get(image), 64);
                BufferedImage emptyImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

                for(int i = 0; i < numberOfCopies; i++) {

                    emptyImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

                    try {
                        Graphics g = emptyImage.getGraphics();
                        Graphics2D g2d = (Graphics2D) g;

                        AffineTransform af = new AffineTransform();
                        af.rotate((Math.random() * Math.PI * 2) + 0.1, img.getWidth()/2, img.getHeight()/2);
                        double scale = (Math.random() * 0.3) + 0.7;
                        af.scale(scale, scale);
                        g2d.drawImage(img, af, null);
                        ImageIO.write(ImageManager.getSquaredImage(emptyImage, 64), "png", new File(path + "\\" + folders.get(folder) + "\\" + image + "_" + i + ".png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //System.out.println(path + "\\" + folders.get(folder) + "\\" + image + "_" + i + ".png");
                }
            }

        }

    }

    /**
     * Methode qui teste l'augmentation de la banque d'image
     * @param args
     */
    public static void main(String[] args) {

        DataAugmentation.increaseData("D:\\Cegep\\Session_4\\IA Data\\Dataset_Marques");
    }

}
