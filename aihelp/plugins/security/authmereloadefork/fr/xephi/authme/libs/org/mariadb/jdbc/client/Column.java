package fr.xephi.authme.libs.org.mariadb.jdbc.client;

public interface Column {
   String getCatalog();

   String getSchema();

   String getTableAlias();

   String getTable();

   String getColumnAlias();

   String getColumnName();

   long getColumnLength();

   DataType getType();

   byte getDecimals();

   boolean isSigned();

   int getDisplaySize();

   boolean isPrimaryKey();

   boolean isAutoIncrement();

   boolean hasDefault();

   boolean isBinary();

   int getFlags();

   String getExtTypeName();
}
