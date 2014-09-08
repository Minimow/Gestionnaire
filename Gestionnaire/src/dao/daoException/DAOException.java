package dao.daoException;

/**
 * La classe DAOException et toutes ses sous-classes héritent de
 * {@link Throwable} qui indique les conditions qu'une application voudrait
 * traiter dans un bloc catch. Il s'agit d'une exception checked qui doit
 * obligatoirement être attrapée dans un bloc catch.
 * <p>
 * Toutes les exceptions générées dans les DAO héritent de cette classe. Le bloc
 * catch (DAOException e) attrape donc toutes les exceptions héritant de cette
 * classe.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class DAOException extends Exception {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * String contenant le texte à afficher lors des messages d'erreurs.
	 */
	public static String TITRE = "Erreur dans la base de données";
	public static String TYPE_CREATE = "L'objet voulu n'a pas pu être créé."
			+ " Veuillez vérifier la validité des informations transmises.";
	public static String TYPE_DELETE = "L'objet n'a pas pu être supprimé."
			+ " Veuillez vérifier la validité des informations transmises.";
	public static String TYPE_UPDATE = "L'objet n'a pas pu être mis à jour."
			+ " Veuillez vérifier la validité des informations transmises.";
	public static String TYPE_FIND = "L'objet n'a pas pu être récupéré."
			+ " Veuillez vérifier la validité des informations transmises.";
	public static String TYPE_GETALL = "Les objets de la table n'ont pas pu être récupérés."
			+ " Veuillez vérifier la validité des informations transmises.";

	/**
	 * Construit un DAOException avec les paramètres par défaut.
	 */
	public DAOException() {
		super();
	}

	/**
	 * Construit un DAOException avec le message d'erreur spécifié.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOException avec le message d'erreur et la cause
	 * spécifiées.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOException avec la cause spécifié.
	 * 
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}