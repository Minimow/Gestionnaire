package dao.daoException;

/**
 * Exception lanc�e lors d'une erreur dans la recherche de toutes les rang�es
 * d'une table dans la base de donn�es par un DAO.
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAOException
 */
public class DAOGetAllException extends DAOException {
	/**
	 * Le num�ro de s�rialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construit un DAOGetAllException avec les param�tres par d�faut.
	 */
	public DAOGetAllException() {
		super();
	}

	/**
	 * Construit un DAOGetAllException avec le message d'erreur sp�cifi�.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 */
	public DAOGetAllException(String message) {
		super(message);
	}

	/**
	 * Construit un DAOGetAllException avec le message d'erreur et la cause
	 * sp�cifi�es.
	 * 
	 * @param message
	 *            message d'erreur de l'exception
	 * @param cause
	 *            erreur qui sera associ�e dans celle-ci
	 */
	public DAOGetAllException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Construit un DAOGetAllException avec la cause sp�cifi�.
	 * 
	 * @param cause
	 *            erreur qui sera associ�e dans celle-ci
	 */
	public DAOGetAllException(Throwable cause) {
		super(cause);
	}
}