package dao.daoException;

/**
 * Exception lanc�e lors d'une erreur dans la creation d'une rang�e dans la base
 * de donn�es par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAOCreateException extends DAOException {
	/**
	 * Le num�ro de s�rialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAOCreateException avec les param�tres par d�faut.
	 */
	public DAOCreateException() {
		super();
	}

	/**
	 * Construit un DAOCreateException avec le message d'erreur sp�cifi�.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOCreateException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOCreateException avec le message d'erreur et la cause
	 * sp�cifi�es.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associ�e dans celle-ci
	 */
	public DAOCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOCreateException avec la cause sp�cifi�.
	 * 
	 * @param cause
	 *            erreur qui sera associ�e dans celle-ci
	 */
	public DAOCreateException(Throwable cause) {
		super(cause);
	}
}