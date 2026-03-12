package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.client.BackoffManager;
import fr.xephi.authme.libs.org.apache.http.client.ConnectionBackoffStrategy;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class BackoffStrategyExec implements ClientExecChain {
   private final ClientExecChain requestExecutor;
   private final ConnectionBackoffStrategy connectionBackoffStrategy;
   private final BackoffManager backoffManager;

   public BackoffStrategyExec(ClientExecChain requestExecutor, ConnectionBackoffStrategy connectionBackoffStrategy, BackoffManager backoffManager) {
      Args.notNull(requestExecutor, "HTTP client request executor");
      Args.notNull(connectionBackoffStrategy, "Connection backoff strategy");
      Args.notNull(backoffManager, "Backoff manager");
      this.requestExecutor = requestExecutor;
      this.connectionBackoffStrategy = connectionBackoffStrategy;
      this.backoffManager = backoffManager;
   }

   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
      Args.notNull(route, "HTTP route");
      Args.notNull(request, "HTTP request");
      Args.notNull(context, "HTTP context");
      CloseableHttpResponse out = null;

      try {
         out = this.requestExecutor.execute(route, request, context, execAware);
      } catch (Exception var7) {
         if (out != null) {
            out.close();
         }

         if (this.connectionBackoffStrategy.shouldBackoff((Throwable)var7)) {
            this.backoffManager.backOff(route);
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof HttpException) {
            throw (HttpException)var7;
         }

         if (var7 instanceof IOException) {
            throw (IOException)var7;
         }

         throw new UndeclaredThrowableException(var7);
      }

      if (this.connectionBackoffStrategy.shouldBackoff((HttpResponse)out)) {
         this.backoffManager.backOff(route);
      } else {
         this.backoffManager.probe(route);
      }

      return out;
   }
}
