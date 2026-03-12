package fr.xephi.authme.libs.org.mariadb.jdbc.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.HostAddress;
import fr.xephi.authme.libs.org.mariadb.jdbc.Statement;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.ExceptionFactory;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.Prepare;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;

public interface Client extends AutoCloseable {
   List<Completion> execute(ClientMessage var1, boolean var2) throws SQLException;

   List<Completion> execute(ClientMessage var1, Statement var2, boolean var3) throws SQLException;

   List<Completion> execute(ClientMessage var1, Statement var2, int var3, long var4, int var6, int var7, boolean var8, boolean var9) throws SQLException;

   List<Completion> executePipeline(ClientMessage[] var1, Statement var2, int var3, long var4, int var6, int var7, boolean var8, boolean var9) throws SQLException;

   void readStreamingResults(List<Completion> var1, int var2, long var3, int var5, int var6, boolean var7) throws SQLException;

   void closePrepare(Prepare var1) throws SQLException;

   void abort(Executor var1) throws SQLException;

   void close() throws SQLException;

   void setReadOnly(boolean var1) throws SQLException;

   int getSocketTimeout();

   void setSocketTimeout(int var1) throws SQLException;

   boolean isClosed();

   void reset();

   boolean isPrimary();

   Context getContext();

   ExceptionFactory getExceptionFactory();

   HostAddress getHostAddress();

   String getSocketIp();
}
