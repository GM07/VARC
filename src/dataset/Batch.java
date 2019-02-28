package dataset;

import java.util.ArrayList;
import java.util.Collections;


/**
 * 
 * @author Gaya Mehenni
 *
 * @param <E> type de donnee qui va etre traitee
 */

public class Batch<E> {

	private ArrayList<E> dataset;

	/**
	 * Constructeur
	 */
	public Batch() {
		dataset = new ArrayList<E>();
	}

	/**
	 * Constructeur
	 * @param dataset le dataset
	 */
	public Batch(ArrayList<E> dataset){
		this.dataset = dataset;
	}
	
	/**
	 * Melange le dataset dans un ordre aleatoire
	 * @return
	 */
	public ArrayList<E> shuffleDataset() {

	    Collections.shuffle(dataset);
        return dataset;
	}

	/**
	 * @param number
	 * @return
	 */
	public ArrayList<E> getPartOfDataset(int number) {

        ArrayList<E> shuffled = shuffleDataset();
        ArrayList<E> temp = new ArrayList<E>();

        for(int i = 0; i < number; i++) {
            temp.add(shuffled.get(i));
        }

		return temp;
	}

	/**
	 * Ajoute un element au dataset
	 * @param element l'element a ajouter
	 */
	public void addElementToDataset(E element) {
		dataset.add(element);
	}

	/**
	 * Retourne une chaine de caracteres qui va afficher les informations sur le batch
	 * @return String
	 */
	public String toString() {

		String s = "BATCH";

		for(int i = 0; i < dataset.size(); i++) {

			s += "\n\t" + dataset.get(i);
		}

		return s;
	}

	/**
	 * Retourne le dataset
	 * @return dataset
	 */
	public ArrayList<E> getDataset() {
		return dataset;
	}

	/**
	 * Change le dataset
	 * @param dataset
	 */
	public void setDataset(ArrayList<E> dataset) {
		this.dataset = dataset;
	}



	public static void main(String[] args) {

		ArrayList<DataElement> dataset = new ArrayList<DataElement>();

		for(int i = 0; i < 10; i++) {
			DataElement<String, Integer> d = new DataElement(i + "", i*i);

			dataset.add(d);
		}

		Batch batch = new Batch(dataset);

		System.out.println(batch);
		batch.setDataset(batch.getPartOfDataset(3));
		System.out.println(batch);
	}
}
