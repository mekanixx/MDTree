/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdtree;

import java.io.Serializable;

/**
 *
 * @author doyle
 */
public class TreeNodes implements Serializable {
    
    private String childNode;
    private String childValue;
    private String childText;

    public TreeNodes(String childNode, String childValue, String childText) {
        this.childNode = childNode;
        this.childValue = childValue;
        this.childText = childText;
    }
    
     public String getchildNode() {
        return childNode;
    }
     
    public void setchildNode(String Node) {
        this.childNode = Node;
    } 

    public String getchildValue() {
        return childValue;
    }
    
    public void setchildValue(String Node) {
        this.childValue = Node;
    } 
    
    public String getchildText() {
        return childText;
    }
    
    public void setchildText(String Node) {
        this.childText = Node;
    }
}
