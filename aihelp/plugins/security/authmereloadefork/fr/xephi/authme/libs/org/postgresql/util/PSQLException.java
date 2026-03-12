package fr.xephi.authme.libs.org.postgresql.util;

import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

public class PSQLException extends SQLException {
   @Nullable
   private ServerErrorMessage serverError;

   @Pure
   public PSQLException(@Nullable String msg, @Nullable PSQLState state, @Nullable Throwable cause) {
      super(msg, state == null ? null : state.getState(), cause);
   }

   @Pure
   public PSQLException(@Nullable String msg, @Nullable PSQLState state) {
      super(msg, state == null ? null : state.getState());
   }

   @Pure
   public PSQLException(ServerErrorMessage serverError) {
      this(serverError, true);
   }

   @Pure
   public PSQLException(ServerErrorMessage serverError, boolean detail) {
      super(detail ? serverError.toString() : serverError.getNonSensitiveErrorMessage(), serverError.getSQLState());
      this.serverError = serverError;
   }

   @Pure
   @Nullable
   public ServerErrorMessage getServerErrorMessage() {
      return this.serverError;
   }
}
