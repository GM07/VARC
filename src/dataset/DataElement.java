package dataset;

/**
 * Classe qui s'occupe de gerer un element d'un dataset en stockant le label et la valeur
 * @author Gaya Mehenni
 * @param <D> Type du label
 * @param <E> Type de la valeur stockee
 */
public class DataElement<D, E> {
	
	private D label;
	private E data;

	/**
	 * Constructeur
	 * @param label label de la donnee
	 * @param data valeur de la donnee
	 */
	public DataElement(D label, E data) {
		this.label = label;
		this.data = data;
	}

	/**
	 * Methode qui definit quoi afficher quand l'element est appele a etre affichee sur la console
	 * @return
	 */
	public String toString() {

		return "LABEL : " + label + "\tDATA : " + data;
	}

	/**
	 * Retourne le label de la donnee
	 * @return
	 */
	public D getLabel() {
		return label;
	}

	/**
	 * Change le label de la donnee
	 * @param label
	 */
	public void setLabel(D label) {
		this.label = label;
	}

	/**
	 * Retourne la valeur de la donnee
	 * @return
	 */
	public E getData() {
		return data;
	}

	/**
	 * Change la valeur de la donnee
	 * @param data
	 */
	public void setData(E data) {
		this.data = data;
	}

	
}
