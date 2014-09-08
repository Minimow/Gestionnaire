package dao.daoException;

/**
 * Exception lancée lors d'une erreur dans la supression d'une rangée dans la
 * base de données par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAODeleteException extends DAOException {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAODeleteException avec les paramètres par défaut.
	 */
	public DAODeleteException() {
		super();
	}

	/**
	 * Construit un DAODeleteException avec le message d'erreur spécifié.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAODeleteException(String message) {
		super(message);
	}

	/**
	 * Construit un DAODeleteException avec le message d'erreur et la cause
	 * spécifiées.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAODeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAODeleteException avec la cause spécifié.
	 * 
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAODeleteException(Throwable cause) {
		super(cause);
	}
}