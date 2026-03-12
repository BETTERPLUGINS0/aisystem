package fr.xephi.authme.libs.org.mariadb.jdbc.export;

import fr.xephi.authme.libs.org.mariadb.jdbc.ServerPreparedStatement;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Client;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ColumnDecoder;
import java.sql.SQLException;

public interface Prepare {
   void close(Client var1) throws SQLException;

   void decrementUse(Client var1, ServerPreparedStatement var2) throws SQLException;

   int getStatementId();

   ColumnDecoder[] getParameters();

   ColumnDecoder[] getColumns();

   void setColumns(ColumnDecoder[] var1);
}
