package com.example.hivesampling.persistence;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseEvidenceIT {
    private static final Path REPORT_DIR = Path.of("reports", "database");
    private static final String URL = "jdbc:h2:file:./target/evidence-db/scanning_ttp;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=FALSE";

    @Test
    void migratesAndExportsSchemaAndRelationships() throws Exception {
        Files.createDirectories(REPORT_DIR);
        Flyway flyway = Flyway.configure()
                .dataSource(URL, "sa", "")
                .locations("classpath:db/migration")
                .cleanDisabled(false)
                .load();
        flyway.clean();
        int migrations = flyway.migrate().migrationsExecuted;
        assertEquals(4, migrations);

        try (Connection connection = DriverManager.getConnection(URL, "sa", "")) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SCRIPT TO 'reports/database/schema.sql'");
            }
            List<String> expected = List.of(
                    "task_run -> validation_task (1:N)",
                    "sampling -> task_run (1:N)",
                    "shard_task -> sampling (1:N)",
                    "task_log -> task_run (1:N)",
                    "audit_event -> task_run (1:N)");
            List<String> actual = foreignKeys(connection);
            for (String relationship : expected) {
                assertTrue(actual.contains(relationship), "Missing foreign key: " + relationship + "; actual=" + actual);
            }
            List<String> reportLines = new ArrayList<>();
            reportLines.add("Flyway executed migrations: " + migrations);
            reportLines.add("Verified physical foreign keys (child -> parent; parent relationship is 1:N):");
            reportLines.addAll(expected);
            reportLines.add("Validation: PASS");
            Files.writeString(REPORT_DIR.resolve("relationship-verification.txt"), String.join("\n", reportLines) + "\n", StandardCharsets.UTF_8);
        }
    }

    private List<String> foreignKeys(Connection connection) throws Exception {
        List<String> relationships = new ArrayList<>();
        String[] tables = {"task_run", "sampling", "shard_task", "task_log", "audit_event"};
        DatabaseMetaData metadata = connection.getMetaData();
        for (String table : tables) {
            try (ResultSet keys = metadata.getImportedKeys(null, null, table)) {
                while (keys.next()) {
                    String parent = keys.getString("PKTABLE_NAME");
                    relationships.add(table + " -> " + parent + " (1:N)");
                }
            }
        }
        return relationships;
    }
}
