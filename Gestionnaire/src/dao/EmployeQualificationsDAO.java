package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import main.DBConnection;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.EmployeQualifications;
import dao.daoPOJO.Qualification;

/**
 * DAO pour les objets {@link EmployeQualifications}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class EmployeQualificationsDAO extends DAO<EmployeQualifications> {

	/**
	 * Construit un EmployeQualificationsDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public EmployeQualificationsDAO(Connection conn) {
		super(conn);
	}

	/**
	 * Méthode de création d'une rangée dans la table
	 * {@link EmployeQualifications} de la base de données.
	 * 
	 * @param empQualif
	 *            {@link EmployeQualifications} qui sera inséré dans la base de
	 *            donnée
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(EmployeQualifications empQualif)
			throws DAOCreateException {
		String query = "INSERT INTO employeQualifications (empQualif_emp,"
				+ " empQualif_qualif, empQualif_dateQualif) VALUES (?, ?, ?)";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
			pState = connection.prepareStatement(query);
			pState.setInt(1, empQualif.getEmployeeId());
			pState.setInt(2, empQualif.getId());
			pState.setString(3,
					sdf.format(empQualif.getDateQualification().getTime()));
			if(pState.executeUpdate() == 0){
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode pour supprimer la rangée associée à l'objet
	 * {@link EmployeQualifications} de la base de données.
	 * 
	 * @param empQualif
	 *            {@link EmployeQualifications} qui sera supprimé dans la base
	 *            de données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(EmployeQualifications empQualif)
			throws DAODeleteException {
		String query = "DELETE FROM employeQualifications WHERE"
				+ " empQualif_emp = ? AND empQualif_qualif = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, empQualif.getEmployeeId());
			pState.setInt(2, empQualif.getId());
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
	 * {@link EmployeQualifications} de la base de données.
	 * 
	 * @param empQualif
	 *            {@link EmployeQualifications} qui sera mis à jour dans la base
	 *            de données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean update(EmployeQualifications empQualif)
			throws DAOUpdateException {
		String query = "UPDATE employeQualifications"
				+ " SET empQualif_dateQualif = ?, empQualif_dateExp = ?"
				+ " WHERE empQualif_emp = ? AND empQualif_qualif = ?";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
			pState = connection.prepareStatement(query);
			pState.setString(2,
					sdf.format(empQualif.getDateQualification().getTime()));
			pState.setString(1, "1900-01-01");
			pState.setInt(3, empQualif.getEmployeeId());
			pState.setInt(4, empQualif.getId());
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
	 * Méthode de recherche de l'objet {@link EmployeQualifications} en fonction
	 * de son id. Dans le cas où il y aurait une erreur, une exception de type
	 * {@link DAOFindException} est lancée.
	 * <p>
	 * ATTENTION : La table EmployeQualifications ne possède pas de clé
	 * surrogate key, en utilisant cette méthode vous ne retrouvez pas toutes
	 * les possibilités. Vous obtenez la premier rangée qui correspond à l'id de
	 * l'employé passé en paramètre. Il est préférable d'utiliser la recherche
	 * par employe et qualification.
	 * 
	 * @param id
	 *            Id de l'objet {@link EmployeQualifications} recherché
	 * @return employe Retourne l'objet {@link EmployeQualifications} recherché
	 *         s'il existe, dans le cas contraire la méthode retourne null. Seul
	 *         le premier sera renvoyé.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public EmployeQualifications find(int id) throws DAOFindException {
		String query = "SELECT empQualif_emp, empQualif_qualif,"
				+ " empQualif_dateQualif, empQualif_dateExp"
				+ " FROM employeQualifications WHERE empQualif_emp = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				Calendar dateQualif = Calendar.getInstance();
				Calendar dateExp = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
				dateQualif.setTime(sdf.parse(result
						.getString("empQualif_dateQualif")));
				dateExp.setTime(sdf.parse(result.getString("empQualif_dateExp")));

				EmployeQualifications empQualif = new EmployeQualifications(
						result.getInt("empQualif_qualif"), dateQualif, dateExp);
				empQualif.setId(result.getInt("empQualif_qualif"));
				return empQualif;
			} else {
				return null;
			}
		} catch (SQLException | ParseException e) {
			throw new DAOFindException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode de recherche de l'objet {@link EmployeQualifications} en fonction
	 * de l'employe et de la qualification. Dans le cas où il y aurait une
	 * erreur, une exception de type {@link DAOFindException} est lancée.
	 * 
	 * @param empId
	 *            Id de l'objet {@link Employe} recherché
	 * @param qualifId
	 *            Id de l'objet {@link Qualification} recherchée
	 * @return employe Retourne l'objet {@link EmployeQualifications} recherché
	 *         s'il existe, dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public EmployeQualifications find(int empId, int qualifId)
			throws DAOFindException {
		String query = "SELECT empQualif_dateQualif, empQualif_dateExp,"
				+ " qualif_nom, qualif_acronyme, qualif_priorite"
				+ " FROM employeQualifications, qualification WHERE empQualif_emp = ?"
				+ " AND empQualif_qualif = ? AND empQualif_qualif = qualif_id";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, empId);
			pState.setInt(2, qualifId);
			result = pState.executeQuery();

			if (result.first()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
				Calendar dateQualif = Calendar.getInstance();
				Calendar dateExp = Calendar.getInstance();
				dateQualif.setTime(sdf.parse(result
						.getString("empQualif_dateQualif")));
				dateExp.setTime(sdf.parse(result.getString("empQualif_dateExp")));

				EmployeQualifications empQualif = new EmployeQualifications(
						empId, dateQualif, dateExp);
				empQualif.setId(qualifId);
				empQualif.setAcronyme(result.getString("qualif_acronyme"));
				empQualif.setName(result.getString("qualif_nom"));
				empQualif.setPriority(result.getInt("qualif_priorite"));
				return empQualif;
			} else {
				return null;
			}
		} catch (SQLException | ParseException e) {
			throw new DAOFindException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode pour obtenir tous les objets {@link EmployeQualifications} qui
	 * correspondent à chacunes des rangées de la table. Dans le cas où il y
	 * aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lancée.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link EmployeQualifications} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<EmployeQualifications> getAll() throws DAOGetAllException {
		String query = "SELECT empQualif_emp, empQualif_qualif,"
				+ " empQualif_dateQualif, empQualif_dateExp"
				+ " FROM employeQualifications";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<EmployeQualifications> al = new ArrayList<EmployeQualifications>();

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
				Calendar dateQualif = Calendar.getInstance();
				Calendar dateExp = Calendar.getInstance();
				dateQualif.setTime(sdf.parse(result
						.getString("empQualif_dateQualif")));
				dateExp.setTime(sdf.parse(result.getString("empQualif_dateExp")));

				EmployeQualifications empQualif = new EmployeQualifications(
						result.getInt("empQualif_emp"), dateQualif, dateExp);
				empQualif.setId(result.getInt("empQualif_qualif"));

				al.add(empQualif);
			}
			return al;
		} catch (SQLException | ParseException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}
}