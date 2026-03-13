package github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.gssapi.GssUtility;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.gssapi.GssapiAuth;
import github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.gssapi.StandardGssapiAuthentication;
import java.io.IOException;
import java.sql.SQLException;

public class SendGssApiAuthPacket implements AuthenticationPlugin {
   private static final GssapiAuth gssapiAuth;
   private final byte[] seed;
   private final String optionServicePrincipalName;

   public SendGssApiAuthPacket(byte[] seed, Configuration conf) {
      this.seed = seed;
      this.optionServicePrincipalName = conf.servicePrincipalName();
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context, boolean sslFingerPrintValidation) throws IOException, SQLException {
      ReadableByteBuf buf = new StandardReadableByteBuf(this.seed, this.seed.length);
      String serverSpn = buf.readStringNullEnd();
      String servicePrincipalName = this.optionServicePrincipalName != null ? this.optionServicePrincipalName : serverSpn;
      String mechanisms = buf.readStringNullEnd();
      if (mechanisms.isEmpty()) {
         mechanisms = "Kerberos";
      }

      gssapiAuth.authenticate(out, in, servicePrincipalName, mechanisms);
      return in.readReusablePacket();
   }

   static {
      Object init;
      try {
         init = GssUtility.getAuthenticationMethod();
      } catch (Throwable var2) {
         init = new StandardGssapiAuthentication();
      }

      gssapiAuth = (GssapiAuth)init;
   }
}
