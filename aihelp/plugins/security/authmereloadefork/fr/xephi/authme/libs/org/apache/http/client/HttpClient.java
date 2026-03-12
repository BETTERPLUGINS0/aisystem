package fr.xephi.authme.libs.org.apache.http.client;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpUriRequest;
import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionManager;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;

public interface HttpClient {
   /** @deprecated */
   @Deprecated
   HttpParams getParams();

   /** @deprecated */
   @Deprecated
   ClientConnectionManager getConnectionManager();

   HttpResponse execute(HttpUriRequest var1) throws IOException, ClientProtocolException;

   HttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException, ClientProtocolException;

   HttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException, ClientProtocolException;

   HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException;

   <T> T execute(HttpUriRequest var1, ResponseHandler<? extends T> var2) throws IOException, ClientProtocolException;

   <T> T execute(HttpUriRequest var1, ResponseHandler<? extends T> var2, HttpContext var3) throws IOException, ClientProtocolException;

   <T> T execute(HttpHost var1, HttpRequest var2, ResponseHandler<? extends T> var3) throws IOException, ClientProtocolException;

   <T> T execute(HttpHost var1, HttpRequest var2, ResponseHandler<? extends T> var3, HttpContext var4) throws IOException, ClientProtocolException;
}
