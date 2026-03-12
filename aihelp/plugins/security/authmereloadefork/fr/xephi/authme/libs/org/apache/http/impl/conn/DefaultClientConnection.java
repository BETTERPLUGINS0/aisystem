package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseFactory;
import fr.xephi.authme.libs.org.apache.http.conn.ManagedHttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.conn.OperatedClientConnection;
import fr.xephi.authme.libs.org.apache.http.impl.SocketHttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParser;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;
import fr.xephi.authme.libs.org.apache.http.params.BasicHttpParams;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.params.HttpProtocolParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/** @deprecated */
@Deprecated
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext {
   private final Log log = LogFactory.getLog(this.getClass());
   private final Log headerLog = LogFactory.getLog("fr.xephi.authme.libs.org.apache.http.headers");
   private final Log wireLog = LogFactory.getLog("fr.xephi.authme.libs.org.apache.http.wire");
   private volatile Socket socket;
   private HttpHost targetHost;
   private boolean connSecure;
   private volatile boolean shutdown;
   private final Map<String, Object> attributes = new HashMap();

   public String getId() {
      return null;
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final boolean isSecure() {
      return this.connSecure;
   }

   public final Socket getSocket() {
      return this.socket;
   }

   public SSLSession getSSLSession() {
      return this.socket instanceof SSLSocket ? ((SSLSocket)this.socket).getSession() : null;
   }

   public void opening(Socket sock, HttpHost target) throws IOException {
      this.assertNotOpen();
      this.socket = sock;
      this.targetHost = target;
      if (this.shutdown) {
         sock.close();
         throw new InterruptedIOException("Connection already shutdown");
      }
   }

   public void openCompleted(boolean secure, HttpParams params) throws IOException {
      Args.notNull(params, "Parameters");
      this.assertNotOpen();
      this.connSecure = secure;
      this.bind(this.socket, params);
   }

   public void shutdown() throws IOException {
      this.shutdown = true;

      try {
         super.shutdown();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " shut down");
         }

         Socket sock = this.socket;
         if (sock != null) {
            sock.close();
         }
      } catch (IOException var2) {
         this.log.debug("I/O error shutting down connection", var2);
      }

   }

   public void close() throws IOException {
      try {
         super.close();
         if (this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " closed");
         }
      } catch (IOException var2) {
         this.log.debug("I/O error closing connection", var2);
      }

   }

   protected SessionInputBuffer createSessionInputBuffer(Socket socket, int bufferSize, HttpParams params) throws IOException {
      SessionInputBuffer inBuffer = super.createSessionInputBuffer(socket, bufferSize > 0 ? bufferSize : 8192, params);
      if (this.wireLog.isDebugEnabled()) {
         inBuffer = new LoggingSessionInputBuffer((SessionInputBuffer)inBuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
      }

      return (SessionInputBuffer)inBuffer;
   }

   protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int bufferSize, HttpParams params) throws IOException {
      SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, bufferSize > 0 ? bufferSize : 8192, params);
      if (this.wireLog.isDebugEnabled()) {
         outbuffer = new LoggingSessionOutputBuffer((SessionOutputBuffer)outbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
      }

      return (SessionOutputBuffer)outbuffer;
   }

   protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
      return new DefaultHttpResponseParser(buffer, (LineParser)null, responseFactory, params);
   }

   public void bind(Socket socket) throws IOException {
      this.bind(socket, new BasicHttpParams());
   }

   public void update(Socket sock, HttpHost target, boolean secure, HttpParams params) throws IOException {
      this.assertOpen();
      Args.notNull(target, "Target host");
      Args.notNull(params, "Parameters");
      if (sock != null) {
         this.socket = sock;
         this.bind(sock, params);
      }

      this.targetHost = target;
      this.connSecure = secure;
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      HttpResponse response = super.receiveResponseHeader();
      if (this.log.isDebugEnabled()) {
         this.log.debug("Receiving response: " + response.getStatusLine());
      }

      if (this.headerLog.isDebugEnabled()) {
         this.headerLog.debug("<< " + response.getStatusLine().toString());
         Header[] headers = response.getAllHeaders();
         Header[] arr$ = headers;
         int len$ = headers.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Header header = arr$[i$];
            this.headerLog.debug("<< " + header.toString());
         }
      }

      return response;
   }

   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Sending request: " + request.getRequestLine());
      }

      super.sendRequestHeader(request);
      if (this.headerLog.isDebugEnabled()) {
         this.headerLog.debug(">> " + request.getRequestLine().toString());
         Header[] headers = request.getAllHeaders();
         Header[] arr$ = headers;
         int len$ = headers.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Header header = arr$[i$];
            this.headerLog.debug(">> " + header.toString());
         }
      }

   }

   public Object getAttribute(String id) {
      return this.attributes.get(id);
   }

   public Object removeAttribute(String id) {
      return this.attributes.remove(id);
   }

   public void setAttribute(String id, Object obj) {
      this.attributes.put(id, obj);
   }
}
