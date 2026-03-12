package fr.xephi.authme.libs.org.apache.http.client.methods;

import fr.xephi.authme.libs.org.apache.http.conn.ClientConnectionRequest;
import fr.xephi.authme.libs.org.apache.http.conn.ConnectionReleaseTrigger;
import java.io.IOException;

/** @deprecated */
@Deprecated
public interface AbortableHttpRequest {
   void setConnectionRequest(ClientConnectionRequest var1) throws IOException;

   void setReleaseTrigger(ConnectionReleaseTrigger var1) throws IOException;

   void abort();
}
