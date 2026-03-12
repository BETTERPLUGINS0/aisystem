package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEventsAPI;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.ConnectionState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.ResolvableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Dimension;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle;
import ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class User implements IRegistryHolder {
   private final Object channel;
   private ConnectionState decoderState;
   private ConnectionState encoderState;
   private ClientVersion clientVersion;
   private final UserProfile profile;
   private int entityId = -1;
   private DimensionType dimensionType;
   private final Map<ResourceLocation, IRegistry<?>> registries;

   public User(Object channel, ConnectionState connectionState, ClientVersion clientVersion, UserProfile profile) {
      this.dimensionType = DimensionTypes.OVERWORLD;
      this.registries = new ConcurrentHashMap();
      this.channel = channel;
      this.decoderState = connectionState;
      this.encoderState = connectionState;
      this.clientVersion = clientVersion;
      this.profile = profile;
   }

   @ApiStatus.Internal
   @Nullable
   public IRegistry<?> getRegistry(ResourceLocation registryKey, ClientVersion version) {
      return (IRegistry)this.registries.get(registryKey);
   }

   @ApiStatus.Internal
   public void putRegistry(IRegistry<?> registry) {
      this.registries.put(registry.getRegistryKey(), registry);
   }

   @ApiStatus.Internal
   public void finalizeRegistries(PacketWrapper<?> wrapper) {
      Iterator var2 = this.registries.values().iterator();

      while(var2.hasNext()) {
         IRegistry<?> registry = (IRegistry)var2.next();
         Iterator var4 = registry.getEntries().iterator();

         while(var4.hasNext()) {
            MappedEntity entry = (MappedEntity)var4.next();
            if (entry instanceof ResolvableEntity) {
               ((ResolvableEntity)entry).doResolve(wrapper);
            }
         }
      }

   }

   public Object getChannel() {
      return this.channel;
   }

   public InetSocketAddress getAddress() {
      return (InetSocketAddress)ChannelHelper.remoteAddress(this.channel);
   }

   @ApiStatus.Obsolete
   public ConnectionState getConnectionState() throws IllegalStateException {
      ConnectionState decoderState = this.decoderState;
      ConnectionState encoderState = this.encoderState;
      if (decoderState != encoderState) {
         throw new IllegalStateException("Can't get common connection state: " + decoderState + " != " + encoderState);
      } else {
         return decoderState;
      }
   }

   @ApiStatus.Internal
   public void setConnectionState(ConnectionState connectionState) {
      this.setDecoderState(connectionState);
      this.setEncoderState(connectionState);
   }

   public ConnectionState getDecoderState() {
      return this.decoderState;
   }

   @ApiStatus.Internal
   public void setDecoderState(ConnectionState decoderState) {
      this.decoderState = decoderState;
      PacketEvents.getAPI().getLogManager().debug("Transitioned " + this.getName() + "'s decoder into " + decoderState + " state!");
   }

   public ConnectionState getEncoderState() {
      return this.encoderState;
   }

   @ApiStatus.Internal
   public void setEncoderState(ConnectionState encoderState) {
      this.encoderState = encoderState;
      PacketEvents.getAPI().getLogManager().debug("Transitioned " + this.getName() + "'s encoder into " + encoderState + " state!");
   }

   public ClientVersion getClientVersion() {
      return this.clientVersion;
   }

   public void setClientVersion(ClientVersion clientVersion) {
      this.clientVersion = clientVersion;
   }

   public UserProfile getProfile() {
      return this.profile;
   }

   public String getName() {
      return this.profile.getName();
   }

   public UUID getUUID() {
      return this.profile.getUUID();
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public void sendPacket(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, buffer);
   }

   public void sendPacket(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, wrapper);
   }

   public void sendPacketSilently(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.channel, buffer);
   }

   public void sendPacketSilently(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().sendPacketSilently(this.channel, wrapper);
   }

   public void writePacket(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().writePacket(this.channel, buffer);
   }

   public void writePacket(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().writePacket(this.channel, wrapper);
   }

   public void writePacketSilently(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().writePacketSilently(this.channel, buffer);
   }

   public void writePacketSilently(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().writePacketSilently(this.channel, wrapper);
   }

   public void receivePacket(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().receivePacket(this.channel, buffer);
   }

   public void receivePacket(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().receivePacket(this.channel, wrapper);
   }

   public void receivePacketSilently(Object buffer) {
      PacketEvents.getAPI().getProtocolManager().receivePacketSilently(this.channel, buffer);
   }

   public void receivePacketSilently(PacketWrapper<?> wrapper) {
      PacketEvents.getAPI().getProtocolManager().receivePacketSilently(this.channel, wrapper);
   }

   public void flushPackets() {
      ChannelHelper.flush(this.channel);
   }

   public void closeConnection() {
      ChannelHelper.close(this.channel);
   }

   public void closeInventory() {
      WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow(0);
      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, (PacketWrapper)closeWindow);
   }

   public void sendMessage(String legacyMessage) {
      this.sendMessage(this.getSerializers().fromLegacy(legacyMessage));
   }

   public void sendMessage(Component component) {
      this.sendMessage(component, ChatTypes.CHAT);
   }

   public void sendMessage(Component component, ChatType type) {
      ClientVersion version = this.getPacketVersion();
      Object chatPacket;
      if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
         chatPacket = new WrapperPlayServerSystemChatMessage(false, component);
      } else {
         Object message;
         if (version.isNewerThanOrEquals(ClientVersion.V_1_16)) {
            message = new ChatMessage_v1_16(component, type, new UUID(0L, 0L));
         } else {
            message = new ChatMessageLegacy(component, type);
         }

         chatPacket = new WrapperPlayServerChatMessage((ChatMessage)message);
      }

      PacketEvents.getAPI().getProtocolManager().sendPacket(this.channel, (PacketWrapper)chatPacket);
   }

   public void sendTitle(String legacyTitle, String legacySubtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
      LegacyComponentSerializer serializer = this.getSerializers().legacy();
      Component title = serializer.deserialize(legacyTitle);
      Component subtitle = serializer.deserialize(legacySubtitle);
      this.sendTitle((Component)title, (Component)subtitle, fadeInTicks, stayTicks, fadeOutTicks);
   }

   public void sendTitle(Component title, Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
      boolean modern = this.getPacketVersion().isNewerThanOrEquals(ClientVersion.V_1_17);
      PacketWrapper<?> setTitle = null;
      PacketWrapper<?> setSubtitle = null;
      Object animation;
      if (modern) {
         animation = new WrapperPlayServerSetTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
         if (title != null) {
            setTitle = new WrapperPlayServerSetTitleText(title);
         }

         if (subtitle != null) {
            setSubtitle = new WrapperPlayServerSetTitleSubtitle(subtitle);
         }
      } else {
         animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction.SET_TIMES_AND_DISPLAY, (Component)null, (Component)null, (Component)null, fadeInTicks, stayTicks, fadeOutTicks);
         if (title != null) {
            setTitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction.SET_TITLE, title, (Component)null, (Component)null, 0, 0, 0);
         }

         if (subtitle != null) {
            setSubtitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.TitleAction.SET_SUBTITLE, (Component)null, subtitle, (Component)null, 0, 0, 0);
         }
      }

      this.sendPacket((PacketWrapper)animation);
      if (setTitle != null) {
         this.sendPacket((PacketWrapper)setTitle);
      }

      if (setSubtitle != null) {
         this.sendPacket((PacketWrapper)setSubtitle);
      }

   }

   public ClientVersion getPacketVersion() {
      PacketEventsAPI<?> api = PacketEvents.getAPI();
      return api.getInjector().isProxy() ? this.getClientVersion() : api.getServerManager().getVersion().toClientVersion();
   }

   public AdventureSerializer getSerializers() {
      return AdventureSerializer.serializer(this.getPacketVersion());
   }

   public int getMinWorldHeight() {
      return this.dimensionType.getMinY();
   }

   /** @deprecated */
   @Deprecated
   public int getMinWorldHeight(@Nullable ClientVersion version) {
      return this.getMinWorldHeight();
   }

   public int getTotalWorldHeight() {
      return this.dimensionType.getHeight();
   }

   /** @deprecated */
   @Deprecated
   public int getTotalWorldHeight(@Nullable ClientVersion version) {
      return this.getTotalWorldHeight();
   }

   public DimensionType getDimensionType() {
      return this.dimensionType;
   }

   @ApiStatus.Internal
   public void setDimensionType(DimensionType dimensionType) {
      this.dimensionType = dimensionType;
   }

   /** @deprecated */
   @Deprecated
   public void setMinWorldHeight(int minWorldHeight) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void setTotalWorldHeight(int totalWorldHeight) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void switchDimensionType(ServerVersion version, Dimension dimension) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void setDefaultWorldHeights(ServerVersion version, Dimension dimension) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void setDefaultWorldHeights(boolean extended) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public void setWorldNBT(NBTList<NBTCompound> worldNBT) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public Dimension getDimension() {
      return Dimension.fromDimensionType(this.dimensionType, this, (ClientVersion)null);
   }

   /** @deprecated */
   @Deprecated
   public void setDimension(Dimension dimension) {
      this.dimensionType = dimension.asDimensionType(this, (ClientVersion)null);
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public NBTCompound getWorldNBT(String worldName) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public NBTCompound getWorldNBT(int worldId) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public NBTCompound getWorldNBT(Dimension dimension) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public String getWorldName(int worldId) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public String getWorldName(Dimension dimension) {
      throw new UnsupportedOperationException();
   }
}
