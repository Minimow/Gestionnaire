package dao.daoException;

/**
 * La classe DAOException et toutes ses sous-classes h�ritent de
 * {@link Throwable} qui indique les conditions qu'une application voudrait
 * traiter dans un bloc catch. Il s'agit d'une exception checked qui doit
 * obligatoirement �tre attrap�e dans un bloc catch.
 * <p>
 * Toutes les exceptions g�n�r�es dans les DAO h�ritent de cette classe. Le bloc
 * catch (DAOException e) attrape donc toutes les exceptions h�ritant de cette
 * classe.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class DAOException extends Exception {
	/**
	 * Le num�ro de s�rialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String contenant le texte � afficher lors des messages d'erreurs.
	 */
	public static String TITRE = "Erreur dans la base de donn�es";
	public static String TYPE_CREATE = "L'objet voulu n'a pas pu �tre cr��."
			+ " Veuillez v�rifier la validit� des informations transmises.";
	public static String TYPE_DELETE = "L'objet n'a pas pu �tre supprim�."
			+ " Veuillez v�rifier la validit� des informations transmises.";
	public static String TYPE_UPDATE = "L'objet n'a pas pu �tre mis � jour."
			+ " Veuillez v�rifier la validit� des informations transmises.";
	public static String TYPE_FIND = "L'objet n'a pas pu �tre r�cup�r�."
			+ " Veuillez v�rifier la validit� des informations transmises.";
	public static String TYPE_GETALL = "Les objets de la table n'ont pas pu �tre r�cup�r�s."
			+ " Veuillez v�rifier la validit� des informations transmises.";

	/**
	 * Construit un DAOException avec les param�tres par d�faut.
	 */
	public DAOException() {
		super();
	}

	/**
	 * Construit un DAOException avec le message d'erreur sp�cifi�.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOException avec le message d'erreur et la cause
	 * sp�cifi�es.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associ�e dans celle-ci
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOException avec la cause sp�cifi�.
	 * 
	 * @param cause
	 *            erreur qui sera associ�e dans celle-ci
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}