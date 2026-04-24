package org.example.sdev200module6assignment1;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;


// Record display and updater
// Displays a record in the database if it exists,
// allows for an update of an existing record, and allows
// for new records to be added. If a record already exists, it will pull up the record
// Chad Swift
// SDEV200-50P
// Module 6 Assignment 1

public class HelloApplication extends Application {
    // create text info message
    Text message = new Text("");
    // create fields so that they can be updated
    TextField idField = new TextField();
    TextField lNameField = new TextField();
    TextField fNameField = new TextField();
    TextField mIField = new TextField();
    TextField addressField = new TextField();
    TextField cityField = new TextField();
    TextField stateField = new TextField();
    TextField telephoneField = new TextField();
    TextField emailField = new TextField();

    @Override
    public void start(Stage stage) throws ClassNotFoundException, SQLException {

        // Load driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver Loaded");

        // create connection
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "");
        // print out that it's connected if everything works
        System.out.println("Database Connected");

        // create a pane
        VBox pane = new VBox();
        // create a horizontal box for the id components
        HBox idBox = new HBox();
        // create a horizontal box for the name components
        HBox nameBox = new HBox();
        // create a horizontal box for the address
        HBox addressBox = new HBox();
        // create a horizontal box for the location components
        HBox locationBox = new HBox();
        // create horizontal box for the contact info
        HBox contactBox = new HBox();

        // spacing is going to be universally 8
        final int SPACING = 8;

        // set the spacing for all the hboxes
        idBox.setSpacing(SPACING);
        nameBox.setSpacing(SPACING);
        addressBox.setSpacing(SPACING);
        locationBox.setSpacing(SPACING);
        contactBox.setSpacing(SPACING);

        // set labels for all the fields
        Label idLabel = new Label("ID:");
        Label lNameLabel = new Label("Last Name");
        Label fNameLabel = new Label("First Name");
        Label mILabel = new Label("MI");
        mIField.setPrefColumnCount(1);
        Label addressLabel = new Label("Address");
        Label cityLabel = new Label("City");
        Label stateLabel = new Label("State");
        stateField.setPrefColumnCount(2);
        Label telephoneLabel = new Label("Telephone");
        Label emailLabel = new Label("Email");

        // add the label components to the label box
        idBox.getChildren().addAll(
                idLabel,
                idField
        );

        // add the name components to the name box
        nameBox.getChildren().addAll(
                lNameLabel,
                lNameField,
                fNameLabel,
                fNameField,
                mILabel,
                mIField
        );

        // add the address components to the address box
        addressBox.getChildren().addAll(
                addressLabel,
                addressField
        );

        // add the location components to the location box
        locationBox.getChildren().addAll(
                cityLabel,
                cityField,
                stateLabel,
                stateField
        );

        // add the contact components to the contact box
        contactBox.getChildren().addAll(
                telephoneLabel,
                telephoneField,
                emailLabel,
                emailField
        );

        // create a button box
        HBox btnBox = new HBox();
        // set the spacing for the button box
        btnBox.setSpacing(SPACING);
        // set alignment of the button box
        btnBox.setAlignment(Pos.CENTER);

        // create 4 new buttons
        Button viewButton = new Button("View");
        Button insertButton = new Button("Insert");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");

        // add the buttons to the button box
        btnBox.getChildren().addAll(viewButton, insertButton, updateButton, clearButton);

        // add all the boxes to the pane
        pane.getChildren().addAll(
                message,
                idBox,
                nameBox,
                addressBox,
                locationBox,
                contactBox,
                btnBox
        );

        // set the spacing and padding of the pane
        pane.setSpacing(SPACING);
        pane.setPadding(new Insets(5, 5, 5,5));

        // put the pane in the scene
        Scene scene = new Scene(pane, 550, 250);

        // set the title
        stage.setTitle("ExtraExercise34-01");
        // set the scene in the stage
        stage.setScene(scene);
        // show the stage
        stage.show();

        //add events on all the buttons
        clearButton.setOnAction(e -> clear());
        // try catch is because IntelliJ told me that it was unhandled
        // I'm actually not sure if I can go without the try catch blocks here
        insertButton.setOnAction(e -> {
            try {
                // runs the insert method, passes in connection
                insert(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        viewButton.setOnAction(e -> {
            try {
                // runs the view method, passes in connection
                view(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        updateButton.setOnAction(e -> {
            try {
                // runs the view method, passes in connection
                update(connection);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Helper function to help get specific records to either display them or
     * check if there is a duplicate
     * @param connection pass in connection that is established to database
     * @return ResultSet
     * @throws SQLException if SQL is incorrect
     */
    public ResultSet getRecord(Connection connection) throws SQLException {
        // create query, we want all the columns from the id, simple parameter
        String queryString = "select * from Staff where id = ?";

        // make prepared statement
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);

        // add the parameter into the prepared statement
        preparedStatement.setString(1, idField.getText());

        // execute the query and return it
        return preparedStatement.executeQuery();
    }

    public void insert(Connection connection) throws SQLException {
        // get prepared statement from helper method to check for a record
        ResultSet rSet = getRecord(connection);

        // if there's already a record here, we can just tell the user it exists, and run the view method to display it
        if (rSet.next()) {
            message.setText("Record already exists with id of " + rSet.getString(1) + ". Displaying Record");
            view(connection);
        } else {
            // if not, we need to create a new query with parameters
            String queryString = "insert into Staff (id, lastName, firstName, mi, address, city, state, telephone, email)" +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // create prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            // set parameters
            preparedStatement.setString(1, idField.getText());
            preparedStatement.setString(2, lNameField.getText());
            preparedStatement.setString(3, fNameField.getText());
            preparedStatement.setString(4, mIField.getText());
            preparedStatement.setString(5, addressField.getText());
            preparedStatement.setString(6, cityField.getText());
            preparedStatement.setString(7, stateField.getText());
            preparedStatement.setString(8, telephoneField.getText());
            preparedStatement.setString(9, emailField.getText());

            // try catch for errors, we display them on the box
            try {
                // execute the update
                int result = preparedStatement.executeUpdate();

                // if the result int is greater than 0, display success
                if (result > 0) {
                    message.setText("You have inserted " + result + " records");
                    clear();
                } else {
                    // else display failure
                    message.setText("Insert operation failed");
                }
            } catch (Exception e) {
                message.setText("Some Error Occurred");
            }
        }

    }

    /**
     * Method that updates a current record
     * @param connection connection passed from main app
     * @throws SQLException if something goes wrong with SQL query
     */
    public void update(Connection connection) throws SQLException {

        // create the appropriate statement,
        String sql = "update Staff " +
                "set lastName = ?, " +
                "firstName = ?, " +
                "mi = ?, " +
                "address = ?, " +
                "city = ?, " +
                "state = ?, " +
                "telephone = ?, " +
                "email = ? " +
                "where id = ?";


        // create prepared statement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // set all the parameters for the statement, this time id is the last one
        preparedStatement.setString(1, lNameField.getText());
        preparedStatement.setString(2, fNameField.getText());
        preparedStatement.setString(3, mIField.getText());
        preparedStatement.setString(4, addressField.getText());
        preparedStatement.setString(5, cityField.getText());
        preparedStatement.setString(6, stateField.getText());
        preparedStatement.setString(7, telephoneField.getText());
        preparedStatement.setString(8, emailField.getText());
        preparedStatement.setString(9, idField.getText());

        // execute
        preparedStatement.executeUpdate();

        // show message
        message.setText("Successfully updated record");
    }

    /**
     * method that fills the fields out to view an existing record
     * @param connection passed in from the main application
     * @throws SQLException for SQL-related errors
     */
    public void view(Connection connection) throws SQLException {

        // use shortcut method to get a ResultSet
        ResultSet rSet = getRecord(connection);

        // if there is a result, fill out the fields using the result set
        if (rSet.next()) {
            String id = rSet.getString(1);
            String lName = rSet.getString(2);
            String fName = rSet.getString(3);
            String mi = rSet.getString(4);
            String address = rSet.getString(5);
            String city = rSet.getString(6);
            String state = rSet.getString(7);
            String telephone = rSet.getString(8);
            String email = rSet.getString(9);

            idField.setText(id);
            lNameField.setText(lName);
            fNameField.setText(fName);
            mIField.setText(mi);
            addressField.setText(address);
            cityField.setText(city);
            stateField.setText(state);
            telephoneField.setText(telephone);
            emailField.setText(email);

        } else {
            // or set a message to say there's not any record there
            message.setText("No Record Found");
        }
    }

    /**
     * Method that clears out all the fields
     */
    public void clear() {

        // clear out all the fields
        idField.clear();
        lNameField.clear();
        fNameField.clear();
        mIField.clear();
        addressField.clear();
        cityField.clear();
        stateField.clear();
        telephoneField.clear();
        emailField.clear();
    }

}
