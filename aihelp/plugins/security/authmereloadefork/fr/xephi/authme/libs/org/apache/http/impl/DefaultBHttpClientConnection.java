package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.HttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.config.MessageConstraints;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import fr.xephi.authme.libs.org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParser;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParserFactory;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriter;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriterFactory;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class DefaultBHttpClientConnection extends BHttpConnectionBase implements HttpClientConnection {
   private final HttpMessageParser<HttpResponse> responseParser;
   private final HttpMessageWriter<HttpRequest> requestWriter;

   public DefaultBHttpClientConnection(int bufferSize, int fragmentSizeHint, CharsetDecoder charDecoder, CharsetEncoder charEncoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
      super(bufferSize, fragmentSizeHint, charDecoder, charEncoder, constraints, incomingContentStrategy, outgoingContentStrategy);
      this.requestWriter = ((HttpMessageWriterFactory)(requestWriterFactory != null ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE)).create(this.getSessionOutputBuffer());
      this.responseParser = ((HttpMessageParserFactory)(responseParserFactory != null ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE)).create(this.getSessionInputBuffer(), constraints);
   }

   public DefaultBHttpClientConnection(int bufferSize, CharsetDecoder charDecoder, CharsetEncoder charEncoder, MessageConstraints constraints) {
      this(bufferSize, bufferSize, charDecoder, charEncoder, constraints, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public DefaultBHttpClientConnection(int bufferSize) {
      this(bufferSize, bufferSize, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   protected void onResponseReceived(HttpResponse response) {
   }

   protected void onRequestSubmitted(HttpRequest request) {
   }

   public void bind(Socket socket) throws IOException {
      super.bind(socket);
   }

   public boolean isResponseAvailable(int timeout) throws IOException {
      this.ensureOpen();

      try {
         return this.awaitInput(timeout);
      } catch (SocketTimeoutException var3) {
         return false;
      }
   }

   public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      this.ensureOpen();
      this.requestWriter.write(request);
      this.onRequestSubmitted(request);
      this.incrementRequestCount();
   }

   public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      this.ensureOpen();
      HttpEntity entity = request.getEntity();
      if (entity != null) {
         OutputStream outStream = this.prepareOutput(request);
         entity.writeTo(outStream);
         outStream.close();
      }
   }

   public HttpResponse receiveResponseHeader() throws HttpException, IOException {
      this.ensureOpen();
      HttpResponse response = (HttpResponse)this.responseParser.parse();
      this.onResponseReceived(response);
      if (response.getStatusLine().getStatusCode() >= 200) {
         this.incrementResponseCount();
      }

      return response;
   }

   public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      this.ensureOpen();
      HttpEntity entity = this.prepareInput(response);
      response.setEntity(entity);
   }

   public void flush() throws IOException {
      this.ensureOpen();
      this.doFlush();
   }
}
