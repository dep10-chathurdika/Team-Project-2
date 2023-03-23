package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Customer;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageCustomerUIController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewCustomer;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Customer> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;
    public void initialize(){
        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        loadCustomers();
        btnNewCustomer.fire();
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((ov,prev,current)->{
            if (current != null) {
                btnDelete.setDisable(false);
                txtId.setText(current.getId());
                txtName.setText(current.getName());
                txtAddress.setText(current.getAddress());
            }

        });
    }
    public void loadCustomers(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");
            ObservableList<Customer> customerList = tblCustomer.getItems();

            while (rst.next()) {

                String customerId = rst.getString("customer_id");
                String customerName = rst.getString("customer_name");
                String customerAddress = rst.getString("customer_address");

                customerList.add(new Customer(customerId,customerName,customerAddress));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed load customer details, try again").showAndWait();
            Platform.exit();
        }
    }



    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Customer selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete customer with customer id " + selectedCustomer.getId() + " ?", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement stmEmployee = connection.prepareStatement("DELETE FROM Customer WHERE customer_id = ?");
                stmEmployee.setString(1, selectedCustomer.getId());
                stmEmployee.executeUpdate();
                connection.commit();

                tblCustomer.getItems().remove(selectedCustomer);
                if (!tblCustomer.getItems().isEmpty()) btnNewCustomer.fire();
                connection.commit();
            } catch (Throwable e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete the customer. Try again!").show();
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @FXML
    void btnNewCustomerOnAction(ActionEvent event) {
        ObservableList<Customer> studentList = tblCustomer.getItems();
        String newCustomerId = "C001";
        if (tblCustomer.getItems().size()!=0) {
            String lastCustomerId = (tblCustomer.getItems().get(tblCustomer.getItems().size() - 1).getId().substring(1));
            newCustomerId = String.format("C%03d", Integer.parseInt(lastCustomerId) + 1);
        }
        txtId.setText(newCustomerId);
        txtName.clear();
        txtAddress.clear();
        tblCustomer.getSelectionModel().clearSelection();
        txtName.requestFocus();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO Customer (customer_id, customer_name, customer_address) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, txtId.getText());
            stm.setString(2, txtName.getText());
            stm.setString(3, txtAddress.getText());
            stm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save the customer, try again!").show();
        }
        Customer customer = new Customer(txtId.getText(), txtName.getText(), txtAddress.getText());
        tblCustomer.getItems().add(customer);


    }
    private boolean isDataValid(){boolean dataValid = true;
        String name = txtName.getText();
        String address = txtAddress.getText();
        if (!name.matches("[A-Za-z ]+")){
            txtName.requestFocus();
            txtName.selectAll();
            dataValid = false;
        }
        if (!address.matches("[A-Za-z0-9 ]+")){
            txtAddress.requestFocus();
            txtAddress.selectAll();
            dataValid = false;
        }
        return dataValid;

    }

    @FXML
    void tblCustomerOnAction(ActionEvent event) {

    }

}
