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
import dao.daoPOJO.TypeRemplacement;

/**
 * DAO pour les objets {@link TypeRemplacement}
 * 
 * @author Julien Bergeron
 * @version 1.0
 * @see DAO
 */
public class TypeRemplacementDAO extends DAO<TypeRemplacement> {

	/**
	 * Construit un TypeRemplacementDAO avec la connexion spécifiée.
	 * 
	 * @param conn
	 *            Connection utilisée pour se connecter à la base de données.
	 */
	public TypeRemplacementDAO(Connection conn) {
		super(conn);
	}

	/**
	 * Méthode de création d'une rangée dans la table {@link TypeRemplacement}
	 * de la base de données.
	 * 
	 * @param typeRemp
	 *            Objet qui sera inséré dans la base de donnée
	 * @return boolean Retourne vrai si la création s'est effectuée avec succès
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lancée.
	 * @throws DAOCreateException
	 *             Lance une exception de type {@link DAOCreateException}
	 */
	@Override
	public boolean create(TypeRemplacement typeRemp) throws DAOCreateException{
		String query = "INSERT INTO typeRemplacement (typeRemp_txt) VALUES (?)";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, typeRemp.getName());
			pState.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw new DAOCreateException(e);
		} finally {
			DBConnection.closePreparedStatement(pState);
		}
	}

	/**
	 * Méthode pour supprimer la rangée associée à l'objet
	 * {@link TypeRemplacement} de la base de données.
	 * 
	 * @param typeRemp
	 *            {@link TypeRemplacement} qui sera supprimé dans la base de
	 *            données
	 * @return boolean Retourne vrai si la supression s'est effectuée avec
	 *         succès dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lancée.
	 * @throws DAODeleteException
	 *             Lance une exception de type DAODeleteException
	 */
	@Override
	public boolean delete(TypeRemplacement typeRemp) throws DAODeleteException{
		String query = "DELETE FROM typeRemplacement WHERE typeRemp_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, typeRemp.getId());
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
	 * {@link TypeRemplacement} de la base de données.
	 * 
	 * @param typeRemp
	 *            {@link TypeRemplacement} qui sera mis à jour dans la base de
	 *            données
	 * @return boolean Retourne vrai si la mise à jour s'est effectuée avec
	 *         succès et faux si aucune rangée n'a été mise à jour.
	 * @throws DAOUpdateException
	 *             Lance une exception de type DAOUpdateException
	 */
	@Override
	public boolean update(TypeRemplacement typeRemp) throws DAOUpdateException{
		String query = "UPDATE typeRemplacement"
				+ " SET typeRemp_txt = ? WHERE typeRemp_id = ?";
		PreparedStatement pState = null;

		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, typeRemp.getName());
			pState.setInt(2, typeRemp.getId());
			if(pState.executeUpdate() == 0){
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
	 * @return typeRemp Retourne l'objet {@link TypeRemplacement} recherché s'il
	 *         existe, dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	@Override
	public TypeRemplacement find(int id) throws DAOFindException{
		String query = "SELECT typeRemp_txt FROM typeRemplacement"
				+ " WHERE typeRemp_id = ?";
		PreparedStatement pState = null;
		ResultSet result = null;
		
		try {
			pState = connection.prepareStatement(query);
			pState.setInt(1, id);
			result = pState.executeQuery();
			
			if (result.first()) {
				TypeRemplacement typeRemp = new TypeRemplacement();
				typeRemp.setId(id);
				typeRemp.setName(result.getString("typeRemp_txt"));
				return typeRemp;
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
	 * Méthode de recherche de l'objet en fonction de son nom. Dans le cas où il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lancée.
	 * 
	 * @param nom
	 *            nom du type de remplacement recherché
	 * @return typeRemp Retourne l'objet {@link TypeRemplacement} recherché s'il
	 *         existe, dans le cas contraire la méthode retourne null.
	 * @throws DAOFindException
	 *             Lance une exception de type DAOFindException
	 */
	public TypeRemplacement find(String nom) throws DAOFindException{
		String query = "SELECT typeRemp_id, typeRemp_txt FROM typeRemplacement"
				+ " WHERE typeRemp_txt = ?";
		PreparedStatement pState = null;
		ResultSet result = null;
		
		try {
			pState = connection.prepareStatement(query);
			pState.setString(1, nom);
			result = pState.executeQuery();
			
			if (result.first()) {
				TypeRemplacement typeRemp = new TypeRemplacement();
				typeRemp.setId(result.getInt("typeRemp_id"));
				typeRemp.setName(result.getString("typeRemp_txt"));
				return typeRemp;
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
	 * Méthode pour obtenir tous les objets {@link TypeRemplacement} qui
	 * correspondent à chacunes des rangées de la table. Dans le cas où il y
	 * aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lancée.
	 * 
	 * @return ArrayList Retourne une ArrayList contenant tous les objets
	 *         {@link TypeRemplacement} de la table.
	 * @throws DAOGetAllException
	 *             Lance une exception de type DAOGetAllException
	 */
	@Override
	public ArrayList<TypeRemplacement> getAll() throws DAOGetAllException{
		String query = "SELECT typeRemp_id, typeRemp_txt FROM typeRemplacement";
		PreparedStatement pState = null;
		ResultSet result = null;
		
		try {
			ArrayList<TypeRemplacement> al = new ArrayList<TypeRemplacement>();
			pState = connection.prepareStatement(query);
			result = pState.executeQuery();
			
			while (result.next()) {
				TypeRemplacement typeRemp = new TypeRemplacement();
				typeRemp.setId(result.getInt("typeRemp_id"));
				typeRemp.setName(result.getString("typeRemp_txt"));

				al.add(typeRemp);
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