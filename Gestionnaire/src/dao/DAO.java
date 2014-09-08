package dao;

import java.sql.Connection;
import java.util.ArrayList;

import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;

/**
 * La classe template DAO est la classe mère de tous les DAO. C'est elle qui
 * définit l'interface de base de tous les DAO. Les classes héritant de celle-ci
 * sont libre d'implanter leurs propres méthodes.
 * <p>
 * Toutes les méthodes lancent des exceptions appartenant à {@link DAOException}.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public abstract class DAO<T> {
	
	/**
	 * Construit un CoordonneesDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public DAO(Connection conn) {
		this.connection = conn;
	}

	/**
	 * Méthode qui formatte l'array des string en un string utilisable pour la
	 * base de données.
	 * 
	 * @param condition
	 *            String qui contient la condition écrite selon le langage de la
	 *            base de données. (Ex: "AND statut_txt IN (" ). Ne pas oublier
	 *            d'inclure la parenthèse ouvrante.
	 * @param filtres
	 *            ArrayList de tous les filtres recherchés. Si l'array est vide
	 *            aucun filtre sur le statut ne sera appliqué.
	 * @return String Retourne un String formatté que l'on peut utiliser dans
	 *         une clause WHERE (INCLUANT LE AND)
	 */
	public static String formatCondition(String condition,
			ArrayList<String> filtres) {
		String strFiltres = condition;
		boolean first = true;
		if (filtres != null) {
			for (int i = 0; i < filtres.size(); i++) {
				if (filtres.get(i).equals("")) {
					continue;
				}
				// Si c'est le premier, on n'ajoute pas de , avant le filtre
				if (first) {
					strFiltres += "'" + filtres.get(i) + "'";
					first = false;
				} else {
					strFiltres += ", '" + filtres.get(i) + "'";
				}

			}
			// Si on a toujours pas trouvé de filtre valide
			if (first) {
				strFiltres = "";
			} else {
				strFiltres += ")";
			}
		} else {
			strFiltres = "";
		}
		return strFiltres;
	}

	/**
	 * Méthode de création d'une rangée dans la table Objet de la base de
	 * données.
	 * 
	 * @param obj
	 *            Objet qui sera inséré dans la base de données
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	public abstract boolean create(T obj) throws DAOCreateException;

	/**
	 * Méthode pour supprimer la rangée associée à l'objet de la base de
	 * données.
	 * 
	 * @param obj
	 *            Objet qui sera supprimé dans la base de données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	public abstract boolean delete(T obj) throws DAODeleteException;

	/**
	 * Méthode pour la mise à jour de la rangée associée à l'objet de la base de
	 * données.
	 * 
	 * @param obj
	 *            Objet qui sera mis à jour dans la base de données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	public abstract boolean update(T obj) throws DAOUpdateException;

	/**
	 * Méthode de recherche de l'objet en fonction de son id. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lancée.
	 * 
	 * @param id
	 *            Id de l'objet recherché
	 * @return Objet Retourne l'objet recherché s'il existe, dans le cas
	 *         contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public abstract T find(int id) throws DAOFindException;

	/**
	 * Méthode pour obtenir tous les objets qui correspondent à chacunes des
	 * rangées de la table. Dans le cas où il y aurait une erreur, une exception
	 * de type {@link DAOGetAllException} est lancée.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets de la
	 *         table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public abstract ArrayList<T> getAll() throws DAOGetAllException;

	/**
	 * Connection vers la base de donnée
	 */
	protected Connection connection = null;
}