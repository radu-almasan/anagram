package com.radualmasan.anagram.strategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An {@link AnagramStrategy} backed by a database.
 *
 * @author almasan.radu@gmail.com
 */
public class DbAnagramStrategy implements AnagramStrategy, AutoCloseable {

    /** The DB connection. */
    private final Connection conn;
    /** The word insert sttatement. */
    private final PreparedStatement insertStmt;
    /** The word lookup statement. */
    private final PreparedStatement lookupStmt;

    /**
     * Create a new instance.
     */
    public DbAnagramStrategy() {
        try {
            Class.forName("org.h2.Driver");
            final String dbName = UUID.randomUUID().toString();
            conn = DriverManager.getConnection(String.format("jdbc:h2:~/%s", dbName), "sa", "");

            final Statement stmt = conn.createStatement();
            stmt.execute(
                    "CREATE TABLE words (id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, word VARCHAR(255) NOT NULL, parent INT)");

            lookupStmt = conn.prepareStatement("SELECT * FROM words WHERE word = ?");
            insertStmt = conn.prepareStatement("INSERT INTO words (word, parent) VALUES (?, ?)");
        } catch (final ClassNotFoundException | SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    @Override
    public Stream<Set<String>> getAnagramPairs() {
        final Spliterators.AbstractSpliterator<Set<String>> spliterator = new Spliterators.AbstractSpliterator<Set<String>>(
                Integer.MAX_VALUE, 0) {

            @Override
            public boolean tryAdvance(Consumer<? super Set<String>> action) {
                try (Statement stmt = conn.createStatement()) {
                    final ResultSet parentsResultSet = stmt.executeQuery("SELECT * FROM words w "
                            + "WHERE parent IS NULL AND (SELECT COUNT(1) FROM words w2 WHERE w2.parent = w.id) > 1"
                            + "ORDER BY LENGTH(word)");

                    final PreparedStatement pairStmt = conn.prepareStatement("SELECT * FROM words WHERE parent = ?");
                    while (parentsResultSet.next()) {
                        final int parentId = parentsResultSet.getInt("id");

                        pairStmt.setInt(1, parentId);
                        final ResultSet pairResutlSet = pairStmt.executeQuery();
                        final Set<String> anagramPair = new HashSet<>();
                        while (pairResutlSet.next()) {
                            anagramPair.add(pairResutlSet.getString("word"));
                        }

                        action.accept(anagramPair);
                    }

                } catch (final SQLException e) {
                    throw new IllegalStateException(e);
                }

                return false;
            }
        };

        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public boolean ingest(String word) throws Exception {
        if (word.length() < 2) {
            return false;
        }

        lookupStmt.setString(1, word);
        if (lookupStmt.executeQuery().next()) {
            return false;
        }

        final String sortedChars = getSortedChars(word);
        lookupStmt.setString(1, sortedChars);
        final ResultSet resultSet = lookupStmt.executeQuery();

        final Integer parentId;
        if (resultSet.next()) {
            parentId = resultSet.getInt("id");

        } else {
            insertStmt.setString(1, sortedChars);
            insertStmt.setObject(2, null);
            insertStmt.executeUpdate();
            final ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            generatedKeys.next();
            parentId = generatedKeys.getInt(1);
        }

        insertStmt.setString(1, word);
        insertStmt.setObject(2, parentId);
        insertStmt.executeUpdate();

        return true;
    }
}
