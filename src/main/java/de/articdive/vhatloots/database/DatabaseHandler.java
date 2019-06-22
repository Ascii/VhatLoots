/*
 * VhatLoots
 * Copyright (C) 2019 Lukas Mansour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copyFile of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.articdive.vhatloots.database;

import de.articdive.enum_to_configuration.EnumConfiguration;
import de.articdive.vhatloots.VhatLoots;
import de.articdive.vhatloots.configuration.CoreConfiguration;
import de.articdive.vhatloots.exceptions.IllegalFileException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.javalite.activejdbc.DB;
import org.javalite.activejdbc.annotations.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lukas Mansour
 */
public class DatabaseHandler {
    private static final Pattern xmlPattern = Pattern.compile(".*/(.*?)/.*");
    private static DatabaseHandler instance = null;
    private final VhatLoots main = VhatLoots.getPlugin(VhatLoots.class);
    private final EnumConfiguration config = main.getConfiguration();
    private final String dbTablePrefix = (String) config.get(CoreConfiguration.DATABASE_TABLE_PREFIX);
    private final DB db = new DB("VhatLoots");
    private String driver;
    private String jdbcURL;
    private String username = (String) config.get(CoreConfiguration.DATABASE_USERNAME);
    private String password = (String) config.get(CoreConfiguration.DATABASE_PASSWORD);
    
    private DatabaseHandler() {
        String dbType = (String) config.get(CoreConfiguration.DATABASE_TYPE);
        switch (dbType.toLowerCase()) {
            case "h2": {
                File dbFolder = new File((String) config.get(CoreConfiguration.DATABASE_HOSTNAME));
                if (!dbFolder.mkdirs() && !dbFolder.isDirectory()) {
                    throw new IllegalFileException("The database folder was a file!");
                }
                driver = "org.h2.Driver";
                jdbcURL = "jdbc:h2:file:" + dbFolder
                        + File.separator + config.get(CoreConfiguration.DATABASE_SCHEMA_NAME)
                        + ";AUTO_SERVER=TRUE";
                username = "";
                password = "";
                break;
            }
            case "sqlite": {
                File dbFolder = new File((String) config.get(CoreConfiguration.DATABASE_HOSTNAME));
                if (!dbFolder.mkdirs() && !dbFolder.isDirectory()) {
                    throw new IllegalFileException("The database directory was a file!");
                }
                driver = "org.sqlite.JDBC";
                jdbcURL = "jdbc:sqlite:" + dbFolder
                        + "/" + config.get(CoreConfiguration.DATABASE_SCHEMA_NAME);
                break;
            }
            case "mysql": {
                driver = "com.mysql.jdbc.Driver";
                jdbcURL = "jdbc:mysql://" + config.get(CoreConfiguration.DATABASE_HOSTNAME)
                        + "/" + config.get(CoreConfiguration.DATABASE_SCHEMA_NAME)
                        + "?verifyServerCertificate=false&useSSL=false&useUnicode=true&characterEncoding=utf-8";
                break;
            }
            case "postgresql": {
                driver = "org.postgresql.Driver";
                jdbcURL = "jdbc:postgresql://" + config.get(CoreConfiguration.DATABASE_HOSTNAME)
                        + "/" + config.get(CoreConfiguration.DATABASE_SCHEMA_NAME);
                break;
            }
            case "sqlserver": {
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                jdbcURL = "jdbc:microsoft:sqlserver://" + config.get(CoreConfiguration.DATABASE_HOSTNAME)
                        + "/" + config.get(CoreConfiguration.DATABASE_SCHEMA_NAME);
                break;
            }
            default: {
                throw new IllegalArgumentException(dbType + " is not a valid database type!");
            }
        }
        // Use classgraph to get all @Table classes.
        // Use the setAnnotation method to update their tables to add the prefix.
        try (ScanResult scanResult = new ClassGraph()
                .addClassLoader(this.getClass().getClassLoader())
                .enableClassInfo()
                .ignoreClassVisibility()
                .enableAnnotationInfo()
                .whitelistPackages("de.articdive.vhatloots.objects")
                .disableDirScanning()
                .disableNestedJarScanning()
                .disableModuleScanning()
                .scan()) {
            scanResult.getClassesWithAnnotation("org.javalite.activejdbc.annotations.Table")
                    .forEach(classInfo -> {
                        try {
                            setTable(classInfo.loadClass());
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
        }
        
        // Connect to DB.
        db.open(driver, jdbcURL, username, password);
        // Liquibase generate Schema.
        generateSchema(db.connection());
        db.close();
    }
    
    public void open() {
        db.open(driver, jdbcURL, username, password);
    }
    
    public void close() {
        db.close();
    }
    
    @SuppressWarnings("ConstantConditions")
    private void generateSchema(Connection connection) {
        try {
            File databaseChangelog = new File(main.getDataFolder() + File.separator + "database" + File.separator + "database-schema.xml");
            if (!databaseChangelog.getParentFile().mkdirs() && !databaseChangelog.getParentFile().isDirectory()) {
                return;
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            this.getClass().getClassLoader().getResourceAsStream("database-schema.xml")
                    )
            );
            PrintWriter writer = new PrintWriter(new FileOutputStream(databaseChangelog));
            
            String str;
            int lineNumber = 1;
            while ((str = reader.readLine()) != null) {
                if (lineNumber >= 3 && lineNumber <= 5) {
                    Matcher m = xmlPattern.matcher(str);
                    if (m.find()) {
                        str = str.replace("/" + m.group(1) + "/", dbTablePrefix + m.group(1));
                    }
                }
                writer.println(str);
                lineNumber = lineNumber + 1;
            }
            writer.close();
            reader.close();
            
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    databaseChangelog.getAbsolutePath()
                    , new FileSystemResourceAccessor(), database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException ignored) {
        
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void setTable(Class clazz) throws NoSuchFieldException, IllegalAccessException {
        Table oldAnnotation = (Table) clazz.getAnnotations()[0];
        Annotation newAnnotation = new Table() {
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return oldAnnotation.annotationType();
            }
            
            @Override
            public String value() {
                return dbTablePrefix + oldAnnotation.value();
            }
        };
        Field annotationDataField = Class.class.getDeclaredField("annotationData");
        annotationDataField.setAccessible(true);
        
        Object annotationData = annotationDataField.get(clazz);
        
        Field annotationsField = annotationData.getClass().getDeclaredField("annotations");
        annotationsField.setAccessible(true);
        
        Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) annotationsField
                .get(annotationData);
        annotations.put(Table.class, newAnnotation);
    }
    
    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
}
