package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class MainController{


    @FXML
    public Button btnLaunchJob;

    @FXML
    public Button btnChangeScene;

    @FXML public Button uc3Btn, productCompBtn;
    @FXML public Button uc4Btn;
    @FXML public Button uc5Btn;
    @FXML private Button uc6Btn;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    // code to get another window/stage from home screen
   /* public void launchWindow(ActionEvent actionEvent) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/jobEntry.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }*/


    //METHOD to go to employee manager screen
    public void employeeManagerScene(ActionEvent actionEvent) throws IOException {
        Stage parent  = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/change.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);


        Scene scene = new Scene(fxmlLoader.load());
        SecondaryController secondaryController = fxmlLoader.getController();
        secondaryController.setReturnScene(btnChangeScene.getScene());
        parent.setScene(scene);

    }

    // takes you to new job creation scene
    public void goToJobEntry(ActionEvent event) throws IOException{
        //System.out.println("To New Job Creation screen..");

        Stage parent  = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/jobEntry.fxml"));

        //fxmlLoader.setControllerFactory(applicationContext::getBean);

        Scene scene = new Scene(fxmlLoader.load());
        JobCreationController jobController = fxmlLoader.getController();
        jobController.setReturnScene(btnLaunchJob.getScene());
        parent.setScene(scene);

    }
    // takes you to add product component scene
    public void toAddProductComponent(ActionEvent event) throws IOException{
        //System.out.println("Go to Add Product Component screen: " + event.toString());

        Stage parent  = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addProductSpec.fxml"));


        Scene scene = new Scene(fxmlLoader.load());
        AddProductCompController controller = fxmlLoader.getController();
        controller.setReturnScene(productCompBtn.getScene());
        parent.setScene(scene);
    }

    // takes you to the product specification of jobs
    public void toProductSpecification(ActionEvent actionEvent) throws IOException{
        Stage parent  = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/productSpecification.fxml"));


        Scene scene = new Scene(fxmlLoader.load());
        JobProductSpecification productSpecificationController = fxmlLoader.getController();
        productSpecificationController.setReturnScene(uc3Btn.getScene());
        parent.setScene(scene);
    }
}
