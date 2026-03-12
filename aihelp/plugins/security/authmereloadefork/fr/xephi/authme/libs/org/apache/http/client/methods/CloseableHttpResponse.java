package fr.xephi.authme.libs.org.apache.http.client.methods;

import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import java.io.Closeable;

public interface CloseableHttpResponse extends HttpResponse, Closeable {
}
