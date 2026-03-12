package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.Provider;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutor;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import java.util.TimeZone;

class QueryExecutorTimeZoneProvider implements Provider<TimeZone> {
   private final QueryExecutor queryExecutor;

   QueryExecutorTimeZoneProvider(QueryExecutor queryExecutor) {
      this.queryExecutor = queryExecutor;
   }

   public TimeZone get() {
      TimeZone timeZone = this.queryExecutor.getTimeZone();
      if (timeZone == null) {
         throw new IllegalStateException(GT.tr("Backend timezone is not known. Backend should have returned TimeZone when establishing a connection"));
      } else {
         return timeZone;
      }
   }
}
