package fr.xephi.authme.libs.org.postgresql.core;

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ResultHandler {
   void handleResultRows(Query var1, Field[] var2, List<Tuple> var3, @Nullable ResultCursor var4);

   void handleCommandStatus(String var1, long var2, long var4);

   void handleWarning(SQLWarning var1);

   void handleError(SQLException var1);

   void handleCompletion() throws SQLException;

   void secureProgress();

   @Nullable
   SQLException getException();

   @Nullable
   SQLWarning getWarning();
}
