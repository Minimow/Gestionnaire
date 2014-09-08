package dao.daoException;

/**
 * Exception lancée lors d'une erreur dans la recherche d'une rangée dans la
 * base de données par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAOFindException extends DAOException {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAOFindException avec les paramètres par défaut.
	 */
	public DAOFindException() {
		super();
	}

	/**
	 * Construit un DAOFindException avec le message d'erreur spécifié.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOFindException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOFindException avec le message d'erreur et la cause
	 * spécifiées.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOFindException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOFindException avec la cause spécifié.
	 * 
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOFindException(Throwable cause) {
		super(cause);
	}
}