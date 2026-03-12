package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.Utils;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.sql.SQLException;
import java.sql.Savepoint;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PSQLSavepoint implements Savepoint {
   private boolean isValid = true;
   private final boolean isNamed;
   private int id;
   @Nullable
   private String name;

   public PSQLSavepoint(int id) {
      this.isNamed = false;
      this.id = id;
   }

   public PSQLSavepoint(String name) {
      this.isNamed = true;
      this.name = name;
   }

   public int getSavepointId() throws SQLException {
      if (!this.isValid) {
         throw new PSQLException(GT.tr("Cannot reference a savepoint after it has been released."), PSQLState.INVALID_SAVEPOINT_SPECIFICATION);
      } else if (this.isNamed) {
         throw new PSQLException(GT.tr("Cannot retrieve the id of a named savepoint."), PSQLState.WRONG_OBJECT_TYPE);
      } else {
         return this.id;
      }
   }

   public String getSavepointName() throws SQLException {
      if (!this.isValid) {
         throw new PSQLException(GT.tr("Cannot reference a savepoint after it has been released."), PSQLState.INVALID_SAVEPOINT_SPECIFICATION);
      } else if (this.isNamed && this.name != null) {
         return this.name;
      } else {
         throw new PSQLException(GT.tr("Cannot retrieve the name of an unnamed savepoint."), PSQLState.WRONG_OBJECT_TYPE);
      }
   }

   public void invalidate() {
      this.isValid = false;
   }

   public String getPGName() throws SQLException {
      if (!this.isValid) {
         throw new PSQLException(GT.tr("Cannot reference a savepoint after it has been released."), PSQLState.INVALID_SAVEPOINT_SPECIFICATION);
      } else {
         return this.isNamed && this.name != null ? Utils.escapeIdentifier((StringBuilder)null, this.name).toString() : "JDBC_SAVEPOINT_" + this.id;
      }
   }
}
