package Main1;

import com.mysql.cj.x.protobuf.MysqlxResultset;

import java.sql.*;
    import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class examples {
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/dbBooks";
    static final String USER = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            // Disable auto-commit mode
            connection.setAutoCommit(false);

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM tblBooks");

            int i = 0;
            while (resultSet.next()) {
                String author = resultSet.getString("Author");
                String title = resultSet.getString("Title");
                String isbnNo = resultSet.getString("ISBN");
                String edition = resultSet.getString("Edition");
                System.out.printf("%s: %s-%s(%s)\n", author, title, isbnNo, edition);
            }

            // Insert records
            System.out.println("Inserting records into the table...");
            String sql = "INSERT INTO tblBooks (ISBN, Author, Title, Edition) " +
                    "VALUES ('13Ahhlj', 'H.Smith', 'Java Programming', '2nd Edition')";
            statement.executeUpdate(sql);

            // Update records
            System.out.println("Updating records in the table...");
            sql = "UPDATE tblBooks SET Author='Mary Smith' WHERE ISBN='11923'";
            statement.executeUpdate(sql);
            System.out.println("Record updated successfully...");

            // Display records
            System.out.println("Retrieving records from the table...");
            sql = "SELECT * FROM tblBooks";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String isbnNo = rs.getString("ISBN");
                String author = rs.getString("Author");
                String title = rs.getString("Title");
                String edition = rs.getString("Edition");
                System.out.println("ISBN: " + isbnNo + ", Author: " + author + ", Title: " + title + ", Edition: " + edition);
            }


            // Create JTable

            String[] columnNames = {"ISBN", "Author", "Title", "Edition"};

            Object[][] rowData = {};

            JTable table = new JTable(rowData, columnNames);



            // Populate JTable with data

            while (rs.next()) {

                String isbnNo = rs.getString("ISBN");

                String author = rs.getString("Author");

                String title = rs.getString("Title");

                String edition = rs.getString("Edition");

                Object[] row = {isbnNo,author,title,edition};



                ((DefaultTableModel)table.getModel()).addRow(row);

            }

            rs.close();

            // Create JPanel to hold JTable

            JPanel panel = new JPanel();

            panel.setLayout(new BorderLayout());

            panel.add(new JScrollPane(table), BorderLayout.CENTER);


            // Create JFrame to hold JPanel

            JFrame frame = new JFrame("Books");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.add(panel);

            frame.pack();

            frame.setVisible(true);

            // Commit the transaction
            connection.commit();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                if (connection != null) {
                    connection.close();
                }

            }
            finally {
                resultSet.close();
            }
        }
    }


}
