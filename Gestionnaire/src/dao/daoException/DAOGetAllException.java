package dao.daoException;

/**
 * Exception lancée lors d'une erreur dans la recherche de toutes les rangées
 * d'une table dans la base de données par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAOGetAllException extends DAOException {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAOGetAllException avec les paramètres par défaut.
	 */
	public DAOGetAllException() {
		super();
	}

	/**
	 * Construit un DAOGetAllException avec le message d'erreur spécifié.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOGetAllException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOGetAllException avec le message d'erreur et la cause
	 * spécifiées.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOGetAllException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOGetAllException avec la cause spécifié.
	 * 
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOGetAllException(Throwable cause) {
		super(cause);
	}
}