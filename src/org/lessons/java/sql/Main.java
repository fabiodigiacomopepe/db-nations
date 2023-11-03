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
                        System.out.println(idNazione + " - " + nomeNazione + " - " + nomeRegione + " - " + nomeContinente);
                    }
                } catch (SQLException e) {
                    System.out.println("Impossibile eseguire la query.");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Impossibile preparare la query.");
                e.printStackTrace();
            }

            System.out.print("Inserisci un id: ");
            String parametroId = scan.nextLine();

            // Creo la query
            query = "SELECT l.`language` " +
                    "FROM countries c " +
                    "JOIN country_languages cl ON cl.country_id = c.country_id " +
                    "JOIN languages l ON l.language_id = cl.language_id " +
                    "WHERE c.country_id = ?";

            // La connection prepara uno statement sql
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Faccio il binding dei parametri
                preparedStatement.setString(1, parametroId);

                // Eseguo il prepared statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Itero sul result set
                    String stringaFinale = "Lingue: ";
                    while (resultSet.next()) {
                        // Ad ogni iterazione resultSet si sposta e punta alla riga successiva
                        String lingua = resultSet.getString("language");
                        // Concateno alla stringa la nuova lingua
                        stringaFinale += lingua + ", ";
                    }
                    // Rimuovo virgola alla fine
                    stringaFinale = stringaFinale.substring(0, stringaFinale.length() - 2);
                    // Stampo la stringa
                    System.out.println(stringaFinale);
                } catch (SQLException e) {
                    System.out.println("Impossibile eseguire la query.");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Impossibile preparare la query.");
                e.printStackTrace();
            }

            // Creo la query
            query = "SELECT cs.`year`, cs.population, cs.gdp " +
                    "FROM countries c " +
                    "JOIN country_stats cs ON cs.country_id = c.country_id " +
                    "WHERE c.country_id = ?" +
                    "ORDER BY cs.`year` DESC " +
                    "LIMIT 1;";

            // La connection prepara uno statement sql
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Faccio il binding dei parametri
                preparedStatement.setString(1, parametroId);

                // Eseguo il prepared statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Itero sul result set
                    while (resultSet.next()) {
                        // Ad ogni iterazione resultSet si sposta e punta alla riga successiva
                        String anno = resultSet.getString("year");
                        String popolazione = resultSet.getString("population");
                        String gdp = resultSet.getString("gdp");

                        // Stampo la stringa
                        System.out.println("Statistiche più recenti");
                        System.out.println("Anno: " + anno);
                        System.out.println("Popolazione: " + popolazione);
                        System.out.println("GDP: " + gdp);
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
