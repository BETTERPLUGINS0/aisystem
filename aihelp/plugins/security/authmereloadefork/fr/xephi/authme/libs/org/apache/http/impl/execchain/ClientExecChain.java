package fr.xephi.authme.libs.org.apache.http.impl.execchain;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.client.methods.CloseableHttpResponse;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpExecutionAware;
import fr.xephi.authme.libs.org.apache.http.client.methods.HttpRequestWrapper;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import java.io.IOException;

public interface ClientExecChain {
   CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException;
}
