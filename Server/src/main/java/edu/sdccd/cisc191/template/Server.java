package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Server extends Application {
    private Map<String, Student> students;
    private ListView<String> studentListView;
    private File dataFile;
    private int[][] twoDArray;

    public Server() {
        students = new HashMap<>();
        dataFile = new File("student_data.txt");
        loadStudentData();
        initializeTwoDArray();
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

        primaryStage.setTitle("Student Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeTwoDArray() {
        int rows = 5; // Example number of rows
        int columns = 5; // Example number of columns
        twoDArray = new int[rows][columns];
    }

    private int getAtIndex(int row, int column) {
        if (row >= 0 && row < twoDArray.length && column >= 0 && column < twoDArray[0].length) {
            return twoDArray[row][column];
        }
        return -1; // Return a placeholder value for invalid indices
    }

    private void setAtIndex(int row, int column, int value) {
        if (row >= 0 && row < twoDArray.length && column >= 0 && column < twoDArray[0].length) {
            twoDArray[row][column] = value;
        }
    }

    private void printAll() {
        for (int[] row : twoDArray) {
            System.out.println(Arrays.toString(row));
        }
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
            students.put(student.getName(), student); // Add student to the map

            // Assuming each new student occupies a new row
            int row = students.size() - 1;
            int column = 0;
            setAtIndex(row, column, student.getId()); // Store the student ID in the 2D array

            updateStudentListView(); // Update the student list view
            saveStudentData(); // Save the student data to file
        });
    }

    private void deleteSelectedStudent() {
        int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedStudentName = studentListView.getItems().get(selectedIndex);
            students.remove(selectedStudentName); // Remove student from the map

            // Assuming selectedIndex is the row of the deleted student
            int deletedRow = selectedIndex;
            int deletedColumn = 0;
            setAtIndex(deletedRow, deletedColumn, -1); // Mark the slot as unused in the 2D array

            updateStudentListView(); // Update the student list view
            saveStudentData(); // Save the student data to file
        }
    }

    private void updateStudentListView() {
        studentListView.getItems().clear(); // Clear existing items in the list view
        studentListView.getItems().addAll(students.keySet()); // Add student names to the list view

        printAll(); // Print the contents of the 2D array
    }

    private void saveStudentData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            outputStream.writeObject(students); // Write the students map to the file using serialization
        } catch (IOException e) {
            System.err.println("Error saving student data: " + e.getMessage());
        }
    }

    private void loadStudentData() {
        if (dataFile.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile))) {
                Object obj = inputStream.readObject();
                if (obj instanceof Map) {
                    students = (Map<String, Student>) obj; // Read the students map from the file using deserialization
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
}

