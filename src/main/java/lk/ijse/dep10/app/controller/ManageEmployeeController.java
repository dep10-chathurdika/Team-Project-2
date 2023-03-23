package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Employee;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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

        Platform.runLater(btnNewEmployee::fire);
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

        if (!isDataValid()) return;

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stmEmployee = connection.prepareStatement("INSERT INTO Employee (employee_id, employee_name, employee_address) VALUES (?, ?, ?)");

            connection.setAutoCommit(false);

            stmEmployee.setString(1, txtID.getText());
            stmEmployee.setString(2, txtName.getText());
            stmEmployee.setString(3, txtAddress.getText());
            stmEmployee.executeUpdate();

            Employee newEmployee = new Employee(txtID.getText(), txtName.getText(), txtAddress.getText());

            connection.commit();
            tblEmployees.getItems().add(newEmployee);
            btnNewEmployee.fire();
        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            new Alert(Alert.AlertType.ERROR, "Failed to saved the Employee").show();
            e.printStackTrace();
        }

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