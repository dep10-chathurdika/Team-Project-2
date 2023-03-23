package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Employee;

import java.sql.*;

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

    public void initialize() {

        tblEmployees.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblEmployees.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblEmployees.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        loadAllEmployees();

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((ov, prev, current) -> {
            btnDelete.setDisable(current == null);
            if (current == null) return;

            txtID.setText(current.getId() + "");
            txtName.setText(current.getName());
            txtAddress.setText(current.getAddress());
        });
    }

    private void loadAllEmployees() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Employee");

            while (rst.next()) {
                String id = rst.getString("employee_id");
                String name = rst.getString("employee_name");
                String address = rst.getString("employee_address");

                Employee employee = new Employee(id, name, address);
                tblEmployees.getItems().add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employee details. Try again!", ButtonType.OK).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) {
        txtName.clear();
        txtName.clear();
        txtAddress.clear();

        txtID.setDisable(false);
        txtName.setDisable(false);
        txtAddress.setDisable(false);
        btnSave.setDisable(false);

        generateID();
        txtName.requestFocus();
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

    private boolean isDataValid() {
        String name = txtName.getText();
        String address = txtAddress.getText();
        boolean dataValid = true;

        if (!name.matches("[A-z ]+")) {
            txtName.requestFocus();
            txtName.selectAll();
            dataValid = false;
        }

        if (!address.matches("[A-z ]+")) {
            txtAddress.requestFocus();
            txtAddress.selectAll();
            dataValid = false;
        }
        return dataValid;
    }

}