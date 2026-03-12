package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpServerConnection;
import fr.xephi.authme.libs.org.apache.http.config.MessageConstraints;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.io.DefaultHttpRequestParserFactory;
import fr.xephi.authme.libs.org.apache.http.impl.io.DefaultHttpResponseWriterFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParser;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriter;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriterFactory;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class DefaultBHttpServerConnection extends BHttpConnectionBase implements HttpServerConnection {
   private final HttpMessageParser<HttpRequest> requestParser;
   private final HttpMessageWriter<HttpResponse> responseWriter;

   public DefaultBHttpServerConnection(int bufferSize, int fragmentSizeHint, CharsetDecoder charDecoder, CharsetEncoder charEncoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageParserFactory<HttpRequest> requestParserFactory, HttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
      super(bufferSize, fragmentSizeHint, charDecoder, charEncoder, constraints, (ContentLengthStrategy)(incomingContentStrategy != null ? incomingContentStrategy : DisallowIdentityContentLengthStrategy.INSTANCE), outgoingContentStrategy);
      this.requestParser = ((HttpMessageParserFactory)(requestParserFactory != null ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE)).create(this.getSessionInputBuffer(), constraints);
      this.responseWriter = ((HttpMessageWriterFactory)(responseWriterFactory != null ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE)).create(this.getSessionOutputBuffer());
   }

   public DefaultBHttpServerConnection(int bufferSize, CharsetDecoder charDecoder, CharsetEncoder charEncoder, MessageConstraints constraints) {
      this(bufferSize, bufferSize, charDecoder, charEncoder, constraints, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   public DefaultBHttpServerConnection(int bufferSize) {
      this(bufferSize, bufferSize, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   protected void onRequestReceived(HttpRequest request) {
   }

   protected void onResponseSubmitted(HttpResponse response) {
   }

   public void bind(Socket socket) throws IOException {
      super.bind(socket);
   }

   public HttpRequest receiveRequestHeader() throws HttpException, IOException {
      this.ensureOpen();
      HttpRequest request = (HttpRequest)this.requestParser.parse();
      this.onRequestReceived(request);
      this.incrementRequestCount();
      return request;
   }

   public void receiveRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      this.ensureOpen();
      HttpEntity entity = this.prepareInput(request);
      request.setEntity(entity);
   }

   public void sendResponseHeader(HttpResponse response) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      this.ensureOpen();
      this.responseWriter.write(response);
      this.onResponseSubmitted(response);
      if (response.getStatusLine().getStatusCode() >= 200) {
         this.incrementResponseCount();
      }

   }

   public void sendResponseEntity(HttpResponse response) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      this.ensureOpen();
      HttpEntity entity = response.getEntity();
      if (entity != null) {
         OutputStream outStream = this.prepareOutput(response);
         entity.writeTo(outStream);
         outStream.close();
      }
   }

   public void flush() throws IOException {
      this.ensureOpen();
      this.doFlush();
   }
}
