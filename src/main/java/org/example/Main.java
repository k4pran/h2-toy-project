package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class Main {
    public static void main(String[] args) {
        // Change the JDBC URL to use an embedded database file
        String jdbcUrl = "jdbc:h2:./database/mydb;MV_STORE=FALSE;TRACE_LEVEL_FILE=0";
        String username = "sa";
        String password = "";

        System.setProperty("liquibase.secureParsing", "false");

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            System.out.println("Connected to H2 database");

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new liquibase.database.jvm.JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());

            System.out.println("Database updated with Liquibase");
        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }
}
