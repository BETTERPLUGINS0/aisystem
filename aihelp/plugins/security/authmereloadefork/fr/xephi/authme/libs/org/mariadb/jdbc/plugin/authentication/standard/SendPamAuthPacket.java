package fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Reader;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.AuthenticationPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class SendPamAuthPacket implements AuthenticationPlugin {
   private String authenticationData;
   private Configuration conf;
   private int counter = 0;

   public String type() {
      return "dialog";
   }

   public void initialize(String authenticationData, byte[] seed, Configuration conf) {
      this.authenticationData = authenticationData;
      this.conf = conf;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context) throws SQLException, IOException {
      ReadableByteBuf buf;
      short type;
      do {
         ++this.counter;
         String password;
         if (this.counter == 1) {
            password = this.authenticationData;
         } else {
            if (!this.conf.nonMappedOptions().containsKey("password" + this.counter)) {
               throw new SQLException("PAM authentication request multiple passwords, but 'password" + this.counter + "' is not set");
            }

            password = (String)this.conf.nonMappedOptions().get("password" + this.counter);
         }

         byte[] bytePwd = password != null ? password.getBytes(StandardCharsets.UTF_8) : new byte[0];
         out.writeBytes(bytePwd, 0, bytePwd.length);
         out.writeByte(0);
         out.flush();
         buf = in.readReusablePacket();
         type = buf.getUnsignedByte();
      } while(type != 254 && type != 0 && type != 255);

      return buf;
   }
}
