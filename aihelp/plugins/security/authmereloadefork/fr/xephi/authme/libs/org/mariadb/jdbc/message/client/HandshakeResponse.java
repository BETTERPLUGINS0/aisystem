package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.Configuration;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl.PacketWriter;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.MutableByte;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Credential;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.authentication.standard.NativePasswordPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.ThreadUtils;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.VersionFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public final class HandshakeResponse implements ClientMessage {
   private static final String _CLIENT_NAME = "_client_name";
   private static final String _CLIENT_VERSION = "_client_version";
   private static final String _SERVER_HOST = "_server_host";
   private static final String _OS = "_os";
   private static final String _THREAD = "_thread";
   private static final String _JAVA_VENDOR = "_java_vendor";
   private static final String _JAVA_VERSION = "_java_version";
   private final String username;
   private final CharSequence password;
   private final String database;
   private final String connectionAttributes;
   private final String host;
   private final long clientCapabilities;
   private final byte exchangeCharset;
   private final byte[] seed;
   private String authenticationPluginType;

   public HandshakeResponse(Credential credential, String authenticationPluginType, byte[] seed, Configuration conf, String host, long clientCapabilities, byte exchangeCharset) {
      this.authenticationPluginType = authenticationPluginType;
      this.seed = seed;
      this.username = credential.getUser();
      this.password = credential.getPassword();
      this.database = conf.database();
      this.connectionAttributes = conf.connectionAttributes();
      this.host = host;
      this.clientCapabilities = clientCapabilities;
      this.exchangeCharset = exchangeCharset;
   }

   private static void writeStringLengthAscii(Writer encoder, String value) throws IOException {
      byte[] valBytes = value.getBytes(StandardCharsets.US_ASCII);
      encoder.writeLength((long)valBytes.length);
      encoder.writeBytes(valBytes);
   }

   private static void writeStringLength(Writer encoder, String value) throws IOException {
      byte[] valBytes = value.getBytes(StandardCharsets.UTF_8);
      encoder.writeLength((long)valBytes.length);
      encoder.writeBytes(valBytes);
   }

   private static void writeConnectAttributes(Writer writer, String connectionAttributes, String host) throws IOException {
      PacketWriter tmpWriter = new PacketWriter((OutputStream)null, 0, 0, (MutableByte)null, (MutableByte)null);
      tmpWriter.pos(0);
      writeStringLengthAscii(tmpWriter, "_client_name");
      writeStringLength(tmpWriter, "MariaDB Connector/J");
      writeStringLengthAscii(tmpWriter, "_client_version");
      writeStringLength(tmpWriter, VersionFactory.getInstance().getVersion());
      writeStringLengthAscii(tmpWriter, "_server_host");
      writeStringLength(tmpWriter, host != null ? host : "");
      writeStringLengthAscii(tmpWriter, "_os");
      writeStringLength(tmpWriter, System.getProperty("os.name"));
      writeStringLengthAscii(tmpWriter, "_thread");
      writeStringLength(tmpWriter, Long.toString(ThreadUtils.getId(Thread.currentThread())));
      writeStringLengthAscii(tmpWriter, "_java_vendor");
      writeStringLength(tmpWriter, System.getProperty("java.vendor"));
      writeStringLengthAscii(tmpWriter, "_java_version");
      writeStringLength(tmpWriter, System.getProperty("java.version"));
      if (connectionAttributes != null) {
         StringTokenizer tokenizer = new StringTokenizer(connectionAttributes, ",");

         while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int separator = token.indexOf(":");
            if (separator != -1) {
               writeStringLength(tmpWriter, token.substring(0, separator));
               writeStringLength(tmpWriter, token.substring(separator + 1));
            } else {
               writeStringLength(tmpWriter, token);
               writeStringLength(tmpWriter, "");
            }
         }
      }

      writer.writeLength((long)tmpWriter.pos());
      writer.writeBytes(tmpWriter.buf(), 0, tmpWriter.pos());
   }

   public int encode(Writer writer, Context context) throws IOException {
      byte[] authData;
      if ("mysql_clear_password".equals(this.authenticationPluginType)) {
         if (!context.hasClientCapability(2048L)) {
            throw new IllegalStateException("Cannot send password in clear if SSL is not enabled.");
         }

         authData = this.password == null ? new byte[0] : this.password.toString().getBytes(StandardCharsets.UTF_8);
      } else {
         this.authenticationPluginType = "mysql_native_password";
         authData = NativePasswordPlugin.encryptPassword(this.password, this.seed);
      }

      writer.writeInt((int)this.clientCapabilities);
      writer.writeInt(1073741824);
      writer.writeByte(this.exchangeCharset);
      writer.writeBytes(new byte[19]);
      writer.writeInt((int)(this.clientCapabilities >> 32));
      writer.writeString(this.username != null ? this.username : System.getProperty("user.name"));
      writer.writeByte(0);
      if (context.hasServerCapability(2097152L)) {
         writer.writeLength((long)authData.length);
         writer.writeBytes(authData);
      } else if (context.hasServerCapability(32768L)) {
         writer.writeByte((byte)authData.length);
         writer.writeBytes(authData);
      } else {
         writer.writeBytes(authData);
         writer.writeByte(0);
      }

      if (context.hasClientCapability(8L)) {
         writer.writeString(this.database);
         writer.writeByte(0);
      }

      if (context.hasServerCapability(524288L)) {
         writer.writeString(this.authenticationPluginType);
         writer.writeByte(0);
      }

      if (context.hasServerCapability(1048576L)) {
         writeConnectAttributes(writer, this.connectionAttributes, this.host);
      }

      writer.flush();
      return 1;
   }
}
