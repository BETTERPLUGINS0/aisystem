package fr.xephi.authme.libs.org.postgresql.util;

import fr.xephi.authme.libs.org.postgresql.Driver;
import java.net.URL;

public class PGJDBCMain {
   public static void main(String[] args) {
      URL url = Driver.class.getResource("/fr/xephi/authme/libs/org/postgresql/Driver.class");
      System.out.printf("%n%s%n", "PostgreSQL JDBC Driver 42.7.3");
      System.out.printf("Found in: %s%n%n", url);
      System.out.printf("The PgJDBC driver is not an executable Java program.%n%nYou must install it according to the JDBC driver installation instructions for your application / container / appserver, then use it by specifying a JDBC URL of the form %n    jdbc:postgresql://%nor using an application specific method.%n%nSee the PgJDBC documentation: http://jdbc.postgresql.org/documentation/head/index.html%n%nThis command has had no effect.%n");
      System.exit(1);
   }
}
