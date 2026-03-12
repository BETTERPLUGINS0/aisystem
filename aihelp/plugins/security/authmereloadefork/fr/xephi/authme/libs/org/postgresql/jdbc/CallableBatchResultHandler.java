package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.ResultCursor;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

class CallableBatchResultHandler extends BatchResultHandler {
   CallableBatchResultHandler(PgStatement statement, Query[] queries, ParameterList[] parameterLists) {
      super(statement, queries, parameterLists, false);
   }

   public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
   }
}
