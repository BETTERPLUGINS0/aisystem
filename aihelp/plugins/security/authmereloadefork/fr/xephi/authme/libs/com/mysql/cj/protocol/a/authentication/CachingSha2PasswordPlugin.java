package fr.xephi.authme.libs.com.mysql.cj.protocol.a.authentication;

import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.CJException;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionInterceptor;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.UnableToConnectException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Protocol;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Security;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativeConstants;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import java.security.DigestException;
import java.util.List;

public class CachingSha2PasswordPlugin extends Sha256PasswordPlugin {
   public static String PLUGIN_NAME = "caching_sha2_password";
   private CachingSha2PasswordPlugin.AuthStage stage;

   public CachingSha2PasswordPlugin() {
      this.stage = CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_SEND_SCRAMBLE;
   }

   public void init(Protocol<NativePacketPayload> prot) {
      super.init(prot);
      this.stage = CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_SEND_SCRAMBLE;
   }

   public void reset() {
      this.stage = CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_SEND_SCRAMBLE;
   }

   public void destroy() {
      super.destroy();
      this.stage = CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_SEND_SCRAMBLE;
   }

   public String getProtocolPluginName() {
      return PLUGIN_NAME;
   }

   public boolean nextAuthenticationStep(NativePacketPayload fromServer, List<NativePacketPayload> toServer) {
      toServer.clear();
      NativePacketPayload packet;
      if (this.password != null && this.password.length() != 0 && fromServer != null) {
         try {
            if (this.stage == CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_SEND_SCRAMBLE) {
               this.seed = fromServer.readString(NativeConstants.StringSelfDataType.STRING_TERM, (String)null);
               toServer.add(new NativePacketPayload(Security.scrambleCachingSha2(StringUtils.getBytes(this.password, this.protocol.getServerSession().getCharsetSettings().getPasswordCharacterEncoding()), this.seed.getBytes())));
               this.stage = CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_READ_RESULT;
               return true;
            }

            if (this.stage == CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_READ_RESULT) {
               int fastAuthResult = fromServer.readBytes(NativeConstants.StringLengthDataType.STRING_FIXED, 1)[0];
               switch(fastAuthResult) {
               case 3:
                  this.stage = CachingSha2PasswordPlugin.AuthStage.FAST_AUTH_COMPLETE;
                  return true;
               case 4:
                  this.stage = CachingSha2PasswordPlugin.AuthStage.FULL_AUTH;
                  break;
               default:
                  throw ExceptionFactory.createException("Unknown server response after fast auth.", this.protocol.getExceptionInterceptor());
               }
            }

            if (this.protocol.getSocketConnection().isSSLEstablished()) {
               packet = new NativePacketPayload(StringUtils.getBytes(this.password, this.protocol.getServerSession().getCharsetSettings().getPasswordCharacterEncoding()));
               packet.setPosition(packet.getPayloadLength());
               packet.writeInteger(NativeConstants.IntegerDataType.INT1, 0L);
               packet.setPosition(0);
               toServer.add(packet);
            } else if (this.serverRSAPublicKeyFile.getValue() != null) {
               packet = new NativePacketPayload(this.encryptPassword());
               toServer.add(packet);
            } else {
               if (!(Boolean)this.protocol.getPropertySet().getBooleanProperty(PropertyKey.allowPublicKeyRetrieval).getValue()) {
                  throw (UnableToConnectException)ExceptionFactory.createException(UnableToConnectException.class, Messages.getString("Sha256PasswordPlugin.2"), this.protocol.getExceptionInterceptor());
               }

               if (this.publicKeyRequested && fromServer.getPayloadLength() > 21) {
                  this.publicKeyString = fromServer.readString(NativeConstants.StringSelfDataType.STRING_TERM, (String)null);
                  packet = new NativePacketPayload(this.encryptPassword());
                  toServer.add(packet);
                  this.publicKeyRequested = false;
               } else {
                  packet = new NativePacketPayload(new byte[]{2});
                  toServer.add(packet);
                  this.publicKeyRequested = true;
               }
            }
         } catch (DigestException | CJException var4) {
            throw ExceptionFactory.createException((String)var4.getMessage(), (Throwable)var4, (ExceptionInterceptor)this.protocol.getExceptionInterceptor());
         }
      } else {
         packet = new NativePacketPayload(new byte[]{0});
         toServer.add(packet);
      }

      return true;
   }

   protected byte[] encryptPassword() {
      return this.protocol.versionMeetsMinimum(8, 0, 5) ? super.encryptPassword() : super.encryptPassword("RSA/ECB/PKCS1Padding");
   }

   public static enum AuthStage {
      FAST_AUTH_SEND_SCRAMBLE,
      FAST_AUTH_READ_RESULT,
      FAST_AUTH_COMPLETE,
      FULL_AUTH;
   }
}
