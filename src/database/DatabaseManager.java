package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database manager <b>!!!!!</b> For the first time execution comment <b>DROP
 * TABLE</b> lines in <b>createTables()</b> method <b>!!!!!</b> For tables view/
 * executing main method, please comment call of <b>createTables()</b> method in
 * the constructor
 */
public class DatabaseManager {

	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String dbURL = "jdbc:derby:derbyDB;create=true";

	private Connection conn = null;

	public static void main(String[] args) {
		DatabaseManager dm = new DatabaseManager();

		dm.viewBatches();
		dm.viewWords();
	}

	public synchronized void insertWord(String word, long time, boolean parity,
			int batch) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("INSERT INTO words VALUES ('" + word + "', " + time
					+ ", '" + parity + "', " + batch + ")");
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public synchronized void insertBatch(int id, long max, long min, long avg) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("INSERT INTO batches VALUES (" + id + ", " + max
					+ ", " + min + ", " + avg + ")");
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public DatabaseManager() {
		createConnection();
		// !!!!!!!!!!!!
		// please comment line below for tables view
		createTables();
		// !!!!!!!!!!!!
	}

	private void createConnection() {
		try {
			Class.forName(DRIVER).newInstance();
			// Get a connection
			conn = DriverManager.getConnection(dbURL);
		} catch (Exception except) {
			except.printStackTrace();
		}
	}

	private void viewBatches() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from  batches");
			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 1; i <= numberCols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t\t");
			}

			System.out
					.println("\n-------------------------------------------------");

			while (results.next()) {
				int id = results.getInt(1);
				long max = results.getLong(2);
				long min = results.getLong(3);
				long avg = results.getLong(4);
				System.out.println(id + "\t\t" + max + "\t\t" + min + "\t\t"
						+ avg);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private void viewWords() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from  words");
			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 1; i <= numberCols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t\t");
			}

			System.out
					.println("\n-------------------------------------------------------------");

			while (results.next()) {
				String word = results.getString(1);
				long procTime = results.getLong(2);
				boolean parity = results.getBoolean(3);
				int batch_id = results.getInt(4);
				System.out.println(String.format("%-20s", word) + "\t"
						+ procTime + "\t\t" + parity + "\t\t" + batch_id);
			}
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	private void createTables() {
		try {
			Statement stmt = conn.createStatement();
			
			// !!!!!
			// please comment these 2 lines below for the first time execution,
			// and then uncomment them for the future use
			stmt.execute("DROP TABLE words");
			stmt.execute("DROP TABLE batches");
			// please comment these 2 lines above for the first time execution,
			// and then uncomment them for the future use
			// !!!!!
			
			stmt.execute("CREATE TABLE batches ( " + "id INT NOT NULL, "
					+ "maxTime BIGINT, " + "minTime BIGINT, "
					+ "avgTime BIGINT, " + "PRIMARY KEY (id) )");

			stmt.execute("CREATE TABLE words ( " + "word VARCHAR(20), "
					+ "process_time BIGINT, " + "parity BOOLEAN, "
					+ "batch_id INT, "
					+ "FOREIGN KEY (batch_id) REFERENCES batches)");
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

}
