package fr.xephi.authme.libs.org.postgresql.copy;

import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CopyOut extends CopyOperation {
   @Nullable
   byte[] readFromCopy() throws SQLException;

   @Nullable
   byte[] readFromCopy(boolean var1) throws SQLException;
}
