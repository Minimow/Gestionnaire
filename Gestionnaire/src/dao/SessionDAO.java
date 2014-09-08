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
import dao.daoPOJO.Session;

/**
 * DAO pour les objets {@link Session}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class SessionDAO extends DAO<Session> {

	/**
	 * Construit un SessionDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public SessionDAO(Connection conn) {
		super(conn);
	}

	/**
	 * Méthode de création d'une rangée dans la table {@link Session} de la base
	 * de données.
	 * 
	 * @param session
	 *            Objet qui sera inséré dans la base de donnée
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(Session session) throws DAOCreateException {
		String query = "INSERT INTO session (session_nom, session_debut,"
				+ " session_fin) VALUES (?, ?, ?)";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pState = connection.prepareStatement(query);
			pState.setString(1, session.getName());
			pState.setString(2, sdf.format(session.getBeginDate().getTime()));
			pState.setString(3, sdf.format(session.getEndDate().getTime()));
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode pour supprimer la rangée associée à l'objet {@link Session} de la
	 * base de données.
	 * 
	 * @param session
	 *            {@link Session} qui sera supprimé dans la base de données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(Session session) throws DAODeleteException {
		String query = "DELETE FROM session WHERE session_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, session.getId());
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
	 * {@link Session} de la base de données.
	 * 
	 * @param session
	 *            {@link Session} qui sera mis à jour dans la base de données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAOUpdateException
	 */
	@Override
	public boolean update(Session session) throws DAOUpdateException {
		String query = "UPDATE session"
				+ " SET session_nom = ?, session_debut = ?, session_fin = ?"
				+ " WHERE session_id = ?";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pState = connection.prepareStatement(query);
			pState.setString(1, session.getName());
			pState.setString(2, sdf.format(session.getBeginDate().getTime()));
			pState.setString(3, sdf.format(session.getEndDate().getTime()));
			pState.setInt(4, session.getId());
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
	 * @return session Retourne l'objet {@link Session} recherché s'il existe,
	 *         dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public Session find(int id) throws DAOFindException {
		String query = "SELECT session_nom, session_debut, session_fin"
				+ " FROM session WHERE session_id = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);

			result = pState.executeQuery();
			if (result.first()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar dateDebut = Calendar.getInstance(), dateFin = Calendar
						.getInstance();
				dateDebut.setTime(sdf.parse(result.getString("session_debut")));
				dateFin.setTime(sdf.parse(result.getString("session_fin")));
				Session session = new Session();
				session.setId(id);
				session.setName(result.getString("session_nom"));
				session.setBeginDate(dateDebut);
				session.setEndDate(dateFin);
				return session;
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
	 * Méthode pour obtenir tous les objets {@link Session} qui correspondent à
	 * chacunes des rangées de la table. Dans le cas où il y aurait une erreur,
	 * une exception de type {@link DAOGetAllException} est lancée.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Session} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<Session> getAll() throws DAOGetAllException {
		String query = "SELECT  session_id, session_nom, session_debut,"
				+ " session_fin FROM session ORDER BY session_debut";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Session> al = new ArrayList<Session>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateDebut = Calendar.getInstance();
				Calendar dateFin = Calendar.getInstance();
				String strDateDebut = result.getString("session_debut");
				if(strDateDebut == null){
					continue;
				}
				dateDebut.setTime(sdf.parse(result.getString("session_debut")));
				dateFin.setTime(sdf.parse(result.getString("session_fin")));
				Session session = new Session();
				session.setId(result.getInt("session_id"));
				session.setName(result.getString("session_nom"));
				session.setBeginDate(dateDebut);
				session.setEndDate(dateFin);

				al.add(session);
			}
			return al;
		} catch (SQLException | ParseException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}
	
	/**
	 * Méthode pour obtenir tous les objets {@link Session} qui correspondent à
	 * chacunes des rangées de la table. Dans le cas où il y aurait une erreur,
	 * une exception de type {@link DAOGetAllException} est lancée.
	 * 
	 * @param desc Vrai si lordre des DESC, faux dans le cas contraire
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Session} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<Session> getAll(boolean desc) throws DAOGetAllException {
		String strDesc = "";
		if(desc){
			strDesc = " DESC ";
		}
		String query = "SELECT  session_id, session_nom, session_debut,"
				+ " session_fin FROM session ORDER BY session_debut" + strDesc;
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Session> al = new ArrayList<Session>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateDebut = Calendar.getInstance();
				Calendar dateFin = Calendar.getInstance();
				String strDateDebut = result.getString("session_debut");
				if(strDateDebut == null){
					continue;
				}
				dateDebut.setTime(sdf.parse(result.getString("session_debut")));
				dateFin.setTime(sdf.parse(result.getString("session_fin")));
				Session session = new Session();
				session.setId(result.getInt("session_id"));
				session.setName(result.getString("session_nom"));
				session.setBeginDate(dateDebut);
				session.setEndDate(dateFin);

				al.add(session);
			}
			return al;
		} catch (SQLException | ParseException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode pour obtenir la liste des années pendant lesquelles il y a au
	 * moins une session existante.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant toutes les années
	 *         actives des {@link Session} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<Integer> getYears() throws DAOGetAllException {
		String query = "SELECT EXTRACT(year from session_debut) as year"
				+ " FROM session WHERE session_debut IS NOT NULL GROUP by year";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Integer> al = new ArrayList<Integer>();
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				al.add(result.getInt("year"));
			}
			return al;
		} catch (SQLException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode pour obtenir la liste des sessions existantes.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les noms des
	 *         {@link Session} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<String> getNoms() throws DAOGetAllException{
		String query = "SELECT session_nom FROM session GROUP by session_nom";
		PreparedStatement pState = null;
		ResultSet result = null;
		
		try {
			ArrayList<String> al = new ArrayList<String>();
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();
			
			while (result.next()) {
				al.add(result.getString("session_nom"));
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