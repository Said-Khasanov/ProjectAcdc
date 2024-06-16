package com.javarush.khasanov.util;

import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.util.Properties;

import static com.javarush.khasanov.config.Config.*;

public class LiquibaseInit {
    public static void main(String[] args) throws Exception {
        System.out.println("Running Liquibase...");

        Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
            CommandScope update = new CommandScope("update");

            update.addArgumentValue("changelogFile", "db/changelog/2024/06/15-01-changelog.xml");
            update.addArgumentValue("url", "jdbc:postgresql://localhost:5432/dev");
            update.addArgumentValue("username", "admin");
            update.addArgumentValue("password", "admin");

            update.execute();
        });

        System.out.println("Running Liquibase...DONE");
    }

    public static void runWithProperties(Properties properties) throws Exception {
        System.out.println("Running Liquibase...");

        Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
            CommandScope update = new CommandScope("update");

            update.addArgumentValue("changelogFile", "db/changelog/2024/06/15-01-changelog.xml");
            update.addArgumentValue("url", properties.getProperty(HIBERNATE_CONNECTION_URL));
            update.addArgumentValue("username", properties.getProperty(HIBERNATE_CONNECTION_USERNAME));
            update.addArgumentValue("password", properties.getProperty(HIBERNATE_CONNECTION_PASSWORD));

            update.execute();
        });

        System.out.println("Running Liquibase...DONE");
    }
}
