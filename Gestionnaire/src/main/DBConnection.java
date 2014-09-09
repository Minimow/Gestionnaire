package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import dao.daoException.DAOException;
import dialogs.ErrorDialog;

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
	public static final String MESSAGE_SUCCESS = "Opération réussie!";
	
	/**
	 * Message utilisé pour signifier que la modification de la base de donnée
	 * ne s'est pas effectuée avec succès.
	 * 
	 */
	public static final String MESSAGE_FAIL = "Opération échouée!";

	/**
	 * Constructeur privé qui se connecte à la base de donnée en utilisant les
	 * paramètres d'un utilisateur régulier.
	 * 
	 */
	private DBConnection() {
		try {
			// Get the driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");
			
			// Read the config file where the info for the DB are stored
			Properties prop = new Properties();
	        String propFileName = "config.properties";
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	        prop.load(inputStream);
	        if (inputStream == null) {
	            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
	        }

	        // Get the property value
	        String url = prop.getProperty("url");
	        String user = prop.getProperty("user");
	        String password = prop.getProperty("pdassword");
	        
	        // Connect to the database using the info from the config file
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connexion O.K.");
			
		} catch (ClassNotFoundException e) {
			ErrorDialog ed = new ErrorDialog(null, MESSAGE_FAIL, "Le pilote"
					+ " nécessaire pour la connexion avec la base de donnée n'a"
					+ " pas pu être chargé.",
					e.getMessage());
			ed.setVisible(true);
		} catch (IOException e) {
			ErrorDialog ed = new ErrorDialog(null, MESSAGE_FAIL, "Les"
					+ " informations nécessaires à la connexion à la base de"
					+ " données n'ont pas pu être récupérées. Vérifiez si le"
					+ " fichier config est bien présent.",
					e.getMessage());
			ed.setVisible(true);
		} catch (SQLException e) {
			ErrorDialog ed = new ErrorDialog(null, MESSAGE_FAIL, "Une erreur est"
					+ " survenue lors de la connexion avec la base de données."
					+ " Vérifiez si le service MySql est bien démarré ou la"
					+ " validité des informations contenues dans le fichier"
					+ " config.",
					e.getMessage());
			ed.setVisible(true);
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