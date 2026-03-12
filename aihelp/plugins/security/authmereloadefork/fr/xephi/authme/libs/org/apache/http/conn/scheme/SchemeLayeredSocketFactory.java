package fr.xephi.authme.libs.org.apache.http.conn.scheme;

import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/** @deprecated */
@Deprecated
public interface SchemeLayeredSocketFactory extends SchemeSocketFactory {
   Socket createLayeredSocket(Socket var1, String var2, int var3, HttpParams var4) throws IOException, UnknownHostException;
}
