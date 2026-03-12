package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.concurrent.Cancellable;
import fr.xephi.authme.libs.org.apache.http.conn.ConnectionReleaseTrigger;
import fr.xephi.authme.libs.org.apache.http.conn.HttpClientConnectionManager;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Contract(
   threading = ThreadingBehavior.SAFE
)
class ConnectionHolder implements ConnectionReleaseTrigger, Cancellable, Closeable {
   private final Log log;
   private final HttpClientConnectionManager manager;
   private final HttpClientConnection managedConn;
   private final AtomicBoolean released;
   private volatile boolean reusable;
   private volatile Object state;
   private volatile long validDuration;
   private volatile TimeUnit timeUnit;

   public ConnectionHolder(Log log, HttpClientConnectionManager manager, HttpClientConnection managedConn) {
      this.log = log;
      this.manager = manager;
      this.managedConn = managedConn;
      this.released = new AtomicBoolean(false);
   }

   public boolean isReusable() {
      return this.reusable;
   }

   public void markReusable() {
      this.reusable = true;
   }

   public void markNonReusable() {
      this.reusable = false;
   }

   public void setState(Object state) {
      this.state = state;
   }

   public void setValidFor(long duration, TimeUnit timeUnit) {
      synchronized(this.managedConn) {
         this.validDuration = duration;
         this.timeUnit = timeUnit;
      }
   }

   private void releaseConnection(boolean reusable) {
      if (this.released.compareAndSet(false, true)) {
         synchronized(this.managedConn) {
            if (reusable) {
               this.manager.releaseConnection(this.managedConn, this.state, this.validDuration, this.timeUnit);
            } else {
               try {
                  this.managedConn.close();
                  this.log.debug("Connection discarded");
               } catch (IOException var9) {
                  if (this.log.isDebugEnabled()) {
                     this.log.debug(var9.getMessage(), var9);
                  }
               } finally {
                  this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
               }
            }
         }
      }

   }

   public void releaseConnection() {
      this.releaseConnection(this.reusable);
   }

   public void abortConnection() {
      if (this.released.compareAndSet(false, true)) {
         synchronized(this.managedConn) {
            try {
               this.managedConn.shutdown();
               this.log.debug("Connection discarded");
            } catch (IOException var8) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug(var8.getMessage(), var8);
               }
            } finally {
               this.manager.releaseConnection(this.managedConn, (Object)null, 0L, TimeUnit.MILLISECONDS);
            }
         }
      }

   }

   public boolean cancel() {
      boolean alreadyReleased = this.released.get();
      this.log.debug("Cancelling request execution");
      this.abortConnection();
      return !alreadyReleased;
   }

   public boolean isReleased() {
      return this.released.get();
   }

   public void close() throws IOException {
      this.releaseConnection(false);
   }
}
