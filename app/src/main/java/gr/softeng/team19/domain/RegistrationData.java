package gr.softeng.team19.domain;

import java.time.LocalDate;

public class RegistrationData {
    // Static variables to be accessible globally during registration
    public static String username;
    public static String password;
    public static String email;
    public static String name;
    public static String surname;
    public static String phone;
    public static LocalDate birthDate;
    public static String card;
    public static String street;
    public static String city;
    public static int streetNumber;
    public static int postalCode;

    //Resets all fields after registration is complete.

    public static void clear() {
        username = null; password = null; email = null;
        name = null; surname = null; phone = null;
        birthDate = null; card = null;
        street = null; city = null;
    }
}