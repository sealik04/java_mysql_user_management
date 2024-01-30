import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

public class Main {
    //jmeno, prijmeni, bydliste, id
    //muzu tam vkladat uzivatele, upravovat je a odstranit uzivatele
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int action;

        String name;
        String surname;
        String housing;
        int id;

        boolean running = true;

        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/uzivatele", "root", "");
        ){
            while (running = true) {
                System.out.println("vyber akci, co chceš provést \n" +
                        "1 - vložit uživatele \n" +
                        "2 - upravit uživatele \n" +
                        "3 - odebrat uživatele \n" +
                        "4 - ukončit aplikaci");
                action = sc.nextInt();

                if (action == 1) {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO seznam (jmeno, prijmeni, bydliste, id) VALUES (?, ?, ?, ?)");

                    System.out.println("---VKLAD NOVÉHO UŽIVATELE---");
                    sc.nextLine();
                    System.out.println("jméno: ");
                    name =  sc.nextLine();
                    System.out.println("příjmení: ");
                    surname = sc.nextLine();
                    System.out.println("bydliště: ");
                    housing = sc.nextLine();
                    System.out.println("id: ");
                    id = sc.nextInt();


                    ps.setString(1, name);
                    ps.setString(2, surname);
                    ps.setString(3, housing);
                    ps.setInt(4, id);
                    int result = ps.executeUpdate();

                } else if (action == 2) {
                    PreparedStatement checkId = con.prepareStatement("SELECT id FROM seznam WHERE id = ?");
                    PreparedStatement psUpdate = con.prepareStatement("UPDATE INTO seznam WHERE id VALUES (?)");

                    System.out.println("---ÚPRAVA UŽIVATELE---");
                    sc.nextLine();

                    System.out.println("zadej id uživatele");
                    id = sc.nextInt();
                    ResultSet rsCheck = checkId.executeQuery();

                    if(rsCheck.next()){

                        boolean updateActive = true;
                        while (updateActive) {
                            System.out.println("co chceš zrobit?\n" +
                                    "1 - upravit jméno \n" +
                                    "2 - upravit příjmení \n" +
                                    "3 - upravit bydliště \n" +
                                    "4 - upravit id \n" +
                                    "5 - konec \n");
                            int edit = sc.nextInt();
                            switch (edit){
                                case 1:
                                    System.out.println("zadej nové jméno");
                                    String newName = sc.nextLine();

                                    System.out.println("JMÉNO UPRAVENO");
                                    break;
                                case 2:
                                    System.out.println("zadej nové příjmení");
                                    String newSurname = sc.nextLine();

                                    System.out.println("PŘÍJMENÍ UPRAVENO");
                                    break;
                                case 3:
                                    System.out.println("zadej nové bydliště");
                                    String newHousing = sc.nextLine();

                                    System.out.println("BYDLIŠTĚ UPRAVENO");
                                    break;
                                case 4:
                                    System.out.println("zadej nové id");
                                    int newId = sc.nextInt();

                                    System.out.println("ID UPRAVENO");
                                    break;
                                case 5:
                                    updateActive = false;
                                    break;
                            }
                        }

                    } else {
                        System.out.println("toto id nemá záznam v tabulce");
                    }

                } else  if (action == 3){
                    System.out.println("delete ještě není zrobený bratře :/");
                }

                else if (action == 4) {
                    running = false;
                }
            }
    } catch (SQLException e){
            e.printStackTrace();
            }
        }
    }