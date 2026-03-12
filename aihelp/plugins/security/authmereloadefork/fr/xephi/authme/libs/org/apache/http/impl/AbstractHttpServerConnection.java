package fr.xephi.authme.libs.org.apache.http.impl;

import fr.xephi.authme.libs.org.apache.http.HttpConnectionMetrics;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestFactory;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpServerConnection;
import fr.xephi.authme.libs.org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.entity.EntityDeserializer;
import fr.xephi.authme.libs.org.apache.http.impl.entity.EntitySerializer;
import fr.xephi.authme.libs.org.apache.http.impl.entity.LaxContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.entity.StrictContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.io.DefaultHttpRequestParser;
import fr.xephi.authme.libs.org.apache.http.impl.io.HttpResponseWriter;
import fr.xephi.authme.libs.org.apache.http.io.EofSensor;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageParser;
import fr.xephi.authme.libs.org.apache.http.io.HttpMessageWriter;
import fr.xephi.authme.libs.org.apache.http.io.HttpTransportMetrics;
import fr.xephi.authme.libs.org.apache.http.io.SessionInputBuffer;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.message.LineFormatter;
import fr.xephi.authme.libs.org.apache.http.message.LineParser;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

/** @deprecated */
@Deprecated
public abstract class AbstractHttpServerConnection implements HttpServerConnection {
   private final EntitySerializer entityserializer = this.createEntitySerializer();
   private final EntityDeserializer entitydeserializer = this.createEntityDeserializer();
   private SessionInputBuffer inBuffer = null;
   private SessionOutputBuffer outbuffer = null;
   private EofSensor eofSensor = null;
   private HttpMessageParser<HttpRequest> requestParser = null;
   private HttpMessageWriter<HttpResponse> responseWriter = null;
   private HttpConnectionMetricsImpl metrics = null;

   protected abstract void assertOpen() throws IllegalStateException;

   protected EntityDeserializer createEntityDeserializer() {
      return new EntityDeserializer(new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0)));
   }

   protected EntitySerializer createEntitySerializer() {
      return new EntitySerializer(new StrictContentLengthStrategy());
   }

   protected HttpRequestFactory createHttpRequestFactory() {
      return DefaultHttpRequestFactory.INSTANCE;
   }

   protected HttpMessageParser<HttpRequest> createRequestParser(SessionInputBuffer buffer, HttpRequestFactory requestFactory, HttpParams params) {
      return new DefaultHttpRequestParser(buffer, (LineParser)null, requestFactory, params);
   }

   protected HttpMessageWriter<HttpResponse> createResponseWriter(SessionOutputBuffer buffer, HttpParams params) {
      return new HttpResponseWriter(buffer, (LineFormatter)null, params);
   }

   protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
      return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
   }

   protected void init(SessionInputBuffer inBuffer, SessionOutputBuffer outbuffer, HttpParams params) {
      this.inBuffer = (SessionInputBuffer)Args.notNull(inBuffer, "Input session buffer");
      this.outbuffer = (SessionOutputBuffer)Args.notNull(outbuffer, "Output session buffer");
      if (inBuffer instanceof EofSensor) {
         this.eofSensor = (EofSensor)inBuffer;
      }

      this.requestParser = this.createRequestParser(inBuffer, this.createHttpRequestFactory(), params);
      this.responseWriter = this.createResponseWriter(outbuffer, params);
      this.metrics = this.createConnectionMetrics(inBuffer.getMetrics(), outbuffer.getMetrics());
   }

   public HttpRequest receiveRequestHeader() throws HttpException, IOException {
      this.assertOpen();
      HttpRequest request = (HttpRequest)this.requestParser.parse();
      this.metrics.incrementRequestCount();
      return request;
   }

   public void receiveRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      this.assertOpen();
      HttpEntity entity = this.entitydeserializer.deserialize(this.inBuffer, request);
      request.setEntity(entity);
   }

   protected void doFlush() throws IOException {
      this.outbuffer.flush();
   }

   public void flush() throws IOException {
      this.assertOpen();
      this.doFlush();
   }

   public void sendResponseHeader(HttpResponse response) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      this.assertOpen();
      this.responseWriter.write(response);
      if (response.getStatusLine().getStatusCode() >= 200) {
         this.metrics.incrementResponseCount();
      }

   }

   public void sendResponseEntity(HttpResponse response) throws HttpException, IOException {
      if (response.getEntity() != null) {
         this.entityserializer.serialize(this.outbuffer, response, response.getEntity());
      }
   }

   protected boolean isEof() {
      return this.eofSensor != null && this.eofSensor.isEof();
   }

   public boolean isStale() {
      if (!this.isOpen()) {
         return true;
      } else if (this.isEof()) {
         return true;
      } else {
         try {
            this.inBuffer.isDataAvailable(1);
            return this.isEof();
         } catch (IOException var2) {
            return true;
         }
      }
   }

   public HttpConnectionMetrics getMetrics() {
      return this.metrics;
   }
}
