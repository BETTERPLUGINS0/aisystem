package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon.gssapi.GssUtility;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon.gssapi.GssapiAuth;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon.gssapi.StandardGssapiAuthentication;
import java.io.IOException;
import java.sql.SQLException;

public class SendGssApiAuthPacket implements AuthenticationPlugin {
   private static final GssapiAuth gssapiAuth;
   private byte[] seed;
   private String optionServicePrincipalName;

   public String type() {
      return "auth_gssapi_client";
   }

   public void initialize(String authenticationData, byte[] seed, Configuration conf) {
      this.seed = seed;
      this.optionServicePrincipalName = conf.servicePrincipalName();
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context) throws IOException, SQLException {
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
