package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Teacher;

import java.sql.*;

public class TeachersViewController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnNewTeacher;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Teacher> tblTeachers;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    public void initialize(){
        tblTeachers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblTeachers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblTeachers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        loadAllTeachers();

        tblTeachers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, current) ->{
            btnDelete.setDisable(current==null);
            if (current==null) return;

            txtId.setText(current.getId());
            txtName.setText(current.getName());
            txtAddress.setText(current.getAddress());
            txtName.setDisable(false);
            txtId.setDisable(false);
            txtAddress.setDisable(false);


        } );
        txtName.setDisable(true);
        txtId.setDisable(true);
        txtAddress.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Teacher selectedteacher = tblTeachers.getSelectionModel().getSelectedItem();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM Teacher WHERE teacher_id=?");
            stm.setString(1,selectedteacher.getId());
            stm.executeUpdate();
            tblTeachers.getItems().remove(selectedteacher);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tblTeachers.getItems().remove(selectedteacher);


    }

    private void loadAllTeachers() {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Teacher");
            while (rst.next()){
                String id = rst.getString("teacher_id");
                String name = rst.getString("teacher_name");
                String address = rst.getString("teacher_address");

                Teacher teacher = new Teacher(id, name, address);
                tblTeachers.getItems().add(teacher);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnNewTeacherOnAction(ActionEvent event) {
        txtId.setText(autoGenerateId());
        txtName.clear();
        txtAddress.clear();
        txtName.requestFocus();
        tblTeachers.getSelectionModel().clearSelection();
        txtName.setDisable(false);
        txtId.setDisable(false);
        txtAddress.setDisable(false);
        btnSave.setDisable(false);


    }

    private String autoGenerateId() {
        String newTeacherId="T001";
        if (tblTeachers.getItems().size()!=0){
            String lastTeacherId = tblTeachers.getItems().get(tblTeachers.getItems().size() - 1).getId().substring(1);
            newTeacherId=String.format("T%03d",(Integer.parseInt(lastTeacherId))+1);
        }
        return newTeacherId;

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!isDataValid()) return;

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Teacher(teacher_id,teacher_name,teacher_address) VALUES (?,?,?)");
            stm.setString(1,txtId.getText());
            stm.setString(2,txtName.getText());
            stm.setString(3,txtAddress.getText());

            stm.executeUpdate();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Failed to save the student").showAndWait();
            e.printStackTrace();

        }

        Teacher teacher = new Teacher(txtId.getText(), txtName.getText(), txtAddress.getText());
        tblTeachers.getItems().add(teacher);


    }

    private boolean isDataValid() {
        boolean isDataValid=true;
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();

        if (!address.matches("[A-z ]{3,}")){
            txtAddress.selectAll();
            txtAddress.requestFocus();
            isDataValid=false;

        }
        if (!name.matches("[A-z ]+")){
            txtName.selectAll();
            txtName.requestFocus();
            isDataValid=false;
        }
        return isDataValid;
    }

    @FXML
    void tblTeachersOnKeyReleased(KeyEvent event) {

    }

}
