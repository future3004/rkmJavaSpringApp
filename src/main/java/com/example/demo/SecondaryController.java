package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class SecondaryController implements Initializable {
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
    private Button btnHome; // main screen

    @FXML
    private Button homeBtn; // for the add, delete, edit employee screens

    // Employee Manager tableView
    @FXML
    public TableView<Employee> employeeTable;
    @FXML
    public TableColumn<Employee, String> idCol;
    @FXML
    public TableColumn<Employee, String> firstNameCol;
    @FXML
    public TableColumn<Employee, String> lastNameCol;
    @FXML
    public TableColumn<Employee, String> emailCol;
    @FXML
    public TableColumn<Employee, String> phoneCol;

    // employee screen/window
    @FXML
    private Button homeButton; // for main employee screen
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    //add employee window
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField phoneNum;
    @FXML
    private TextField employeeID;

    @FXML
    private Button addBtn;
    @FXML
    private Button cancelBtn;

    // edit employee window
    @FXML private TextField email_edit;
    @FXML private Button editSubmitBtn;
    @FXML
    private Text emp_FName;
    @FXML
    private Text emp_LName;
    @FXML
    private Text emp_email;
    @FXML
    private Text emp_phone;
    @FXML private TextField edit_firstName;
    @FXML private TextField edit_lastName;
    @FXML private TextField edit_emailField;
    @FXML private TextField edit_phoneField;
    @FXML private Button editBtn;

    // delete screen
    @FXML private TextField del_email;
    @FXML private Text del_id;
    @FXML private Text del_fName;
    @FXML private Text del_LName;
    @FXML private Text deleteEmailText;
    @FXML private Text deletePhoneText;
    @FXML private Button deleteBtn;


    @Autowired
    ConfigurableApplicationContext applicationContext;

    public void setReturnScene(Scene scene){
        this.scene = scene;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{

        List<Employee> data = new ArrayList<>();

        // get data from h2 db
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");
            stmt = conn.createStatement();
            String sql = "SELECT ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER FROM EMPLOYEE_TEST3";
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 4: Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int employee_db_id  = rs.getInt("ID");
                String id = String.valueOf(employee_db_id);
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                String email = rs.getString("EMAIL");
                String phoneNumber = rs.getString("PHONE_NUMBER");

                // Display values
                //System.out.print("ID: " + id);
               // System.out.print(", Age: " + age);
               // System.out.print(", First: " + first);
                //System.out.println(", Last: " + last);

                // add to list<employee class>
                Employee employee = new Employee(id, firstName, lastName, email, phoneNumber);

                data.add(employee);

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
        System.out.println("Goodbye, Richard KM!");

        //need to load employee info/populate table
        idCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeId"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("phoneNumber"));

        employeeTable.getItems().setAll(data);

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

  /*  private List<Employee> employeeTableData() {
        return null;
    }*/

    public void takeMeHome(ActionEvent actionEvent) {

        if(scene == null) {
            btnHome.getScene().getWindow().hide();
            homeBtn.getScene().getWindow().hide();
        } else {
            ((Stage)btnHome.getScene().getWindow()).setScene(scene);
            //((Stage)homeBtn.getScene().getWindow()).setScene(scene);

        }
    }

    // employee screen/window
    public void backHome(ActionEvent actionEvent) {

        if(scene == null) {
            homeButton.getScene().getWindow().hide();
        } else {
            ((Stage)homeButton.getScene().getWindow()).setScene(scene);
        }
    }

    public void addEmployee(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/test.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void editEmployee(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/test2.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void deleteEmployee(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/test3.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // back to main employee Manager
    public void backToEmpManager(ActionEvent actionEvent) {
        homeBtn.getScene().getWindow().hide();
    }

    // add employee screen
    public void employeeScreenAdd(ActionEvent actionEvent) {
        String first_name;
        String last_name;
        String employee_email;
        String phone_number;
        int employee_id;

        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || email.getText().isEmpty()
                || employeeID.getText().isEmpty() || phoneNum.getText().isEmpty()) {
            // textFields are empty
            System.out.println("Some textFields are empty");
        } else {

                // proceed

            first_name = firstName.getText();
            last_name = lastName.getText();
            employee_email = email.getText();
            phone_number = phoneNum.getText();
            employee_id = Integer.parseInt(employeeID.getText());


            // save new employee to our h2 db
            Connection conn = null;
            PreparedStatement statement = null;

            try {
                // STEP 1: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //STEP 2: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);


                //STEP 3: Execute a query

                String query = "INSERT INTO EMPLOYEE_TEST3(ID,FIRST_NAME,LAST_NAME,EMAIL,PHONE_NUMBER) VALUES(?,?,?,?,?)";
                statement = conn.prepareStatement(query);
                statement.setInt(1, employee_id);
                statement.setString(2, first_name);
                statement.setString(3, last_name);
                statement.setString(4, employee_email);
                statement.setString(5, phone_number);
                statement.executeUpdate();

                System.out.println("Success!!!! Added New Employee..");

                // STEP 4: Clean-up environment
                //stmt.close();
                statement.close();
                conn.close();
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (statement != null) statement.close();
                } catch (SQLException se2) {
                    se2.printStackTrace();
                } // nothing we can do
                try {
                    if (conn != null) conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                } //end finally try
            } //end try


            // console log
       /* System.out.println("Add Employee Button selected \n\n");
        System.out.println("f_name: " + first_name + " \n");
        System.out.println("last_name: " + last_name + " \n");
        System.out.println("Empl_email: " + employee_email + " \n");
        System.out.println("phone: " + phone_number + " \n");
        System.out.println("ID: " + String.valueOf(employee_id) + " \n");*/

        }
    }

    public void employeeScreenCancel(ActionEvent actionEvent) {
        firstName.clear();
        lastName.clear();
        email.clear();
        phoneNum.clear();
        employeeID.clear();

        System.out.println("Cancel Button selected");
    }

    // edit employee window
    public void fetchEmployeeInfo(ActionEvent actionEvent) {
        if (email_edit.getText().isEmpty()) {
            // no email provided to fetch employee data
            System.out.println("Employee Email is required");
        } else {
            // proceed to fetch employee data

            String email = email_edit.getText();

             Connection conn = null;
             Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // STEP 3: Execute a query
            System.out.println("Connected database successfully...");

            stmt = conn.createStatement();
            String query="SELECT * FROM EMPLOYEE_TEST3 WHERE EMAIL='" + email +"'";

            ResultSet rs = stmt.executeQuery(query);

            // STEP 4: Extract data from result set
            while(rs.next()) {
                // Retrieve by column name
                int id  = rs.getInt("ID");
                String saved_id = String.valueOf(id);
                String fName = rs.getString("FIRST_NAME");
                String lName = rs.getString("LAST_NAME");
                String savedDb_email = rs.getString("EMAIL");
                String phoneNum = rs.getString("PHONE_NUMBER");

                // store/show values
                Employee employeeData = new Employee(saved_id, fName, lName, savedDb_email, phoneNum);
                emp_FName.setText(employeeData.getFirstName());
                emp_LName.setText(employeeData.getLastName());
                emp_email.setText(employeeData.getEmail());
                emp_phone.setText(employeeData.getPhoneNumber());
            }
            // STEP 5: Clean-up environment
            rs.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if(stmt !=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            } // end finally try
        } // end try
        }
    }

    public void editEmployeeInfo(ActionEvent actionEvent) {
       if (edit_firstName.getText().isEmpty() && edit_lastName.getText().isEmpty()
                && edit_emailField.getText().isEmpty() && edit_phoneField.getText().isEmpty()) {
            // pressed edit button but all fields are empty
            System.out.println("No update info provided");
        } else {
           String updateFName;
           String updateLName;
           String updateEmail;
           String updatePhone;

           String empty_error;
           String email = email_edit.getText();

           updateFName = edit_firstName.getText();
           updateLName = edit_lastName.getText();
           updateEmail = edit_emailField.getText();
           updatePhone = edit_phoneField.getText();

           if (updateFName.matches("")) {
               System.out.println("first name empty field");
           } else if (updateEmail.matches("")) {
               System.out.println("email empty field");
           } else if (updatePhone.matches("")) {
               System.out.println("phone empty field");

           } else if (updateLName.matches("")) {
               System.out.println("last name empty field");
           } else {
               System.out.println(updateEmail + updateFName + updateLName + updatePhone);
           }

           Connection conn = null;
           Statement stmt = null;
           PreparedStatement statement = null;
           PreparedStatement statement2 = null;
           PreparedStatement statement3 = null;
           PreparedStatement statement4 = null;

           try {
               // STEP 1: Register JDBC driver
               Class.forName(JDBC_DRIVER);

               // STEP 2: Open a connection
               System.out.println("Connecting to a database...");
               conn = DriverManager.getConnection(DB_URL, USER, PASS);

               // STEP 3: Execute a query
               System.out.println("Connected database successfully...");
               //stmt = conn.createStatement();


               String query = "UPDATE EMPLOYEE_TEST3 SET FIRST_NAME ='" + updateFName + "' WHERE  EMAIL ='" + emp_email + "'";
               String query2 = "UPDATE EMPLOYEE_TEST3 SET LAST_NAME ='" + updateLName + "' WHERE  EMAIL ='" + emp_email + "'";
               String query3 = "UPDATE EMPLOYEE_TEST3 SET EMAIL ='" + updateEmail + "' WHERE  EMAIL ='" + emp_email + "'";
               String query4 = "UPDATE EMPLOYEE_TEST3 SET PHONE_NUMBER ='" + updateEmail + "' WHERE  EMAIL ='" + emp_email + "'";

               statement = conn.prepareStatement(query);
               statement2 = conn.prepareStatement(query2);
               statement3 = conn.prepareStatement(query3);
               statement4 = conn.prepareStatement(query4);

               statement.executeUpdate();
               statement2.executeUpdate();
               statement3.executeUpdate();
               statement4.executeUpdate();

               // Now you can extract all the records
               // to see the updated records
               stmt = conn.createStatement();
               String sql = "SELECT * FROM EMPLOYEE_TEST3 WHERE EMAIL='" + emp_email + "'";

               ResultSet rs = stmt.executeQuery(sql);

               // STEP 4: Extract data from result set
               while (rs.next()) {
                   // Retrieve by column name
                   int id = rs.getInt("ID");
                   String saved_id = String.valueOf(id);
                   String fName = rs.getString("FIRST_NAME");
                   String lName = rs.getString("LAST_NAME");
                   String savedDb_email = rs.getString("EMAIL");
                   String phoneNum = rs.getString("PHONE_NUMBER");

                   // store/show values
                   Employee employeeData = new Employee(saved_id, fName, lName, savedDb_email, phoneNum);
                   emp_FName.setText(employeeData.getFirstName());
                   emp_LName.setText(employeeData.getLastName());
                   emp_email.setText(employeeData.getEmail());
                   emp_phone.setText(employeeData.getPhoneNumber());
               }
               // STEP 5: Clean-up environment
               rs.close();
           } catch (SQLException se) {
               // Handle errors for JDBC
               se.printStackTrace();
           } catch (Exception e) {
               // Handle errors for Class.forName
               e.printStackTrace();
           } finally {
               // finally block used to close resources
               try {
                   if (stmt != null) stmt.close();
                   if(statement!=null) statement.close();
                   if(statement2!=null) statement2.close();
                   if(statement3!=null) statement3.close();
                   if(statement4!=null) statement4.close();
               } catch (SQLException se2) {
               } // nothing we can do
               try {
                   if (conn != null) conn.close();
               } catch (SQLException se) {
                   se.printStackTrace();
               } // end finally try

           }
       }
    }

    public void editEmployeeCancel(ActionEvent actionEvent) {
        // clear screen

        email_edit.clear();
        emp_FName.setText("");
        emp_LName.setText("");
        emp_email.setText("");
        emp_phone.setText("");
        edit_firstName.clear();
        edit_lastName.clear();
        edit_emailField.clear();
        edit_phoneField.clear();

    }

    // delete employee window
    public void deleteSubmit(ActionEvent actionEvent) {
        // get employee email for delete

    }

    public void performDelete(ActionEvent actionEvent) {
        // get employee email for delete

        String employeeEmail = del_email.getText();

        if (!employeeEmail.matches("")) {
            // have email, delete employee

            Connection conn = null;
            PreparedStatement statement = null;
            try {
                // STEP 1: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                // STEP 2: Open a connection
                System.out.println("Connecting to a database...");
                conn = DriverManager.getConnection(DB_URL,USER,PASS);

                // STEP 3: Execute a query
                System.out.println("Connected database successfully...");

                String query="DELETE FROM EMPLOYEE_TEST3 WHERE EMAIL='" + employeeEmail +"'";
                statement= conn.prepareStatement(query);
                statement.executeUpdate();

                System.out.println("Employee Deleted successfully...");
            } catch(SQLException se) {
                // Handle errors for JDBC
                se.printStackTrace();
            } catch(Exception e) {
                // Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                // finally block used to close resources
                try {
                    if(statement!=null) statement.close();
                } catch(SQLException se2) {
                } // nothing we can do
                try {
                    if(conn!=null) conn.close();
                } catch(SQLException se) {
                    se.printStackTrace();
                } // end finally try
            } // end try
        }
    }

    public void deleteCancel(ActionEvent actionEvent) {
        // get employee email for delete
        // close window

        deleteBtn.getScene().getWindow().hide();
    }



}
