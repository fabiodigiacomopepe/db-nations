package org.lessons.java.sql;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Inizializzo lo scanner da tastiera
        Scanner scan = new Scanner(System.in);

        // Parametri di connessione
        String url = "jdbc:mysql://localhost:3306/db-nations";
        String user = "root";
        String password = "root";

        // Provo ad aprire una connection con try-with-resources
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Dentro al try ho la connection aperta

            System.out.print("Inserisci un parametro di ricerca: ");
            String parametroRicerca = scan.nextLine();

            // Creo la query
            String query = "SELECT c.name AS nome, c.country_id AS id, r.name AS nome_della_regione, c2.name AS nome_del_continente " +
                    "FROM countries c " +
                    "JOIN regions r ON r.region_id = c.region_id " +
                    "JOIN continents c2 ON c2.continent_id = r.continent_id " +
                    "WHERE c.name LIKE ?" +
                    "ORDER BY c.name ASC;";

            // La connection prepara uno statement sql
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Faccio il binding dei parametri
                preparedStatement.setString(1, "%" + parametroRicerca + "%");

                // Eseguo il prepared statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Itero sul result set
                    while (resultSet.next()) {
                        // Ad ogni iterazione resultSet si sposta e punta alla riga successiva
                        String nomeNazione = resultSet.getString("nome");
                        int idNazione = resultSet.getInt("id");
                        String nomeRegione = resultSet.getString("nome_della_regione");
                        String nomeContinente = resultSet.getString("nome_del_continente");
                        // Stampo la riga
                        System.out.println(nomeNazione + " - " + idNazione + " - " + nomeRegione + " - " + nomeContinente);
                    }
                } catch (SQLException e) {
                    System.out.println("Impossibile eseguire la query.");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Impossibile preparare la query.");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Impossibile aprire una connessione.");
            e.printStackTrace();
        }

        // Chiudo lo scanner da tastiera
        scan.close();
    }
}
