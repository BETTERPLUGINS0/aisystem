package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.HttpConnectionFactory;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.config.ConnectionConfig;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriterFactory;
import java.io.IOException;
import java.net.Socket;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DefaultBHttpServerConnectionFactory implements HttpConnectionFactory<DefaultBHttpServerConnection> {
   public static final DefaultBHttpServerConnectionFactory INSTANCE = new DefaultBHttpServerConnectionFactory();
   private final ConnectionConfig cconfig;
   private final ContentLengthStrategy incomingContentStrategy;
   private final ContentLengthStrategy outgoingContentStrategy;
   private final HttpMessageParserFactory<HttpRequest> requestParserFactory;
   private final HttpMessageWriterFactory<HttpResponse> responseWriterFactory;

   public DefaultBHttpServerConnectionFactory(ConnectionConfig cconfig, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageParserFactory<HttpRequest> requestParserFactory, HttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
      this.cconfig = cconfig != null ? cconfig : ConnectionConfig.DEFAULT;
      this.incomingContentStrategy = incomingContentStrategy;
      this.outgoingContentStrategy = outgoingContentStrategy;
      this.requestParserFactory = requestParserFactory;
      this.responseWriterFactory = responseWriterFactory;
   }

   public DefaultBHttpServerConnectionFactory(ConnectionConfig cconfig, HttpMessageParserFactory<HttpRequest> requestParserFactory, HttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
      this(cconfig, (ContentLengthStrategy)null, (ContentLengthStrategy)null, requestParserFactory, responseWriterFactory);
   }

   public DefaultBHttpServerConnectionFactory(ConnectionConfig cconfig) {
      this(cconfig, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   public DefaultBHttpServerConnectionFactory() {
      this((ConnectionConfig)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   public DefaultBHttpServerConnection createConnection(Socket socket) throws IOException {
      DefaultBHttpServerConnection conn = new DefaultBHttpServerConnection(this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestParserFactory, this.responseWriterFactory);
      conn.bind(socket);
      return conn;
   }
}
