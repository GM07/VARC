package dataset;

public class DataElement<D, E> {
	
	private D label;
	private E data;
	
	public DataElement(D label, E data) {
		this.label = label;
		this.data = data;
	}

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
