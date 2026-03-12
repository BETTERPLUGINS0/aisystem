package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.handshaking.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.exception.InvalidHandshakeException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperHandshakingClientHandshake extends PacketWrapper<WrapperHandshakingClientHandshake> {
   private int protocolVersion;
   private ClientVersion clientVersion;
   private String serverAddress;
   private int serverPort;
   private WrapperHandshakingClientHandshake.ConnectionIntention intention;

   public WrapperHandshakingClientHandshake(PacketReceiveEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperHandshakingClientHandshake(int protocolVersion, String serverAddress, int serverPort, ConnectionState nextConnectionState) {
      this(protocolVersion, serverAddress, serverPort, WrapperHandshakingClientHandshake.ConnectionIntention.LOGIN);
      this.setNextConnectionState(nextConnectionState);
   }

   public WrapperHandshakingClientHandshake(int protocolVersion, String serverAddress, int serverPort, WrapperHandshakingClientHandshake.ConnectionIntention intention) {
      super((PacketTypeCommon)PacketType.Handshaking.Client.HANDSHAKE);
      this.protocolVersion = protocolVersion;
      this.clientVersion = ClientVersion.getById(protocolVersion);
      this.serverAddress = serverAddress;
      this.serverPort = serverPort;
      this.intention = intention;
   }

   public void read() {
      try {
         this.protocolVersion = this.readVarInt();
         this.clientVersion = ClientVersion.getById(this.protocolVersion);
         this.serverAddress = this.readString(32767);
         this.serverPort = this.readUnsignedShort();
         int nextStateIndex = this.readVarInt();
         this.intention = WrapperHandshakingClientHandshake.ConnectionIntention.fromId(nextStateIndex);
      } catch (Exception var2) {
         throw new InvalidHandshakeException();
      }
   }

   public void write() {
      this.writeVarInt(this.protocolVersion);
      this.writeString(this.serverAddress, 32767);
      this.writeShort(this.serverPort);
      this.writeVarInt(this.intention.getId());
   }

   public void copy(WrapperHandshakingClientHandshake wrapper) {
      this.protocolVersion = wrapper.protocolVersion;
      this.clientVersion = wrapper.clientVersion;
      this.serverAddress = wrapper.serverAddress;
      this.serverPort = wrapper.serverPort;
      this.intention = wrapper.intention;
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }

   public void setProtocolVersion(int protocolVersion) {
      this.protocolVersion = protocolVersion;
      this.clientVersion = ClientVersion.getById(protocolVersion);
   }

   public ClientVersion getClientVersion() {
      return this.clientVersion;
   }

   public void setClientVersion(ClientVersion clientVersion) {
      this.clientVersion = clientVersion;
      this.protocolVersion = clientVersion.getProtocolVersion();
   }

   public String getServerAddress() {
      return this.serverAddress;
   }

   public void setServerAddress(String serverAddress) {
      this.serverAddress = serverAddress;
   }

   public int getServerPort() {
      return this.serverPort;
   }

   public void setServerPort(int serverPort) {
      this.serverPort = serverPort;
   }

   public ConnectionState getNextConnectionState() {
      return this.intention.getTargetState();
   }

   /** @deprecated */
   public void setNextConnectionState(ConnectionState nextConnectionState) {
      switch(nextConnectionState) {
      case LOGIN:
         this.intention = WrapperHandshakingClientHandshake.ConnectionIntention.LOGIN;
         break;
      case STATUS:
         this.intention = WrapperHandshakingClientHandshake.ConnectionIntention.STATUS;
         break;
      default:
         throw new IllegalArgumentException("Illegal next connection state: " + nextConnectionState);
      }

   }

   public WrapperHandshakingClientHandshake.ConnectionIntention getIntention() {
      return this.intention;
   }

   public void setIntention(WrapperHandshakingClientHandshake.ConnectionIntention intention) {
      this.intention = intention;
   }

   public static enum ConnectionIntention {
      STATUS(1, ConnectionState.STATUS),
      LOGIN(2, ConnectionState.LOGIN),
      TRANSFER(3, ConnectionState.LOGIN);

      private final int id;
      private final ConnectionState targetState;

      private ConnectionIntention(int id, ConnectionState targetState) {
         this.id = id;
         this.targetState = targetState;
      }

      public static WrapperHandshakingClientHandshake.ConnectionIntention fromId(int id) {
         switch(id) {
         case 1:
            return STATUS;
         case 2:
            return LOGIN;
         case 3:
            return TRANSFER;
         default:
            throw new IllegalArgumentException("Illegal connection intention: " + id);
         }
      }

      public int getId() {
         return this.id;
      }

      public ConnectionState getTargetState() {
         return this.targetState;
      }

      // $FF: synthetic method
      private static WrapperHandshakingClientHandshake.ConnectionIntention[] $values() {
         return new WrapperHandshakingClientHandshake.ConnectionIntention[]{STATUS, LOGIN, TRANSFER};
      }
   }
}
