package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SetupQueryRunner {
   @Nullable
   public static Tuple run(QueryExecutor executor, String queryString, boolean wantResults) throws SQLException {
      Query query = executor.createSimpleQuery(queryString);
      SetupQueryRunner.SimpleResultHandler handler = new SetupQueryRunner.SimpleResultHandler();
      int flags = 1041;
      if (!wantResults) {
         flags |= 6;
      }

      try {
         executor.execute((Query)query, (ParameterList)null, (ResultHandler)handler, 0, 0, flags);
      } finally {
         query.close();
      }

      if (!wantResults) {
         return null;
      } else {
         List<Tuple> tuples = handler.getResults();
         if (tuples != null && tuples.size() == 1) {
            return (Tuple)tuples.get(0);
         } else {
            throw new PSQLException(GT.tr("An unexpected result was returned by a query."), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
         }
      }
   }

   private static class SimpleResultHandler extends ResultHandlerBase {
      @Nullable
      private List<Tuple> tuples;

      private SimpleResultHandler() {
      }

      @Nullable
      List<Tuple> getResults() {
         return this.tuples;
      }

      public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
         this.tuples = tuples;
      }

      public void handleWarning(SQLWarning warning) {
      }

      // $FF: synthetic method
      SimpleResultHandler(Object x0) {
         this();
      }
   }
}
