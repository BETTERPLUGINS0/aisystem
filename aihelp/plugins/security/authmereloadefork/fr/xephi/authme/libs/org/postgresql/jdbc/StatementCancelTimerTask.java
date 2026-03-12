package fr.xephi.authme.libs.org.postgresql.jdbc;

import java.util.TimerTask;
import org.checkerframework.checker.nullness.qual.Nullable;

class StatementCancelTimerTask extends TimerTask {
   @Nullable
   private PgStatement statement;

   StatementCancelTimerTask(PgStatement statement) {
      this.statement = statement;
   }

   public boolean cancel() {
      boolean result = super.cancel();
      this.statement = null;
      return result;
   }

   public void run() {
      PgStatement statement = this.statement;
      if (statement != null) {
         statement.cancelIfStillNeeded(this);
      }

      this.statement = null;
   }
}
