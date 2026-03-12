package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon.gssapi;

import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.SspiUtil;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsSecurityContext;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsSecurityContextImpl;
import java.io.IOException;

public class WindowsNativeSspiAuthentication implements GssapiAuth {
   public void authenticate(Writer out, Reader in, String servicePrincipalName, String mechanisms) throws IOException {
      IWindowsSecurityContext clientContext = WindowsSecurityContextImpl.getCurrent(mechanisms, servicePrincipalName);

      while(true) {
         byte[] tokenForTheServerOnTheClient = clientContext.getToken();
         if (tokenForTheServerOnTheClient != null && tokenForTheServerOnTheClient.length > 0) {
            out.writeBytes(tokenForTheServerOnTheClient);
            out.flush();
         }

         if (!clientContext.isContinue()) {
            clientContext.dispose();
            return;
         }

         ReadableByteBuf buf = in.readReusablePacket();
         byte[] tokenForTheClientOnTheServer = new byte[buf.readableBytes()];
         buf.readBytes(tokenForTheClientOnTheServer);
         Sspi.SecBufferDesc continueToken = new SspiUtil.ManagedSecBufferDesc(2, tokenForTheClientOnTheServer);
         clientContext.initialize(clientContext.getHandle(), continueToken, servicePrincipalName);
      }
   }
}
