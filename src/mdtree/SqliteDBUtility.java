/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdtree;

import java.sql.*;
import java.sql.DriverManager;
import javafx.scene.control.TextArea;

/**
 *
 * @author doyle
 */
public class SqliteDBUtility {
    
    public  String sDriver = ""; 
    public  String sUrl = null;
    public  int iTimeout = 30;
    public  Connection conn = null;
    public  Statement statement = null;

  public void init(String sDriverVar, String sUrlVar) throws Exception
    {
        setDriver(sDriverVar);
        setUrl(sUrlVar);
        setConnection();
        setStatement();
    }  
    
      private void setDriver(String sDriverVar)
    {
        sDriver = sDriverVar;
    }

    private void setUrl(String sUrlVar)
    {
        sUrl = sUrlVar;
    }

    public  void setConnection() throws Exception {
        Class.forName(sDriver);
        conn = DriverManager.getConnection(sUrl);
    }


    public  Connection getConnection() {
        return conn;
    }

    public  void setStatement() throws Exception {
        if (conn == null) {
            setConnection();
        }
        statement = conn.createStatement();
        statement.setQueryTimeout(iTimeout);  // set timeout to 30 sec.
    }

    public  Statement getStatement() {
        return statement;
    }

    public  void executeStmt(String instruction) throws SQLException {
       // System.out.println(instruction);
        statement.executeUpdate(instruction);
    }

    public ResultSet executeQry(String instruction) throws SQLException {
        return statement.executeQuery(instruction);
    } 

    public void closeConnection() {
        try { conn.close(); } catch (Exception ignore) {}
    }
  
    
  public static void connectDBUtility(String DB)  {
      Connection c = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:" + DB);
        } catch ( Exception e ) {
          System.out.println("ERROR opening database");  
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
  }
    
  public static void createDBUtility(String DB, String sql)  {
      Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:" + DB);
          stmt = c.createStatement();
          stmt.executeUpdate(sql);
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          System.out.println("ERROR creating Table");  
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
  }
  
  public static void insertDBUtility(String DB, String sql)
  {
      Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:" + DB);
          c.setAutoCommit(false);
          stmt = c.createStatement();
          //String sql = "INSERT INTO treeData (nodeName, nodeValue, node) VALUES ('" + nodeName + "','" +  nodeValue + "'," + node + ");";
          stmt.executeUpdate(sql);
          stmt.close();
          c.commit();
          c.close();
        } catch ( Exception e ) {
          System.out.println("ERROR creating Records");
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
  }
  
  public static void selectDBUtility(String DB, String sql)
  {
        Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:" + DB);
          c.setAutoCommit(false);
          System.out.println("Opened database successfully");
          //System.out.println(query);
          stmt = c.createStatement();
          ResultSet rs = stmt.executeQuery( sql );
          if (rs.next()){
              //txtArea.setText(rs.getString("nodeValue"));
          }
          
//          while ( rs.next() ) {
//             String  nodeName = rs.getString("nodeName");
//             String  nodeValue = rs.getString("nodeValue");
//             System.out.println( "Name : " + nodeName );
//             System.out.println( "Message : " + nodeValue );
//             System.out.println();
//          }
          rs.close();
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.out.println("Operation done successfully");  
  }
  
  public static void updateDBUtility(String DB, String sql)
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:" + DB);
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
 
      stmt = c.createStatement();
     // String sql = "UPDATE treeData SET 'nodeValue' = '" + nodeValue + "' WHERE 'nodeName' = '" + nodeName + "';";
      stmt.executeUpdate(sql);
      c.commit();
 
      ResultSet rs = stmt.executeQuery( "SELECT * FROM treeData;" );
      while ( rs.next() ) {
//             nodeName = rs.getString("nodeName");
//             nodeValue = rs.getString("nodeValue");
//             System.out.println( "Name : " + nodeName );
//             System.out.println( "Message : " + nodeValue );
//             System.out.println();
      }
      rs.close();
      stmt.close();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Operation done successfully");
  }
  
  public static void deleteDBUtility(String DB, String sql)
  {
      Connection c = null;
        Statement stmt = null;
        try {
          Class.forName("org.sqlite.JDBC");
          c = DriverManager.getConnection("jdbc:sqlite:" + DB);
          c.setAutoCommit(false);
          System.out.println("Opened database successfully");
 
          stmt = c.createStatement();
         // String sql = "DELETE FROM treeData WHERE nodeName = " + nodeName + ";";
          stmt.executeUpdate(sql);
          c.commit();
 
         // ResultSet rs = stmt.executeQuery( "SELECT * FROM treeData;" );
          //while ( rs.next() ) {
//             nodeName = rs.getString("nodeName");
//             nodeValue = rs.getString("nodeValue");
//             System.out.println( "Name : " + nodeName );
//             System.out.println( "Message : " + nodeValue );
//             System.out.println();
          //}
          //rs.close();
          stmt.close();
          c.close();
        } catch ( Exception e ) {
          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
          System.exit(0);
        }
        System.out.println("Operation done successfully");
  }

    
}
