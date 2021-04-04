/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.github.javafaker.Faker;

/**
 *
 * @author User
 */
public class Helper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Faker faker = new Faker();

    String streetName = faker.address().streetName();
    String number = faker.address().buildingNumber();
    String city = faker.address().city();
    String country = faker.address().country();
    String fullname=faker.name().fullName();
    
    System.out.println(String.format("%s\n%s\n%s\n%s\n%s",
            fullname,
      number,
      streetName,
      city,
      country));
        }
    
}
