package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.config.ConnectionConfig;
import fr.xephi.authme.libs.org.apache.http.conn.HttpConnectionFactory;
import fr.xephi.authme.libs.org.apache.http.conn.ManagedHttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.entity.LaxContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.entity.StrictContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriterFactory;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> {
   private static final AtomicLong COUNTER = new AtomicLong();
   public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
   private final Log log;
   private final Log headerLog;
   private final Log wireLog;
   private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
   private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
   private final ContentLengthStrategy incomingContentStrategy;
   private final ContentLengthStrategy outgoingContentStrategy;

   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
      this.log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
      this.headerLog = LogFactory.getLog("fr.xephi.authme.libs.org.apache.http.headers");
      this.wireLog = LogFactory.getLog("fr.xephi.authme.libs.org.apache.http.wire");
      this.requestWriterFactory = (HttpMessageWriterFactory)(requestWriterFactory != null ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE);
      this.responseParserFactory = (HttpMessageParserFactory)(responseParserFactory != null ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE);
      this.incomingContentStrategy = (ContentLengthStrategy)(incomingContentStrategy != null ? incomingContentStrategy : LaxContentLengthStrategy.INSTANCE);
      this.outgoingContentStrategy = (ContentLengthStrategy)(outgoingContentStrategy != null ? outgoingContentStrategy : StrictContentLengthStrategy.INSTANCE);
   }

   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
      this(requestWriterFactory, responseParserFactory, (ContentLengthStrategy)null, (ContentLengthStrategy)null);
   }

   public ManagedHttpClientConnectionFactory(HttpMessageParserFactory<HttpResponse> responseParserFactory) {
      this((HttpMessageWriterFactory)null, responseParserFactory);
   }

   public ManagedHttpClientConnectionFactory() {
      this((HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public ManagedHttpClientConnection create(HttpRoute route, ConnectionConfig config) {
      ConnectionConfig cconfig = config != null ? config : ConnectionConfig.DEFAULT;
      CharsetDecoder charDecoder = null;
      CharsetEncoder charEncoder = null;
      Charset charset = cconfig.getCharset();
      CodingErrorAction malformedInputAction = cconfig.getMalformedInputAction() != null ? cconfig.getMalformedInputAction() : CodingErrorAction.REPORT;
      CodingErrorAction unmappableInputAction = cconfig.getUnmappableInputAction() != null ? cconfig.getUnmappableInputAction() : CodingErrorAction.REPORT;
      if (charset != null) {
         charDecoder = charset.newDecoder();
         charDecoder.onMalformedInput(malformedInputAction);
         charDecoder.onUnmappableCharacter(unmappableInputAction);
         charEncoder = charset.newEncoder();
         charEncoder.onMalformedInput(malformedInputAction);
         charEncoder.onUnmappableCharacter(unmappableInputAction);
      }

      String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
      return new LoggingManagedHttpClientConnection(id, this.log, this.headerLog, this.wireLog, cconfig.getBufferSize(), cconfig.getFragmentSizeHint(), charDecoder, charEncoder, cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
   }
}
