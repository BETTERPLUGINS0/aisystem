package github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon;

import github.nighter.smartspawner.libs.mariadb.Configuration;
import github.nighter.smartspawner.libs.mariadb.HostAddress;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.export.SslMode;
import github.nighter.smartspawner.libs.mariadb.plugin.AuthenticationPlugin;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ClearPasswordPlugin implements AuthenticationPlugin {
   private final String authenticationData;
   private final HostAddress hostAddress;
   private final Configuration conf;

   public ClearPasswordPlugin(String authenticationData, HostAddress hostAddress, Configuration conf) {
      this.authenticationData = authenticationData;
      this.hostAddress = hostAddress;
      this.conf = conf;
   }

   public ReadableByteBuf process(Writer out, Reader in, Context context, boolean sslFingerPrintValidation) throws IOException, SQLException {
      SslMode sslMode = this.hostAddress.sslMode == null ? this.conf.sslMode() : this.hostAddress.sslMode;
      if (sslMode != SslMode.DISABLE && sslMode != SslMode.TRUST && sslFingerPrintValidation) {
         throw new SQLException("Driver cannot send password in clear when using SSL when certificates are not explicitly passed on configuration.", "S1010");
      } else {
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
}
