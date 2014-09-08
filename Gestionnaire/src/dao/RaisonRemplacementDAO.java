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
import dao.daoPOJO.RaisonRemplacement;

/**
 * DAO pour les objets {@link RaisonRemplacement}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class RaisonRemplacementDAO extends DAO<RaisonRemplacement> {

	/**
	 * Construit un RaisonRemplacementDAO avec la connexion sp�cifi�e.
	 * 
	 * @param conn
	 *            Connection utilis�e pour se connecter � la base de donn�es.
	 */
	public RaisonRemplacementDAO(Connection conn) {
		super(conn);
	}

	/**
	 * M�thode de cr�ation d'une rang�e dans la table {@link RaisonRemplacement}
	 * de la base de donn�es.
	 * 
	 * @param raisonRemp
	 *            {@link RaisonRemplacement} qui sera ins�r� dans la base de
	 *            donn�e
	 * @return boolean Retourne vrai si la cr�ation s'est effectu�e avec succ�s
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lanc�e.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(RaisonRemplacement raisonRemp)
			throws DAOCreateException {
		String query = "INSERT INTO raisonRemplacement (raisonRemp_txt)"
				+ " VALUES (?)";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, raisonRemp.getName());
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * M�thode pour supprimer la rang�e associ�e � l'objet
	 * {@link RaisonRemplacement} de la base de donn�es.
	 * 
	 * @param raisonRemp
	 *            {@link RaisonRemplacement} qui sera supprim� dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la supression s'est effectu�e avec
	 *         succ�s dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lanc�e.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(RaisonRemplacement raisonRemp)
			throws DAODeleteException {
		String query = "DELETE FROM raisonRemplacement WHERE raisonRemp_id = ?";
		PreparedStatement pState = null;
		
		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, raisonRemp.getId());
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
	 * M�thode pour la mise � jour de la rang�e associ�e � l'objet
	 * {@link RaisonRemplacement} de la base de donn�es.
	 * 
	 * @param raisonRemp
	 *            {@link RaisonRemplacement} qui sera mis � jour dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la mise � jour s'est effectu�e avec
	 *         succ�s et faux si aucune rang�e n'a �t� mise � jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean update(RaisonRemplacement raisonRemp)
			throws DAOUpdateException {
		String query = "UPDATE raisonRemplacement SET raisonRemp_txt = ?"
				+ " WHERE raisonRemp_id = ?";
		PreparedStatement pState = null;
		
		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, raisonRemp.getName());
			pState.setInt(2, raisonRemp.getId());
			if (pState.executeUpdate() == 0) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new DAOUpdateException(e);
		}finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * M�thode de recherche de l'objet {@link RaisonRemplacement} en fonction de
	 * son id. Dans le cas o� il y aurait une erreur, une exception de type
	 * {@link DAOFindException} est lanc�e.
	 * 
	 * @param id
	 *            Id de l'objet {@link RaisonRemplacement} recherch�
	 * @return raisonRemp Retourne l'objet {@link RaisonRemplacement} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null. Seul le
	 *         premier sera renvoy�.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public RaisonRemplacement find(int id) throws DAOFindException {
		String query = "SELECT raisonRemp_txt FROM raisonRemplacement"
				+ " WHERE raisonRemp_id = ?";
		PreparedStatement pState = null;
		ResultSet result = null;
		
		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				RaisonRemplacement raisonRemp = new RaisonRemplacement();
				raisonRemp.setId(id);
				raisonRemp.setName(result.getString("raisonRemp_txt"));
				return raisonRemp;
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
	 * M�thode de recherche de l'objet {@link RaisonRemplacement} en fonction de
	 * son nom qui est unique. Dans le cas o� il y aurait une erreur, une
	 * exception de type {@link DAOFindException} est lanc�e.
	 * 
	 * @param nom
	 *            nom de l'objet {@link RaisonRemplacement} recherch�
	 * @return qualif Retourne l'objet {@link RaisonRemplacement} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null. Seul le
	 *         premier sera renvoy�.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public RaisonRemplacement findByNom(String nom) throws DAOFindException{
		String query = "SELECT raisonRemp_id, raisonRemp_txt "
				+ "FROM raisonRemplacement WHERE raisonRemp_txt = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, nom);
			result = pState.executeQuery();
			
			if (result.first()) {
				RaisonRemplacement raisonRemp = new RaisonRemplacement();
				raisonRemp.setId(result.getInt("raisonRemp_id"));
				raisonRemp.setName(result.getString("raisonRemp_txt"));
				return raisonRemp;
			}
			else{
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
	 * M�thode pour obtenir tous les objets {@link RaisonRemplacement} qui
	 * correspondent � chacunes des rang�es de la table. Dans le cas o� il y
	 * aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lanc�e.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link RaisonRemplacement} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<RaisonRemplacement> getAll() throws DAOGetAllException{
		String query = "SELECT raisonRemp_id, raisonRemp_txt"
				+ " FROM raisonRemplacement";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<RaisonRemplacement> al = new ArrayList<RaisonRemplacement>();
			
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();
			
			while (result.next()) {
				RaisonRemplacement raisonRemp = new RaisonRemplacement();
				raisonRemp.setId(result.getInt("raisonRemp_id"));
				raisonRemp.setName(result.getString("raisonRemp_txt"));

				al.add(raisonRemp);
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