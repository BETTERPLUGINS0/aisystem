package fr.xephi.authme.libs.org.mariadb.jdbc.message.server;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.ReadableByteBuf;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.ServerVersion;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.ServerMessage;
import fr.xephi.authme.libs.org.mariadb.jdbc.message.server.util.ServerVersionUtility;

public final class InitialHandshakePacket implements ServerMessage {
   private static final String MARIADB_RPL_HACK_PREFIX = "5.5.5-";
   private final long threadId;
   private final byte[] seed;
   private final long capabilities;
   private final short defaultCollation;
   private final short serverStatus;
   private final String authenticationPluginType;
   private final ServerVersion version;

   private InitialHandshakePacket(String serverVersion, long threadId, byte[] seed, long capabilities, short defaultCollation, short serverStatus, boolean mariaDBServer, String authenticationPluginType) {
      this.threadId = threadId;
      this.seed = seed;
      this.capabilities = capabilities;
      this.defaultCollation = defaultCollation;
      this.serverStatus = serverStatus;
      this.authenticationPluginType = authenticationPluginType;
      this.version = new ServerVersionUtility(serverVersion, mariaDBServer);
   }

   public static InitialHandshakePacket decode(ReadableByteBuf reader) {
      byte protocolVersion = reader.readByte();
      if (protocolVersion != 10) {
         throw new IllegalArgumentException(String.format("Unexpected initial handshake protocol value [%s]", protocolVersion));
      } else {
         String serverVersion = reader.readStringNullEnd();
         long threadId = (long)reader.readInt();
         byte[] seed1 = new byte[8];
         reader.readBytes(seed1);
         reader.skip();
         int serverCapabilities2FirstBytes = reader.readUnsignedShort();
         short defaultCollation = reader.readUnsignedByte();
         short serverStatus = reader.readShort();
         int serverCapabilities4FirstBytes = serverCapabilities2FirstBytes + (reader.readShort() << 16);
         int saltLength = 0;
         if ((serverCapabilities4FirstBytes & 524288) != 0) {
            saltLength = Math.max(12, reader.readByte() - 9);
         } else {
            reader.skip();
         }

         reader.skip(6);
         long mariaDbAdditionalCapacities = (long)reader.readInt();
         byte[] seed;
         if ((serverCapabilities4FirstBytes & '耀') != 0) {
            byte[] seed2;
            if (saltLength > 0) {
               seed2 = new byte[saltLength];
               reader.readBytes(seed2);
            } else {
               seed2 = reader.readBytesNullEnd();
            }

            seed = new byte[seed1.length + seed2.length];
            System.arraycopy(seed1, 0, seed, 0, seed1.length);
            System.arraycopy(seed2, 0, seed, seed1.length, seed2.length);
         } else {
            seed = seed1;
         }

         reader.skip();
         boolean serverMariaDb;
         if (serverVersion.startsWith("5.5.5-")) {
            serverMariaDb = true;
            serverVersion = serverVersion.substring("5.5.5-".length());
         } else {
            serverMariaDb = serverVersion.contains("MariaDB");
         }

         long serverCapabilities;
         if ((serverCapabilities4FirstBytes & 1) == 0) {
            serverCapabilities = ((long)serverCapabilities4FirstBytes & 4294967295L) + (mariaDbAdditionalCapacities << 32);
            serverMariaDb = true;
         } else {
            serverCapabilities = (long)serverCapabilities4FirstBytes & 4294967295L;
         }

         String authenticationPluginType = null;
         if ((serverCapabilities4FirstBytes & 524288) != 0) {
            authenticationPluginType = reader.readStringNullEnd();
         }

         return new InitialHandshakePacket(serverVersion, threadId, seed, serverCapabilities, defaultCollation, serverStatus, serverMariaDb, authenticationPluginType);
      }
   }

   public ServerVersion getVersion() {
      return this.version;
   }

   public long getThreadId() {
      return this.threadId;
   }

   public byte[] getSeed() {
      return this.seed;
   }

   public long getCapabilities() {
      return this.capabilities;
   }

   public short getDefaultCollation() {
      return this.defaultCollation;
   }

   public short getServerStatus() {
      return this.serverStatus;
   }

   public String getAuthenticationPluginType() {
      return this.authenticationPluginType;
   }
}
