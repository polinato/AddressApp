package db;

import addressapp.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends Configs {
    
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?zeroDateTimeBehavior=convertToNull";

        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    public ObservableList<Person> readDBTable() {
        
        ObservableList<Person> personObservableList = null;
        
        try {
            List<Person> listOfPerson = new ArrayList();
            try (Connection conn = getDbConnection()) {
                
                String query = "SELECT * FROM " + Const.TABLE;
                
                try (Statement st = conn.createStatement()) {
                    
                    ResultSet rs = st.executeQuery(query);
                    
                    while (rs.next()) {
                        
                        Person person = new Person();
                       
                        String firstname = rs.getString(Const.FIRSTNAME);
                        String lastname = rs.getString(Const.LASTNAME);
                        String street = rs.getString(Const.STREET);
                        int postalcode = rs.getInt(Const.POSTALCODE);
                        String city = rs.getString(Const.CITY);
                        Date birthday = rs.getDate(Const.BIRTHDAY);
                        
                        person.setFirstname(firstname);
                        person.setLastname(lastname);
                        person.setCity(city);
                        person.setStreet(street);
                        person.setBirthday(birthday);
                        person.setPostalCode(postalcode);
                        
                        listOfPerson.add(person);
                    }
                }
                
                personObservableList = FXCollections.observableArrayList(listOfPerson);
            }
        } 
        catch (ClassNotFoundException | SQLException e) {
            
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return personObservableList;
    }

    public void addPersonToDB(Person person) throws SQLException, ClassNotFoundException {

        Connection conn;
        
        try {  
            String insertQueryStatement = "INSERT INTO " + Const.TABLE + " VALUES  (?,?,?,?,?,?,?)";
            conn = getDbConnection();
            PreparedStatement statement = conn.prepareStatement(insertQueryStatement);
            statement.setString(1, null);
            statement.setString(2, person.getFirstname());
            statement.setString(3, person.getLastname());
            statement.setString(4, person.getStreet());
            statement.setInt(5, person.getPostalCode());
            statement.setString(6, person.getCity());
            statement.setDate(7, (Date) person.getBirthdayDate());
            statement.executeUpdate();
        } 
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void changePerson(int index, Person person){
        
        Statement stmt = null;

        try {
            Connection conn = getDbConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query="SELECT * FROM " + Const.TABLE;

            ResultSet rset = stmt.executeQuery(query);

            rset.absolute(index+1);
            rset.updateString(Const.FIRSTNAME, person.getFirstname());
            rset.updateString(Const.LASTNAME, person.getLastname());
            rset.updateString(Const.CITY, person.getCity());
            rset.updateString(Const.STREET, person.getStreet());
            rset.updateInt(Const.POSTALCODE, person.getPostalCode());
            rset.updateDate(Const.BIRTHDAY, (Date) person.getBirthdayDate());
            rset.updateRow();
           // System.out.println("Row Updated");
           // System.out.println(rset.getMetaData());
        } 
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void deletePerson(Person person) {
        
        try {
            Connection conn = getDbConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM " + Const.TABLE + " WHERE " +  Const.FIRSTNAME + " = ?");
            //System.out.println("cheak FN: " + person.getFirstname());
            //System.out.println("cheak SN: " + person.getLastname());
            st.setString(1, person.getFirstname());
            st.executeUpdate();
        } 
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteAllRowsTable() {
        try {
            try (Connection connection = getDbConnection()) {
                String query = "TRUNCATE " + Const.TABLE;
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        } 
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
