package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

    }

    public static User getNewInfo() {
        Scanner scan = new Scanner(System.in);
        int id = 0;
        while (true) {
            System.out.print("Select user: ");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid input");
                System.out.print("Select user: ");
                scan.nextLine();
            }
            id = scan.nextInt();
            scan.nextLine();
            try {
                if (DBUtil.isRow(DBUtil.connect(),"users", id)) {
                    break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("User do not exist.");
        }

        System.out.print("New email: ");
        String email = scan.nextLine();
        System.out.print("New username: ");
        String username = scan.nextLine();
        System.out.print("New password: ");
        String password = scan.nextLine();
        return new User(id, email, username, password);
    }


}
