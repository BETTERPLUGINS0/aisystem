package fr.xephi.authme.libs.com.mysql.cj.jdbc.result;

import fr.xephi.authme.libs.com.mysql.cj.result.DefaultColumnDefinition;

public class CachedResultSetMetaDataImpl extends DefaultColumnDefinition implements CachedResultSetMetaData {
   java.sql.ResultSetMetaData metadata;

   public java.sql.ResultSetMetaData getMetadata() {
      return this.metadata;
   }

   public void setMetadata(java.sql.ResultSetMetaData metadata) {
      this.metadata = metadata;
   }
}
