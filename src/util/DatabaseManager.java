package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseManager {

	private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String dbURL = "jdbc:derby:derbyDB;create=true";

	private Connection conn = null;
	
	public static void main (String[] args){
		DatabaseManager dm = new DatabaseManager();
//		dm.insertBatch(1, 1000, 40, 19);
//		dm.insertBatch(2, 10120, 40, 12);
//		dm.insertBatch(3, 10021, 40, 11);
//		dm.insertWord("zoki", 1502, true, 2);
//		dm.insertWord("laza", 1503, false, 1);
//		dm.insertWord("boli", 1501, true, 2);
		
		dm.viewBatches();
		dm.viewWords();
	}

	public synchronized void  insertWord(String word, long time, boolean parity, int batch) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("INSERT INTO words VALUES ('" + word + "', "
					+ time + ", '" + parity + "', " + batch + ")");
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public synchronized void insertBatch(int id, long max, long min, long avg) {
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("INSERT INTO batches VALUES (" + id + ", "
					+ max + ", " + min + ", " + avg + ")");
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	public DatabaseManager() {
		createConnection();
		//createTables();
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
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from  batches");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                long max = results.getLong(2);
                long min = results.getLong(3);
                long avg = results.getLong(4);
                System.out.println(id + "\t\t" + max + "\t\t" + min  + "\t\t" + avg );
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
	}
	
	private void viewWords() {
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from  words");
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                String word = results.getString(1);
                long procTime = results.getLong(2);
                boolean parity = results.getBoolean(3);
                int batch_id = results.getInt(4);
                System.out.println(word + "\t\t" + procTime + "\t\t" + parity  + "\t\t" + batch_id );
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
	}

	private void createTables() {
		try {
			Statement stmt = conn.createStatement();
		
			stmt.execute("DROP TABLE words");
			stmt.execute("DROP TABLE batches");
			
			stmt.execute("CREATE TABLE batches ( " + "id INT NOT NULL, "
					+ "maxTime BIGINT, " + "minTime BIGINT, " + "avgTime BIGINT, "
					+ "PRIMARY KEY (id) )");

			stmt.execute("CREATE TABLE words ( " + "word VARCHAR(20), "
					+ "process_time BIGINT, " + "parity BOOLEAN, "
					+ "batch_id INT, "
					+ "FOREIGN KEY (batch_id) REFERENCES batches)");
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

}
