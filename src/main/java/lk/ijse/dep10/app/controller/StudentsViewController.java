package lk.ijse.dep10.app.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.util.Student;

import java.sql.*;

public class StudentsViewController {
    public Button btnNewStudent;
    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public Button btnSave;
    public Button btnDelete;
    public TableView<Student> tblStudents;

    public void initialize(){
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        loadAllStudents();

        tblStudents.getSelectionModel().selectedItemProperty().addListener((ov,prev,current)->{
            btnDelete.setDisable(current==null);
            if (current==null) return;
        });
        btnNewStudent.fire();
    }

    private void loadAllStudents() {
        Connection connection= DBConnection.getInstance().getConnection();
        try {
            Statement stm=connection.createStatement();
            ResultSet rst=stm.executeQuery("SELECT *FROM Student");
            ObservableList<Student> studentList=tblStudents.getItems();

            while (rst.next()){
                String id= rst.getString("id");
                String name=rst.getString("name");
                String address =rst.getString("address");
                studentList.add(new Student(id,name,address));
            }




        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to load students");
        }


    }


    public void btnNewStudentOnAction(ActionEvent actionEvent) {
        ObservableList<Student> studentList=tblStudents.getItems();
        int oldId=(studentList.isEmpty()? 1: Integer.parseInt(studentList.get(studentList.size()-1).getId()+1));
        String newId=String.format("S%03d",oldId);

        txtId.setText(newId);
        txtName.clear();
        txtAddress.clear();

        txtName.requestFocus();

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if(!isDataValid()) return;

        Connection connection=DBConnection.getInstance().getConnection();

        try {
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Student(student_id,student_name,student_address) VALUES (?,?,?)");
            stm.setString(1,txtId.getText());
            stm.setString(2,txtName.getText());
            stm.setString(3,txtAddress.getText());
            stm.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to save the student");
        }
        Student student = new Student(txtId.getText(),txtName.getText(),txtAddress.getText());
        tblStudents.getItems().add(student);


    }

    private boolean isDataValid() {
        boolean dataValid=true;


        String name=txtName.getText();
        String address=txtAddress.getText();

        if(!address.matches("[A-Za-z ]+")){
            dataValid=false;
            txtAddress.requestFocus();
            txtAddress.selectAll();
            txtAddress.getStyleClass().add("invalid");
        }

        if(!name.matches("[A-Za-z ]+")){
            dataValid=false;
            txtName.requestFocus();
            txtName.selectAll();
            txtName.getStyleClass().add("invalid");
        }
        return dataValid;
    }

    public void tblStudentsOnKeyReleased(KeyEvent keyEvent) {

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {

        Student selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        Connection connection=DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM Student WHERE student_id=?");
            stm.setString(1,selectedStudent.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tblStudents.getItems().remove(selectedStudent);


    }
}
