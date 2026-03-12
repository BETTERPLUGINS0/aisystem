package fr.xephi.authme.libs.com.mysql.cj.protocol;

import fr.xephi.authme.libs.com.mysql.cj.MysqlConnection;
import fr.xephi.authme.libs.com.mysql.cj.Query;
import fr.xephi.authme.libs.com.mysql.cj.Session;

public interface ResultsetRowsOwner {
   void closeOwner(boolean var1);

   MysqlConnection getConnection();

   Session getSession();

   Object getSyncMutex();

   String getPointOfOrigin();

   int getOwnerFetchSize();

   Query getOwningQuery();

   int getOwningStatementMaxRows();

   int getOwningStatementFetchSize();

   long getOwningStatementServerId();
}
