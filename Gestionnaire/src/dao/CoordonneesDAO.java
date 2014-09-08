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
import dao.daoPOJO.Coordonnees;
import dao.daoPOJO.Employe;
import utilitiesBound.StringUtils;

/**
 * DAO pour les objets {@link Coordonnees}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class CoordonneesDAO extends DAO<Coordonnees> {

	/**
	 * Construit un CoordonneesDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public CoordonneesDAO(Connection conn) {
		super(conn);
	}

	/**
	 * Méthode de création d'une rangée dans la table {@link Coordonnees} de la
	 * base de données.
	 * 
	 * @param coord
	 *            Objet qui sera inséré dans la base de donnée
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(Coordonnees coord) throws DAOCreateException {
		String query = "INSERT into coordonnees (coord_id, coord_courriel,"
				+ " coord_numCivic, coord_rue, coord_apt, coord_ville,"
				+ " coord_codePostal, coord_tel1, coord_tel1Ext, coord_tel2,"
				+ " coord_tel2Ext) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, coord.getNoEmploye());
			pState.setString(2, coord.getEmail());
			pState.setInt(3, coord.getNoCivic());
			pState.setString(4, coord.getStreetName());
			pState.setString(5, coord.getNoApt());
			pState.setString(6, coord.getCity());
			pState.setString(7, coord.getZipCode());
			pState.setString(8, coord.getPhone1());
			pState.setString(9, coord.getExt1());
			pState.setString(10, coord.getPhone2());
			pState.setString(11, coord.getExt2());
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode pour supprimer la rangée associée à l'objet {@link Coordonnees}
	 * de la base de données.
	 * 
	 * @param coord
	 *            {@link Coordonnees} qui sera supprimé dans la base de données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(Coordonnees coord) throws DAODeleteException {
		String query = "DELETE FROM coordonnees WHERE coord_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, coord.getNoEmploye());
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
	 * {@link Coordonnees} de la base de données.
	 * 
	 * @param coord
	 *            {@link Coordonnees} qui sera mis à jour dans la base de
	 *            données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAOUpdateException
	 */
	@Override
	public boolean update(Coordonnees coord) throws DAOUpdateException {
		String query = "UPDATE coordonnees SET coord_courriel = ?,"
				+ " coord_numCivic = ?, coord_rue = ?, coord_apt = ?,"
				+ " coord_ville = ?, coord_codePostal = ?, coord_tel1 = ?,"
				+ " coord_tel1Ext = ?, coord_tel2 = ?, coord_tel2Ext = ?"
				+ " WHERE coord_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, coord.getEmail());
			pState.setInt(2, coord.getNoCivic());
			pState.setString(3, coord.getStreetName());
			pState.setString(4, coord.getNoApt());
			pState.setString(5, coord.getCity());
			pState.setString(6, coord.getZipCode());
			pState.setString(7, coord.getPhone1());
			pState.setString(8, coord.getExt1());
			pState.setString(9, coord.getPhone2());
			pState.setString(10, coord.getExt2());
			pState.setInt(11, coord.getNoEmploye());
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
	 * @return coord Retourne l'objet {@link Coordonnees} recherché s'il existe,
	 *         dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public Coordonnees find(int id) throws DAOFindException {
		String query = "SELECT coord_courriel, coord_numCivic, coord_rue,"
				+ " coord_apt, coord_ville, coord_codePostal, coord_tel1,"
				+ " coord_tel1Ext, coord_tel2, coord_tel2Ext"
				+ " FROM coordonnees WHERE coord_id = ?";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();

			if (result.first()) {
				Coordonnees coord = new Coordonnees();
				coord.setNoEmploye(id);
				coord.setEmail(result.getString("coord_courriel"));
				coord.setNoCivic(result.getInt("coord_numCivic"));
				coord.setStreetName(result.getString("coord_rue"));
				coord.setNoApt(result.getString("coord_apt"));
				coord.setCity(result.getString("coord_ville"));
				coord.setZipCode(result.getString("coord_codePostal"));
				coord.setPhone1(result.getString("coord_tel1"));
				coord.setExt1(result.getString("coord_tel1Ext"));
				coord.setPhone2(result.getString("coord_tel2"));
				coord.setExt2(result.getString("coord_tel2Ext"));
				return coord;
			} else
				return null;
		} catch (SQLException e) {
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
	 *         {@link Coordonnees} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<Coordonnees> getAll() throws DAOGetAllException {
		String query = "SELECT coord_id, coord_courriel, coord_numCivic,"
				+ " coord_rue, coord_apt, coord_ville, coord_codePostal,"
				+ " coord_tel1, coord_tel1Ext, coord_tel2, coord_tel2Ext"
				+ " FROM coordonnees";
		PreparedStatement pState = null;
		ResultSet result = null;

		try {
			ArrayList<Coordonnees> al = new ArrayList<Coordonnees>();

			pState = connection.prepareStatement(query);
			result = pState.executeQuery();

			while (result.next()) {
				Coordonnees coord = new Coordonnees();
				coord.setNoEmploye(result.getInt("coord_id"));
				coord.setEmail(result.getString("coord_courriel"));
				coord.setNoCivic(result.getInt("coord_numCivic"));
				coord.setStreetName(result.getString("coord_rue"));
				coord.setNoApt(result.getString("coord_apt"));
				coord.setCity(result.getString("coord_ville"));
				coord.setZipCode(result.getString("coord_codePostal"));
				coord.setPhone1(result.getString("coord_tel1"));
				coord.setExt1(result.getString("coord_tel1Ext"));
				coord.setPhone2(result.getString("coord_tel2"));
				coord.setExt2(result.getString("coord_tel2Ext"));

				al.add(coord);
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
	 * Méthode qui vérifie la validité du courriel
	 * 
	 * @param courriel
	 *            String contenant le courriel de l'employé
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isCourrielValid(String courriel) {
		if (courriel == null) {
			return false;
		}
		courriel = StringUtils.TrimFull(courriel);
		if (!courriel.contains("@") || courriel.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Méthode qui vérifie la validité de l'extension du numéro de téléphone.
	 * 
	 * @param ext
	 *            String contenant le courriel de l'employé
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isExtensionValid(String ext) {
		if (ext == null) {
			return false;
		}
		ext = StringUtils.TrimFull(ext);
		if (!ext.matches(StringUtils.REGX_DIGIT) || ext.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Méthode qui vérifie la validité du numéro civic.
	 * 
	 * @param numCivic
	 *            String contenant le numCivic
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isNumCivicValid(String numCivic) {
		if (numCivic == null) {
			return false;
		}
		numCivic = StringUtils.TrimFull(numCivic);
		if (!numCivic.matches(StringUtils.REGX_DIGIT) || numCivic.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Méthode qui vérifie la validité du numéro de téléphone.
	 * 
	 * @param phoneNumber
	 *            String contenant le numéro de téléphone
	 * @return Un boolean qui indique si le champs est valide
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		if (phoneNumber == null) {
			return false;
		}
		phoneNumber = StringUtils.TrimFull(phoneNumber);
		if (!phoneNumber.matches(StringUtils.REGX_DIGIT) || phoneNumber.isEmpty()) {
			return false;
		}
		return true;
	}
}