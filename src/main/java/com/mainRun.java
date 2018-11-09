package com;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Created by PC on 5/8/2017.
 */
public class mainRun {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Full table scan => db: verodbhost:3306/vero_app");
        System.out.println("Enter username: ");
        String username =  sc.nextLine();
        System.out.println("Enter password: ");
        String pass = sc.nextLine();

        int i = 0;
        System.out.print("1 - LIKE, 2 - EQUAL,  -1 - For keys column: ");
        if (sc.hasNextInt()) {
            i = sc.nextInt();
        } else {
            System.out.println("Вы ввели не целое число");
        }

        System.out.print("search request: ");
        sc.nextLine();
        final String search = sc.nextLine();
        System.out.println("working...");


        mysql handler = new mysql(username, pass);
        ArrayList tableList = handler.getTables();

        LinkedHashMap<String, ArrayList> tablesField = new LinkedHashMap();

        tableList.forEach((tableName) -> {
            try {
                tablesField.put((String) tableName, handler.getFieldsByTable((String) tableName));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        if (i == -1) {
            System.out.println("FIELD " + search + " tables:-");
            tablesField.forEach((k, v) -> v.forEach(item -> {
                if (item.equals(search)) {
                    System.out.println("-" + k);
                }
            }));
        }


        // generate sql
        boolean flag = true;
        if (i == 2) {
            flag = false;
        }
        if (i != -1)
            handler.generateSQL(flag, tablesField, search);
        System.out.println("\nEND.");

    }
}
