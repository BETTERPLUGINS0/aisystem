package fr.xephi.authme.libs.com.mysql.cj;

import fr.xephi.authme.libs.com.mysql.cj.conf.HostInfo;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJOperationNotSupportedException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.log.Log;
import fr.xephi.authme.libs.com.mysql.cj.log.ProfilerEventHandler;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Message;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultBuilder;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ServerSession;
import fr.xephi.authme.libs.com.mysql.cj.result.Row;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public interface Session {
   PropertySet getPropertySet();

   <M extends Message> MessageBuilder<M> getMessageBuilder();

   void changeUser(String var1, String var2, String var3);

   ExceptionInterceptor getExceptionInterceptor();

   void setExceptionInterceptor(ExceptionInterceptor var1);

   void quit();

   void forceClose();

   boolean versionMeetsMinimum(int var1, int var2, int var3);

   long getThreadId();

   boolean isSetNeededForAutoCommitMode(boolean var1);

   Log getLog();

   ProfilerEventHandler getProfilerEventHandler();

   HostInfo getHostInfo();

   String getQueryTimingUnits();

   ServerSession getServerSession();

   boolean isSSLEstablished();

   SocketAddress getRemoteSocketAddress();

   String getProcessHost();

   void addListener(Session.SessionEventListener var1);

   void removeListener(Session.SessionEventListener var1);

   boolean isClosed();

   String getIdentifierQuoteString();

   DataStoreMetadata getDataStoreMetadata();

   default <M extends Message, R, RES> RES query(M message, Predicate<Row> rowFilter, Function<Row, R> rowMapper, Collector<R, ?, RES> collector) {
      throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
   }

   default <M extends Message, R extends QueryResult> R query(M message, ResultBuilder<R> resultBuilder) {
      throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
   }

   default <M extends Message, R extends QueryResult> CompletableFuture<R> queryAsync(M message, ResultBuilder<R> resultBuilder) {
      throw (CJOperationNotSupportedException)ExceptionFactory.createException(CJOperationNotSupportedException.class, "Not supported");
   }

   public interface SessionEventListener {
      void handleNormalClose();

      void handleReconnect();

      void handleCleanup(Throwable var1);
   }
}
