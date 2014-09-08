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
	 * Construit un TypeRemplacementDAO avec la connexion sp�cifi�e.
	 * 
	 * @param conn
	 *            Connection utilis�e pour se connecter � la base de donn�es.
	 */
	public TypeRemplacementDAO(Connection conn) {
		super(conn);
	}

	/**
	 * M�thode de cr�ation d'une rang�e dans la table {@link TypeRemplacement}
	 * de la base de donn�es.
	 * 
	 * @param typeRemp
	 *            Objet qui sera ins�r� dans la base de donn�e
	 * @return boolean Retourne vrai si la cr�ation s'est effectu�e avec succ�s
	 *         dans le cas contraire une exception de type
	 *         {@link DAOCreateException} est lanc�e.
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
	 * M�thode pour supprimer la rang�e associ�e � l'objet
	 * {@link TypeRemplacement} de la base de donn�es.
	 * 
	 * @param typeRemp
	 *            {@link TypeRemplacement} qui sera supprim� dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la supression s'est effectu�e avec
	 *         succ�s dans le cas contraire une exception de type
	 *         {@link DAODeleteException} est lanc�e.
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
	 * M�thode pour la mise � jour de la rang�e associ�e � l'objet
	 * {@link TypeRemplacement} de la base de donn�es.
	 * 
	 * @param typeRemp
	 *            {@link TypeRemplacement} qui sera mis � jour dans la base de
	 *            donn�es
	 * @return boolean Retourne vrai si la mise � jour s'est effectu�e avec
	 *         succ�s et faux si aucune rang�e n'a �t� mise � jour.
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
	 * M�thode de recherche de l'objet en fonction de son id. Dans le cas o� il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lanc�e.
	 * 
	 * @param id
	 *            Id de l'objet recherch�
	 * @return typeRemp Retourne l'objet {@link TypeRemplacement} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null.
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
	 * M�thode de recherche de l'objet en fonction de son nom. Dans le cas o� il
	 * y aurait une erreur, une exception de type {@link DAOFindException} est
	 * lanc�e.
	 * 
	 * @param nom
	 *            nom du type de remplacement recherch�
	 * @return typeRemp Retourne l'objet {@link TypeRemplacement} recherch� s'il
	 *         existe, dans le cas contraire la m�thode retourne null.
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
	 * M�thode pour obtenir tous les objets {@link TypeRemplacement} qui
	 * correspondent � chacunes des rang�es de la table. Dans le cas o� il y
	 * aurait une erreur, une exception de type {@link DAOGetAllException} est
	 * lanc�e.
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