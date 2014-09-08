package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import main.DBConnection;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Statut;
import utilitiesBound.StringUtils;

/**
 * DAO pour les objets {@link Employe}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class EmployeDAO extends DAO<Employe> {
	/**
	 * Liste des possibilités pour le tri des employés lorsqu'il y a plusieurs
	 * employés retournés
	 */
	public static enum EMPLOYE_SORT {
		NOM("emp_nom, emp_prenom", "Nom"), PRENOM("emp_prenom", "Prénom"), NUM_EMPLOYE(
				"emp_id", "Numéro d'employé"), DATE_EMBAUCHE(
				"emp_dateEmbauche", "Date d'embauche");

		private final String dbColumnName;
		private final String showName;

		private EMPLOYE_SORT(String dbColumnName, String showName) {
			this.dbColumnName = dbColumnName;
			this.showName = showName;
		}

		@Override
		public String toString() {
			return showName;
		}

		public String toDBString() {
			return dbColumnName;
		}
	}

	/**
	 * Construit un EmployeDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public EmployeDAO(Connection conn) {
		super(conn);
	}

	/**
	 * Méthode de création d'une rangée dans la table {@link Employe} de la base
	 * de données.
	 * 
	 * @param employe
	 *            {@link Employe} qui sera inséré dans la base de donnée
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(Employe employe) throws DAOCreateException {
		String query = "INSERT INTO employe (emp_id, emp_nom, emp_prenom,"
				+ " emp_sexe, emp_dateEmbauche, emp_classementEmbauche,"
				+ " emp_dateNaissance, emp_numSDeS, emp_statut)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, (SELECT statut_id"
				+ " FROM statut WHERE statut_txt = ?))";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			pState = connection.prepareStatement(query);
			pState.setInt(1, employe.getNoEmploye());
			pState.setString(2, employe.getLastName());
			pState.setString(3, employe.getFirstName());
			pState.setString(4, employe.getSex());
			pState.setString(5, sdf.format(employe.getHiringDate().getTime()));
			pState.setInt(6, employe.getHiringRank());
			pState.setString(7,
					sdf.format(employe.getDateOfBirth().getTime()));
			pState.setString(8, employe.getNoSDeS());
			pState.setString(9, employe.getStatus().getName());
			pState.executeUpdate();
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
		return true;
	}

	/**
	 * Méthode pour supprimer la rangée associée à l'objet {@link Employe} de la
	 * base de données.
	 * 
	 * @param employe
	 *            {@link Employe} qui sera supprimé dans la base de données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(Employe employe) throws DAODeleteException {
		String query = "DELETE FROM employe WHERE emp_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, employe.getNoEmploye());

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
	 * {@link Employe} de la base de données.
	 * 
	 * @param employe
	 *            {@link Employe} qui sera mis à jour dans la base de données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean update(Employe employe) throws DAOUpdateException {
		String query = "UPDATE employe SET emp_nom = ?, emp_prenom = ?,"
				+ " emp_sexe = ?, emp_dateEmbauche = ?, emp_dateNaissance = ?,"
				+ " emp_numSDeS = ?, emp_classementEmbauche = ?, emp_statut ="
				+ " (SELECT statut_id FROM statut WHERE statut_txt = ?)"
				+ " WHERE emp_id = ?";
		PreparedStatement pState = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

			pState = connection.prepareStatement(query);
			pState.setString(1, employe.getLastName());
			pState.setString(2, employe.getFirstName());
			pState.setString(3, employe.getSex());
			pState.setString(4, sdf.format(employe.getHiringDate().getTime()));
			pState.setString(5,
					sdf.format(employe.getDateOfBirth().getTime()));
			pState.setString(6, employe.getNoSDeS());
			pState.setInt(7, employe.getHiringRank());
			pState.setString(8, employe.getStatus().getName());
			pState.setInt(9, employe.getNoEmploye());
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
	 * Méthode de recherche de l'objet {@link Employe} en fonction de son id.
	 * Dans le cas où il y aurait une erreur, une exception de type
	 * {@link DAOFindException} est lancée.
	 * 
	 * @param id
	 *            Id de l'objet {@link Employe} recherché
	 * @return employe Retourne l'objet {@link Employe} recherché s'il existe,
	 *         dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public Employe find(int id) throws DAOFindException {
		String query = "SELECT emp_nom, emp_prenom, emp_sexe, emp_dateEmbauche,"
				+ " emp_dateNaissance, emp_numSDeS, statut_txt, emp_classementEmbauche"
				+ " FROM employe, statut WHERE emp_id = ?"
				+ " AND emp_statut = statut_id";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
			Calendar dateEmbauche = Calendar.getInstance();
			Calendar dateNaissance = Calendar.getInstance();

			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				dateEmbauche.setTime(sdf.parse(result
						.getString("emp_dateEmbauche")));
				dateNaissance.setTime(sdf.parse(result
						.getString("emp_dateNaissance")));

				Employe employe = new Employe(id, result.getString("emp_nom"),
						result.getString("emp_prenom"),
						result.getString("emp_sexe"), dateEmbauche,
						dateNaissance, result.getString("emp_numSDeS"));
				employe.setStatus(new Statut(result.getString("statut_txt")));
				employe.setHiringRank(result.getInt("emp_classementEmbauche"));
				return employe;
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
	 * Méthode pour obtenir tous les objets {@link Employe} qui correspondent à
	 * chacunes des rangées de la table. Dans le cas où il y aurait une erreur,
	 * une exception de type {@link DAOGetAllException} est lancée.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link Employe} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<Employe> getAll() throws DAOGetAllException {
		String query = "SELECT emp_id, emp_nom, emp_prenom, emp_sexe,"
				+ " emp_dateEmbauche, emp_dateNaissance, emp_numSDeS, emp_classementEmbauche,"
				+ " statut_txt FROM employe, statut WHERE emp_statut = statut_id"
				+ " ORDER BY emp_nom";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Employe> al = new ArrayList<Employe>();
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
				employe.setHiringRank(result.getInt("emp_classementEmbauche"));

				al.add(employe);
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
	 * Méthode pour obtenir tous les objets {@link Employe} en ordre de demandes
	 * de remplacement. Dans le cas où il y aurait une erreur, une exception de
	 * type {@link DAOGetAllException} est lancée.
	 * 
	 * @return Map<Employe,Integer> Retourne une Map<Employe,Integer> avec
	 *         chaque employe associé au nombre total de remplacement.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<Employe, Integer> getMostRemp() throws DAOGetAllException {
		String query = "SELECT emp_id, emp_nom, emp_prenom, emp_sexe,"
				+ " emp_dateEmbauche, emp_dateNaissance, emp_numSDeS, emp_classementEmbauche,"
				+ " statut_txt, count(*) as occ"
				+ " FROM employe JOIN remplacement"
				+ " ON (remplacement.remp_empDemandeur = employe.emp_id)"
				+ " JOIN statut ON (employe.emp_statut = statut.statut_id)"
				+ " GROUP BY employe.emp_id" + " ORDER BY occ DESC";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			Map<Employe, Integer> map = new LinkedHashMap<Employe, Integer>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateEmbauche = Calendar.getInstance();
				Calendar dateNaissance = Calendar.getInstance();
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
				employe.setHiringRank(result.getInt("emp_classementEmbauche"));

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

	/**
	 * Méthode pour obtenir tous les objets {@link Employe} en ordre de du plus
	 * grand nombre de remplacement effectués. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lancée.
	 * 
	 * @return Map<Employe,Integer> Retourne une Map<Employe,Integer> avec
	 *         chaque employe associé au nombre total de remplacement.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<Employe, Integer> getMostPreneur() throws DAOGetAllException {
		String query = "SELECT emp_id, emp_nom, emp_prenom, emp_sexe,"
				+ " emp_dateEmbauche, emp_dateNaissance, emp_numSDeS, emp_classementEmbauche,"
				+ " statut_txt, count(*) as occ"
				+ " FROM employe JOIN remplacement"
				+ " ON (remplacement.remp_empPreneur = employe.emp_id)"
				+ " JOIN statut ON (employe.emp_statut = statut.statut_id)"
				+ " GROUP BY employe.emp_id" + " ORDER BY occ DESC";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			Map<Employe, Integer> map = new LinkedHashMap<Employe, Integer>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateEmbauche = Calendar.getInstance();
				Calendar dateNaissance = Calendar.getInstance();
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
				employe.setHiringRank(result.getInt("emp_classementEmbauche"));

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
	
	/**
	 * Méthode pour obtenir le nombre d'employés avec le même âge. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lancée.
	 * 
	 * @return Map<Integer,Integer> Retourne une Map<Integer,Integer> avec
	 *         chaque âge associé au nombre d'employés avec cet âge..
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<Integer, Integer> getTranchesAge() throws DAOGetAllException {
		String query = "SELECT TIMESTAMPDIFF(YEAR, emp_dateNaissance, CURDATE())"
				+ " as age, count(*) as total" +
				" FROM employe" +
				" WHERE emp_statut = 1 OR emp_statut = 2" +
				" GROUP BY age" +
				" ORDER BY age";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				map.put(result.getInt("age"), result.getInt("total"));
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
	 * Méthode pour obtenir le nombre d'employés embauchés à chaque année. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lancée.
	 * 
	 * @return Map<Integer,Integer> Retourne une Map<Integer,Integer> avec
	 *         chaque année associé au nombre d'employés embauchés.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public Map<Integer, Integer> getEmpParAnneeEmbauche() throws DAOGetAllException {
		String query = "SELECT YEAR(emp_dateEmbauche)as annee, count(*) as total"
				+ " FROM employe GROUP BY annee ORDER BY annee";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				map.put(result.getInt("annee"), result.getInt("total"));
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
	 * Méthode pour obtenir le moyenne d'âge des employés. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lancée.
	 * 
	 * @return float Retourne un float qui correspond à la moyenne d'âge.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public float getAgeMoyen() throws DAOGetAllException {
		String query = "SELECT AVG(TIMESTAMPDIFF(YEAR, emp_dateNaissance,"
				+ " CURDATE())) as average FROM employe"
				+ " WHERE emp_statut = 1 OR emp_statut = 2;";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			float avg = 0f;

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				avg = result.getFloat("average");
			}
			return avg;
		} catch (SQLException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}

	/**
	 * Méthode pour obtenir le nombre total d'employés
	 * 
	 * @return int Nombre d'employés total
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public int getCountEmploye() throws DAOGetAllException {
		String query = "SELECT COUNT(*) as total FROM employe";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			if (result.first()) {
				return result.getInt("total");
			}
			return 0;
		} catch (SQLException e) {
			throw new DAOGetAllException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
			DBConnection.closeResultSet(result);
		}
	}
	
	/**
	 * Méthode pour obtenir le nombre d'hommes et de femmes employés.
	 * 
	 * @return ArrayList<Integer> Hommes, femmes, total
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<Integer> getSexRatio() throws DAOGetAllException {
		String query = "SELECT COUNT(case when emp_sexe = 'M' THEN 1 END)"
				+ " hommes, COUNT(case when emp_sexe = 'F' THEN 1 END)"
				+ " femmes, COUNT(1) as total FROM employe";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Integer> al = new ArrayList<Integer>();
			
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			if (result.first()) {
				al.add(result.getInt("hommes"));
				al.add(result.getInt("femmes"));
				al.add(result.getInt("total"));
				return al;
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
	 * Méthode pour obtenir tous les objets {@link Employe} qui correspondent à
	 * chacunes des rangées de la table selon les filtres sélectionnés et
	 * l'ordre demandé. Dans le cas où il y aurait une erreur, une exception de
	 * type {@link DAOGetAllException} est lancée.
	 * 
	 * @param sort
	 *            Type de tri selon {@link EMPLOYE_SORT}. Si le type est null,
	 *            le tri se fera selon les préférences de la base de données.
	 * @param filtresStatut
	 *            ArrayList de tous les statuts recherchés. Si l'array est vide
	 *            aucun filtre sur le statut ne sera appliqué.
	 * @param filtresQualif
	 *            ArrayList de toutes les qualifications recherchées. Si l'array
	 *            est vide aucun filtre sur les qualifications ne sera appliqué.
	 * @param reverseSort
	 *            Indique si on reverse(DESC) le tri.
	 * @param reverseQualif
	 *            Indique si on choisit les qualifs sélectionnées ou on les
	 *            ignore.
	 * @param reverseStatut
	 *            Indique si on choisit les statuts sélectionnés ou on les
	 *            ignore.
	 * @return ArrayList Retourne une ArrayList filtrées et triées contenant
	 *         tous les objets {@link Employe} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	public ArrayList<Employe> getAll(EMPLOYE_SORT sort,
			ArrayList<String> filtresStatut, ArrayList<String> filtresQualif,
			boolean reverseSort, boolean reverseQualif, boolean reverseStatut)
			throws DAOGetAllException {
		String strSort;
		if (sort == null) {
			strSort = "";
		} else {
			strSort = sort.toDBString();
			if (sort == EMPLOYE_SORT.DATE_EMBAUCHE) {
				strSort += ", emp_classementEmbauche";
			}
			if (reverseSort) {
				strSort += " DESC";
			}
		}

		String strFiltresStatut;
		if (reverseStatut) {
			strFiltresStatut = DAO.formatCondition("AND statut_txt NOT IN (",
					filtresStatut);
		} else {
			strFiltresStatut = DAO.formatCondition("AND statut_txt IN (",
					filtresStatut);
		}
		String strFiltresQualif = formatQualif(filtresQualif, reverseQualif);

		String query = "SELECT emp_id, emp_nom, emp_prenom, emp_sexe,"
				+ " emp_dateEmbauche, emp_dateNaissance, emp_numSDeS,"
				+ " emp_classementEmbauche, statut_txt"
				+ " FROM employe, statut WHERE emp_statut = statut_id "
				+ strFiltresStatut + " " + strFiltresQualif + " ORDER BY "
				+ strSort;
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Employe> al = new ArrayList<Employe>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Calendar dateEmbauche = Calendar.getInstance();
				Calendar dateNaissance = Calendar.getInstance();
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
				employe.setHiringRank(result.getInt("emp_classementEmbauche"));

				al.add(employe);
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
	 * Méthode qui vérifie la validité du prénom de l'employé
	 * 
	 * @param firstName
	 *            String contenant le nom à valider.
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isNameValid(String name) {
		if (name == null) {
			return false;
		}
		name = StringUtils.TrimDoubleSpaces(name);
		if (!name.matches(StringUtils.REGX_ALPHA) || name.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Méthode qui vérifie la validité du numéro de l'employé
	 * 
	 * @param numEmploye
	 *            String contenant le numéro d'employé
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isNumEmployeValid(String numEmploye) {
		if (numEmploye == null) {
			return false;
		}
		numEmploye = StringUtils.TrimFull(numEmploye);
		if (!numEmploye.matches(StringUtils.REGX_DIGIT) || numEmploye.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Méthode qui vérifie la validité du numéro de la société de sauvetage de
	 * l'employé
	 * 
	 * @param numSDeS
	 *            String contenant le numéro d'employé
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isNumSDeSValid(String numSDeS) {
		if (numSDeS == null) {
			return false;
		}
		numSDeS = StringUtils.TrimFull(numSDeS);
		if (!numSDeS.matches(StringUtils.REGX_DIGIT) || numSDeS.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Méthode qui vérifie la validité du sexe de l'employé
	 * 
	 * @param sexe
	 *            String contenant le sexe de l'employé
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isSexeValid(String sexe) {
		if (sexe == null) {
			return false;
		}
		if ((!sexe.equals("M") && !sexe.equals("F")) || sexe.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Méthode qui formatte l'array des qualifications demandées en un string
	 * utilisable pour la base de données. L'employé doit posséder minimalement
	 * toutes les qualifications demandées pour être sélectionné.
	 * 
	 * @param filtresQualif
	 *            ArrayList de toutes les qualifications recherchées. Si l'array
	 *            est vide aucun filtre sur le statut ne sera appliqué.
	 * @return String Retourne un String formatté que l'on peut utiliser dans
	 *         une clause WHERE (INCLUANT LE AND)
	 */
	private String formatQualif(ArrayList<String> filtresQualif, boolean reverse) {
		String strIn;
		if (reverse) {
			strIn = " NOT IN ";
		} else {
			strIn = " IN ";
		}
		String strFiltres = "AND emp_id "
				+ strIn
				+ " ( SELECT empQualif_emp FROM employeQualifications WHERE empQualif_qualif IN (";
		int size = 0;
		boolean first = true;
		if (filtresQualif != null) {
			for (int i = 0; i < filtresQualif.size(); i++) {
				if (filtresQualif.get(i).equals("")) {
					continue;
				}

				// Si c'est le premier, on n'ajoute pas de , avant le filtre
				if (first) {
					strFiltres += "'" + filtresQualif.get(i) + "'";
					first = false;
				} else {
					strFiltres += ", '" + filtresQualif.get(i) + "'";
				}
				size++;
			}
			strFiltres += ")";

			// S'assure que l'employe possede exactement toutes les qualifs
			// demande. Si on retourne n resultats et qu'on demandait n qualif
			// on sait que l'employe est valide
			if (size != 0) {
				strFiltres += " GROUP BY empQualif_emp HAVING COUNT(*) = "
						+ size + ")";
			} else {
				strFiltres = "";
			}
		} else {
			strFiltres = "";
		}
		return strFiltres;
	}
}