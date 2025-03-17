package Essentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Essentials.addEvents.*;
import Essentials.loadEvents.*;
import Essentials.modifiedEvents.*;
import Essentials.removeEvents.*;
import java.util.Arrays;

public class GUI extends JFrame implements ActionListener {

    private JPanel boarder;
    private JButton searchbutton, studbuttons, addStudentButton, probgButton, collegebutton, addprog, addcollege;
    private JButton delete, deleteprog, deletecollege;
    private JButton modifystudent, modifyprogram, modifycollege;
    private JTextField searchbar;
    private DefaultTableModel model, programmodel, collegemodel;
    private JTable table, programTable, collegetable;
    private TableRowSorter<DefaultTableModel> sorter, progsorter, collsorter;
    private create writer; // File handler object
    private delete deleter;
    private JScrollPane tableScrollPane, progpane, collegepane;
    private JFrame menu;
    // jComboBox for sorting
    private JComboBox<String> comboBox;
    // sorters
    private JComboBox<String> progsortcombo, collsortcombo;
    private addstudentGUI classstudent;
    private addcollegeGUI classcollege;
    private addprogGUI classprogram;
    private removestud deletestudent;
    private removecollege deletecoll;
    private removeprogram deleteprogram;
    private modifystudentGUI modifystud;
    private modifyprogram modifyprog;
    private modifycollege modifycoll;

    
    public GUI() {
        // Initialize file handler and deleter
        writer = new create();
        deleter = new delete();

        // Table column names (assumes no header row in the CSV)
        String[] columnNames = {"ID", "First Name", "Last Name", "Year Level", "Gender", "Program Code"};
        model = new DefaultTableModel(columnNames, 0);

        // Create table and sorter
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        

        // Program table
        String[] progname = {"Program Code", "Program Name", "College Code"};
        programmodel = new DefaultTableModel(progname, 0);
        programTable = new JTable(programmodel);
        progsorter = new TableRowSorter<>(programmodel);
        programTable.setRowSorter(progsorter);
        programTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        programTable.getColumnModel().getColumn(0).setPreferredWidth((int)(585 * 0.2));
        programTable.getColumnModel().getColumn(1).setPreferredWidth((int)(585 * 0.6));
        programTable.getColumnModel().getColumn(2).setPreferredWidth((int)(585 * 0.2));

        // College table
        String[] collheader = {"Sort by:", "Sort by College Code", "Sort by College Name"};
        collsortcombo = new JComboBox<>(collheader);
        // (Assuming AutoCompletion works correctly)
        AutoCompletion.enable(collsortcombo);
        collsortcombo.setBounds(370, 50, 200, 30);
        
        String[] coll = {"College Code", "College Name"};
        collegemodel = new DefaultTableModel(coll, 0);
        collegetable = new JTable(collegemodel);
        collsorter = new TableRowSorter<>(collegemodel);
        collegetable.setRowSorter(collsorter);

        // jComboBox for sorting student table
        String[] options = {"Sort by:", "Sort by ID", "Sort by First Name", "Sort by Last Name", "Sort by Year Level", "Sort by Gender", "Sort by Program Code"};
        comboBox = new JComboBox<>(options);
        AutoCompletion.enable(comboBox);
        comboBox.setBounds(370, 50, 200, 30);

        // jComboBox for sorting program table
        String[] progcombo = {"Sort by:", "Sort by Program Code", "Sort by Program Name", "Sort by College Code"};
        progsortcombo = new JComboBox<>(progcombo);
        AutoCompletion.enable(progsortcombo);
        progsortcombo.setBounds(370, 50, 200, 30);

        // Add tables to scroll panes
        progpane = new JScrollPane(programTable);
        progpane.setBounds(25, 87, 585, 230);
        progpane.setVisible(false);
        
        collegepane = new JScrollPane(collegetable);
        collegepane.setBounds(25, 87, 585, 230);
        collegepane.setVisible(false);
        
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(25, 87, 585, 230);

        // Load data
        new loadprogram(this, writer);
        new loadstudent(this, writer);
        new loadcollege(this, writer);

        // Create main window 'menu'
        menu = new JFrame();
        
        // Disable native decorations and use plain dialog style on menu
        JFrame.setDefaultLookAndFeelDecorated(true);
        menu.setUndecorated(true);
        menu.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);

        menu.setBounds(400, 150, 637, 450);
        menu.setLayout(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Search bar and search button
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(35, 80, 100, 30);
        searchLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        
  
        searchbar = new JTextField();
        searchbar.setBounds(100,80, 120, 30);
   
     
        searchbar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtertable();
                filterprogram();
                filtercollege();
            }
        });

        boarder = new JPanel();
    
     

        boarder.setBounds(15,44,605,350);
        boarder.setBackground(Color.gray);
        

        // Create other buttons
        studbuttons = new JButton("STUDENTS");
        studbuttons.setBounds(30, 10, 80, 34);
        studbuttons.setFont(new Font("Tahoma", Font.BOLD, 9));

        probgButton = new JButton("PROGRAM");
        probgButton.setBounds(120, 10, 80, 34);
        probgButton.setFont(new Font("Tahoma", Font.BOLD, 9));
    
        collegebutton = new JButton("COLLEGE");
        collegebutton.setBounds(210, 10, 80, 34);
        collegebutton.setFont(new Font("Tahoma", Font.BOLD, 9));
    
        addStudentButton = new JButton("Add Student");
        addStudentButton.setBounds(280, 380, 100, 34);
        addStudentButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        addStudentButton.setVisible(true);
        
        addprog = new JButton("Add program");
        addprog.setBounds(280, 350, 100, 34);
        addprog.setFont(new Font("Tahoma", Font.BOLD, 9));
        addprog.setVisible(false);
        
        addcollege = new JButton("Add college");
        addcollege.setBounds(280, 350, 100, 34);
        addcollege.setFont(new Font("Tahoma", Font.BOLD, 9));
        addcollege.setVisible(false);

        delete = new JButton("Delete Student");
        delete.setBounds(390, 350, 100, 34);
        delete.setFont(new Font("Tahoma", Font.BOLD, 9));
        delete.setVisible(true);

        deleteprog = new JButton("Delete Program");
        deleteprog.setBounds(390, 350, 100, 34);
        deleteprog.setFont(new Font("Tahoma", Font.BOLD, 8));   
        deleteprog.setVisible(false);

        deletecollege = new JButton("Delete College");
        deletecollege.setBounds(390, 350, 100, 34);
        deletecollege.setFont(new Font("Tahoma", Font.BOLD, 9));
        deletecollege.setVisible(false);

        modifystudent = new JButton("Modify Student");
        modifystudent.setBounds(500,350, 100, 34);
        modifystudent.setFont(new Font("Tahoma", Font.BOLD, 9));
        modifystudent.setVisible(true);

        modifyprogram = new JButton("Modify Program");
        modifyprogram.setBounds(500,350, 100, 34);
        modifyprogram.setFont(new Font("Tahoma", Font.BOLD, 8));
        modifyprogram.setVisible(false);

        modifycollege = new JButton("Modify College");
        modifycollege.setBounds(500,350, 100, 34);
        modifycollege.setFont(new Font("Tahoma", Font.BOLD, 9));
        modifycollege.setVisible(false);

        // Set table row heights
        table.setRowHeight(25);
        programTable.setRowHeight(25);
        collegetable.setRowHeight(25);
        
        
        // Add components to menu
        menu.add(modifystudent);
        menu.add(addcollege);
       
       
        menu.add(progsortcombo);
        menu.add(tableScrollPane);
        menu.add(studbuttons);
        menu.add(addStudentButton);
        menu.add(comboBox);
        menu.add(progpane);
        menu.add(probgButton);
        menu.add(collegepane);
        menu.add(collegebutton);
        menu.add(collsortcombo);
        menu.add(addprog);
        menu.add(delete);
        menu.add(deleteprog);
        menu.add(deletecollege);
        menu.add(modifyprogram);
        menu.add(modifycollege);
        menu.add(boarder);
        menu.add(searchLabel);
        
      
    
    

      
    
        studbuttons.addActionListener(this);
        collegebutton.addActionListener(this);
        addStudentButton.addActionListener(this);
        probgButton.addActionListener(this);
        comboBox.addActionListener(this);
        progsortcombo.addActionListener(this);
        collsortcombo.addActionListener(this);
        addprog.addActionListener(this);
        addcollege.addActionListener(this);
        delete.addActionListener(this);
        modifystudent.addActionListener(this);
        deleteprog.addActionListener(this); 
        deletecollege.addActionListener(this);
        modifyprogram.addActionListener(this);
        modifycollege.addActionListener(this);

        // Add some components to the layered pane
        JLayeredPane layeredPane = menu.getLayeredPane();
        layeredPane.add(addStudentButton, JLayeredPane.DRAG_LAYER);
        layeredPane.add(searchbar, JLayeredPane.DRAG_LAYER);
        layeredPane.add(searchLabel,JLayeredPane.DRAG_LAYER);

        progsortcombo.setVisible(false);
        collsortcombo.setVisible(false);
        menu.setResizable(false);
        menu.setVisible(true);
    }

    

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == modifystudent) {
            modifystud = new modifystudentGUI(this);
        }
        if(e.getSource() == modifyprogram){
            modifyprog = new modifyprogram(this);
        }
        if(e.getSource() == modifycollege){
            modifycoll = new modifycollege(this);
        }
        if (e.getSource() == searchbutton) {
            filtertable();
        }
        if (e.getSource() == studbuttons) {
            tableScrollPane.setVisible(true);
            progpane.setVisible(false);
            comboBox.setVisible(true);
            collegepane.setVisible(false);
            searchbar.setText("");
            progsortcombo.setVisible(false);
            collsortcombo.setVisible(false);    

    
            addStudentButton.setVisible(true);
            addprog.setVisible(false);
            addcollege.setVisible(false);
        
            delete.setVisible(true); 
            deleteprog.setVisible(false); 
        deletecollege.setVisible(false); 
        
            modifystudent.setVisible(true);
            modifyprogram.setVisible(false);
        modifycollege.setVisible(false);
        filtertable();
        }
        if (e.getSource() == probgButton) {
            tableScrollPane.setVisible(false);
            progpane.setVisible(true);
            comboBox.setVisible(false);
            collegepane.setVisible(false);
            progsortcombo.setVisible(true);
            searchbar.setText("");
            collsortcombo.setVisible(false);

            addStudentButton.setVisible(false);
            addprog.setVisible(true);
            addcollege.setVisible(false);
        
            delete.setVisible(false);
            deleteprog.setVisible(true);
        deletecollege.setVisible(false); 
        
            modifystudent.setVisible(false);
            modifyprogram.setVisible(true);
        modifycollege.setVisible(false);
        filterprogram();

       
        }
        if (e.getSource() == collegebutton) {
            comboBox.setVisible(false);
            tableScrollPane.setVisible(false);
            progpane.setVisible(false);
            collegepane.setVisible(true);
            progsortcombo.setVisible(false);
            searchbar.setText("");
            collsortcombo.setVisible(true);

            addStudentButton.setVisible(false);
            addprog.setVisible(false);
            addcollege.setVisible(true);
        
            delete.setVisible(false); 
            deleteprog.setVisible(false); 
        deletecollege.setVisible(true); 
        
            modifystudent.setVisible(false);
            modifyprogram.setVisible(false);
        modifycollege.setVisible(true);
        filtercollege();
        }
        if (e.getSource() == progsortcombo) {
            String selectedprog = (String) progsortcombo.getSelectedItem();
            switch (selectedprog) {
                case "Sort by Program Code":
                    progsorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
                    break;
                case "Sort by Program Name":
                    progsorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                    break;
                case "Sort by College Code":
                    progsorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(2, SortOrder.ASCENDING)));
                    break;
                case "Sort by:":
                    progsorter.setSortKeys(null);
                    break;
            }
        }
        
        if (e.getSource() == comboBox) {
            String selected = (String) comboBox.getSelectedItem();
            switch (selected) {
                case "Sort by ID":
                    sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
                    break;
                case "Sort by First Name":
                    sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                    break;
                case "Sort by Last Name":
                    sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(2, SortOrder.ASCENDING)));
                    break;
                case "Sort by Year Level":
                    sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(3, SortOrder.ASCENDING)));
                    break;
                case "Sort by Gender":
                    sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(4, SortOrder.ASCENDING)));
                    break;
                case "Sort by Program Code":
                    sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(5, SortOrder.ASCENDING)));
                    break;
                case "Sort by:":
                    sorter.setSortKeys(null);
                    break;
            }
        }
        if (e.getSource() == collsortcombo) {
            String selectedcoll = (String) collsortcombo.getSelectedItem();
            switch (selectedcoll) {
                case "Sort by College Code":
                    collsorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
                    break;
                case "Sort by College Name":
                    collsorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                    break;
                case "Sort by:":
                    collsorter.setSortKeys(null);
                    break;
            }
        }
        if (e.getSource() == addcollege) {
            classcollege = new addcollegeGUI(this, writer);
        }
        if (e.getSource() == addStudentButton) {
            classstudent = new addstudentGUI(this, writer);
        }
        if (e.getSource() == addprog) {
          
            classprogram = new addprogGUI(this, writer);
        }
        if (e.getSource() == delete) {
            deletestudent = new removestud(this, deleter);
        }
        if (e.getSource() == deleteprog) {
            deleteprogram = new removeprogram(this, deleter);
        }
        if (e.getSource() == deletecollege) {
            deletecoll = new removecollege(this, deleter);}
        }

        public addprogGUI getAddProgramInstance() {
            return classprogram; 
        }
    
        public JComboBox<String> getProgramDropdown() {
            if (classprogram == null) {
                System.out.println("Error: classprogram is null.");
                return new JComboBox<>(); 
            }
            return classprogram.getProgramCodeCombo();
        }
        public addcollegeGUI collegeaddcollegeGUI(){
            return classcollege;
        }

      

        
        public addstudentGUI getAddStudentInstance(){
            return classstudent;
        }
        
       
        
        

    public DefaultTableModel getstudentModel() {
        return model;
    }

    public DefaultTableModel getcollegeModel() {
        return collegemodel;
    }

    public DefaultTableModel getprogramModel() {
        return programmodel;
    }

    public JTable getStudentTable (){
        return table;
    }

    public JTable getprogramTable(){
        return programTable;
    }
    public JTable getcollegeTable(){
        return collegetable;
    }
  

    
    
    private void filtertable(){
        String query = searchbar.getText().trim();
        if (query.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    private void filterprogram(){
        String query = searchbar.getText().trim();
        if (query.isEmpty()) {
            progsorter.setRowFilter(null);
        } else {
            progsorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }

    private void filtercollege() {
        String query = searchbar.getText().trim();
        if (query.isEmpty()) {
            collsorter.setRowFilter(null);
        } else {
            collsorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
    }
   
    
} 