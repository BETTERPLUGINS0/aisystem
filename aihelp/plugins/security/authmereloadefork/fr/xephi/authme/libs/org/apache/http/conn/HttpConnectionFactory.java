package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.HttpConnection;
import fr.xephi.authme.libs.org.apache.http.config.ConnectionConfig;

public interface HttpConnectionFactory<T, C extends HttpConnection> {
   C create(T var1, ConnectionConfig var2);
}
