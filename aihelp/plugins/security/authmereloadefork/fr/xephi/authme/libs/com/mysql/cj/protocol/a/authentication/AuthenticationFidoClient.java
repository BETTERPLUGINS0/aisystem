package fr.xephi.authme.libs.com.mysql.cj.protocol.a.authentication;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.callback.FidoAuthenticationCallback;
import fr.xephi.authme.libs.com.mysql.cj.callback.MysqlCallbackHandler;
import fr.xephi.authme.libs.com.mysql.cj.callback.UsernameCallback;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.protocol.AuthenticationPlugin;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Protocol;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativeConstants;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;
import fr.xephi.authme.libs.com.mysql.cj.util.Util;
import java.util.List;

public class AuthenticationFidoClient implements AuthenticationPlugin<NativePacketPayload> {
   public static String PLUGIN_NAME = "authentication_fido_client";
   private String sourceOfAuthData;
   private MysqlCallbackHandler usernameCallbackHandler;
   private MysqlCallbackHandler fidoAuthenticationCallbackHandler;

   public AuthenticationFidoClient() {
      this.sourceOfAuthData = PLUGIN_NAME;
      this.usernameCallbackHandler = null;
      this.fidoAuthenticationCallbackHandler = null;
   }

   public void init(Protocol<NativePacketPayload> protocol, MysqlCallbackHandler callbackHandler) {
      this.usernameCallbackHandler = callbackHandler;
      String fidoCallbackHandlerClassName = (String)protocol.getPropertySet().getStringProperty(PropertyKey.authenticationFidoCallbackHandler).getValue();
      if (fidoCallbackHandlerClassName == null) {
         throw ExceptionFactory.createException(Messages.getString("AuthenticationFidoClientPlugin.MissingCallbackHandler"));
      } else {
         this.fidoAuthenticationCallbackHandler = (MysqlCallbackHandler)Util.getInstance(MysqlCallbackHandler.class, fidoCallbackHandlerClassName, (Class[])null, (Object[])null, protocol.getExceptionInterceptor());
      }
   }

   public void destroy() {
      this.reset();
      this.usernameCallbackHandler = null;
      this.fidoAuthenticationCallbackHandler = null;
   }

   public String getProtocolPluginName() {
      return PLUGIN_NAME;
   }

   public boolean requiresConfidentiality() {
      return false;
   }

   public boolean isReusable() {
      return false;
   }

   public void setAuthenticationParameters(String user, String password) {
      if (user == null && this.usernameCallbackHandler != null) {
         this.usernameCallbackHandler.handle(new UsernameCallback(System.getProperty("user.name")));
      }

   }

   public void setSourceOfAuthData(String sourceOfAuthData) {
      this.sourceOfAuthData = sourceOfAuthData;
   }

   public boolean nextAuthenticationStep(NativePacketPayload fromServer, List<NativePacketPayload> toServer) {
      toServer.clear();
      if (!this.sourceOfAuthData.equals(PLUGIN_NAME)) {
         return true;
      } else if (fromServer.getPayloadLength() == 0) {
         throw ExceptionFactory.createException(Messages.getString("AuthenticationFidoClientPlugin.IncompleteRegistration"));
      } else {
         byte[] scramble = fromServer.readBytes(NativeConstants.StringSelfDataType.STRING_LENENC);
         String relyingPartyId = fromServer.readString(NativeConstants.StringSelfDataType.STRING_LENENC, "UTF-8");
         byte[] credentialId = fromServer.readBytes(NativeConstants.StringSelfDataType.STRING_LENENC);
         FidoAuthenticationCallback fidoAuthCallback = new FidoAuthenticationCallback(scramble, relyingPartyId, credentialId);
         this.fidoAuthenticationCallbackHandler.handle(fidoAuthCallback);
         byte[] authenticatorData = fidoAuthCallback.getAuthenticatorData();
         if (authenticatorData != null && authenticatorData.length != 0) {
            byte[] signature = fidoAuthCallback.getSignature();
            if (signature != null && signature.length != 0) {
               NativePacketPayload packet = new NativePacketPayload(authenticatorData.length + signature.length + 4);
               packet.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, authenticatorData);
               packet.writeBytes(NativeConstants.StringSelfDataType.STRING_LENENC, signature);
               toServer.add(packet);
               return true;
            } else {
               throw ExceptionFactory.createException(Messages.getString("AuthenticationFidoClientPlugin.InvalidSignature"));
            }
         } else {
            throw ExceptionFactory.createException(Messages.getString("AuthenticationFidoClientPlugin.InvalidAuthenticatorData"));
         }
      }
   }
}
