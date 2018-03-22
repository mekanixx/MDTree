/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdtree;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.StatusBar;


/**
 *
 * @author Doyle
 */
public class MDTreeController implements Initializable {
    private Tab tabNotes;
    private Tab tabVendors;
    private boolean loaded = false;
    @FXML
    private TreeView<String> idNotesTreeView;
    @FXML
    private TextArea idNotesTextEdit;
    @FXML
    private ListView<String> idVendorListView;
    @FXML
    private TextField idVendorText;
    @FXML
    private TextField idAddress1Text;
    @FXML
    private TextField idAddress2Text;
    @FXML
    private TextField idEmailText;
    @FXML
    private TextField idUrlText;
    @FXML
    private TextField idCityText;
    @FXML
    private TextField idContactText;
    @FXML
    private TextField idPhoneText;
    @FXML
    private TextField idFaxText;
    @FXML
    private TextArea idNotesText;
    @FXML
    private MenuItem idMenuClose;
    @FXML
    private MenuItem idMenuDelete;
    @FXML
    private MenuItem idMenuAbout;
    @FXML
    private StatusBar idStatusbar;
    @FXML
    private TabPane idTabPane;
    @FXML
    private BorderPane idBorderPane;
    @FXML
    private SplitPane idVertSplitNotes;
    @FXML
    private SplitPane idVertSplitVendor;
    @FXML
    private Tab noteTab;
    @FXML
    private Tab vendorTab;
    @FXML
    private Label logoLabel;
    @FXML
    private Label logoView;
    
    private Integer NODE;
    private String NODENAME;
    private String NODEVALUE;
    
    private String DB_NAME = "mdtree.sqlite";
    
    private boolean notesIsDirty = false;
    private boolean vendorsIsDirty = false;
    
    ObservableList<String> vendorList = FXCollections.observableArrayList();
    Image icon1 = new Image(getClass().getResourceAsStream("/images/folder.png"));
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //vendor, vendorname, address1, address2, city, email, url, contact, phone, fax, notes 
        idNotesTextEdit.setTooltip(new Tooltip("Add Notes about this topic here."));
        idVendorText.setTooltip(new Tooltip("Add vendor's name here."));
        idAddress1Text.setTooltip(new Tooltip("Add vendor's main address here."));
        idAddress2Text.setTooltip(new Tooltip("Add vendor's secondary address here."));
        idCityText.setTooltip(new Tooltip("Add vendor's City."));
        idEmailText.setTooltip(new Tooltip("Add vendor's Email here."));
        idUrlText.setTooltip(new Tooltip("Add vendor's Web Site URL here."));
        idContactText.setTooltip(new Tooltip("Add contact details here."));
        idPhoneText.setTooltip(new Tooltip("Add vendor's phone number here."));
        idFaxText.setTooltip(new Tooltip("Add vendor's fax number here."));
        idNotesText.setTooltip(new Tooltip("Add Notes about this vendor here."));
        logoLabel.setTooltip(new Tooltip("MDTree™"));
        
        //Check for a database file - DB_NAME
        boolean isData = false;
        try {
	     File file = new File(DB_NAME);
             if(file.exists() && !file.isDirectory()) {
                 isData = countDataItems();
                 try {
                     if (!isData){
                         buildDatabase();
                     }
                 } catch (Exception ex) {
                     Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }else{
                 boolean fvar = file.createNewFile();
                 if (fvar){
                     try {
                         isData = countDataItems();
                         if (!isData){
                            buildDatabase();
                         }   
                     } catch (Exception ex) {
                         Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
             } 
             } catch (IOException e) {
    		System.out.println("Exception Occurred:");
	        e.printStackTrace();
            }
                // Update the message Label when the selected item changes
        idVendorListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, final String oldvalue, final String newvalue) {
                
                idStatusbar.setText(newvalue);
                
                //vendor, vendorname, address1, address2, city, email, url, contact, phone, fax, notes 
                String qry = "SELECT vendor, address1, address2, city, email, url, contact, phone, fax, notes "
                        + "FROM vendorData WHERE vendor = '" + newvalue + "'";

                 SqliteDBUtility sdbu = new SqliteDBUtility();
                    try {
                        sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
                    } catch (Exception ex) {
                        Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        ResultSet rs = sdbu.executeQry(qry);
                        while (rs.next()) {
                            String vendor = rs.getString("vendor");
                            idVendorText.setText(vendor);
                            String address1 = rs.getString("address1");
                            idAddress1Text.setText(address1);
                            String address2 = rs.getString("address2");
                            idAddress2Text.setText(address2);
                            String city = rs.getString("city");
                            idCityText.setText(city);
                            String email = rs.getString("email");
                            idEmailText.setText(email);
                            String url = rs.getString("url");
                            idUrlText.setText(url);
                            String contact = rs.getString("contact");
                            idContactText.setText(contact);
                            String phone = rs.getString("phone");
                            idPhoneText.setText(phone);
                            String fax = rs.getString("fax");
                            idFaxText.setText(fax);
                            String notes = rs.getString("notes");
                            idNotesText.setText(notes);
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        sdbu.closeConnection();
             }
        });
                
        // Handle TextField text changes.
        idVendorText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded ) {
                    String sql = "UPDATE vendorData SET vendor = '" + newValue.trim() + "' WHERE vendor = '" + oldValue.trim() + "';";
                    upDateVendorDB(sql);
                }
            }
        });
        idAddress1Text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET address1 = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }
        });
        idAddress2Text.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET address2 = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }    
        });
        idCityText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET city = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }
            }   
        });
        idEmailText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                System.out.print(oldValue);
                System.out.print(newValue);
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET email = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }
        });
        idUrlText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET url = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }
            }
        });
        idContactText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET contact = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }
        });
        idPhoneText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET phone = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }
        });
        idFaxText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET fax = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }
        });
        idNotesText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.isEmpty() && !newValue.isEmpty()) {
                if (!newValue.contentEquals(oldValue)&& loaded) {
                    String vendorTextContents = idVendorText.getText();
                    String sql = "UPDATE vendorData SET notes = '" + newValue.trim() + "' WHERE vendor = '" + vendorTextContents.trim() + "';";
                    upDateVendorDB(sql);
                }   
            }
        });
        
        
        try {
            readTreeViewDB();
        } catch (Exception ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            readListViewDB();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        idNotesTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSelectedItem(newValue));
        idNotesTreeView.setEditable(true);
        idNotesTreeView.setCellFactory((TreeView<String> p) -> new TextFieldTreeCellImpl());

        idVertSplitNotes.setDividerPositions(0.25);
        idNotesTreeView.maxWidthProperty().bind(idVertSplitNotes.widthProperty().multiply(0.25));
        idNotesTextEdit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                  notesIsDirty = true;  
            }
        });
        
        idTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            //System.err.println("changed");
         });
        selectNotes();        
    }    
   
    private void selectNotes(){
        MultipleSelectionModel msm = idNotesTreeView.getSelectionModel();
        TreeItem<String> root = idNotesTreeView.getRoot();
        // This line is the not-so-clearly documented magic.
        int row = idNotesTreeView.getRow( root );
        // Now the row can be selected.
        msm.select( row );
    }
    
    private boolean countDataItems() {
     SqliteDBUtility sdbu = new SqliteDBUtility();
     String query;
     Integer count = 0;
        try {
            sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
            
            //check for any data
            query = "SELECT * FROM vendorData";
            ResultSet rs = sdbu.executeQry(query);
            while (rs.next()) {
               String vendor = rs.getString("vendor");
               count+= count;
            }
            sdbu.closeConnection();
            
            //if we got 5 items we assume the database is good...
            if (count >= 5) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    private void buildDatabase() throws SQLException, Exception {
        
        SqliteDBUtility sdbu = new SqliteDBUtility();
        ArrayList<String> treeList = new ArrayList<String>();
        String query;
                
        sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
        query = "CREATE TABLE IF NOT EXISTS vendorData ('vendor' VARCHAR PRIMARY KEY NOT NULL, 'address1' TEXT, 'address2' TEXT, 'city' TEXT, 'email' TEXT, 'url' TEXT, 'contact' TEXT, 'phone' TEXT, 'fax' TEXT, 'notes' TEXT);";
        sdbu.executeStmt(query);
        query =  "CREATE TABLE IF NOT EXISTS treeData ('id' INTEGER PRIMARY KEY NOT NULL,'nodeName' TEXT, 'nodeValue' TEXT, 'node' TEXT);";
        sdbu.executeStmt(query);
        
        TreepadWriter treeWriter = new TreepadWriter();
        ArrayList<TreeNodes> obj = new ArrayList<TreeNodes>();
        obj = treeWriter.writer();
        
        for(int i=0; i<obj.size(); i++){
            try {
              sdbu.executeStmt("INSERT  OR IGNORE INTO treeData (node, nodeName, nodeValue) VALUES ('" + obj.get(i).getchildNode() + "','" + obj.get(i).getchildValue() + "','" + obj.get(i).getchildText() + "');");
            } catch (SQLException ex) {
              Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         sdbu.closeConnection();
         
        VendorWriter vend = new VendorWriter();
        vend.vendorWriter();
    }
    
    @FXML
    private void ontabNotes(Event event) {
        //System.out.println("Tab Notes select ok");
        selectNotes();
    }

    @FXML
    private void ontabVendors(Event event) {
        //System.out.println("Tab Vendors select ok");
        idVendorListView.getSelectionModel().select(0);
       // loaded = true;
    }

    @FXML
    private void onidNotesTreeViewClicked(MouseEvent event) {
    }

    @FXML
    private void onidNotesTextEditContextMenuClicked(ContextMenuEvent event) {
    }

    @FXML
    private void onidVendorListViewClicked(MouseEvent event) {
    }

    @FXML
    private void onidMenuClose(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onidMenuDelete(ActionEvent event) {
    }

    @FXML
    private void onidMenuAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About MDTree™");
        alert.setHeaderText("Welcome to MDTree!");
        String s = "MDTree™ is a Personal Information Manager,\n";
        s = s + "Organizer, Database, and much more.\n";
        alert.setContentText(s);
        alert.show();
    }

    
    @FXML
    void onMouseClickedEvent(MouseEvent event) {

    }

    private void upDateVendorDB(String sql) {
        SqliteDBUtility sdbu = new SqliteDBUtility();
        try {
            sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
        } catch (Exception ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(sql);
        try {
            sdbu.executeStmt(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        sdbu.closeConnection();

    }
    
    @FXML
    private void onKeyPressEvent(KeyEvent event) {
        // if the editor is clicked...
//        if (event.getSource() == idNotesTextEdit){
//            Event e;
//           // idNotesTextEdit.setOnInputMethodTextChanged(onTextChanged(e));
//            NODEVALUE = idNotesTextEdit.getText();
//            String sql = "UPDATE treeData SET 'nodeValue' = '" + NODEVALUE + "', 'node' = '" + NODE + "' WHERE 'nodeName' = '" + NODENAME + "';";
//            updateNotesDB(sql);
//            notesIsDirty = true;
//           // System.out.println("TextEditor");
//        }else{
//            // got to be a text field!
//           if (event.getSource().getClass().getTypeName().contentEquals("javafx.scene.control.TextField")) {
//              vendorsIsDirty = true;
//          }
//
//          if (event.getSource().getClass().getTypeName().contentEquals("javafx.scene.control.TextArea")) {
//              vendorsIsDirty = true;
//          }
//
//          if (vendorsIsDirty == true) {
//              try {
//                  String vendor = idVendorText.getText();
//                  if (idVendorText.getLength() > 0) {
//                      vendor = vendor.trim();
//                  }
//                  String address1 = idAddress1Text.getText();
//                  if (idAddress1Text.getLength() > 0) {
//                      address1 = address1.trim();
//                  }
//                  String address2 = idAddress2Text.getText();
//                  if (idAddress2Text.getLength() > 0) {
//                      address2 = address2.trim();
//                  }
//                  String city = idCityText.getText();
//                  if (idCityText.getLength() > 0) {
//                      city = city.trim();
//                  }
//                  String email = idEmailText.getText();
//                  if (idEmailText.getLength() > 0) {
//                      email = email.trim();
//                  }
//                  String url = idUrlText.getText();
//                  if (idUrlText.getLength() > 0) {
//                      url = url.trim();
//                  }
//                  String contact = idContactText.getText();
//                  if (idContactText.getLength() > 0) {
//                      contact = contact.trim();
//                  }
//                  String phone = idPhoneText.getText();
//                  if (idPhoneText.getLength() > 0) {
//                      phone = phone.trim();
//                  }
//                  String fax = idFaxText.getText();
//                  if (idFaxText.getLength() > 0) {
//                      fax = fax.trim();
//                  }
//                  String notes = idNotesText.getText().trim();
//                  if (idNotesText.getLength() > 0) {
//                      notes = notes.trim();
//                  }
//                  SqliteDBUtility sdbu = new SqliteDBUtility();
//                  try {
//                      sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
//                  } catch (Exception ex) {
//                      Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
//                  }
//                  // String sql = "UPDATE treeData SET 'nodeValue' = '" + nodeValue + "' WHERE 'nodeName' = '" + nodeName + "';";
//                  String query = "UPDATE vendorData SET vendor = '" + vendor + "'," + "address1 = '" + address1 + "'," + "address2 = '" + address2 + "'," + "city = '" + city + "'," + "email = '" + email + "'," + "url = '" + url + "'," + "contact = '" + contact + "'," + "phone = '" + phone + "'," + "fax = '" + fax +  "'," + "notes = '" + notes + "' WHERE vendor = '" + vendor + "';";          
//                  //String query = "INSERT INTO vendorData VALUES ('" + vendor + "','" + address1 + "','" + address2 + "','" + city + "','"  + email + "','"  + url + "','" + contact + "','" + phone + "','" + fax + "','" + notes + "');";
//                  
//                  System.out.println(query);
//                  sdbu.executeStmt(query);
//                  sdbu.closeConnection();
//                  vendorsIsDirty = false;
//              } catch (SQLException ex) {
//                  Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
//              }
//          }     
//               //System.out.println(event.getSource().getClass().getTypeName());
//       }
    }

    public static void updateNotesDB(String sql) {
        SqliteDBUtility sdbu = new SqliteDBUtility();
        try {
                sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
            } catch (Exception ex) {
                Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            sdbu.executeStmt(sql);
        } catch (SQLException ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
            sdbu.closeConnection();
    }


        private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Add Item");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(new EventHandler() {
                public void handle(Event t) {
                    TreeItem newEmployee
                            = new TreeItem<String>("New Item");
                    getTreeItem().getChildren().add(newEmployee);
                }
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
                if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
                    setContextMenu(addMenu);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent t) {
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(textField.getText());
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

    }    
    
       private void updateSelectedItem(Object newValue) {
           
         if (notesIsDirty){
            NODEVALUE = idNotesTextEdit.getText();
            String sql = "UPDATE treeData SET 'nodeValue' = '" + NODEVALUE + "', 'node' = '" + NODE + "' WHERE 'nodeName' = '" + NODENAME + "';";
            updateNotesDB(sql);
            notesIsDirty = false;  
        }    
           
        TreeItem<String> selectedItem = idNotesTreeView.getSelectionModel().getSelectedItem();
        idStatusbar.setText(selectedItem.getValue());
        String qry = "SELECT nodeValue, node FROM treeData WHERE nodeName = '" + selectedItem.getValue() + "';";
        NODENAME = selectedItem.getValue();
    
        SqliteDBUtility sdbu = new SqliteDBUtility();
        try {
                sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
            } catch (Exception ex) {
                Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            
            ResultSet rs = sdbu.executeQry(qry);
            while ( rs.next() ) {
                NODEVALUE = rs.getString("nodeValue");
                NODE = rs.getInt(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
            idNotesTextEdit.setText(NODEVALUE);
            sdbu.closeConnection();
    }

       
    private void readListViewDB() throws ClassNotFoundException {
        SqliteDBUtility sdbu = new SqliteDBUtility();
        try {
                sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
            } catch (Exception ex) {
                Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {

        String sql = "SELECT * FROM vendorData";
        ResultSet rs = sdbu.executeQry(sql);
            while (rs.next()) {
                String vendor = rs.getString("vendor");
                vendorList.add(vendor);
                String address1 = rs.getString("address1");
                String address2 = rs.getString("address2");
                String state = rs.getString("city");
                String zip = rs.getString("email");
                String country = rs.getString("url");
                String contact = rs.getString("contact");
                String phone = rs.getString("phone");
                String fax = rs.getString("fax");
                String notes = rs.getString("notes");
            }
            idVendorListView.setItems(vendorList);
        } catch (SQLException ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
            sdbu.closeConnection();
        }

    private void readTreeViewDB() {
        String query = new String();
        String nodeText = new String();
        TreeItem<String> root = new TreeItem();
        TreeItem<String> node1 = new TreeItem();
        TreeItem<String> node2 = new TreeItem();
        String childNode = new String();
        String node = null;

        SqliteDBUtility sdbu = new SqliteDBUtility();
        try {
                sdbu.init("org.sqlite.JDBC", "jdbc:sqlite:mdtree.sqlite");
            } catch (Exception ex) {
                Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        try {
        String sql = "SELECT nodeName, node FROM treeData;";
        ResultSet rs = sdbu.executeQry(sql);
            while (rs.next()) {
                String nodeName = rs.getString("nodeName");
                node = rs.getString("node");
                if (node.contentEquals("0")) {
                    root = new TreeItem<>(nodeName, new ImageView(icon1));
                    idNotesTreeView.setRoot(root);
                }
                if (node.contentEquals("1")) {
                    node1 = new TreeItem<>(nodeName, new ImageView(icon1));
                    root.getChildren().add(node1);
                }
                if (node.contentEquals("2")) {
                    node2 = new TreeItem<>(nodeName, new ImageView(icon1));
                    node1.getChildren().add(node2);
                }
            }
            root.setExpanded(true);
        } catch (SQLException ex) {
            Logger.getLogger(MDTreeController.class.getName()).log(Level.SEVERE, null, ex);
        }
            sdbu.closeConnection();
        }

}
