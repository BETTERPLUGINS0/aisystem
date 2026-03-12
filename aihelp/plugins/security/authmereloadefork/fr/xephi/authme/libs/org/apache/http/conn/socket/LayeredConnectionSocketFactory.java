package fr.xephi.authme.libs.org.apache.http.conn.socket;

import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public interface LayeredConnectionSocketFactory extends ConnectionSocketFactory {
   Socket createLayeredSocket(Socket var1, String var2, int var3, HttpContext var4) throws IOException, UnknownHostException;
}
