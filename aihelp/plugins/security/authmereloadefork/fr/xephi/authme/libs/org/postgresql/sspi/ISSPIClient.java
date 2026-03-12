package fr.xephi.authme.libs.org.postgresql.sspi;

import java.io.IOException;
import java.sql.SQLException;

public interface ISSPIClient {
   boolean isSSPISupported();

   void startSSPI() throws SQLException, IOException;

   void continueSSPI(int var1) throws SQLException, IOException;

   void dispose();
}
