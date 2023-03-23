package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lk.ijse.dep10.app.model.Employee;

public class ManageEmployeeController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewEmployee;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Employee> tblEmployees;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

    }

    private void generateID() {
        if (tblEmployees.getItems().size() == 0) txtID.setText("E001");
        else {
            int newID = Integer.parseInt(tblEmployees.getItems().get(tblEmployees.getItems().size() - 1).getId().substring(1));
            newID++;
            txtID.setText(String.format("E%03d", newID));
        }
    }

}