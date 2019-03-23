package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

@Component
public class JobCreationController implements Initializable {

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    // to hold our return scene
    private Scene scene;

    @FXML
    private Button btnHome;

    @FXML
    private Button submitJobBtn;

    @FXML private Text errorMessage, successMessage;

    @FXML private TextField customerName;
    @FXML private TextField customerPhone;
    @FXML private TextField customerAddress;
    @FXML private TextField jobNameField;

    @FXML private TextArea jobDescField;

    @FXML private RadioButton preStage;

    private String custName = "";
    private String custPhone = "";
    private String custAddress = "";
    private String job_name = "";
    private String job_description = "";
    private String jobStage_initial = "Pre-Production";

    public void setReturnScene(Scene scene){
        this.scene = scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // disable radio button since job is always going to
        // be in pre-production stage
        preStage.setDisable(true);

        // button on action
        btnHome.setOnAction(value ->  {
            //System.out.println("Need to go back to Main Screen");
            takeMeHome(value);
        });

        submitJobBtn.setOnAction(event -> {
            System.out.println("Verify Info before submitting to our db");

            // function/method call
            developmentTest();
        });
    }

    private void developmentTest() {

        // get input data
        custName = customerName.getText();
        custPhone = customerPhone.getText();
        custAddress = customerAddress.getText();
        job_name = jobNameField.getText();
        job_description = jobDescField.getText();

        // verify info, no empty fields/data
        if (custName.matches("") || custPhone.matches("") ||
                custAddress.matches("") || job_name.matches("") ||
                job_description.matches("")) {
            // have at least one empty field/ data
            // can't proceed to save data
            // show error to user
            System.out.println("Error, Empty field(s). All fields are required.");
            successMessage.setText("");
            errorMessage.setText("Error, Empty field(s). All fields are required.");

        } else if (!custName.matches("") && !custPhone.matches("") &&
                !custAddress.matches("") && !job_name.matches("") &&
                !job_description.matches("")) {
            // have all the fields filled
            // here we can proceed

            // logs the data
            System.out.println(" Customer NAme: " + custName);
            System.out.println(" Customer Phone: " + custPhone);
            System.out.println(" Customer Address: " + custAddress);
            System.out.println(" Job Name: " + job_name);
            System.out.println(" Job Desc: " + job_description);

            // method call
            saveJob();
        }

    }

    // method to go back to main screen
    private void takeMeHome(ActionEvent actionEvent) {

        if(scene == null) {
            btnHome.getScene().getWindow().hide();

        } else {
            ((Stage)btnHome.getScene().getWindow()).setScene(scene);
            //((Stage)homeBtn.getScene().getWindow()).setScene(scene);

        }
    }

    private void saveJob() {

        Connection conn = null;
        Statement stmt = null;
        PreparedStatement statement = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 3: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS JOB_TABLE " +
                    "(CUSTOMER_NAME VARCHAR(255) not NULL, " +
                    " CUSTOMER_ADDRESS VARCHAR(255) not NULL, " +
                    " CUSTOMER_PHONE VARCHAR (255) not NULL," +
                    " JOB_NAME VARCHAR(255) not NULL, " +
                    " JOB_DESCRIPTION VARCHAR(255)," +
                    " JOB_STAGE VARCHAR(255) not NULL," +
                    " PRIMARY KEY ( CUSTOMER_NAME ))";
            stmt.executeUpdate(sql);
            System.out.println("Created Job table in given database...");

            // save job data to table
            String query = "INSERT INTO JOB_TABLE(CUSTOMER_NAME,CUSTOMER_ADDRESS,CUSTOMER_PHONE," +
                    "JOB_NAME,JOB_DESCRIPTION,JOB_STAGE) VALUES(?,?,?,?,?,?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, custName);
            statement.setString(2, custAddress);
            statement.setString(3, custPhone);
            statement.setString(4, job_name);
            statement.setString(5, job_description);
            statement.setString(6, jobStage_initial);
            statement.executeUpdate();

            System.out.println("Success!!!! Created New Job..");
            // show success message to user
            errorMessage.setText("");
            successMessage.setText("Successfully created a Job!!");
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
            } catch(SQLException se2) {
                se2.printStackTrace();
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        } //end try
        
    }

    private void clearScreenData() {

        customerName.clear();
        customerPhone.clear();
        customerAddress.clear();
        jobNameField.clear();

        jobDescField.clear();

    }


}
