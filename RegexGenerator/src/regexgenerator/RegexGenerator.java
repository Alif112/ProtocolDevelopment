/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexgenerator;


import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author User
 */
public class RegexGenerator {
    static int sizeOfList=100000;
    
    public static void main(String[] args) throws FileNotFoundException {
        Faker faker = new Faker();

        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = faker.name().firstName(); // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = faker.address().streetAddress();
        
        System.out.println("Name: "+name);
        System.out.println("FirstName: "+firstName);
        System.out.println("LastName: "+lastName);
        System.out.println("StreetAddress: "+streetAddress);
        
//        PrintWriter out = new PrintWriter("filename.txt");
//        
//        for(int i=0;i<sizeOfList;i++){
//            out.println(faker.name().fullName());
//        }
        
        
        
	}
}
