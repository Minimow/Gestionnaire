package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Singleton qui permet la connexion à la base de données.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class DBConnection {
	/**
	 * Instance unique de la classe. On utilise static et synchronized pour
	 * éviter les problème de multithreading.
	 * 
	 */
	private volatile static DBConnection _instance = new DBConnection();

	/**
	 * Connexion utilisée par la classe pour se connecter à la base de données.
	 * 
	 */
	private static Connection conn;

	/**
	 * Message utilisé pour signifier que la modification de la base de donnée
	 * s'est effectuée avec succès.
	 * 
	 */
	public static String MESSAGE_SUCCESS = "Opération réussie!";
	
	/**
	 * Message utilisé pour signifier que la modification de la base de donnée
	 * ne s'est pas effectuée avec succès.
	 * 
	 */
	public static String MESSAGE_FAIL = "Opération échouée!";

	/**
	 * Constructeur privé qui se connecte à la base de donnée en utilisant les
	 * paramètres d'un utilisateur régulier.
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
	 * Méthode qui permet d'obtenir la connexion de la classe. Dans le cas où,
	 * elle n'est pas créée, une nouvelle sera créée.
	 * 
	 * @return conn La connexion ouverte prête à être utilisée.
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
	 * Méthode qui permet de fermer une connexion
	 * 
	 * @param connection
	 *            La connexion à fermer.
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
	 * Méthode qui permet de fermer un PreparedStatement
	 * 
	 * @param pState
	 *            Le PreparedStatement à fermer.
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
	 * Méthode qui permet de fermer un ResultSet
	 * 
	 * @param resultSet
	 *            Le ResultSet à fermer.
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