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
	public static final String MESSAGE_SUCCESS = "Op�ration r�ussie!";
	
	/**
	 * Message utilis� pour signifier que la modification de la base de donn�e
	 * ne s'est pas effectu�e avec succ�s.
	 * 
	 */
	public static final String MESSAGE_FAIL = "Op�ration �chou�e!";

	/**
	 * Constructeur priv� qui se connecte � la base de donn�e en utilisant les
	 * param�tres d'un utilisateur r�gulier.
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
					+ " n�cessaire pour la connexion avec la base de donn�e n'a"
					+ " pas pu �tre charg�.",
					e.getMessage());
			ed.setVisible(true);
		} catch (IOException e) {
			ErrorDialog ed = new ErrorDialog(null, MESSAGE_FAIL, "Les"
					+ " informations n�cessaires � la connexion � la base de"
					+ " donn�es n'ont pas pu �tre r�cup�r�es. V�rifiez si le"
					+ " fichier config est bien pr�sent.",
					e.getMessage());
			ed.setVisible(true);
		} catch (SQLException e) {
			ErrorDialog ed = new ErrorDialog(null, MESSAGE_FAIL, "Une erreur est"
					+ " survenue lors de la connexion avec la base de donn�es."
					+ " V�rifiez si le service MySql est bien d�marr� ou la"
					+ " validit� des informations contenues dans le fichier"
					+ " config.",
					e.getMessage());
			ed.setVisible(true);
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