package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

@Component
public class AddProductCompController implements Initializable {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";


    // to hold our return scene
    private Scene scene;

    @FXML private Text compError, compSuccess;

    @FXML
    private Button add_prodCompBtnHome, addCompBtn;

    @FXML private TextField compNameField;
    @FXML private TextField compPriceField;

    @FXML private TextArea compDescField;

    public void setReturnScene(Scene scene){
        this.scene = scene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // text format for the price field
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        compPriceField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));

        //add component button
        addCompBtn.setOnAction(value -> {

            // method call
                addComponent();

        });


        // go back to main screen from add product component screen
        add_prodCompBtnHome.setOnAction(value -> {
            if(scene == null) {
                add_prodCompBtnHome.getScene().getWindow().hide();

            } else {
                ((Stage)add_prodCompBtnHome.getScene().getWindow()).setScene(scene);
                //((Stage)homeBtn.getScene().getWindow()).setScene(scene);

            }
        });

        // logs

    }

    private void addComponent() {
        String compName = compNameField.getText();
        int compPrice;
        String compDescription = compDescField.getText();

        if (compPriceField.getText().matches("")) {
            compPrice = 0;
        } else {
            compPrice = Integer.valueOf(compPriceField.getText());
        }


        // validate input text before proceeding
        if (compPriceField.getText().matches("") || compName.matches("")
                || compDescription.matches("")) {
            //System.out.println(String.valueOf(compPriceField.getText()));
            compSuccess.setText("");
            compError.setText("Error, Missing Information: empty field(s)");
        } else {
            //compError.setText("");
            //compSuccess.setText("Success! Have all component info");
            System.out.println("comp Name: " + compName);
            System.out.println("Comp Price = " + String.valueOf(compPrice));
            System.out.println("Descrp: " + compDescription);

            // proceed to save to our db
            saveComponentToDb(compName, compPrice, compDescription);
        }
    }

    private void saveComponentToDb(String componentName, int componentPrice,
                                   String componentDescp) {
        // save component to our h2 db

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
            stmt = conn.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS PRODUCT_COMPONENTS " +
                    "(COMPONENT_NAME VARCHAR(255) not NULL, " +
                    " COMPONENT_PRICE INTEGER not NULL, " +
                    " COMPONENT_DESCRIPTION VARCHAR(255) not NULL, " +
                    " PRIMARY KEY ( COMPONENT_NAME ))";
            stmt.executeUpdate(sql);
            System.out.println("Created Product Component table in our database...");

            // save component to table
            String query = "INSERT INTO PRODUCT_COMPONENTS(COMPONENT_NAME,COMPONENT_PRICE," +
                    "COMPONENT_DESCRIPTION) VALUES(?,?,?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, componentName);
            statement.setInt(2, componentPrice);
            statement.setString(3, componentDescp);
            statement.executeUpdate();

            System.out.println("Success!!!! Created New Product Component..");
            // show success message to user
            compError.setText("");
            compSuccess.setText("Successfully created a Product Component!!");
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
        compNameField.clear();
        compPriceField.clear();
        compDescField.clear();
    }
}
