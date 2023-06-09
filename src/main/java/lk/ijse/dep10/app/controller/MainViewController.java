package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainViewController {

    @FXML
    private Button btnMngCustomers;

    @FXML
    private Button btnMngEmployees;

    @FXML
    private Button btnMngStudents;

    @FXML
    private Button btnMngTeachers;

    @FXML
    void btnMngCustomersOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/ManageCustomerUI.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnMngCustomers.getScene().getWindow());
        stage.setTitle("Manage Customer");
        stage.show();
        stage.centerOnScreen();

    }

    @FXML
    void btnMngEmployeesOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/ManageEmployee.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnMngCustomers.getScene().getWindow());
        stage.setTitle("Manage Employee");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnMngStudentsOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();

        URL fxmlFile = getClass().getResource("/view/StudentsView.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile);
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(btnMngCustomers.getScene().getWindow());
        stage.setTitle("Manage Students");
        stage.show();
        stage.centerOnScreen();

    }

    @FXML
    void btnMngTeachersOnAction(ActionEvent event) throws IOException {
        Stage stage=(Stage) btnMngCustomers.getScene().getWindow();
        stage.setTitle("Teachers Form");
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/TeacherView.fxml")).load()));
        stage.show();

    }

}
