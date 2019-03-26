package com.example.demo;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class JobProductSpecification implements Initializable {
    // h2 db
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test"; // for test

    //url--> jdbc:h2:mem:testdb - build test

    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    // to hold our return scene
    private Scene scene;

    @FXML
    private Button product_specBtnHome, selectJobBtn, saveCompBtn, previewCompBtn;

    @FXML private Text errorMessage, successMessage, taxText, totalText;

    // jobs table
    @FXML private TableView<JobsPreStage> jobsTable;

    @FXML private TableColumn<JobsPreStage, String> jobNameCol;
    @FXML private TableColumn<JobsPreStage, String> jobDescCol;
    @FXML private TableColumn<JobsPreStage, String> stageCol;
    @FXML private TableColumn<JobsPreStage, String> customerNameCol;
    @FXML private TableColumn<JobsPreStage, String> customerPhoneCol;

    @FXML private Text selectedJobTxt, selectedCompsText;
    @FXML private ListView<String> componentsListView;


    List<AvailableComponents> componentsListCopy = new ArrayList<>();

    public void setReturnScene(Scene scene){
        this.scene = scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // populate jobs table
        // show the table with jobs in pre-prod stage
        getPreStageJobTable();

        // button on action
        product_specBtnHome.setOnAction(value ->  {
            //System.out.println("Need to go back to Main Screen");
            takeMeHome(value);
        });


        // select button
        selectJobBtn.setOnAction(value -> {
            if (!jobsTable.getSelectionModel().isEmpty()) {
                String selectedJob = jobsTable.getSelectionModel().getSelectedItem().getJobName();

                // show selected job name
                selectedJobTxt.setText(selectedJob);

                //logs
                System.out.println("Selected: " + selectedJob);

                // fetch components
                fetchAvailableComponents();
               componentsListView.setVisible(true);

            } else {
                // here no item is selected from table
                componentsListView.setVisible(false);
                selectedJobTxt.setText("NONE");
                System.out.println("No item is Selected from Table");
            }

        });

        // add component(s) button
        saveCompBtn.setOnAction(value -> {
            // save selected component(s) to job product
            saveJob();

        });

        // preview button
        previewCompBtn.setOnAction(value -> {
            showSelectedCompInfo();
        });






    }

    // method to go back to main screen
    private void takeMeHome(ActionEvent actionEvent) {

        if(scene == null) {
            product_specBtnHome.getScene().getWindow().hide();

        } else {
            ((Stage)product_specBtnHome.getScene().getWindow()).setScene(scene);
            //((Stage)homeBtn.getScene().getWindow()).setScene(scene);

        }
    }

    private void getPreStageJobTable() {
        List<JobsPreStage> jobsData = new ArrayList<>();

        // get data from h2 db
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "SELECT CUSTOMER_NAME, CUSTOMER_ADDRESS, CUSTOMER_PHONE, JOB_NAME, JOB_DESCRIPTION, JOB_STAGE FROM JOB_TABLE";
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 4: Extract data from result set
            while(rs.next()) {
                // Retrieve by column name

                String jobName = rs.getString("JOB_NAME");
                String jobDescription = rs.getString("JOB_DESCRIPTION");
                String jobStage = rs.getString("JOB_STAGE");
                String customerName = rs.getString("CUSTOMER_NAME");
                String customerPhone = rs.getString("CUSTOMER_PHONE");
                String customerAddress = rs.getString("CUSTOMER_ADDRESS");

                // add to list
                JobsPreStage jobsList = new JobsPreStage(jobName, jobDescription, jobStage,
                        customerName, customerPhone, customerAddress);

                jobsData.add(jobsList);

            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            System.out.println(se.getMessage());

        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
                se2.printStackTrace();
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try


        //need to load employee info/populate table
        jobNameCol.setCellValueFactory(new PropertyValueFactory<JobsPreStage, String>("jobName"));
        jobDescCol.setCellValueFactory(new PropertyValueFactory<JobsPreStage, String>("jobDescription"));
        stageCol.setCellValueFactory(new PropertyValueFactory<JobsPreStage, String>("jobStage"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<JobsPreStage, String>("customerName"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<JobsPreStage, String>("customerPhone"));

        jobsTable.getItems().setAll(jobsData);
    }

    // show selected component details
    private void showSelectedCompInfo() {
        List<String> comps = new ArrayList<>();
        // show whenever there is change
        /*componentsListView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        // do something with the values new and old
                        System.out.println("New value: " + newValue);
                    }
                });*/

        String message = "";
        List<Integer> pricesList = new ArrayList<>();


        //String string = "004-034556";
        //String[] parts = string.split("-");
        //String part1 = parts[0]; // 004
        //String part2 = parts[1]; // 034556

        ObservableList<String> selectedComponentList;
        selectedComponentList = componentsListView.getSelectionModel().getSelectedItems();

        for (String selected: selectedComponentList) {
            message += selected + "\n";

            String[] parts = selected.split("=");
            String part1 = parts[0];
            String part2 = parts[1];


            pricesList.add(Integer.valueOf(part2.stripLeading()));
            //System.out.println("Hhfef" + part2.stripLeading());

            comps.add(selected);

        }


        selectedCompsText.setText(message);

        System.out.println(message);

        // calculate prices
        int componentsTotal = 0;


        for (int k = 0; k < pricesList.size(); k++) {
            componentsTotal += pricesList.get(k);
            System.out.println(pricesList.get(k));

        }


        // mark-up 10%
        double markUP = ((10.0/100) * componentsTotal);
        double totalBeforeTaxes = componentsTotal + markUP;

        // 3.4%
        double taxes = (3.4/100) * totalBeforeTaxes;

        double finalTotalPrice = totalBeforeTaxes + taxes;

        // show to user
        taxText.setText(String.valueOf(taxes));
        totalText.setText(String.valueOf(finalTotalPrice));

        // logs
        System.out.println("Comps Total = " + String.valueOf(componentsTotal));
        System.out.println("MarkUp = " + String.valueOf(markUP));
        System.out.println("Taxes = " + String.valueOf(taxes));
        System.out.println("Final Price = " + String.valueOf(finalTotalPrice));

    }

    private void fetchAvailableComponents() {
        // method to get the available components in our db

        List<AvailableComponents> componentsList = new ArrayList<>();

        // get data from h2 db
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "SELECT COMPONENT_NAME, COMPONENT_PRICE, COMPONENT_DESCRIPTION FROM PRODUCT_COMPONENTS";
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 4: Extract data from result set
            while(rs.next()) {
                // Retrieve by column name

                String component_name = rs.getString("COMPONENT_NAME");
                int component_price = rs.getInt("COMPONENT_PRICE");
                String component_description = rs.getString("COMPONENT_DESCRIPTION");


                // add to list
                AvailableComponents availableComponents = new AvailableComponents(component_name,
                        component_price, component_description);

                componentsList.add(availableComponents);
                componentsListCopy.add(availableComponents);

            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            System.out.println(se.getMessage());

        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
                se2.printStackTrace();
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try


        //need to load components info/populate table
        //compNameCol.setCellValueFactory(new PropertyValueFactory<>("componentName"));
        //compPriceCol.setCellValueFactory(new PropertyValueFactory<>("componentPrice"));

        //compBreakDownTable.getItems().setAll(componentsList);

        // components list -- we only show component name in list
        for (int i = 0; i < componentsList.size(); i++) {
            componentsListView.getItems().addAll(componentsList.get(i).getComponentName() + " = " +
            componentsList.get(i).getComponentPrice());
        }

        componentsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    // finalize the job
    private void saveJob() {

        //System.out.println("Add component(s) to job and save to db!");

        String message = "";
        String saveComps = "";
        List<Integer> pricesList = new ArrayList<>();

        ObservableList<String> selectedComponentList;
        selectedComponentList = componentsListView.getSelectionModel().getSelectedItems();

        for (String selected: selectedComponentList) {
            message += selected + "\n";
            saveComps += selected + " + ";

            String[] parts = selected.split("=");
            String part1 = parts[0];
            String part2 = parts[1];

            pricesList.add(Integer.valueOf(part2.stripLeading()));

        }

        selectedCompsText.setText(message);

        System.out.println(message);
        System.out.println("how comps db save: " + saveComps);

        // calculate prices
        int componentsTotal = 0;


        for (int k = 0; k < pricesList.size(); k++) {
            componentsTotal += pricesList.get(k);
            System.out.println(pricesList.get(k));

        }


        // mark-up 10%
        double markUP = ((10.0/100) * componentsTotal);
        double totalBeforeTaxes = componentsTotal + markUP;

        // 3.4%
        double taxes = (3.4/100) * totalBeforeTaxes;

        double finalTotalPrice = totalBeforeTaxes + taxes;

        // show to user
        taxText.setText(String.valueOf(taxes));
        totalText.setText(String.valueOf(finalTotalPrice));

        int finalPriceInt = (int) Math.round(finalTotalPrice);

        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement = null;

        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 3: Execute a query
            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS JOB_COMPONENTS " +
                    "(JOB_NAME VARCHAR(255) not NULL, " +
                    " JOB_PRICE INTEGER not NULL, " +
                    " COMPONENTS VARCHAR(255) not NULL, " +
                    " PRIMARY KEY ( JOB_NAME ))";
            stmt.executeUpdate(sql);
            System.out.println("Created Job Component table in our database...");

            // save component to table
            String query = "INSERT INTO JOB_COMPONENTS(JOB_NAME,JOB_PRICE," +
                    "COMPONENTS) VALUES(?,?,?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, selectedJobTxt.getText());
            statement.setInt(2, finalPriceInt);
            statement.setString(3, saveComps);
            statement.executeUpdate();

            System.out.println("Success!!!! Created Job Product Component..");
            // show success message to user
            errorMessage.setText("");
            successMessage.setText("Successfully created a Job Product Component!!");
            // clear screen if successful
            clearScreenData();

            // STEP 4: Clean-up environment
            stmt.close();
            statement.close();
            conn.close();

        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
            System.out.println(se.getMessage());
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
                if (statement != null) statement.close();
            } catch(SQLException se3) {
                se3.printStackTrace();
            }
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        } //end try
    }

    private void clearScreenData() {
        System.out.println("You can do whatever you want! after successfully saving the job component");
    }

}
