package com.kms;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  private static final Logger logger = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    System.out.println("Hello World!");
    logger.info("This is an info message.");
    logger.error("This is an error message");
    logger.debug("This is debug message");

    // Create an instance of UserRow
    UserRow user1 = new UserRow();

    // Use Lombok-generated setters
    user1.setID(1);
    user1.setEmail("quangdm961@gmail.com");
    user1.setFirstName("Minh");
    user1.setLastName("Quang");

    // Use Lombok-generated getters and toString
    System.out.println("ID: " + user1.getID());
    System.out.println("Email: " + user1.getEmail());
    System.out.println("First Name: " + user1.getFirstName());
    System.out.println("Last Name: " + user1.getLastName());
    System.out.println(user1);

    // Use Lombok-generated constructor
    UserRow user2 = new UserRow(2, "quangdo@kms.technology.com", "Quang", "Do");

    // Use Lombok-generated getters and toString
    System.out.println("ID: " + user2.getID());
    System.out.println("Email: " + user2.getEmail());
    System.out.println("First Name: " + user2.getFirstName());
    System.out.println("Last Name: " + user2.getLastName());
    System.out.println(user2);

    CsvReader csvReader = new CsvReader();
    try {
      List<UserRow> users = csvReader.readCsv("data.csv");

      for (UserRow user : users) {
        System.out.println(user);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
