package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.HttpClient;
import fr.xephi.authme.libs.org.apache.http.client.ResponseHandler;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpUriRequest;
import fr.xephi.authme.libs.org.apache.http.concurrent.FutureCallback;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Contract(
   threading = ThreadingBehavior.SAFE
)
public class FutureRequestExecutionService implements Closeable {
   private final HttpClient httpclient;
   private final ExecutorService executorService;
   private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
   private final AtomicBoolean closed = new AtomicBoolean(false);

   public FutureRequestExecutionService(HttpClient httpclient, ExecutorService executorService) {
      this.httpclient = httpclient;
      this.executorService = executorService;
   }

   public <T> HttpRequestFutureTask<T> execute(HttpUriRequest request, HttpContext context, ResponseHandler<T> responseHandler) {
      return this.execute(request, context, responseHandler, (FutureCallback)null);
   }

   public <T> HttpRequestFutureTask<T> execute(HttpUriRequest request, HttpContext context, ResponseHandler<T> responseHandler, FutureCallback<T> callback) {
      if (this.closed.get()) {
         throw new IllegalStateException("Close has been called on this httpclient instance.");
      } else {
         this.metrics.getScheduledConnections().incrementAndGet();
         HttpRequestTaskCallable<T> callable = new HttpRequestTaskCallable(this.httpclient, request, context, responseHandler, callback, this.metrics);
         HttpRequestFutureTask<T> httpRequestFutureTask = new HttpRequestFutureTask(request, callable);
         this.executorService.execute(httpRequestFutureTask);
         return httpRequestFutureTask;
      }
   }

   public FutureRequestExecutionMetrics metrics() {
      return this.metrics;
   }

   public void close() throws IOException {
      this.closed.set(true);
      this.executorService.shutdownNow();
      if (this.httpclient instanceof Closeable) {
         ((Closeable)this.httpclient).close();
      }

   }
}
