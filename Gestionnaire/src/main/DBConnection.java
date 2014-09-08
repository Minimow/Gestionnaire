package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Singleton qui permet la connexion � la base de donn�es.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class DBConnection {
	/**
	 * Instance unique de la classe. On utilise static et synchronized pour
	 * �viter les probl�me de multithreading.
	 * 
	 */
	private volatile static DBConnection _instance = new DBConnection();

	/**
	 * Connexion utilis�e par la classe pour se connecter � la base de donn�es.
	 * 
	 */
	private static Connection conn;

	/**
	 * Message utilis� pour signifier que la modification de la base de donn�e
	 * s'est effectu�e avec succ�s.
	 * 
	 */
	public static String MESSAGE_SUCCESS = "Op�ration r�ussie!";
	
	/**
	 * Message utilis� pour signifier que la modification de la base de donn�e
	 * ne s'est pas effectu�e avec succ�s.
	 * 
	 */
	public static String MESSAGE_FAIL = "Op�ration �chou�e!";

	/**
	 * Constructeur priv� qui se connecte � la base de donn�e en utilisant les
	 * param�tres d'un utilisateur r�gulier.
	 * 
	 */
	private DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");
			conn = DriverManager
					.getConnection(
							"jdbc:mysql://localhost:3306/gestionnaire?allowMultiQueries=true",
							"julien", "bic12125");
			System.out.println("Connexion O.K.");
		} catch (Exception err) {
			System.out.println("Error: could not connect to the database.");
		}
	}

	/**
	 * M�thode qui permet d'obtenir la connexion de la classe. Dans le cas o�,
	 * elle n'est pas cr��e, une nouvelle sera cr��e.
	 * 
	 * @return conn La connexion ouverte pr�te � �tre utilis�e.
	 */
	public static Connection getInstance() {
		if (_instance == null) {
			synchronized (DBConnection.class) {
				if (_instance == null)
					_instance = new DBConnection();
			}
		}
		return conn;
	}

	/**
	 * M�thode qui permet de fermer une connexion
	 * 
	 * @param connection
	 *            La connexion � fermer.
	 */
	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				/* Do nothing */
			}
		}
	}

	/**
	 * M�thode qui permet de fermer un PreparedStatement
	 * 
	 * @param pState
	 *            Le PreparedStatement � fermer.
	 */
	public static void closePreparedStatement(PreparedStatement pState) {
		if (pState != null) {
			try {
				pState.close();
			} catch (SQLException e) {
				/* Do nothing */
			}
		}
	}

	/**
	 * M�thode qui permet de fermer un ResultSet
	 * 
	 * @param resultSet
	 *            Le ResultSet � fermer.
	 */
	public static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				/* Do nothing */
			}
		}
	}
}