package fr.xephi.authme.libs.org.postgresql.copy;

import java.sql.SQLException;

public interface CopyOperation {
   int getFieldCount();

   int getFormat();

   int getFieldFormat(int var1);

   boolean isActive();

   void cancelCopy() throws SQLException;

   long getHandledRowCount();
}
