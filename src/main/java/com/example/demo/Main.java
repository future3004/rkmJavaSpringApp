package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@ComponentScan({"com.example.demo"})
public class Main extends Application {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";

    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";


    private ConfigurableApplicationContext springContext;
    private Parent root;

   // @Autowired
    private EmployeeRepository employeeRepository;

    public static void main(String[] args) {
        launch();
    }

    /**
     * The application initialization method. This method is called immediately
     * after the Application class is loaded and constructed. An application may
     * override this method to perform initialization prior to the actual starting
     * of the application.
     *
     * <p>
     * The implementation of this method provided by the Application class does nothing.
     * </p>
     *
     * <p>
     * NOTE: This method is not called on the JavaFX Application Thread. An
     * application must not construct a Scene or a Stage in this
     * method.
     * An application may construct other JavaFX objects in this method.
     * </p>
     *
     * @throws Exception if something goes wrong
     */
    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Main.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();
        super.init();

        // Add this if you need this class autowired
        springContext.getAutowireCapableBeanFactory().autowireBean(this);


    }


    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //URL url = new File("src/main/resources/fxml/main.fxml").toURL();
        //Parent root = FXMLLoader.load(url);

        //springContext = SpringApplication.run(Main.class);
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("../../../../resources/fxml/main.fxml"));
        //fxmlLoader.setControllerFactory(springContext::getBean);

        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));


        // Add this if you need this class autowired
        //springContext.getAutowireCapableBeanFactory().autowireBean(this);

        /*Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //(ID INTEGER  GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),

            //STEP 3: Execute a query
            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql =  "CREATE TABLE EMPLOYEE_TEST3 " +
                    "(ID INTEGER not NULL, " +
                    " FIRST_NAME VARCHAR(255) not NULL, " +
                    " LAST_NAME VARCHAR(255) not NULL, " +
                    " EMAIL VARCHAR(255) not NULL, " +
                    " PHONE_NUMBER VARCHAR (255)," +
                    " PRIMARY KEY ( EMAIL ))";
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            stmt = conn.createStatement();
            String sql2 = "INSERT INTO EMPLOYEE_TEST3 " + "VALUES (1234,'Jane', 'Ali', 'ali@test.com', '234-567-9786')";

            stmt.executeUpdate(sql2);
            sql = "INSERT INTO EMPLOYEE_TEST3 " + "VALUES (456, 'Mahnaz', 'Fatma', 'fatma@acn.com', '101-567-9700')";

            stmt.executeUpdate(sql);
            sql = "INSERT INTO EMPLOYEE_TEST3 " + "VALUES (5678, 'Zaid', 'Khan', 'ali@som.com', '2345679786')";

            stmt.executeUpdate(sql);
            sql = "INSERT INTO EMPLOYEE_TEST3 " + "VALUES(567, 'Sumit', 'Mittal', 'mittal@test.com', '2346797868')";

            stmt.executeUpdate(sql);

            sql = "INSERT INTO EMPLOYEE_TEST3 " + "VALUES(001, 'Akdk', 'Gdhd', 'mt@test2.com', '')";

            stmt.executeUpdate(sql);

            System.out.println("Inserted records into the table...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        } //end try
        System.out.println("Goodbye!");*/

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
