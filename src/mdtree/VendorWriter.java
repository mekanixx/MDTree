/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author doyle
 */
public class VendorWriter {
     
     public static void vendorWriter() throws IOException {
        String nodeText = new String();
        String[] vendor = null; String[] address1 = null; String[] address2 = null; String[] city = null;
        String[] state = null; String[] zip = null; String[] country = null; String[] contact = null;
        String[] phone = null; String[] ext = null; String[] fax = null; String[] notes = null;
        String concat = "";
        try {                  //C:\Users\freef\Documents\NetBeansProjects\MDTree\src\mdtree
            String inputFile = "C:" + File.separator + "Users" + File.separator + "freef" + File.separator + "Documents" + File.separator + "NetBeansProjects" + File.separator + "MDTree" + File.separator + "src" + File.separator + "mdtree" + File.separator + "vendornew.txt";
            File treeFile = new File(inputFile);
            
            Scanner treeScanner = new Scanner(treeFile);
            while (treeScanner.hasNextLine()) {
                
                nodeText = treeScanner.nextLine();
                if (nodeText.startsWith(" 1 ")) {
                  vendor = nodeText.split("="); 
                  //System.out.println(vendor[1]);
                }
                if (nodeText.startsWith(" 2 ")) {
                  address1 = nodeText.split("=");
                }
                if (nodeText.startsWith(" 3 ")) {
                  address2 = nodeText.split("=");
                }
                if (nodeText.startsWith(" 4 ")) {
                  city = nodeText.split("=");
                }
                if (nodeText.startsWith(" 5 ")) {
                  state = nodeText.split("=");
                }
                if (nodeText.startsWith(" 6 ")) {
                  zip = nodeText.split("=");
                  concat = city[1].concat(state[1]).concat(zip[1]);
                }
                //if (nodeText.startsWith(" 7 ")) {
                //  country = nodeText.split("=");
                //}
                if (nodeText.startsWith(" 8 ")) {
                  contact = nodeText.split("=");
                }
                if (nodeText.startsWith(" 9 ")) {
                  phone = nodeText.split("=");
                }
               // if (nodeText.startsWith(" 10 ")) {
                //  ext = nodeText.split("=");
                //}
                if (nodeText.startsWith(" 11 ")) {
                  fax = nodeText.split("="); 
                }
                if (nodeText.startsWith(" 12 ")) {
                  notes = nodeText.split("="); 
                 // System.out.println(notes[1]);
                }
               
                if (nodeText.startsWith("----------------------")) {
                    String sql = "INSERT  OR IGNORE INTO vendorData (vendor, address1, address2, city, contact, phone, fax, notes) VALUES ('"
                    + vendor[1] + "','" + address1[1] + "','" + address2[1] + "','" + concat + "','" + contact[1] + "','" + phone[1] + "','" + fax[1] + "','" + notes[1] + "');";  
                    //System.out.println(sql);

                    SqliteDBUtility sdbu = new SqliteDBUtility();
                    try {
                        sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
                    } catch (Exception ex) {
                        Logger.getLogger(VendorWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        sdbu.executeStmt(sql);
                    } catch (SQLException ex) {
                        Logger.getLogger(VendorWriter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        sdbu.closeConnection();
                        nodeText="";
                }
               
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VendorWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
}
