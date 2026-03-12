package fr.xephi.authme.libs.org.apache.http.impl.io;

import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.net.Socket;

/** @deprecated */
@Deprecated
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
   public SocketOutputBuffer(Socket socket, int bufferSize, HttpParams params) throws IOException {
      Args.notNull(socket, "Socket");
      int n = bufferSize;
      if (bufferSize < 0) {
         n = socket.getSendBufferSize();
      }

      if (n < 1024) {
         n = 1024;
      }

      this.init(socket.getOutputStream(), n, params);
   }
}
