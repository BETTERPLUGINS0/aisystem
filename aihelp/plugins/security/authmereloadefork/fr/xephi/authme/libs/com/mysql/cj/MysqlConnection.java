package fr.xephi.authme.libs.com.mysql.cj;

import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSessionStateController;
import java.util.Properties;

public interface MysqlConnection {
   PropertySet getPropertySet();

   void createNewIO(boolean var1);

   long getId();

   Properties getProperties();

   Object getConnectionMutex();

   Session getSession();

   String getURL();

   String getUser();

   ExceptionInterceptor getExceptionInterceptor();

   void checkClosed();

   void normalClose();

   void cleanup(Throwable var1);

   ServerSessionStateController getServerSessionStateController();
}
