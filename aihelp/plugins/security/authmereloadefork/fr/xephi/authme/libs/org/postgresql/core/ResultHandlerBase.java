package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ResultHandlerBase implements ResultHandler {
   @Nullable
   private SQLException firstException;
   @Nullable
   private SQLException lastException;
   @Nullable
   private SQLWarning firstWarning;
   @Nullable
   private SQLWarning lastWarning;

   public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
   }

   public void handleCommandStatus(String status, long updateCount, long insertOID) {
   }

   public void secureProgress() {
   }

   public void handleWarning(SQLWarning warning) {
      if (this.firstWarning == null) {
         this.firstWarning = this.lastWarning = warning;
      } else {
         SQLWarning lastWarning = (SQLWarning)Nullness.castNonNull(this.lastWarning);
         lastWarning.setNextException(warning);
         this.lastWarning = warning;
      }
   }

   public void handleError(SQLException error) {
      if (this.firstException == null) {
         this.firstException = this.lastException = error;
      } else {
         ((SQLException)Nullness.castNonNull(this.lastException)).setNextException(error);
         this.lastException = error;
      }
   }

   public void handleCompletion() throws SQLException {
      SQLException firstException = this.firstException;
      if (firstException != null) {
         throw firstException;
      }
   }

   @Nullable
   public SQLException getException() {
      return this.firstException;
   }

   @Nullable
   public SQLWarning getWarning() {
      return this.firstWarning;
   }
}
