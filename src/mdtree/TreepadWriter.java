/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdtree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;

/**
 *
 * @author doyle
 */
public class TreepadWriter {
    
    public static ArrayList<TreeNodes> writer() {
        String nodeText = new String();
        String childValue = new String();
        String childNode = new String(); 
        ArrayList<TreeNodes> obj = new ArrayList<TreeNodes>();
        
        try {
            String inputFile = "C:" + File.separator + "Users" + File.separator + "freef" + File.separator + "Documents" + File.separator + "NetBeansProjects" + File.separator + "MDTree" + File.separator + "src" + File.separator + "mdtree" + File.separator + "maint.txt";
            File treeFile = new File(inputFile);

            Scanner treeScanner = new Scanner(treeFile);
            String nextLine = treeScanner.nextLine();
            while (treeScanner.hasNextLine()) {
                
                nextLine = treeScanner.nextLine();
                if (nextLine.trim().contentEquals("<node>")) {
                    childValue = treeScanner.nextLine();
                    childNode = treeScanner.nextLine();
                } else {
                    if (!nextLine.trim().contentEquals("dt=Text") && !nextLine.trim().contentEquals("<end node> 5P9i0s8y19Z")) {
                        nodeText = nodeText.concat(nextLine).concat("\n");
                    }
                    if (nextLine.trim().contentEquals("<end node> 5P9i0s8y19Z")) {
                        TreeNodes tn = new TreeNodes(childNode, childValue, nodeText) ;
                        obj.add(tn);
                        nodeText = "";
                    }
                }
                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TreepadWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
         return obj;
    }
    
    
}
     
