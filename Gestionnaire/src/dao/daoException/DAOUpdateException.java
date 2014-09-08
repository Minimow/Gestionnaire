package dao.daoException;

/**
 * Exception lancée lors d'une erreur dans la mise à jour d'une rangée dans la
 * base de données par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAOUpdateException extends DAOException {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAOUpdateException avec les paramètres par défaut.
	 */
	public DAOUpdateException() {
		super();
	}

	/**
	 * Construit un DAOUpdateException avec le message d'erreur spécifié.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOUpdateException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOUpdateException avec le message d'erreur et la cause
	 * spécifiées.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOUpdateException avec la cause spécifié.
	 * 
	 * @param cause
	 *            erreur qui sera associée dans celle-ci
	 */
	public DAOUpdateException(Throwable cause) {
		super(cause);
	}
}