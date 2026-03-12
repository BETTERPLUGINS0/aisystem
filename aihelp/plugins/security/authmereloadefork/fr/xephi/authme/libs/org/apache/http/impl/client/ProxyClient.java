package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.ConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.auth.AuthSchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScope;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.auth.Credentials;
import fr.xephi.authme.libs.org.apache.http.client.config.RequestConfig;
import fr.xephi.authme.libs.org.apache.http.client.params.HttpClientParamConfig;
import fr.xephi.authme.libs.org.apache.http.client.protocol.RequestClientConnControl;
import fr.xephi.authme.libs.org.apache.http.config.ConnectionConfig;
import fr.xephi.authme.libs.org.apache.http.conn.HttpConnectionFactory;
import fr.xephi.authme.libs.org.apache.http.conn.ManagedHttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.conn.routing.RouteInfo;
import fr.xephi.authme.libs.org.apache.http.entity.BufferedHttpEntity;
import fr.xephi.authme.libs.org.apache.http.impl.DefaultConnectionReuseStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.auth.BasicSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.impl.auth.DigestSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.impl.auth.KerberosSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.impl.auth.NTLMSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.impl.auth.SPNegoSchemeFactory;
import fr.xephi.authme.libs.org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import fr.xephi.authme.libs.org.apache.http.message.BasicHttpRequest;
import fr.xephi.authme.libs.org.apache.http.params.BasicHttpParams;
import fr.xephi.authme.libs.org.apache.http.params.HttpParamConfig;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.protocol.BasicHttpContext;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpProcessor;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpRequestExecutor;
import fr.xephi.authme.libs.org.apache.http.protocol.ImmutableHttpProcessor;
import fr.xephi.authme.libs.org.apache.http.protocol.RequestTargetHost;
import fr.xephi.authme.libs.org.apache.http.protocol.RequestUserAgent;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.net.Socket;

public class ProxyClient {
   private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
   private final ConnectionConfig connectionConfig;
   private final RequestConfig requestConfig;
   private final HttpProcessor httpProcessor;
   private final HttpRequestExecutor requestExec;
   private final ProxyAuthenticationStrategy proxyAuthStrategy;
   private final fr.xephi.authme.libs.org.apache.http.impl.auth.HttpAuthenticator authenticator;
   private final AuthState proxyAuthState;
   private final AuthSchemeRegistry authSchemeRegistry;
   private final ConnectionReuseStrategy reuseStrategy;

   public ProxyClient(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, ConnectionConfig connectionConfig, RequestConfig requestConfig) {
      this.connFactory = (HttpConnectionFactory)(connFactory != null ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE);
      this.connectionConfig = connectionConfig != null ? connectionConfig : ConnectionConfig.DEFAULT;
      this.requestConfig = requestConfig != null ? requestConfig : RequestConfig.DEFAULT;
      this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent()});
      this.requestExec = new HttpRequestExecutor();
      this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
      this.authenticator = new fr.xephi.authme.libs.org.apache.http.impl.auth.HttpAuthenticator();
      this.proxyAuthState = new AuthState();
      this.authSchemeRegistry = new AuthSchemeRegistry();
      this.authSchemeRegistry.register("Basic", new BasicSchemeFactory());
      this.authSchemeRegistry.register("Digest", new DigestSchemeFactory());
      this.authSchemeRegistry.register("NTLM", new NTLMSchemeFactory());
      this.authSchemeRegistry.register("Negotiate", new SPNegoSchemeFactory());
      this.authSchemeRegistry.register("Kerberos", new KerberosSchemeFactory());
      this.reuseStrategy = new DefaultConnectionReuseStrategy();
   }

   /** @deprecated */
   @Deprecated
   public ProxyClient(HttpParams params) {
      this((HttpConnectionFactory)null, HttpParamConfig.getConnectionConfig(params), HttpClientParamConfig.getRequestConfig(params));
   }

   public ProxyClient(RequestConfig requestConfig) {
      this((HttpConnectionFactory)null, (ConnectionConfig)null, requestConfig);
   }

   public ProxyClient() {
      this((HttpConnectionFactory)null, (ConnectionConfig)null, (RequestConfig)null);
   }

   /** @deprecated */
   @Deprecated
   public HttpParams getParams() {
      return new BasicHttpParams();
   }

   /** @deprecated */
   @Deprecated
   public AuthSchemeRegistry getAuthSchemeRegistry() {
      return this.authSchemeRegistry;
   }

   public Socket tunnel(HttpHost proxy, HttpHost target, Credentials credentials) throws IOException, HttpException {
      Args.notNull(proxy, "Proxy host");
      Args.notNull(target, "Target host");
      Args.notNull(credentials, "Credentials");
      HttpHost host = target;
      if (target.getPort() <= 0) {
         host = new HttpHost(target.getHostName(), 80, target.getSchemeName());
      }

      HttpRoute route = new HttpRoute(host, this.requestConfig.getLocalAddress(), proxy, false, RouteInfo.TunnelType.TUNNELLED, RouteInfo.LayerType.PLAIN);
      ManagedHttpClientConnection conn = (ManagedHttpClientConnection)this.connFactory.create(route, this.connectionConfig);
      HttpContext context = new BasicHttpContext();
      HttpRequest connect = new BasicHttpRequest("CONNECT", host.toHostString(), HttpVersion.HTTP_1_1);
      BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
      credsProvider.setCredentials(new AuthScope(proxy), credentials);
      context.setAttribute("http.target_host", target);
      context.setAttribute("http.connection", conn);
      context.setAttribute("http.request", connect);
      context.setAttribute("http.route", route);
      context.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
      context.setAttribute("http.auth.credentials-provider", credsProvider);
      context.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
      context.setAttribute("http.request-config", this.requestConfig);
      this.requestExec.preProcess(connect, this.httpProcessor, context);

      while(true) {
         if (!conn.isOpen()) {
            Socket socket = new Socket(proxy.getHostName(), proxy.getPort());
            conn.bind(socket);
         }

         this.authenticator.generateAuthResponse(connect, this.proxyAuthState, context);
         HttpResponse response = this.requestExec.execute(connect, conn, context);
         int status = response.getStatusLine().getStatusCode();
         if (status < 200) {
            throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
         }

         HttpEntity entity;
         if (!this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context) || !this.authenticator.handleAuthChallenge(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
            status = response.getStatusLine().getStatusCode();
            if (status > 299) {
               entity = response.getEntity();
               if (entity != null) {
                  response.setEntity(new BufferedHttpEntity(entity));
               }

               conn.close();
               throw new fr.xephi.authme.libs.org.apache.http.impl.execchain.TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
            } else {
               return conn.getSocket();
            }
         }

         if (this.reuseStrategy.keepAlive(response, context)) {
            entity = response.getEntity();
            EntityUtils.consume(entity);
         } else {
            conn.close();
         }

         connect.removeHeaders("Proxy-Authorization");
      }
   }
}
