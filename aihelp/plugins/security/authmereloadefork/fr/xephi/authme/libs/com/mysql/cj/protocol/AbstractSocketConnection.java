package fr.xephi.authme.libs.com.mysql.cj.protocol;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertySet;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.UnableToConnectException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import fr.xephi.authme.libs.com.mysql.jdbc.SocketFactoryWrapper;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class AbstractSocketConnection implements SocketConnection {
   protected String host = null;
   protected int port = 3306;
   protected SocketFactory socketFactory = null;
   protected Socket mysqlSocket = null;
   protected FullReadInputStream mysqlInput = null;
   protected BufferedOutputStream mysqlOutput = null;
   protected ExceptionInterceptor exceptionInterceptor;
   protected PropertySet propertySet;

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public Socket getMysqlSocket() {
      return this.mysqlSocket;
   }

   public FullReadInputStream getMysqlInput() throws IOException {
      if (this.mysqlInput != null) {
         return this.mysqlInput;
      } else {
         throw new IOException(Messages.getString("SocketConnection.1"));
      }
   }

   public void setMysqlInput(FullReadInputStream mysqlInput) {
      this.mysqlInput = mysqlInput;
   }

   public BufferedOutputStream getMysqlOutput() throws IOException {
      if (this.mysqlOutput != null) {
         return this.mysqlOutput;
      } else {
         throw new IOException(Messages.getString("SocketConnection.1"));
      }
   }

   public boolean isSSLEstablished() {
      return ExportControlled.enabled() && ExportControlled.isSSLEstablished(this.getMysqlSocket());
   }

   public SocketFactory getSocketFactory() {
      return this.socketFactory;
   }

   public void setSocketFactory(SocketFactory socketFactory) {
      this.socketFactory = socketFactory;
   }

   public void forceClose() {
      try {
         this.getNetworkResources().forceClose();
      } finally {
         this.mysqlSocket = null;
         this.mysqlInput = null;
         this.mysqlOutput = null;
      }

   }

   public NetworkResources getNetworkResources() {
      return new NetworkResources(this.mysqlSocket, this.mysqlInput, this.mysqlOutput);
   }

   public ExceptionInterceptor getExceptionInterceptor() {
      return this.exceptionInterceptor;
   }

   public PropertySet getPropertySet() {
      return this.propertySet;
   }

   protected SocketFactory createSocketFactory(String socketFactoryClassName) {
      if (socketFactoryClassName == null) {
         throw (UnableToConnectException)ExceptionFactory.createException(UnableToConnectException.class, Messages.getString("SocketConnection.0"), this.getExceptionInterceptor());
      } else {
         try {
            return (SocketFactory)Util.getInstance(SocketFactory.class, socketFactoryClassName, (Class[])null, (Object[])null, this.getExceptionInterceptor());
         } catch (WrongArgumentException var5) {
            if (var5.getCause() == null) {
               try {
                  return new SocketFactoryWrapper((fr.xephi.authme.libs.com.mysql.jdbc.SocketFactory)Util.getInstance(fr.xephi.authme.libs.com.mysql.jdbc.SocketFactory.class, socketFactoryClassName, (Class[])null, (Object[])null, this.getExceptionInterceptor()));
               } catch (Exception var4) {
                  throw var5;
               }
            } else {
               throw var5;
            }
         }
      }
   }
}
