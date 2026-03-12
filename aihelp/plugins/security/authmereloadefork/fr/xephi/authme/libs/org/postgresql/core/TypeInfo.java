package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.PGobject;
import java.sql.SQLException;
import java.util.Iterator;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TypeInfo {
   void addCoreType(String var1, Integer var2, Integer var3, String var4, Integer var5);

   void addDataType(String var1, Class<? extends PGobject> var2) throws SQLException;

   int getSQLType(int var1) throws SQLException;

   int getSQLType(String var1) throws SQLException;

   int getJavaArrayType(String var1) throws SQLException;

   int getPGType(String var1) throws SQLException;

   @Nullable
   String getPGType(int var1) throws SQLException;

   int getPGArrayElement(int var1) throws SQLException;

   int getPGArrayType(String var1) throws SQLException;

   char getArrayDelimiter(int var1) throws SQLException;

   Iterator<String> getPGTypeNamesWithSQLTypes();

   Iterator<Integer> getPGTypeOidsWithSQLTypes();

   @Nullable
   Class<? extends PGobject> getPGobject(String var1);

   String getJavaClass(int var1) throws SQLException;

   @Nullable
   String getTypeForAlias(String var1);

   int getPrecision(int var1, int var2);

   int getScale(int var1, int var2);

   boolean isCaseSensitive(int var1);

   boolean isSigned(int var1);

   int getDisplaySize(int var1, int var2);

   int getMaximumPrecision(int var1);

   boolean requiresQuoting(int var1) throws SQLException;

   boolean requiresQuotingSqlType(int var1) throws SQLException;

   int longOidToInt(long var1) throws SQLException;

   long intOidToLong(int var1);
}
