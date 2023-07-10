package edu.sdccd.cisc191.template;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Server extends Application {
    private Map<String, Student> students;
    private ListView<String> studentListView;
    private File dataFile;

    public Server() {
        students = new HashMap<>();
        dataFile = new File("student_data.txt");
        loadStudentData();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 300);

        // Create the student list view
        studentListView = new ListView<>();
        VBox.setVgrow(studentListView, Priority.ALWAYS);

        // Create the menu bar
        MenuBar menuBar = new MenuBar();
        Menu studentMenu = new Menu("Student");
        MenuItem addStudentMenuItem = new MenuItem("Add Student");
        MenuItem deleteStudentMenuItem = new MenuItem("Delete Student");
        studentMenu.getItems().addAll(addStudentMenuItem, deleteStudentMenuItem);
        menuBar.getMenus().add(studentMenu);

        // Add menu bar and student list view to the root pane
        root.setTop(menuBar);
        root.setCenter(studentListView);

        // Event handlers for menu items
        addStudentMenuItem.setOnAction(event -> showAddStudentDialog());
        deleteStudentMenuItem.setOnAction(event -> deleteSelectedStudent());

        // Event handler for student selection
        studentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showStudentDetails(newValue);
            }
        });

        primaryStage.setTitle("Student Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddStudentDialog() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter student information:");

        // Create form fields
        TextField nameField = new TextField();
        TextField idField = new TextField();
        TextField gradeField = new TextField();

        // Create layout for form fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(new Label("ID:"), 0, 1);
        gridPane.add(idField, 1, 1);
        gridPane.add(new Label("Grade:"), 0, 2);
        gridPane.add(gradeField, 1, 2);

        dialog.getDialogPane().setContent(gridPane);

        // Add buttons to the dialog
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Convert the result of the dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String name = nameField.getText().trim();
                int id = Integer.parseInt(idField.getText().trim());
                int grade = Integer.parseInt(gradeField.getText().trim());
                return new Student(name, id, grade);
            }
            return null;
        });

        // Show the dialog and handle the result
        dialog.showAndWait().ifPresent(student -> {
            students.put(student.getName(), student);
            updateStudentListView();
            saveStudentData();
        });
    }

    private void deleteSelectedStudent() {
        int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedStudentName = studentListView.getItems().get(selectedIndex);
            students.remove(selectedStudentName);
            updateStudentListView();
            saveStudentData();
        }
    }

    private void updateStudentListView() {
        studentListView.getItems().clear();
        studentListView.getItems().addAll(students.keySet());
    }

    private void saveStudentData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            outputStream.writeObject(students);
        } catch (IOException e) {
            System.err.println("Error saving student data: " + e.getMessage());
        }
    }

    private void loadStudentData() {
        if (dataFile.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile))) {
                Object obj = inputStream.readObject();
                if (obj instanceof Map) {
                    students = (Map<String, Student>) obj;
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading student data: " + e.getMessage());
            }
        }
    }

    private void showStudentDetails(String studentName) {
        Student student = students.get(studentName);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Student Details");
        alert.setHeaderText(null);
        alert.setContentText("Name: " + student.getName() + "\nID: " + student.getId() + "\nGrade: " + student.getGrade());
        alert.showAndWait();
    }

    private static class Student implements Serializable {
        private String name;
        private int id;
        private int grade;

        public Student(String name, int id, int grade) {
            this.name = name;
            this.id = id;
            this.grade = grade;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }
}
