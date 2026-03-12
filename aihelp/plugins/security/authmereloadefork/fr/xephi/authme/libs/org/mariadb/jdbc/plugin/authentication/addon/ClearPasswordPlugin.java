package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.addon;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClearPasswordPlugin implements AuthenticationPlugin {
   public static final String TYPE = "mysql_clear_password";
   private String authenticationData;

   public String type() {
      return "mysql_clear_password";
   }

   public void initialize(String authenticationData, byte[] authData, Configuration conf) {
      this.authenticationData = authenticationData;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context) throws IOException {
      if (this.authenticationData == null) {
         out.writeEmptyPacket();
      } else {
         byte[] bytePwd = this.authenticationData.getBytes(StandardCharsets.UTF_8);
         out.writeBytes(bytePwd);
         out.writeByte(0);
         out.flush();
      }

      return in.readReusablePacket();
   }
}
