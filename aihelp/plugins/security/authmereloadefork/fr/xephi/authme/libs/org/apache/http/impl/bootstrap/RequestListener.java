package fr.xephi.authme.libs.org.apache.http.impl.bootstrap;

import fr.xephi.authme.libs.org.apache.http.ExceptionLogger;
import fr.xephi.authme.libs.org.apache.http.HttpConnectionFactory;
import fr.xephi.authme.libs.org.apache.http.HttpServerConnection;
import fr.xephi.authme.libs.org.apache.http.config.SocketConfig;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

class RequestListener implements Runnable {
   private final SocketConfig socketConfig;
   private final ServerSocket serversocket;
   private final HttpService httpService;
   private final HttpConnectionFactory<? extends HttpServerConnection> connectionFactory;
   private final ExceptionLogger exceptionLogger;
   private final ExecutorService executorService;
   private final AtomicBoolean terminated;

   public RequestListener(SocketConfig socketConfig, ServerSocket serversocket, HttpService httpService, HttpConnectionFactory<? extends HttpServerConnection> connectionFactory, ExceptionLogger exceptionLogger, ExecutorService executorService) {
      this.socketConfig = socketConfig;
      this.serversocket = serversocket;
      this.connectionFactory = connectionFactory;
      this.httpService = httpService;
      this.exceptionLogger = exceptionLogger;
      this.executorService = executorService;
      this.terminated = new AtomicBoolean(false);
   }

   public void run() {
      while(true) {
         try {
            if (!this.isTerminated() && !Thread.interrupted()) {
               Socket socket = this.serversocket.accept();
               socket.setSoTimeout(this.socketConfig.getSoTimeout());
               socket.setKeepAlive(this.socketConfig.isSoKeepAlive());
               socket.setTcpNoDelay(this.socketConfig.isTcpNoDelay());
               if (this.socketConfig.getRcvBufSize() > 0) {
                  socket.setReceiveBufferSize(this.socketConfig.getRcvBufSize());
               }

               if (this.socketConfig.getSndBufSize() > 0) {
                  socket.setSendBufferSize(this.socketConfig.getSndBufSize());
               }

               if (this.socketConfig.getSoLinger() >= 0) {
                  socket.setSoLinger(true, this.socketConfig.getSoLinger());
               }

               HttpServerConnection conn = (HttpServerConnection)this.connectionFactory.createConnection(socket);
               Worker worker = new Worker(this.httpService, conn, this.exceptionLogger);
               this.executorService.execute(worker);
               continue;
            }
         } catch (Exception var4) {
            this.exceptionLogger.log(var4);
         }

         return;
      }
   }

   public boolean isTerminated() {
      return this.terminated.get();
   }

   public void terminate() throws IOException {
      if (this.terminated.compareAndSet(false, true)) {
         this.serversocket.close();
      }

   }
}
