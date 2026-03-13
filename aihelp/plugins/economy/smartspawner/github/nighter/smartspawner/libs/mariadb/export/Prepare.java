package github.nighter.smartspawner.libs.mariadb.export;

import github.nighter.smartspawner.libs.mariadb.BasePreparedStatement;
import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.ColumnDecoder;
import java.sql.SQLException;

public interface Prepare {
   void close(Client var1) throws SQLException;

   void decrementUse(Client var1, BasePreparedStatement var2) throws SQLException;

   int getStatementId();

   ColumnDecoder[] getParameters();

   ColumnDecoder[] getColumns();

   void setColumns(ColumnDecoder[] var1);
}
