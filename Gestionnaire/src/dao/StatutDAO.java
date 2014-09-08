package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.DBConnection;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Statut;

/**
 * DAO pour les objets {@link Statut}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class StatutDAO extends DAO<Statut> {

	/**
	 * Construit un StatutDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public StatutDAO(Connection conn) {
		super(conn);
	}

	/**
	 * Méthode de création d'une rangée dans la table {@link Statut} de la base
	 * de données.
	 * 
	 * @param statut
	 *            Objet qui sera inséré dans la base de donnée
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	public boolean create(Statut statut) throws DAOCreateException {
		String query = "INSERT INTO statut (statut_txt) VALUES" + " (?)";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, statut.getName());
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode pour supprimer la rangée associée à l'objet {@link Statut} de la
	 * base de données.
	 * 
	 * @param statut
	 *            {@link Statut} qui sera supprimé dans la base de données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	public boolean delete(Statut statut) throws DAODeleteException {
		String query = "DELETE FROM statut WHERE statut_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, statut.getId());
			if (pState.executeUpdate() == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new DAODeleteException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode pour la mise à jour de la rangée associée à l'objet
	 * {@link Statut} de la base de données.
	 * 
	 * @param statut
	 *            {@link Statut} qui sera mis à jour dans la base de données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAOUpdateException
	 */
	public boolean update(Statut statut) throws DAOUpdateException {
		String query = "UPDATE statut SET statut_txt = ? WHERE statut_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, statut.getName());
			pState.setInt(2, statut.getId());
			if (pState.executeUpdate() == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new DAOUpdateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode de recherche de l'objet en fonction de son id. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lancée.
	 * 
	 * @param id
	 *            Id de l'objet recherché
	 * @return statut Retourne l'objet {@link Statut} recherché s'il existe,
	 *         dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public Statut find(int id) throws DAOFindException {
		String query = "SELECT statut_txt FROM statut WHERE statut_id = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				Statut statut = new Statut(id, result.getString("statut_txt"));
				return statut;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DAOFindException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode de recherche de l'objet en fonction de son nom. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lancée.
	 * 
	 * @param nom
	 *            nom du statut recherché
	 * @return statut Retourne l'objet {@link Statut} recherché s'il existe, dans
	 *         le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public Statut findByNom(String nom) throws DAOFindException {
		String query = "SELECT statut_id FROM statut WHERE statut_txt = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, nom);
			result = pState.executeQuery();
			if (result.first()) {
				Statut statut = new Statut(result.getInt("statut_id"), nom);
				return statut;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new DAOFindException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode pour obtenir tous les objets {@link Statut} qui correspondent à
	 * chacunes des rangées de la table. Dans le cas où il y aurait une erreur,
	 * une exception de type {@link DAOGetAllException} est lancée.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Statut} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<Statut> getAll() throws DAOGetAllException{
		String query = "SELECT statut_id, statut_txt FROM statut"
				+ " ORDER BY statut_id";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Statut> al = new ArrayList<Statut>();
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Statut statut = new Statut(result.getInt("statut_id"),
						result.getString("statut_txt"));
				al.add(statut);
			}
			return al;
		} catch (SQLException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}
}