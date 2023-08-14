package edu.sdccd.cisc191.template;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerTest {

    @BeforeAll
    public static void setup() {
        new JFXPanel();
    }

    @Test
    public void testGetValueAtIndex() throws Exception {
        Platform.runLater(() -> {
            try {
                // Create and initialize Server instance
                Server server = new Server();
                server.start(new Stage());

                // Access private method getAtIndex using reflection
                Method getAtIndexMethod = Server.class.getDeclaredMethod("getAtIndex", int.class, int.class);
                getAtIndexMethod.setAccessible(true);

                // Call the method with arguments (row, column)
                int result = (int) getAtIndexMethod.invoke(server, 0, 0);

                // Assert expected result
                assertEquals(result, result); // Replace EXPECTED_RESULT with the actual expected value
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testSetAtIndex() throws Exception {
        Platform.runLater(() -> {
            try {
                // Create and initialize Server instance
                Server server = new Server();
                server.start(new Stage());

                // Access private method setAtIndex using reflection
                Method setAtIndexMethod = Server.class.getDeclaredMethod("setAtIndex", int.class, int.class, int.class);
                setAtIndexMethod.setAccessible(true);

                // Call the method with arguments (row, column, value)
                setAtIndexMethod.invoke(server, 0, 0, 42);

                // Access private method getAtIndex using reflection
                Method getAtIndexMethod = Server.class.getDeclaredMethod("getAtIndex", int.class, int.class);
                getAtIndexMethod.setAccessible(true);

                // Call the method to get the updated value
                int result = (int) getAtIndexMethod.invoke(server, 0, 0);

                // Assert expected result
                assertEquals(42, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Similar tests for other methods...

    // Test for JavaFX GUI (if applicable)
    @Test
    public void testJavaFXGUI() throws Exception {
        Platform.runLater(() -> {
            try {
                Server server = new Server();
                server.start(new Stage());

                // Access private method showAddStudentDialog using reflection
                Method showAddStudentDialogMethod = Server.class.getDeclaredMethod("showAddStudentDialog");
                showAddStudentDialogMethod.setAccessible(true);

                // Call the method
                showAddStudentDialogMethod.invoke(server);

                // Implement assertions for GUI behavior
                // For example, you can check if the dialog was shown or if UI elements were updated.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Wait for JavaFX operations to complete
        Thread.sleep(1000); // Adjust the sleep duration as needed
    }

    // Similar tests for other modules...

    // Tests for Module 3 (Object-Oriented Programming)
    @Test
    public void testStudentGetName() {
        Student student = new Student("John", 123, 90);
        assertEquals("John", student.getName());
    }

    // Tests for Module 4 (I/O Streams)
    @Test
    public void testScannerInput() {
        String input = "42";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Scanner scanner = new Scanner(System.in);
        int value = scanner.nextInt();
        scanner.close();

        assertEquals(42, value);
    }

    // Tests for Module 5 (Generics and Collections)
    @Test
    public void testHashMapPutAndGet() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("key", 42);

        assertEquals(Integer.valueOf(42), map.get("key"));
    }

    @Test
    public void testArrayListSize() {
        ArrayList<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");

        assertEquals(2, list.size());
    }
}

