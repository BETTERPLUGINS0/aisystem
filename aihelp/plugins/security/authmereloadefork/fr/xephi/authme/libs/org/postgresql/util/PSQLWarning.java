package fr.xephi.authme.libs.org.postgresql.util;

import java.sql.SQLWarning;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PSQLWarning extends SQLWarning {
   private final ServerErrorMessage serverError;

   public PSQLWarning(ServerErrorMessage err) {
      super(err.toString(), err.getSQLState());
      this.serverError = err;
   }

   @Nullable
   public String getMessage() {
      return this.serverError.getMessage();
   }

   public ServerErrorMessage getServerErrorMessage() {
      return this.serverError;
   }
}
