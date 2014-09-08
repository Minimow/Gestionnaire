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
import dao.daoPOJO.Qualification;

/**
 * DAO pour les objets {@link Qualification}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class QualificationDAO extends DAO<Qualification> {

	/**
	 * Construit un QualificationDAO avec la connexion sp�cifi�e.
	 * 
	 * @param conn
	 *            Connection utilis�e pour se connecter � la base de donn�es.
	 */
	public QualificationDAO(Connection conn) {
		super(conn);
	}

	/**
	 * M�thode de cr�ation d'une rang�e dans la table {@link Qualification} de
	 * la base de donn�es.
	 * 
	 * @param qualif
	 *            {@link Qualification} qui sera ins�r� dans la base de donn�e
	 * @return boolean Retourne vrai si la cr�ation s'est effectu�e avec succ�s
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lanc�e.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(Qualification qualif) throws DAOCreateException {
		String query = "INSERT INTO qualification (qualif_nom, qualif_acronyme,"
				+ " qualif_priorite, qualif_anneesValide) VALUES (?, ?, ?, ?)";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, qualif.getName());
			pState.setString(2, qualif.getAcronyme());
			pState.setInt(3, qualif.getPriority());
			pState.setInt(4, qualif.getYearsValid());
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * M�thode pour supprimer la rang�e associ�e � l'objet {@link Qualification}
	 * de la base de donn�es.
	 * 
	 * @param qualif
	 *            {@link Qualification} qui sera supprim� dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la supression s'est effectu�e avec
	 *         succ�s dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lanc�e.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(Qualification qualif) throws DAODeleteException {
		String query = "DELETE FROM qualification WHERE qualif_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, qualif.getId());
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
	 * {@link Qualification} de la base de donn�es.
	 * 
	 * @param qualif
	 *            {@link Qualification} qui sera mis � jour dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la mise � jour s'est effectu�e avec
	 *         succ�s et faux si aucune rang�e n'a �t� mise � jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean update(Qualification qualif) throws DAOUpdateException {
		String query = "Update qualification"
				+ " SET qualif_nom = ?, qualif_acronyme = ?,"
				+ " qualif_priorite = ?, qualif_anneesValide = ?"
				+ " WHERE qualif_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, qualif.getName());
			pState.setString(2, qualif.getAcronyme());
			pState.setInt(3, qualif.getPriority());
			pState.setInt(4, qualif.getYearsValid());
			pState.setInt(5, qualif.getId());
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
	 * M�thode de recherche de l'objet {@link Qualification} en fonction de son
	 * id. Dans le cas o� il y aurait une erreur, une exception de type
	 * {@link DAOFindException} est lanc�e.
	 * 
	 * @param id
	 *            Id de l'objet {@link Qualification} recherch�
	 * @return qualif Retourne l'objet {@link Qualification} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null. Seul le
	 *         premier sera renvoy�.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public Qualification find(int id) throws DAOFindException {
		String query = "SELECT qualif_id, qualif_nom, qualif_acronyme,"
				+ " qualif_priorite, qualif_anneesValide FROM qualification WHERE qualif_id = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				Qualification qualif = new Qualification(
						result.getInt("qualif_id"),
						result.getString("qualif_nom"),
						result.getString("qualif_acronyme"),
						result.getInt("qualif_priorite"));
				qualif.setYearsValid(result.getInt("qualif_anneesValide"));
				return qualif;
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
	 * M�thode de recherche de l'objet {@link Qualification} en fonction de son
	 * acronyme qui est unique. Dans le cas o� il y aurait une erreur, une
	 * exception de type {@link DAOFindException} est lanc�e.
	 * 
	 * @param acronyme
	 *            acronyme de l'objet {@link Qualification} recherch�
	 * @return qualif Retourne l'objet {@link Qualification} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null. Seul le
	 *         premier sera renvoy�.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public Qualification findByAcronyme(String acronyme)
			throws DAOFindException {
		String query = "SELECT qualif_id, qualif_nom, qualif_acronyme,"
				+ " qualif_priorite, qualif_anneesValide FROM qualification"
				+ " WHERE qualif_acronyme = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, acronyme);
			result = pState.executeQuery();
			if (result.first()) {
				Qualification qualif = new Qualification(
						result.getInt("qualif_id"),
						result.getString("qualif_nom"),
						result.getString("qualif_acronyme"),
						result.getInt("qualif_priorite"));
				qualif.setYearsValid(result.getInt("qualif_anneesValide"));
				return qualif;
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
	 * M�thode de recherche de l'objet {@link Qualification} en fonction de son
	 * id. Dans le cas o� il y aurait une erreur, une exception de type
	 * {@link DAOFindException} est lanc�e.
	 * 
	 * @param nom
	 *            nom de l'objet {@link Qualification} recherch�
	 * @return qualif Retourne l'objet {@link Qualification} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null. Seul le
	 *         premier sera renvoy�.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public Qualification findByNom(String nom) throws DAOFindException {
		String query = "SELECT qualif_id, qualif_nom, qualif_acronyme,"
				+ " qualif_priorite, qualif_anneesValide FROM qualification"
				+ " WHERE qualif_nom = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, nom);
			result = pState.executeQuery();

			if (result.first()) {
				Qualification qualif = new Qualification(
						result.getInt("qualif_id"),
						result.getString("qualif_nom"),
						result.getString("qualif_acronyme"),
						result.getInt("qualif_priorite"));
				qualif.setYearsValid(result.getInt("qualif_anneesValide"));
				return qualif;
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
	 * M�thode pour obtenir tous les objets {@link Qualification} qui
	 * correspondent � chacunes des rang�es de la table. Dans le cas o� il y
	 * aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lanc�e.
	 * <p>
	 * Les qualifications sont ordonn�es par leur priorit� en ordre descendant.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Qualification} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<Qualification> getAll() throws DAOGetAllException{
		String query = "SELECT qualif_id, qualif_nom, qualif_acronyme,"
				+ " qualif_priorite, qualif_anneesValide FROM qualification"
				+ " ORDER BY qualif_priorite DESC";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Qualification> al = new ArrayList<Qualification>();
			pState = connection.prepareStatement(query);

			result = pState.executeQuery();
			while (result.next()) {
				Qualification qualif = new Qualification(
						result.getInt("qualif_id"),
						result.getString("qualif_nom"),
						result.getString("qualif_acronyme"),
						result.getInt("qualif_priorite"));
				qualif.setYearsValid(result.getInt("qualif_anneesValide"));
				al.add(qualif);
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