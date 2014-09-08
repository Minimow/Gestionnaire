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
 * La classe template DAO est la classe m�re de tous les DAO. C'est elle qui
 * d�finit l'interface de base de tous les DAO. Les classes h�ritant de celle-ci
 * sont libre d'implanter leurs propres m�thodes.
 * <p>
 * Toutes les m�thodes lancent des exceptions appartenant � {@link DAOException}.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public abstract class DAO<T> {
	
	/**
	 * Construit un CoordonneesDAO avec la connexion sp�cifi�e.
	 * 
	 * @param conn
	 *            Connection utilis�e pour se connecter � la base de donn�es.
	 */
	public DAO(Connection conn) {
		this.connection = conn;
	}

	/**
	 * M�thode qui formatte l'array des string en un string utilisable pour la
	 * base de donn�es.
	 * 
	 * @param condition
	 *            String qui contient la condition �crite selon le langage de la
	 *            base de donn�es. (Ex: "AND statut_txt IN (" ). Ne pas oublier
	 *            d'inclure la parenth�se ouvrante.
	 * @param filtres
	 *            ArrayList de tous les filtres recherch�s. Si l'array est vide
	 *            aucun filtre sur le statut ne sera appliqu�.
	 * @return String Retourne un String formatt� que l'on peut utiliser dans
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
			// Si on a toujours pas trouv� de filtre valide
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
	 * M�thode de cr�ation d'une rang�e dans la table Objet de la base de
	 * donn�es.
	 * 
	 * @param obj
	 *            Objet qui sera ins�r� dans la base de donn�es
	 * @return boolean Retourne vrai si la cr�ation s'est effectu�e avec succ�s
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lanc�e.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	public abstract boolean create(T obj) throws DAOCreateException;

	/**
	 * M�thode pour supprimer la rang�e associ�e � l'objet de la base de
	 * donn�es.
	 * 
	 * @param obj
	 *            Objet qui sera supprim� dans la base de donn�es
	 * @return boolean Retourne vrai si la supression s'est effectu�e avec
	 *         succ�s dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lanc�e.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	public abstract boolean delete(T obj) throws DAODeleteException;

	/**
	 * M�thode pour la mise � jour de la rang�e associ�e � l'objet de la base de
	 * donn�es.
	 * 
	 * @param obj
	 *            Objet qui sera mis � jour dans la base de donn�es
	 * @return boolean Retourne vrai si la mise � jour s'est effectu�e avec
	 *         succ�s et faux si aucune rang�e n'a �t� mise � jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	public abstract boolean update(T obj) throws DAOUpdateException;

	/**
	 * M�thode de recherche de l'objet en fonction de son id. Dans le cas o� il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lanc�e.
	 * 
	 * @param id
	 *            Id de l'objet recherch�
	 * @return Objet Retourne l'objet recherch� s'il existe, dans le cas
	 *         contraire la m�thode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public abstract T find(int id) throws DAOFindException;

	/**
	 * M�thode pour obtenir tous les objets qui correspondent � chacunes des
	 * rang�es de la table. Dans le cas o� il y aurait une erreur, une exception
	 * de type {@link DAOGetAllException} est lanc�e.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets de la
	 *         table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public abstract ArrayList<T> getAll() throws DAOGetAllException;

	/**
	 * Connection vers la base de donn�e
	 */
	protected Connection connection = null;
}