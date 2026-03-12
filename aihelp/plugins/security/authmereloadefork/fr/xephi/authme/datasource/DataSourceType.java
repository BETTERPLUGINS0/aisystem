package fr.xephi.authme.datasource;

public enum DataSourceType {
   H2,
   MYSQL,
   MARIADB,
   POSTGRESQL,
   SQLITE;

   // $FF: synthetic method
   private static DataSourceType[] $values() {
      return new DataSourceType[]{H2, MYSQL, MARIADB, POSTGRESQL, SQLITE};
   }
}
