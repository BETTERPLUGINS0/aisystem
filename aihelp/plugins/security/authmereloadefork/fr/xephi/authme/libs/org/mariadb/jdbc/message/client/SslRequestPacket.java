package fr.xephi.authme.libs.org.mariadb.jdbc.message.client;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.Writer;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ClientMessage;
import java.io.IOException;

public final class SslRequestPacket implements ClientMessage {
   private final long clientCapabilities;
   private final byte exchangeCharset;

   private SslRequestPacket(long clientCapabilities, byte exchangeCharset) {
      this.clientCapabilities = clientCapabilities;
      this.exchangeCharset = exchangeCharset;
   }

   public static SslRequestPacket create(long clientCapabilities, byte exchangeCharset) {
      return new SslRequestPacket(clientCapabilities, exchangeCharset);
   }

   public int encode(Writer writer, Context context) throws IOException {
      writer.writeInt((int)this.clientCapabilities);
      writer.writeInt(1073741824);
      writer.writeByte(this.exchangeCharset);
      writer.writeBytes(new byte[19]);
      writer.writeInt((int)(this.clientCapabilities >> 32));
      writer.flush();
      return 0;
   }
}
