import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int action;

        String name;
        String surname;
        String housing;
        int id;

        boolean running = true;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/uzivatele", "root", "")) {
            while (running) {
                System.out.println("vyber akci, co chceš provést \n" +
                        "1 - vložit uživatele \n" +
                        "2 - upravit uživatele \n" +
                        "3 - odebrat uživatele \n" +
                        "4 - ukončit aplikaci");
                action = sc.nextInt();

                switch (action) {
                    case 1:
                        PreparedStatement insertStatement = con.prepareStatement("INSERT INTO seznam (jmeno, prijmeni, bydliste, id) VALUES (?, ?, ?, ?)");

                        System.out.println("---VKLAD NOVÉHO UŽIVATELE---");
                        sc.nextLine();
                        System.out.println("jméno: ");
                        name = sc.nextLine();
                        System.out.println("příjmení: ");
                        surname = sc.nextLine();
                        System.out.println("bydliště: ");
                        housing = sc.nextLine();
                        System.out.println("id: ");
                        id = sc.nextInt();

                        insertStatement.setString(1, name);
                        insertStatement.setString(2, surname);
                        insertStatement.setString(3, housing);
                        insertStatement.setInt(4, id);
                        int result = insertStatement.executeUpdate();
                        System.out.println("Uživatel vložen.");
                        break;
                    case 2:
                        PreparedStatement checkId = con.prepareStatement("SELECT id FROM seznam WHERE id = ?");
                        PreparedStatement updateName = con.prepareStatement("UPDATE seznam SET jmeno = ? WHERE id = ?");
                        PreparedStatement updateSurname = con.prepareStatement("UPDATE seznam SET prijmeni = ? WHERE id = ?");
                        PreparedStatement updateHousing = con.prepareStatement("UPDATE seznam SET bydliste = ? WHERE id = ?");
                        PreparedStatement updateId = con.prepareStatement("UPDATE seznam SET id = ? WHERE id = ?");

                        System.out.println("---ÚPRAVA UŽIVATELE---");
                        sc.nextLine();

                        System.out.println("zadej id uživatele");
                        id = sc.nextInt();
                        checkId.setInt(1, id);
                        ResultSet rsCheck = checkId.executeQuery();

                        if (rsCheck.next()) {
                            boolean updateActive = true;
                            while (updateActive) {
                                System.out.println("co chceš zrobit?\n" +
                                        "1 - upravit jméno \n" +
                                        "2 - upravit příjmení \n" +
                                        "3 - upravit bydliště \n" +
                                        "4 - upravit id \n" +
                                        "5 - konec \n");
                                int edit = sc.nextInt();
                                sc.nextLine();
                                switch (edit) {
                                    case 1:
                                        System.out.println("zadej nové jméno");
                                        String newName = sc.nextLine();
                                        updateName.setString(1, newName);
                                        updateName.setInt(2, id);
                                        updateName.executeUpdate();
                                        break;
                                    case 2:
                                        System.out.println("zadej nové příjmení");
                                        String newSurname = sc.nextLine();
                                        updateSurname.setString(1, newSurname);
                                        updateSurname.setInt(2, id);
                                        updateSurname.executeUpdate();
                                        break;
                                    case 3:
                                        System.out.println("zadej nové bydliště");
                                        String newHousing = sc.nextLine();
                                        updateHousing.setString(1, newHousing);
                                        updateHousing.setInt(2, id);
                                        updateHousing.executeUpdate();
                                        break;
                                    case 4:
                                        System.out.println("zadej nové id");
                                        int newId = sc.nextInt();
                                        updateId.setInt(2, id);
                                        updateId.setInt(1, newId);
                                        updateId.executeUpdate();
                                        id = newId;
                                        break;
                                    case 5:
                                        updateActive = false;
                                        break;
                                }
                            }
                        } else {
                            System.out.println("toto id nemá záznam v tabulce");
                        }
                        break;
                    case 3:
                        System.out.println("---ODSTRANĚNÍ UŽIVATELE---");
                        System.out.println("zadej id uživatele, kterého chceš odstranit:");
                        id = sc.nextInt();
                        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM seznam WHERE id = ?");
                        deleteStatement.setInt(1, id);
                        int rowsAffected = deleteStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("uživatel byl odstraněn.");
                        } else {
                            System.out.println("uživatel s tímto id nebyl nalezen.");
                        }
                        break;
                    case 4:
                        running = false;
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
