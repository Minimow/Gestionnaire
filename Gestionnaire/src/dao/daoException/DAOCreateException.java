package dao.daoException;

/**
 * Exception lancée lors d'une erreur dans la creation d'une rangée dans la base
 * de données par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAOCreateException extends DAOException {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAOCreateException avec les paramètres par défaut.
	 */
	public DAOCreateException() {
		super();
	}

	/**
	 * Construit un DAOCreateException avec le message d'erreur spécifié.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOCreateException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOCreateException avec le message d'erreur et la cause
	 * spécifiées.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOCreateException avec la cause spécifié.
	 * 
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOCreateException(Throwable cause) {
		super(cause);
	}
}