package addressapp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Person {

    private String firstname;
    private String lastname;
    private String street;
    private int postalCode;
    private String city;
    private LocalDate birthday;
    
    public Person(String firstName, String lastName) {
        
        this.firstname = firstName;
        this.lastname = lastName;
        this.street = "Козакова, 38";
        this.postalCode = 49000;
        this.city = "Дніпро";
        this.birthday = LocalDate.of(1999, 2, 21);
    }

    public Person() {
        this("first","last");
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Date getBirthdayDate() {
        return java.sql.Date.valueOf(birthday);
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setBirthday(Date date) {
        this.birthday = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


}
