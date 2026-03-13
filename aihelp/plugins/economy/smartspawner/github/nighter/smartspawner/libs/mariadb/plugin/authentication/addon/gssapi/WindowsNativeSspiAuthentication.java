package github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.gssapi;

import com.sun.jna.platform.win32.Sspi.SecBufferDesc;
import com.sun.jna.platform.win32.SspiUtil.ManagedSecBufferDesc;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import java.io.IOException;
import waffle.windows.auth.IWindowsSecurityContext;
import waffle.windows.auth.impl.WindowsSecurityContextImpl;

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
         if (buf.getByte() == 1) {
            buf.skip();
         }

         byte[] tokenForTheClientOnTheServer = new byte[buf.readableBytes()];
         buf.readBytes(tokenForTheClientOnTheServer);
         SecBufferDesc continueToken = new ManagedSecBufferDesc(2, tokenForTheClientOnTheServer);
         clientContext.initialize(clientContext.getHandle(), continueToken, servicePrincipalName);
      }
   }
}
