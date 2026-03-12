package fr.xephi.authme.libs.com.mysql.cj.protocol.a.authentication;

import fr.xephi.authme.libs.com.mysql.cj.callback.MysqlCallbackHandler;
import fr.xephi.authme.libs.com.mysql.cj.callback.UsernameCallback;
import fr.xephi.authme.libs.com.mysql.cj.protocol.AuthenticationPlugin;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Protocol;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Security;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativeConstants;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;
import java.util.List;

public class MysqlNativePasswordPlugin implements AuthenticationPlugin<NativePacketPayload> {
   public static String PLUGIN_NAME = "mysql_native_password";
   private Protocol<NativePacketPayload> protocol = null;
   private MysqlCallbackHandler usernameCallbackHandler = null;
   private String password = null;

   public void init(Protocol<NativePacketPayload> prot, MysqlCallbackHandler cbh) {
      this.protocol = prot;
      this.usernameCallbackHandler = cbh;
   }

   public void destroy() {
      this.reset();
      this.protocol = null;
      this.usernameCallbackHandler = null;
      this.password = null;
   }

   public String getProtocolPluginName() {
      return PLUGIN_NAME;
   }

   public boolean requiresConfidentiality() {
      return false;
   }

   public boolean isReusable() {
      return true;
   }

   public void setAuthenticationParameters(String user, String password) {
      this.password = password;
      if (user == null && this.usernameCallbackHandler != null) {
         this.usernameCallbackHandler.handle(new UsernameCallback(System.getProperty("user.name")));
      }

   }

   public boolean nextAuthenticationStep(NativePacketPayload fromServer, List<NativePacketPayload> toServer) {
      toServer.clear();
      NativePacketPayload packet = null;
      String pwd = this.password;
      if (fromServer != null && pwd != null && pwd.length() != 0) {
         packet = new NativePacketPayload(Security.scramble411(pwd, fromServer.readBytes(NativeConstants.StringSelfDataType.STRING_TERM), this.protocol.getServerSession().getCharsetSettings().getPasswordCharacterEncoding()));
      } else {
         packet = new NativePacketPayload(new byte[0]);
      }

      toServer.add(packet);
      return true;
   }
}
