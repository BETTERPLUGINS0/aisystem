package fr.xephi.authme.libs.org.mariadb.jdbc.message.server;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ServerMessage;
import java.util.Arrays;

public class AuthSwitchPacket implements ServerMessage {
   private final String plugin;
   private final byte[] seed;

   public AuthSwitchPacket(String plugin, byte[] seed) {
      this.plugin = plugin;
      this.seed = seed;
   }

   public static AuthSwitchPacket decode(ReadableByteBuf buf) {
      buf.skip(1);
      String plugin = buf.readStringNullEnd();
      byte[] seed = new byte[buf.readableBytes()];
      buf.readBytes(seed);
      return new AuthSwitchPacket(plugin, seed);
   }

   public static byte[] getTruncatedSeed(byte[] seed) {
      return seed.length > 0 ? Arrays.copyOfRange(seed, 0, seed.length - 1) : new byte[0];
   }

   public String getPlugin() {
      return this.plugin;
   }

   public byte[] getSeed() {
      return this.seed;
   }
}
