package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import main.DBConnection;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Remplacement;
import dao.daoPOJO.Statut;

/**
 * DAO pour les objets {@link Remplacement}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class RemplacementDAO extends DAO<Remplacement> {

	/**
	 * Construit un RemplacementDAO avec la connexion sp�cifi�e.
	 * 
	 * @param conn
	 *            Connection utilis�e pour se connecter � la base de donn�es.
	 */
	public RemplacementDAO(Connection conn) {
		super(conn);
	}

	/**
	 * M�thode de cr�ation d'une rang�e dans la table {@link Remplacement} de la
	 * base de donn�es.
	 * 
	 * @param remp
	 *            {@link Remplacement} qui sera ins�r� dans la base de donn�e
	 * @return boolean Retourne vrai si la cr�ation s'est effectu�e avec succ�s
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lanc�e.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(Remplacement remp) throws DAOCreateException {
		String query = "INSERT INTO Remplacement (remp_empDemandeur,"
				+ " remp_empPreneur, remp_tempsDebut, remp_tempsFin, remp_type,"
				+ " remp_raison, remp_details, remp_approuve) VALUES"
				+ " (?, ?, ?, ?, (SELECT typeRemp_id FROM typeRemplacement"
				+ " WHERE typeRemp_txt = ?), (SELECT raisonRemp_id FROM"
				+ " raisonRemplacement WHERE raisonRemp_txt = ?), ?, ?)";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-00");
			pState = connection.prepareStatement(query);
			pState.setInt(1, remp.getRequester());

			if (remp.getTaker() == 0) {
				pState.setNull(2, remp.getTaker());
			} else {
				pState.setInt(2, remp.getTaker());
			}
			pState.setString(3, sdf.format(remp.getBeginDate().getTime()));
			pState.setString(4, sdf.format(remp.getEndDate().getTime()));
			pState.setString(5, remp.getType());
			pState.setString(6, remp.getReason());
			pState.setString(7, remp.getDetails());
			pState.setInt(8, (remp.isApproved() ? 1 : 0));
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * M�thode pour supprimer la rang�e associ�e � l'objet {@link Remplacement}
	 * de la base de donn�es.
	 * 
	 * @param remp
	 *            {@link Remplacement} qui sera supprim� dans la base de donn�es
	 * @return boolean Retourne vrai si la supression s'est effectu�e avec
	 *         succ�s dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lanc�e.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(Remplacement remp) throws DAODeleteException {
		String query = "DELETE FROM remplacement WHERE remp_empDemandeur = ?"
				+ " AND remp_tempsDebut = ?";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-00");
			pState = connection.prepareStatement(query);
			pState.setInt(1, remp.getRequester());
			pState.setString(2, sdf.format(remp.getBeginDate().getTime()));
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
	 * {@link Remplacement} de la base de donn�es.
	 * 
	 * @param remp
	 *            {@link Remplacement} qui sera mis � jour dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la mise � jour s'est effectu�e avec
	 *         succ�s et faux si aucune rang�e n'a �t� mise � jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean update(Remplacement remp) throws DAOUpdateException {
		String query = "UPDATE Remplacement"
				+ " SET remp_empPreneur = ?, remp_tempsFin = ?, remp_type ="
				+ " (SELECT typeRemp_id FROM typeRemplacement"
				+ " WHERE typeRemp_txt = ?),"
				+ " remp_raison = (SELECT raisonRemp_id FROM raisonRemplacement"
				+ " WHERE raisonRemp_txt = ?), remp_details = ?,"
				+ " remp_approuve = ? WHERE remp_empDemandeur = ?"
				+ " AND remp_tempsDebut = ?";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			pState = connection.prepareStatement(query);
			if (remp.getTaker() == 0) {
				pState.setNull(1, remp.getTaker());
			} else {
				pState.setInt(1, remp.getTaker());
			}
			pState.setString(2, sdf.format(remp.getEndDate().getTime()));
			pState.setString(3, remp.getType());
			pState.setString(4, remp.getReason());
			pState.setString(5, remp.getDetails());
			pState.setInt(6, (remp.isApproved() ? 1 : 0));
			pState.setInt(7, remp.getRequester());
			pState.setString(8, sdf.format(remp.getBeginDate().getTime()));
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
	 * M�thode pour la mise � jour de la rang�e associ�e � l'objet
	 * {@link Remplacement} de la base de donn�es. On utilise cette m�thode
	 * lorsque la cl� primaire du remplacement est modifi�e (dateD�but et
	 * demandeur).
	 * 
	 * @param rempInit
	 *            {@link Remplacement} remplacement initial.
	 * @param rempNew
	 *            {@link Remplacement} remplacement final.
	 * @return boolean Retourne vrai si la mise � jour s'est effectu�e avec
	 *         succ�s et faux si aucune rang�e n'a �t� mise � jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	public boolean updateFull(Remplacement rempInit, Remplacement rempNew)
			throws DAOUpdateException {
		String query = "UPDATE Remplacement"
				+ " SET remp_empDemandeur = ?, remp_empPreneur = ?,"
				+ " remp_tempsDebut = ?, remp_tempsFin = ?, remp_type ="
				+ " (SELECT typeRemp_id FROM typeRemplacement"
				+ " WHERE typeRemp_txt = ?), remp_raison ="
				+ " (SELECT raisonRemp_id FROM raisonRemplacement"
				+ " WHERE raisonRemp_txt = ?),"
				+ " remp_details = ?, remp_approuve = ?"
				+ " WHERE remp_empDemandeur = ? AND remp_tempsDebut = ?";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			pState = connection.prepareStatement(query);
			pState.setInt(1, rempNew.getRequester());
			if (rempNew.getTaker() == 0) {
				pState.setNull(2, rempNew.getTaker());
			} else {
				pState.setInt(2, rempNew.getTaker());
			}
			pState.setString(3, sdf.format(rempNew.getBeginDate().getTime()));
			pState.setString(4, sdf.format(rempNew.getEndDate().getTime()));
			pState.setString(5, rempNew.getType());
			pState.setString(6, rempNew.getReason());
			pState.setString(7, rempNew.getDetails());
			pState.setInt(8, (rempNew.isApproved() ? 1 : 0));
			pState.setInt(9, rempInit.getRequester());
			pState.setString(10, sdf.format(rempInit.getBeginDate().getTime()));
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
	 * M�thode de recherche de l'objet {@link Remplacement} en fonction de son
	 * id. Dans le cas o� il y aurait une erreur, une exception de type
	 * {@link DAOFindException} est lanc�e.
	 * <p>
	 * ATTENTION : La table remplacement ne poss�de pas de cl� surrogate key, en
	 * utilisant cette m�thode vous ne retrouvez pas toutes les possibilit�s.
	 * Vous obtenez la premier rang�e qui correspond � l'id de l'employ�
	 * demandeur pass� en param�tre. Il est pr�f�rable d'utiliser la recherche
	 * par employe et date de d�but.
	 * 
	 * @param id
	 *            Id du demandeur de l'objet {@link Remplacement} recherch�
	 * @return remp Retourne l'objet {@link Remplacement} recherch� s'il existe,
	 *         dans le cas contraire la m�thode retourne null. Seul le premier
	 *         sera renvoy�.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public Remplacement find(int id) throws DAOFindException {
		String query = "SELECT remp_empDemandeur, remp_empPreneur,"
				+ " remp_tempsDebut, remp_tempsFin, typeRemp_txt,"
				+ " raisonRemp_txt, remp_details, remp_approuve, session_nom)"
				+ " FROM remplacement, typeRemplacement, raisonRemplacement,"
				+ " session WHERE remp_empDemandeur = ? AND"
				+ " remp_type = typeRemp_id AND remp_Raison = raisonRemp_id"
				+ " AND remp_session = session_id";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:00");
				Calendar dateDebut = Calendar.getInstance();
				Calendar dateFin = Calendar.getInstance();
				dateDebut
						.setTime(sdf.parse(result.getString("remp_tempsDebut")));
				dateFin.setTime(sdf.parse(result.getString("remp_tempsFin")));

				Remplacement remp = new Remplacement();
				remp.setRequester(result.getInt("remp_empDemandeur"));
				remp.setTaker(result.getInt("remp_empPreneur"));
				remp.setBeginDate(dateDebut);
				remp.setEndDate(dateFin);
				remp.setType(result.getString("typeRemp_txt"));
				remp.setReason(result.getString("raisonRemp_txt"));
				remp.setDetails(result.getString("remp_details"));
				remp.setApproved(result.getBoolean("remp_approuve"));
				remp.setSession(result.getString("session_nom"));
				return remp;
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
	 * M�thode pour obtenir tous les objets {@link Remplacement} qui
	 * correspondent � chacunes des rang�es de la table. Dans le cas o� il y
	 * aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lanc�e.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Remplacement} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<Remplacement> getAll() throws DAOGetAllException {
		String query = "SELECT remp_empDemandeur, remp_empPreneur,"
				+ " remp_tempsDebut, remp_tempsFin, typeRemp_txt,"
				+ " raisonRemp_txt, remp_details, remp_approuve, session_nom"
				+ " FROM remplacement, typeRemplacement,"
				+ " raisonRemplacement, session WHERE remp_type = typeRemp_id"
				+ " AND remp_Raison = raisonRemp_id AND"
				+ " remp_session = session_id";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Remplacement> al = new ArrayList<Remplacement>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateDebut = Calendar.getInstance();
				Calendar dateFin = Calendar.getInstance();
				dateDebut
						.setTime(sdf.parse(result.getString("remp_tempsDebut")));
				dateFin.setTime(sdf.parse(result.getString("remp_tempsFin")));

				Remplacement remp = new Remplacement();
				remp = new Remplacement();
				remp.setRequester(result.getInt("remp_empDemandeur"));
				remp.setTaker(result.getInt("remp_empPreneur"));
				remp.setBeginDate(dateDebut);
				remp.setEndDate(dateFin);
				remp.setType(result.getString("typeRemp_txt"));
				remp.setReason(result.getString("raisonRemp_txt"));
				remp.setDetails(result.getString("remp_details"));
				remp.setApproved(result.getBoolean("remp_approuve"));
				remp.setSession(result.getString("session_nom"));

				al.add(remp);
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
	 * M�thode pour obtenir tous les objets {@link Remplacement} qui ont comme
	 * demandeur l'id pass� en param�tre. Dans le cas o� il y aurait une erreur,
	 * une exception de type {@link DAOGetAllException} est lanc�e. Les
	 * {@link Remplacement} sont ordonn�s selon leur temps de d�but.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Remplacement} de l'employ� demandeur.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 * @param demandeurId
	 *            Le num�ro d'employ� (Id) de l'employ�.
	 */
	public ArrayList<Remplacement> getAll(int demandeurId)
			throws DAOGetAllException {
		String query = "SELECT remp_empDemandeur, remp_empPreneur,"
				+ " remp_tempsDebut, remp_tempsFin, typeRemp_txt,"
				+ " raisonRemp_txt, remp_details, remp_approuve, session_nom"
				+ " FROM remplacement, typeRemplacement, raisonRemplacement,"
				+ " session WHERE remp_empDemandeur = ?"
				+ "AND remp_type = typeRemp_id AND remp_Raison = raisonRemp_id"
				+ " AND remp_session = session_id ORDER BY remp_tempsDebut";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Remplacement> al = new ArrayList<Remplacement>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

			pState = connection.prepareStatement(query);
			pState.setInt(1, demandeurId);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateDebut = Calendar.getInstance();
				Calendar dateFin = Calendar.getInstance();
				dateDebut
						.setTime(sdf.parse(result.getString("remp_tempsDebut")));
				dateFin.setTime(sdf.parse(result.getString("remp_tempsFin")));

				Remplacement remp = new Remplacement();
				remp = new Remplacement();
				remp.setRequester(result.getInt("remp_empDemandeur"));
				remp.setTaker(result.getInt("remp_empPreneur"));
				remp.setBeginDate(dateDebut);
				remp.setEndDate(dateFin);
				remp.setType(result.getString("typeRemp_txt"));
				remp.setReason(result.getString("raisonRemp_txt"));
				remp.setDetails(result.getString("remp_details"));
				remp.setApproved(result.getBoolean("remp_approuve"));
				remp.setSession(result.getString("session_nom"));

				al.add(remp);
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
	 * M�thode pour obtenir tous les objets {@link Remplacement} qui ont comme
	 * demandeur l'id de l'employ� pass� en param�tre et selon les filtres
	 * s�lectionn�s. Dans le cas o� il y aurait une erreur, une exception de
	 * type {@link DAOGetAllException} est lanc�e.
	 * 
	 * @param demandeurId
	 *            Id de {@link Employe}.
	 * @param filtresAnnees
	 *            ArrayList de toutes les ann�es recherch�es. Si l'array est
	 *            vide aucun filtre sur les ann�es ne sera appliqu�.
	 * @param filtresSession
	 *            ArrayList de toutes les sessions recherch�es. Si l'array est
	 *            vide aucun filtre sur les session ne sera appliqu�.
	 * @return ArrayList Retourne une ArrayList filtr�es contenant tous les
	 *         objets {@link Remplacement} voulus.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<Remplacement> getAll(int demandeurId,
			ArrayList<String> filtresAnnees, ArrayList<String> filtresSession)
			throws DAOGetAllException {

		String strFiltresAnnee = DAO.formatCondition(
				"AND EXTRACT(year FROM remp_tempsDebut) IN (", filtresAnnees);
		String strFiltresSession = DAO.formatCondition("AND session_nom IN (",
				filtresSession);

		String query = "SELECT remp_empDemandeur, remp_empPreneur, remp_tempsDebut, remp_tempsFin,"
				+ " typeRemp_txt, raisonRemp_txt, remp_details, remp_approuve, session_nom"
				+ " FROM remplacement, typeRemplacement, raisonRemplacement, session WHERE remp_empDemandeur = ?"
				+ " AND remp_type = typeRemp_id AND remp_Raison = raisonRemp_id AND remp_session = session_id"
				+ " "
				+ strFiltresAnnee
				+ " "
				+ strFiltresSession
				+ " ORDER BY remp_tempsDebut DESC";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Remplacement> al = new ArrayList<Remplacement>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

			pState = connection.prepareStatement(query);
			pState.setInt(1, demandeurId);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateDebut = Calendar.getInstance();
				Calendar dateFin = Calendar.getInstance();
				dateDebut
						.setTime(sdf.parse(result.getString("remp_tempsDebut")));
				dateFin.setTime(sdf.parse(result.getString("remp_tempsFin")));

				Remplacement remp = new Remplacement();
				remp = new Remplacement();
				remp.setRequester(result.getInt("remp_empDemandeur"));
				remp.setTaker(result.getInt("remp_empPreneur"));
				remp.setBeginDate(dateDebut);
				remp.setEndDate(dateFin);
				remp.setType(result.getString("typeRemp_txt"));
				remp.setReason(result.getString("raisonRemp_txt"));
				remp.setDetails(result.getString("remp_details"));
				remp.setApproved(result.getBoolean("remp_approuve"));
				remp.setSession(result.getString("session_nom"));

				al.add(remp);
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
	 * M�thode qui retourne le nombre de remplacements pour chaque session dans
	 * une map.
	 * 
	 * @return LinkedHashMap Retourne une map avec les sessions/nb de
	 *         remplacements
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<String, Integer> getRempParSession() throws DAOGetAllException {

		String query = "SELECT session_nom,"
				+ " EXTRACT(year from session_debut) as year, count(*) as occ"
				+ " FROM V_remp_parJour remplacement JOIN session"
				+ " ON (remplacement.remp_session = session.session_id)"
				+ " GROUP BY remplacement.remp_session, session.session_id"
				+ " ORDER BY session_debut DESC";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			Map<String, Integer> map = new LinkedHashMap<String, Integer>();

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				String session = result.getString("session_nom");
				session += " " + result.getInt("year");
				map.put(session, result.getInt("occ"));
			}
			return map;
		} catch (SQLException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * M�thode qui retourne le nombre de remplacements pour chaque session pour
	 * chaque employ�. Dans la map chaque list correspond au nombre de
	 * remplacements pour chaque session class�es en ordre chronologique
	 * 
	 * @return LinkedHashMap Retourne une map avec les sessions/nb de
	 *         remplacements
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<Integer, List<Integer>> getRempSessionEmp(boolean desc)
			throws DAOGetAllException {
		String order = "";
		if(desc){
			order = " DESC ";
		}

		String concatSession =" SELECT GROUP_CONCAT(DISTINCT CONCAT(' count(case when session_id =''',"
				+ " session_id, ''' THEN 1 END) ', session_nom,"
				+ " EXTRACT(year from session_debut)) order by session_debut)"
				+ " From session"
				+ " ORDER BY session_debut " + order + ";";
		/*String query = "SET @sql = NULL;"
				+ " SELECT GROUP_CONCAT(DISTINCT CONCAT(' count(case when session_id =''',"
				+ " session_id, ''' THEN 1 END) ', session_nom,"
				+ " EXTRACT(year from session_debut)) order by session_debut) into  @sql"
				+ " From session"
				+ " ORDER BY session_debut DESC;"
				+ " SET @sql = CONCAT('select emp_id, ', @sql, '"
				+ " from remplacement remp left join employe emp"
				+ " on remp.remp_empDemandeur = emp.emp_id left join session sess"
				+ " on remp.remp_session = sess.session_id group by emp.emp_id');"
				+ " PREPARE stmt1 FROM @sql; EXECUTE stmt1; DEALLOCATE PREPARE stmt1;";*/

		PreparedStatement pState = null;
		ResultSet result = null;
		ResultSetMetaData metadata = null;

		try {
			Map<Integer, List<Integer>> map = new LinkedHashMap<Integer, List<Integer>>();

			pState = connection.prepareStatement(concatSession);
			result = pState.executeQuery();
			String strSession = "";
			if(result.first()){
				strSession = result.getString(1);
			}
			
			String query =  " SELECT emp_id, " + strSession
					+ " from V_remp_parJour remp left join employe emp"
					+ " on remp.remp_empDemandeur = emp.emp_id left join session sess"
					+ " on remp.remp_session = sess.session_id group by emp.emp_id;";
			
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();
			metadata = result.getMetaData();

			while (result.next()) {
				ArrayList<Integer> countList = new ArrayList<Integer>();
				int empId = result.getInt("emp_id");

				// On ignore la column 0 pcq cest l<id
				for (int i = 2; i <= metadata.getColumnCount(); i++) {
					countList.add(result.getInt(i));
				}

				map.put(empId, countList);
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/*
	 * SET @sql = NULL; SELECT GROUP_CONCAT(DISTINCT CONCAT(' count(case when
	 * session_id =''', session_id, ''' THEN 1 END) ', session_nom, EXTRACT(year
	 * from session_debut)) order by session_debut ) into @sql From session
	 * ORDER BY session_debut DESC;
	 * 
	 * SET @sql = CONCAT('select emp.emp_prenom, emp.emp_nom, ', @sql, ' from
	 * remplacement remp left join employe emp on remp.remp_empDemandeur =
	 * emp.emp_id left join session sess on remp.remp_session = sess.session_id
	 * group by emp.emp_id');
	 * 
	 * PREPARE stmt1 FROM @sql; EXECUTE stmt1; DEALLOCATE PREPARE stmt1;
	 */

	/**
	 * M�thode qui retourne une LinkedHashMap qui contient les employ�s qui ont
	 * pris des remplacements et le nombre de fois que cela est arriv�.
	 * 
	 * @return LinkedHashMap Retourne une map avec les employ�s/nb de
	 *         remplacements
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<Employe, Integer> getPreneursCount() throws DAOGetAllException {

		String query = "SELECT emp_id, emp_nom, emp_prenom, emp_sexe,"
				+ " emp_dateEmbauche, emp_dateNaissance, emp_numSDeS,"
				+ " statut_txt, count(*) as occ FROM employe JOIN"
				+ " V_remp_parJour remplacement"
				+ " ON (remplacement.remp_empPreneur = employe.emp_id)"
				+ " JOIN statut ON (employe.emp_statut = statut.statut_id)"
				+ " GROUP BY employe.emp_id" + " ORDER BY occ DESC";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			Map<Employe, Integer> map = new LinkedHashMap<Employe, Integer>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
			Calendar dateEmbauche = Calendar.getInstance();
			Calendar dateNaissance = Calendar.getInstance();

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				dateEmbauche.setTime(sdf.parse(result
						.getString("emp_dateEmbauche")));
				dateNaissance.setTime(sdf.parse(result
						.getString("emp_dateNaissance")));

				Employe employe = new Employe(result.getInt("emp_id"),
						result.getString("emp_nom"),
						result.getString("emp_prenom"),
						result.getString("emp_sexe"), dateEmbauche,
						dateNaissance, result.getString("emp_numSDeS"));
				employe.setStatus(new Statut(result.getString("statut_txt")));

				map.put(employe, result.getInt("occ"));
			}
			return map;
		} catch (SQLException | ParseException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}
}
