package fr.xephi.authme.libs.org.mariadb.jdbc.util;

public interface PrepareResult {
   String getSql();

   int getParamCount();
}
