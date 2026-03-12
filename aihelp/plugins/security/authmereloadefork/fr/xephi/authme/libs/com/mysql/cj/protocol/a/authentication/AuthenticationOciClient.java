package fr.xephi.authme.libs.com.mysql.cj.protocol.a.authentication;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import fr.xephi.authme.libs.com.mysql.cj.Messages;
import fr.xephi.authme.libs.com.mysql.cj.callback.MysqlCallbackHandler;
import fr.xephi.authme.libs.com.mysql.cj.callback.UsernameCallback;
import fr.xephi.authme.libs.com.mysql.cj.conf.PropertyKey;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.RSAException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.AuthenticationPlugin;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ExportControlled;
import fr.xephi.authme.libs.com.mysql.cj.protocol.Protocol;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativeConstants;
import fr.xephi.authme.libs.com.mysql.cj.protocol.a.NativePacketPayload;
import fr.xephi.authme.libs.com.mysql.cj.util.StringUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.List;

public class AuthenticationOciClient implements AuthenticationPlugin<NativePacketPayload> {
   public static String PLUGIN_NAME = "authentication_oci_client";
   private String sourceOfAuthData;
   protected Protocol<NativePacketPayload> protocol;
   private MysqlCallbackHandler usernameCallbackHandler;
   private String configFingerprint;
   private String configKeyFile;
   private String configSecurityTokenFile;
   private RSAPrivateKey privateKey;
   private byte[] token;

   public AuthenticationOciClient() {
      this.sourceOfAuthData = PLUGIN_NAME;
      this.protocol = null;
      this.usernameCallbackHandler = null;
      this.configFingerprint = null;
      this.configKeyFile = null;
      this.configSecurityTokenFile = null;
      this.privateKey = null;
      this.token = null;
   }

   public void init(Protocol<NativePacketPayload> prot, MysqlCallbackHandler cbh) {
      this.protocol = prot;
      this.usernameCallbackHandler = cbh;
   }

   public void reset() {
      this.configFingerprint = null;
      this.privateKey = null;
   }

   public void destroy() {
      this.reset();
      this.protocol = null;
      this.usernameCallbackHandler = null;
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
      if (this.sourceOfAuthData.equals(PLUGIN_NAME) && fromServer.getPayloadLength() != 0) {
         this.loadOciConfig();
         this.initializePrivateKey();
         this.initializeToken();
         byte[] nonce = fromServer.readBytes(NativeConstants.StringSelfDataType.STRING_EOF);
         byte[] signature = ExportControlled.sign(nonce, this.privateKey);
         if (signature == null) {
            signature = new byte[0];
         }

         String payload = String.format("{\"fingerprint\":\"%s\", \"signature\":\"%s\", \"token\":\"%s\"}", this.configFingerprint, Base64.getEncoder().encodeToString(signature), new String(this.token));
         toServer.add(new NativePacketPayload(payload.getBytes(Charset.defaultCharset())));
         return true;
      } else {
         toServer.add(new NativePacketPayload(0));
         return true;
      }
   }

   private void loadOciConfig() {
      ConfigFile configFile;
      try {
         String configFilePath = this.protocol.getPropertySet().getStringProperty(PropertyKey.ociConfigFile.getKeyName()).getStringValue();
         String configProfile = this.protocol.getPropertySet().getStringProperty(PropertyKey.ociConfigProfile.getKeyName()).getStringValue();
         if (StringUtils.isNullOrEmpty(configFilePath)) {
            configFile = ConfigFileReader.parseDefault(configProfile);
         } else {
            if (!Files.exists(Paths.get(configFilePath), new LinkOption[0])) {
               throw ExceptionFactory.createException(Messages.getString("AuthenticationOciClientPlugin.ConfigFileNotFound"));
            }

            configFile = ConfigFileReader.parse(configFilePath, configProfile);
         }
      } catch (NoClassDefFoundError var4) {
         throw ExceptionFactory.createException((String)Messages.getString("AuthenticationOciClientPlugin.OciSdkNotFound"), (Throwable)var4);
      } catch (IOException var5) {
         throw ExceptionFactory.createException((String)Messages.getString("AuthenticationOciClientPlugin.OciConfigFileError"), (Throwable)var5);
      } catch (IllegalArgumentException var6) {
         throw ExceptionFactory.createException((String)Messages.getString("AuthenticationOciClientPlugin.ProfileNotFound"), (Throwable)var6);
      }

      this.configFingerprint = configFile.get("fingerprint");
      if (StringUtils.isNullOrEmpty(this.configFingerprint)) {
         throw ExceptionFactory.createException(Messages.getString("AuthenticationOciClientPlugin.OciConfigFileMissingEntry"));
      } else {
         this.configKeyFile = configFile.get("key_file");
         if (StringUtils.isNullOrEmpty(this.configKeyFile)) {
            throw ExceptionFactory.createException(Messages.getString("AuthenticationOciClientPlugin.OciConfigFileMissingEntry"));
         } else {
            this.configSecurityTokenFile = configFile.get("security_token_file");
         }
      }
   }

   private void initializePrivateKey() {
      if (this.privateKey == null) {
         try {
            Path keyFilePath = Paths.get(this.configKeyFile);
            if (Files.notExists(keyFilePath, new LinkOption[0])) {
               throw ExceptionFactory.createException(Messages.getString("AuthenticationOciClientPlugin.PrivateKeyNotFound"));
            } else {
               String key = new String(Files.readAllBytes(keyFilePath));
               this.privateKey = ExportControlled.decodeRSAPrivateKey(key);
            }
         } catch (IOException var3) {
            throw ExceptionFactory.createException((String)Messages.getString("AuthenticationOciClientPlugin.FailedReadingPrivateKey"), (Throwable)var3);
         } catch (IllegalArgumentException | RSAException var4) {
            throw ExceptionFactory.createException((String)Messages.getString("AuthenticationOciClientPlugin.PrivateKeyNotValid"), (Throwable)var4);
         }
      }
   }

   private void initializeToken() {
      if (this.token == null) {
         if (StringUtils.isNullOrEmpty(this.configSecurityTokenFile)) {
            this.token = new byte[0];
         } else {
            try {
               Path securityTokenFilePath = Paths.get(this.configSecurityTokenFile);
               if (Files.notExists(securityTokenFilePath, new LinkOption[0])) {
                  throw ExceptionFactory.createException(Messages.getString("AuthenticationOciClientPlugin.SecurityTokenFileNotFound"));
               } else {
                  long size = Files.size(securityTokenFilePath);
                  if (size > 10240L) {
                     throw ExceptionFactory.createException(Messages.getString("AuthenticationOciClientPlugin.SecurityTokenTooBig"));
                  } else {
                     this.token = Files.readAllBytes(Paths.get(this.configSecurityTokenFile));
                  }
               }
            } catch (IOException var4) {
               throw ExceptionFactory.createException((String)Messages.getString("AuthenticationOciClientPlugin.FailedReadingSecurityTokenFile"), (Throwable)var4);
            }
         }
      }
   }
}
