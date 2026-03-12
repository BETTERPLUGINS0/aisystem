package fr.xephi.authme.libs.org.apache.http.conn;

import fr.xephi.authme.libs.org.apache.http.conn.scheme.SchemeRegistry;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;

/** @deprecated */
@Deprecated
public interface ClientConnectionManagerFactory {
   ClientConnectionManager newInstance(HttpParams var1, SchemeRegistry var2);
}
