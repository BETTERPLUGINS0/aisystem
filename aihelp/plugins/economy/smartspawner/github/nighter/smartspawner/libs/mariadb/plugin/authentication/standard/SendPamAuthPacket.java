package github.nighter.smartspawner.libs.mariadb.plugin.authentication.standard;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class SendPamAuthPacket implements AuthenticationPlugin {
   private final String authenticationData;
   private final Configuration conf;
   private int counter = 0;

   public SendPamAuthPacket(String authenticationData, Configuration conf) {
      this.authenticationData = authenticationData;
      this.conf = conf;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context, boolean sslFingerPrintValidation) throws SQLException, IOException {
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
