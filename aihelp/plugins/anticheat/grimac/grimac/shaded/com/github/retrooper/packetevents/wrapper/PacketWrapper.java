package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.VersionComparison;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.PacketSide;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.MessageSignature;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.Node;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.Parsers;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.SignedCommandArgument;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.filter.FilterMaskType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.EntityMetadataProvider;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfession;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStackSerialization;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.PublicProfileKey;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.MerchantItemCost;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.data.MerchantOffer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Dimension;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Either;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.KnownPack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.StringUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.MinecraftEncryptionUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.SaltSignature;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.crypto.SignatureData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public class PacketWrapper<T extends PacketWrapper<T>> {
   @Nullable
   public Object buffer;
   @ApiStatus.Internal
   public final Object bufferLock;
   protected ClientVersion clientVersion;
   protected ServerVersion serverVersion;
   private PacketTypeData packetTypeData;
   @Nullable
   protected User user;
   private static final int MODERN_MESSAGE_LENGTH = 262144;
   private static final int LEGACY_MESSAGE_LENGTH = 32767;

   public PacketWrapper(ClientVersion clientVersion, ServerVersion serverVersion, int packetID) {
      this.bufferLock = new Object();
      if (packetID == -1) {
         throw new IllegalArgumentException("Packet does not exist on this protocol version!");
      } else {
         this.clientVersion = clientVersion;
         this.serverVersion = serverVersion;
         this.buffer = null;
         this.packetTypeData = new PacketTypeData((PacketTypeCommon)null, packetID);
      }
   }

   public PacketWrapper(PacketReceiveEvent event) {
      this(event, true);
   }

   public PacketWrapper(PacketReceiveEvent event, boolean readData) {
      this.bufferLock = new Object();
      this.clientVersion = event.getUser().getClientVersion();
      this.serverVersion = event.getServerVersion();
      this.user = event.getUser();
      this.buffer = event.getByteBuf();
      this.packetTypeData = new PacketTypeData(event.getPacketType(), event.getPacketId());
      if (readData) {
         this.readEvent(event);
      }

   }

   public PacketWrapper(PacketSendEvent event) {
      this(event, true);
   }

   public PacketWrapper(PacketSendEvent event, boolean readData) {
      this.bufferLock = new Object();
      this.clientVersion = event.getUser().getClientVersion();
      this.serverVersion = event.getServerVersion();
      this.buffer = event.getByteBuf();
      this.packetTypeData = new PacketTypeData(event.getPacketType(), event.getPacketId());
      this.user = event.getUser();
      if (readData) {
         this.readEvent(event);
      }

   }

   public PacketWrapper(int packetID, ClientVersion clientVersion) {
      this(clientVersion, PacketEvents.getAPI().getServerManager().getVersion(), packetID);
   }

   public PacketWrapper(int packetID) {
      this.bufferLock = new Object();
      if (packetID == -1) {
         throw new IllegalArgumentException("Packet does not exist on this protocol version!");
      } else {
         this.clientVersion = ClientVersion.UNKNOWN;
         this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
         this.buffer = null;
         this.packetTypeData = new PacketTypeData((PacketTypeCommon)null, packetID);
      }
   }

   public PacketWrapper(PacketTypeCommon packetType) {
      this.bufferLock = new Object();
      this.clientVersion = ClientVersion.UNKNOWN;
      this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
      this.buffer = null;
      int id = packetType.getId(this.serverVersion.toClientVersion());
      this.packetTypeData = new PacketTypeData(packetType, id);
   }

   public static PacketWrapper<?> createDummyWrapper(ClientVersion version) {
      return new PacketWrapper(version, version.toServerVersion(), -2);
   }

   public static PacketWrapper<?> createUniversalPacketWrapper(Object byteBuf) {
      return createUniversalPacketWrapper(byteBuf, PacketEvents.getAPI().getServerManager().getVersion());
   }

   public static PacketWrapper<?> createUniversalPacketWrapper(Object byteBuf, ServerVersion version) {
      PacketWrapper<?> wrapper = new PacketWrapper(ClientVersion.UNKNOWN, version, -2);
      wrapper.buffer = byteBuf;
      return wrapper;
   }

   public static int getChunkX(long chunkKey) {
      return (int)(chunkKey & 4294967295L);
   }

   public static int getChunkZ(long chunkKey) {
      return (int)(chunkKey >>> 32 & 4294967295L);
   }

   public static long getChunkKey(int chunkX, int chunkZ) {
      return (long)chunkX & 4294967295L | ((long)chunkZ & 4294967295L) << 32;
   }

   @ApiStatus.Internal
   public final void prepareForSend(Object channel, boolean outgoing, boolean proxy) {
      if (this.buffer == null || ByteBufHelper.refCnt(this.buffer) == 0) {
         this.buffer = ChannelHelper.pooledByteBuf(channel);
      }

      if (proxy) {
         User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
         if (this.packetTypeData.getPacketType() == null) {
            this.packetTypeData.setPacketType(PacketType.getById(outgoing ? PacketSide.SERVER : PacketSide.CLIENT, user.getConnectionState(), this.serverVersion.toClientVersion(), this.packetTypeData.getNativePacketId()));
         }

         this.serverVersion = user.getClientVersion().toServerVersion();
         int id = this.packetTypeData.getPacketType().getId(user.getClientVersion());
         this.writeVarInt(id);
      } else {
         this.writeVarInt(this.packetTypeData.getNativePacketId());
      }

      this.write();
   }

   @ApiStatus.Internal
   public final void prepareForSend(Object channel, boolean outgoing) {
      this.prepareForSend(channel, outgoing, PacketEvents.getAPI().getInjector().isProxy());
   }

   public void read() {
   }

   public void write() {
   }

   public void copy(T wrapper) {
   }

   public final void readEvent(ProtocolPacketEvent event) {
      PacketWrapper<?> last = event.getLastUsedWrapper();
      if (this.getClass().isInstance(last)) {
         this.copy(last);
      } else {
         this.read();
      }

      event.setLastUsedWrapper(this);
   }

   public ClientVersion getClientVersion() {
      return this.clientVersion;
   }

   public void setClientVersion(ClientVersion clientVersion) {
      this.clientVersion = clientVersion;
   }

   public ServerVersion getServerVersion() {
      return this.serverVersion;
   }

   public void setServerVersion(ServerVersion serverVersion) {
      this.serverVersion = serverVersion;
   }

   public Object getBuffer() {
      return this.buffer;
   }

   public void setBuffer(Object buffer) {
      this.buffer = buffer;
   }

   /** @deprecated */
   @Deprecated
   public int getPacketId() {
      return this.getNativePacketId();
   }

   /** @deprecated */
   @Deprecated
   public void setPacketId(int packetID) {
      this.setNativePacketId(packetID);
   }

   public int getNativePacketId() {
      return this.packetTypeData.getNativePacketId();
   }

   public void setNativePacketId(int nativePacketId) {
      this.packetTypeData.setNativePacketId(nativePacketId);
   }

   @ApiStatus.Internal
   public PacketTypeData getPacketTypeData() {
      return this.packetTypeData;
   }

   public int getMaxMessageLength() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? 262144 : 32767;
   }

   /** @deprecated */
   @Deprecated
   public void resetByteBuf() {
      ByteBufHelper.clear(this.buffer);
   }

   public void resetBuffer() {
      ByteBufHelper.clear(this.buffer);
   }

   public byte readByte() {
      return ByteBufHelper.readByte(this.buffer);
   }

   public void writeByte(int value) {
      ByteBufHelper.writeByte(this.buffer, value);
   }

   public short readUnsignedByte() {
      return ByteBufHelper.readUnsignedByte(this.buffer);
   }

   public boolean readBoolean() {
      return this.readByte() != 0;
   }

   public void writeBoolean(boolean value) {
      this.writeByte(value ? 1 : 0);
   }

   public int readInt() {
      return ByteBufHelper.readInt(this.buffer);
   }

   public void writeInt(int value) {
      ByteBufHelper.writeInt(this.buffer, value);
   }

   public long readUnsignedInt() {
      return ByteBufHelper.readUnsignedInt(this.buffer);
   }

   public int readMedium() {
      return ByteBufHelper.readMedium(this.buffer);
   }

   public void writeMedium(int value) {
      ByteBufHelper.writeMedium(this.buffer, value);
   }

   public int readVarInt() {
      int value = 0;
      int length = 0;

      byte currentByte;
      do {
         currentByte = this.readByte();
         value |= (currentByte & 127) << length * 7;
         ++length;
         if (length > 5) {
            throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
         }
      } while((currentByte & 128) == 128);

      return value;
   }

   public void writeVarInt(int value) {
      if ((value & -128) == 0) {
         this.writeByte(value);
      } else {
         int w;
         if ((value & -16384) == 0) {
            w = (value & 127 | 128) << 8 | value >>> 7;
            this.writeShort(w);
         } else if ((value & -2097152) == 0) {
            w = (value & 127 | 128) << 16 | (value >>> 7 & 127 | 128) << 8 | value >>> 14;
            this.writeMedium(w);
         } else if ((value & -268435456) == 0) {
            w = (value & 127 | 128) << 24 | (value >>> 7 & 127 | 128) << 16 | (value >>> 14 & 127 | 128) << 8 | value >>> 21;
            this.writeInt(w);
         } else {
            w = (value & 127 | 128) << 24 | (value >>> 7 & 127 | 128) << 16 | (value >>> 14 & 127 | 128) << 8 | value >>> 21 & 127 | 128;
            this.writeInt(w);
            this.writeByte(value >>> 28);
         }
      }

   }

   public <K, V> Map<K, V> readMap(PacketWrapper.Reader<K> keyFunction, PacketWrapper.Reader<V> valueFunction) {
      return this.readMap(keyFunction, valueFunction, Integer.MAX_VALUE);
   }

   public <K, V> Map<K, V> readMap(PacketWrapper.Reader<K> keyFunction, PacketWrapper.Reader<V> valueFunction, int maxSize) {
      int size = this.readVarInt();
      if (size > maxSize) {
         throw new RuntimeException(size + " elements exceeded max size of: " + maxSize);
      } else {
         Map<K, V> map = new HashMap(size);

         for(int i = 0; i < size; ++i) {
            K key = keyFunction.apply(this);
            V value = valueFunction.apply(this);
            map.put(key, value);
         }

         return map;
      }
   }

   public <K, V> void writeOptionalMap(Map<K, Optional<V>> map, PacketWrapper.Writer<K> keyConsumer, PacketWrapper.Writer<V> valueConsumer) {
      this.writeVarInt(map.size());
      Iterator var4 = map.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<K, Optional<V>> entry = (Entry)var4.next();
         K key = entry.getKey();
         Optional<V> value = (Optional)entry.getValue();
         keyConsumer.accept(this, key);
         value.ifPresent((v) -> {
            valueConsumer.accept(this, v);
         });
      }

   }

   public <K, V> void writeMap(Map<K, V> map, PacketWrapper.Writer<K> keyConsumer, PacketWrapper.Writer<V> valueConsumer) {
      this.writeVarInt(map.size());
      Iterator var4 = map.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<K, V> entry = (Entry)var4.next();
         K key = entry.getKey();
         V value = entry.getValue();
         keyConsumer.accept(this, key);
         valueConsumer.accept(this, value);
      }

   }

   public VillagerData readVillagerData() {
      VillagerType type = (VillagerType)this.readMappedEntity((IRegistry)VillagerTypes.getRegistry());
      VillagerProfession profession = (VillagerProfession)this.readMappedEntity((IRegistry)VillagerProfessions.getRegistry());
      int level = this.readVarInt();
      return new VillagerData(type, profession, level);
   }

   public void writeVillagerData(VillagerData data) {
      this.writeMappedEntity(data.getType());
      this.writeMappedEntity(data.getProfession());
      this.writeVarInt(data.getLevel());
   }

   public ItemStack readItemStackModern() {
      return ItemStackSerialization.readModern(this);
   }

   public ItemStack readPresentItemStack() {
      ItemStack itemStack = this.readItemStack();
      if (itemStack.isEmpty()) {
         throw new RuntimeException("Empty ItemStack not allowed");
      } else {
         return itemStack;
      }
   }

   @NotNull
   public ItemStack readItemStack() {
      return ItemStackSerialization.read(this);
   }

   public void writeItemStackModern(ItemStack stack) {
      ItemStackSerialization.writeModern(this, stack);
   }

   public void writePresentItemStack(ItemStack itemStack) {
      if (itemStack != null && !itemStack.isEmpty()) {
         this.writeItemStack(itemStack);
      } else {
         throw new RuntimeException("Empty ItemStack not allowed");
      }
   }

   public void writeItemStack(ItemStack stack) {
      ItemStackSerialization.write(this, stack);
   }

   public NBTCompound readNBT() {
      return (NBTCompound)this.readNBTRaw();
   }

   @Nullable
   public NBT readNullableNBT() {
      NBT tag = this.readNBTRaw();
      return tag == NBTEnd.INSTANCE ? null : tag;
   }

   public NBT readNBTRaw() {
      return NBTCodec.readNBTFromBuffer(this.buffer, this.serverVersion);
   }

   public NBTCompound readUnlimitedNBT() {
      return (NBTCompound)this.readUnlimitedNBTRaw();
   }

   public NBT readUnlimitedNBTRaw() {
      return NBTCodec.readNBTFromBuffer(this.buffer, this.serverVersion, NBTLimiter.noop());
   }

   public void writeNBT(NBTCompound nbt) {
      this.writeNBTRaw(nbt);
   }

   public void writeNBTRaw(NBT nbt) {
      NBTCodec.writeNBTToBuffer(this.buffer, this.serverVersion, nbt);
   }

   public String readString() {
      return this.readString(32767);
   }

   public String readString(int maxLen) {
      int j = this.readVarInt();
      if (j > maxLen * 4) {
         throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLen * 4 + ")");
      } else if (j < 0) {
         throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
      } else {
         String s = ByteBufHelper.toString(this.buffer, ByteBufHelper.readerIndex(this.buffer), j, StandardCharsets.UTF_8);
         ByteBufHelper.readerIndex(this.buffer, ByteBufHelper.readerIndex(this.buffer) + j);
         if (s.length() > maxLen) {
            throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
         } else {
            return s;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public String readComponentJSON() {
      return this.getSerializers().asJson(this.readComponent());
   }

   public void writeString(String s) {
      this.writeString(s, 32767);
   }

   public void writeString(String s, int maxLen) {
      this.writeString(s, maxLen, true);
   }

   public void writeString(String s, int maxLen, boolean substr) {
      if (substr) {
         s = StringUtil.maximizeLength(s, maxLen);
      }

      byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
      if (!substr && bytes.length > maxLen) {
         throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
      } else {
         this.writeVarInt(bytes.length);
         ByteBufHelper.writeBytes(this.buffer, bytes);
      }
   }

   public AdventureSerializer getSerializers() {
      return AdventureSerializer.serializer(this);
   }

   /** @deprecated */
   @Deprecated
   public void writeComponentJSON(String json) {
      this.writeComponent(this.getSerializers().fromJson(json));
   }

   public Component readComponent() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3) ? this.readComponentAsNBT() : this.readComponentAsJSON();
   }

   public Component readComponentAsNBT() {
      return this.getSerializers().fromNbtTag(this.readNBTRaw(), this);
   }

   public Component readComponentAsJSON() {
      String jsonString = this.readString(this.getMaxMessageLength());
      return this.getSerializers().fromJson(jsonString);
   }

   public void writeComponent(Component component) {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeComponentAsNBT(component);
      } else {
         this.writeComponentAsJSON(component);
      }

   }

   public void writeComponentAsNBT(Component component) {
      this.writeNBTRaw(this.getSerializers().asNbtTag(component, this));
   }

   public void writeComponentAsJSON(Component component) {
      String jsonString = this.getSerializers().asJson(component);
      this.writeString(jsonString, this.getMaxMessageLength());
   }

   public Style readStyle() {
      return this.getSerializers().nbt().deserializeStyle(this.readNBT(), this);
   }

   public void writeStyle(Style style) {
      this.writeNBT(this.getSerializers().nbt().serializeStyle(style, this));
   }

   public ResourceLocation readIdentifier(int maxLen) {
      return new ResourceLocation(this.readString(maxLen));
   }

   public ResourceLocation readIdentifier() {
      return this.readIdentifier(32767);
   }

   public void writeIdentifier(ResourceLocation identifier, int maxLen) {
      this.writeString(identifier.toString(), maxLen);
   }

   public void writeIdentifier(ResourceLocation identifier) {
      this.writeIdentifier(identifier, 32767);
   }

   public int readUnsignedShort() {
      return ByteBufHelper.readUnsignedShort(this.buffer);
   }

   public short readShort() {
      return ByteBufHelper.readShort(this.buffer);
   }

   public void writeShort(int value) {
      ByteBufHelper.writeShort(this.buffer, value);
   }

   public void writeShortLE(int value) {
      ByteBufHelper.writeShortLE(this.buffer, value);
   }

   public int readVarShort() {
      int low = this.readUnsignedShort();
      int high = 0;
      if ((low & '耀') != 0) {
         low &= 32767;
         high = this.readUnsignedByte();
      }

      return (high & 255) << 15 | low;
   }

   public void writeVarShort(int value) {
      int low = value & 32767;
      int high = (value & 8355840) >> 15;
      if (high != 0) {
         low |= 32768;
      }

      this.writeShort(low);
      if (high != 0) {
         this.writeByte(high);
      }

   }

   public long readLong() {
      return ByteBufHelper.readLong(this.buffer);
   }

   public void writeLong(long value) {
      ByteBufHelper.writeLong(this.buffer, value);
   }

   public long readVarLong() {
      long value = 0L;

      int size;
      byte b;
      for(size = 0; ((b = this.readByte()) & 128) == 128; value |= (long)(b & 127) << size++ * 7) {
      }

      return value | (long)(b & 127) << size * 7;
   }

   public void writeVarLong(long l) {
      while((l & -128L) != 0L) {
         this.writeByte((int)(l & 127L) | 128);
         l >>>= 7;
      }

      this.writeByte((int)l);
   }

   public float readFloat() {
      return ByteBufHelper.readFloat(this.buffer);
   }

   public void writeFloat(float value) {
      ByteBufHelper.writeFloat(this.buffer, value);
   }

   public double readDouble() {
      return ByteBufHelper.readDouble(this.buffer);
   }

   public void writeDouble(double value) {
      ByteBufHelper.writeDouble(this.buffer, value);
   }

   public byte[] readRemainingBytes() {
      return this.readBytes(ByteBufHelper.readableBytes(this.buffer));
   }

   public byte[] readBytes(int size) {
      byte[] bytes = new byte[size];
      ByteBufHelper.readBytes(this.buffer, bytes);
      return bytes;
   }

   public void writeBytes(byte[] array) {
      ByteBufHelper.writeBytes(this.buffer, array);
   }

   public byte[] readByteArray(int maxLength) {
      int len = this.readVarInt();
      if (len > maxLength) {
         throw new RuntimeException("The received byte array length is longer than maximum allowed (" + len + " > " + maxLength + ")");
      } else {
         return this.readBytes(len);
      }
   }

   public byte[] readByteArray() {
      return this.readByteArray(ByteBufHelper.readableBytes(this.buffer));
   }

   public void writeByteArray(byte[] array) {
      this.writeVarInt(array.length);
      this.writeBytes(array);
   }

   public int[] readVarIntArray() {
      int readableBytes = ByteBufHelper.readableBytes(this.buffer);
      int size = this.readVarInt();
      if (size > readableBytes) {
         throw new IllegalStateException("VarIntArray with size " + size + " is bigger than allowed " + readableBytes);
      } else {
         int[] array = new int[size];

         for(int i = 0; i < size; ++i) {
            array[i] = this.readVarInt();
         }

         return array;
      }
   }

   public void writeVarIntArray(int[] array) {
      this.writeVarInt(array.length);
      int[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         this.writeVarInt(i);
      }

   }

   public long[] readLongArray(int size) {
      long[] array = new long[size];

      for(int i = 0; i < array.length; ++i) {
         array[i] = this.readLong();
      }

      return array;
   }

   public byte[] readByteArrayOfSize(int size) {
      byte[] array = new byte[size];
      ByteBufHelper.readBytes(this.buffer, array);
      return array;
   }

   public void writeByteArrayOfSize(byte[] array) {
      ByteBufHelper.writeBytes(this.buffer, array);
   }

   public int[] readVarIntArrayOfSize(int size) {
      int[] array = new int[size];

      for(int i = 0; i < array.length; ++i) {
         array[i] = this.readVarInt();
      }

      return array;
   }

   public void writeVarIntArrayOfSize(int[] array) {
      int[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int i = var2[var4];
         this.writeVarInt(i);
      }

   }

   public long[] readLongArray() {
      int readableBytes = ByteBufHelper.readableBytes(this.buffer) / 8;
      int size = this.readVarInt();
      if (size > readableBytes) {
         throw new IllegalStateException("LongArray with size " + size + " is bigger than allowed " + readableBytes);
      } else {
         long[] array = new long[size];

         for(int i = 0; i < array.length; ++i) {
            array[i] = this.readLong();
         }

         return array;
      }
   }

   public void writeLongArray(long[] array) {
      this.writeVarInt(array.length);
      long[] var2 = array;
      int var3 = array.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         long l = var2[var4];
         this.writeLong(l);
      }

   }

   public UUID readUUID() {
      long mostSigBits = this.readLong();
      long leastSigBits = this.readLong();
      return new UUID(mostSigBits, leastSigBits);
   }

   public void writeUUID(UUID uuid) {
      this.writeLong(uuid.getMostSignificantBits());
      this.writeLong(uuid.getLeastSignificantBits());
   }

   public Vector3i readBlockPosition() {
      long val = this.readLong();
      return new Vector3i(val, this.serverVersion);
   }

   public void writeBlockPosition(Vector3i pos) {
      long val = pos.getSerializedPosition(this.serverVersion);
      this.writeLong(val);
   }

   public GameMode readGameMode() {
      return GameMode.getById(this.readByte());
   }

   public void writeGameMode(@Nullable GameMode mode) {
      int id = mode == null ? -1 : mode.getId();
      this.writeByte(id);
   }

   public List<EntityData<?>> readEntityMetadata() {
      List<EntityData<?>> list = new ArrayList();
      int typeID;
      EntityDataType type;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         boolean v1_10 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);

         short index;
         while((index = this.readUnsignedByte()) != 255) {
            typeID = v1_10 ? this.readVarInt() : this.readUnsignedByte();
            type = EntityDataTypes.getById(this.serverVersion.toClientVersion(), typeID);
            if (type == null) {
               throw new IllegalStateException("Unknown entity metadata type id: " + typeID + " version " + this.serverVersion.toClientVersion());
            }

            list.add(new EntityData(index, type, type.read(this)));
         }
      } else {
         for(byte data = this.readByte(); data != 127; data = this.readByte()) {
            int typeID = (data & 224) >> 5;
            typeID = data & 31;
            type = EntityDataTypes.getById(this.serverVersion.toClientVersion(), typeID);
            EntityData<?> entityData = new EntityData(typeID, type, type.read(this));
            list.add(entityData);
         }
      }

      return list;
   }

   public void writeEntityMetadata(List<EntityData<?>> list) {
      if (list == null) {
         list = new ArrayList();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
         boolean v1_10 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);

         EntityData entityData;
         for(Iterator var3 = ((List)list).iterator(); var3.hasNext(); entityData.getType().write(this, entityData.getValue())) {
            entityData = (EntityData)var3.next();
            this.writeByte(entityData.getIndex());
            if (v1_10) {
               this.writeVarInt(entityData.getType().getId(this.serverVersion.toClientVersion()));
            } else {
               this.writeByte(entityData.getType().getId(this.serverVersion.toClientVersion()));
            }
         }

         this.writeByte(255);
      } else {
         Iterator var7 = ((List)list).iterator();

         while(var7.hasNext()) {
            EntityData<?> entityData = (EntityData)var7.next();
            int typeID = entityData.getType().getId(this.serverVersion.toClientVersion());
            int index = entityData.getIndex();
            int data = (typeID << 5 | index & 31) & 255;
            this.writeByte(data);
            entityData.getType().write(this, entityData.getValue());
         }

         this.writeByte(127);
      }

   }

   public void writeEntityMetadata(EntityMetadataProvider metadata) {
      this.writeEntityMetadata(metadata.entityData(this.serverVersion.toClientVersion()));
   }

   /** @deprecated */
   @Deprecated
   public Dimension readDimension() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         return new Dimension(this.readVarInt());
      } else if (!this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) && !this.serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
         return new Dimension(this.readNBT());
      } else {
         Dimension dimension = new Dimension(new NBTCompound());
         dimension.setDimensionName(this.readIdentifier().toString());
         return dimension;
      }
   }

   /** @deprecated */
   @Deprecated
   public void writeDimension(Dimension dimension) {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         this.writeVarInt(dimension.getId());
      } else {
         if (!this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) && !this.serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
            this.writeNBT(dimension.getAttributes());
         } else {
            this.writeString(dimension.getDimensionName(), 32767);
         }

      }
   }

   public SaltSignature readSaltSignature() {
      long salt = this.readLong();
      byte[] signature;
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         if (this.readBoolean()) {
            signature = this.readBytes(256);
         } else {
            signature = new byte[0];
         }
      } else {
         signature = this.readByteArray(256);
      }

      return new SaltSignature(salt, signature);
   }

   public void writeSaltSignature(SaltSignature signature) {
      this.writeLong(signature.getSalt());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
         boolean present = signature.getSignature().length != 0;
         this.writeBoolean(present);
         if (present) {
            this.writeBytes(signature.getSignature());
         }
      } else {
         this.writeByteArray(signature.getSignature());
      }

   }

   public PublicKey readPublicKey() {
      return MinecraftEncryptionUtil.publicKey(this.readByteArray(512));
   }

   public void writePublicKey(PublicKey publicKey) {
      this.writeByteArray(publicKey.getEncoded());
   }

   public PublicProfileKey readPublicProfileKey() {
      Instant expiresAt = this.readTimestamp();
      PublicKey key = this.readPublicKey();
      byte[] keySignature = this.readByteArray(4096);
      return new PublicProfileKey(expiresAt, key, keySignature);
   }

   public void writePublicProfileKey(PublicProfileKey key) {
      this.writeTimestamp(key.getExpiresAt());
      this.writePublicKey(key.getKey());
      this.writeByteArray(key.getKeySignature());
   }

   public RemoteChatSession readRemoteChatSession() {
      return new RemoteChatSession(this.readUUID(), this.readPublicProfileKey());
   }

   public void writeRemoteChatSession(RemoteChatSession chatSession) {
      this.writeUUID(chatSession.getSessionId());
      this.writePublicProfileKey(chatSession.getPublicProfileKey());
   }

   public Instant readTimestamp() {
      return Instant.ofEpochMilli(this.readLong());
   }

   public void writeTimestamp(Instant timestamp) {
      this.writeLong(timestamp.toEpochMilli());
   }

   public SignatureData readSignatureData() {
      return new SignatureData(this.readTimestamp(), this.readPublicKey(), this.readByteArray(4096));
   }

   public void writeSignatureData(SignatureData signatureData) {
      this.writeTimestamp(signatureData.getTimestamp());
      this.writePublicKey(signatureData.getPublicKey());
      this.writeByteArray(signatureData.getSignature());
   }

   public static <K> IntFunction<K> limitValue(IntFunction<K> function, int limit) {
      return (i) -> {
         if (i > limit) {
            throw new RuntimeException("Value " + i + " is larger than limit " + limit);
         } else {
            return function.apply(i);
         }
      };
   }

   public WorldBlockPosition readWorldBlockPosition() {
      return new WorldBlockPosition(this.readIdentifier(), this.readBlockPosition());
   }

   public void writeWorldBlockPosition(WorldBlockPosition pos) {
      this.writeIdentifier(pos.getWorld());
      this.writeBlockPosition(pos.getBlockPosition());
   }

   public LastSeenMessages.Entry readLastSeenMessagesEntry() {
      return new LastSeenMessages.Entry(this.readUUID(), this.readByteArray());
   }

   public void writeLastMessagesEntry(LastSeenMessages.Entry entry) {
      this.writeUUID(entry.getUUID());
      this.writeByteArray(entry.getLastVerifier());
   }

   public LastSeenMessages.Update readLastSeenMessagesUpdate() {
      int signedMessages = this.readVarInt();
      BitSet seen = BitSet.valueOf(this.readBytes(3));
      byte checksum = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5) ? this.readByte() : 0;
      return new LastSeenMessages.Update(signedMessages, seen, checksum);
   }

   public void writeLastSeenMessagesUpdate(LastSeenMessages.Update update) {
      this.writeVarInt(update.getOffset());
      this.writeBytes(Arrays.copyOf(update.getAcknowledged().toByteArray(), 3));
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         this.writeByte(update.getChecksum());
      }

   }

   public LastSeenMessages.LegacyUpdate readLegacyLastSeenMessagesUpdate() {
      LastSeenMessages lastSeenMessages = this.readLastSeenMessages();
      LastSeenMessages.Entry lastReceived = (LastSeenMessages.Entry)this.readOptional(PacketWrapper::readLastSeenMessagesEntry);
      return new LastSeenMessages.LegacyUpdate(lastSeenMessages, lastReceived);
   }

   public void writeLegacyLastSeenMessagesUpdate(LastSeenMessages.LegacyUpdate legacyUpdate) {
      this.writeLastSeenMessages(legacyUpdate.getLastSeenMessages());
      this.writeOptional(legacyUpdate.getLastReceived(), PacketWrapper::writeLastMessagesEntry);
   }

   public MessageSignature readMessageSignature() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3) ? new MessageSignature(this.readBytes(256)) : new MessageSignature(this.readByteArray());
   }

   public void writeMessageSignature(MessageSignature messageSignature) {
      this.writeBytes(messageSignature.getBytes());
   }

   public MessageSignature.Packed readMessageSignaturePacked() {
      int id = this.readVarInt() - 1;
      return id == -1 ? new MessageSignature.Packed(new MessageSignature(this.readBytes(256))) : new MessageSignature.Packed(id);
   }

   public void writeMessageSignaturePacked(MessageSignature.Packed messageSignaturePacked) {
      this.writeVarInt(messageSignaturePacked.getId() + 1);
      if (messageSignaturePacked.getFullSignature().isPresent()) {
         this.writeBytes(((MessageSignature)messageSignaturePacked.getFullSignature().get()).getBytes());
      }

   }

   public LastSeenMessages.Packed readLastSeenMessagesPacked() {
      List<MessageSignature.Packed> packedMessageSignatures = (List)this.readCollection(limitValue(ArrayList::new, 20), PacketWrapper::readMessageSignaturePacked);
      return new LastSeenMessages.Packed(packedMessageSignatures);
   }

   public void writeLastSeenMessagesPacked(LastSeenMessages.Packed lastSeenMessagesPacked) {
      this.writeCollection(lastSeenMessagesPacked.getPackedMessageSignatures(), PacketWrapper::writeMessageSignaturePacked);
   }

   public LastSeenMessages readLastSeenMessages() {
      List<LastSeenMessages.Entry> entries = (List)this.readCollection(limitValue(ArrayList::new, 5), PacketWrapper::readLastSeenMessagesEntry);
      return new LastSeenMessages(entries);
   }

   public void writeLastSeenMessages(LastSeenMessages lastSeenMessages) {
      this.writeCollection(lastSeenMessages.getEntries(), PacketWrapper::writeLastMessagesEntry);
   }

   public List<SignedCommandArgument> readSignedCommandArguments() {
      return (List)this.readCollection(limitValue(ArrayList::new, 8), (_packet) -> {
         return new SignedCommandArgument(this.readString(16), this.readMessageSignature());
      });
   }

   public void writeSignedCommandArguments(List<SignedCommandArgument> signedArguments) {
      this.writeCollection(signedArguments, (_packet, argument) -> {
         this.writeString(argument.getArgument(), 16);
         this.writeMessageSignature(argument.getSignature());
      });
   }

   public BitSet readBitSet() {
      return BitSet.valueOf(this.readLongArray());
   }

   public void writeBitSet(BitSet bitSet) {
      this.writeLongArray(bitSet.toLongArray());
   }

   public FilterMask readFilterMask() {
      FilterMaskType type = FilterMaskType.getById(this.readVarInt());
      switch(type) {
      case PARTIALLY_FILTERED:
         return new FilterMask(this.readBitSet());
      case PASS_THROUGH:
         return FilterMask.PASS_THROUGH;
      case FULLY_FILTERED:
         return FilterMask.FULLY_FILTERED;
      default:
         return null;
      }
   }

   public void writeFilterMask(FilterMask filterMask) {
      this.writeVarInt(filterMask.getType().getId());
      if (filterMask.getType() == FilterMaskType.PARTIALLY_FILTERED) {
         this.writeBitSet(filterMask.getMask());
      }

   }

   public MerchantOffer readMerchantOffer() {
      ItemStack buyItemPrimary = MerchantItemCost.readItem(this);
      ItemStack sellItem = this.readItemStack();
      ItemStack buyItemSecondary = !this.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5) && !this.getServerVersion().isOlderThan(ServerVersion.V_1_19) ? this.readItemStack() : (ItemStack)this.readOptional(MerchantItemCost::readItem);
      boolean tradeDisabled = this.readBoolean();
      int uses = this.readInt();
      int maxUses = this.readInt();
      int xp = this.readInt();
      int specialPrice = this.readInt();
      float priceMultiplier = this.readFloat();
      int demand = this.readInt();
      MerchantOffer data = MerchantOffer.of(buyItemPrimary, buyItemSecondary, sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
      if (tradeDisabled) {
         data.setUses(data.getMaxUses());
      }

      return data;
   }

   public void writeMerchantOffer(MerchantOffer data) {
      MerchantItemCost.writeItem(this, data.getFirstInputItem());
      this.writeItemStack(data.getOutputItem());
      ItemStack buyItemSecondary = data.getSecondInputItem();
      if (buyItemSecondary != null && buyItemSecondary.isEmpty()) {
         buyItemSecondary = null;
      }

      if (!this.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5) && !this.getServerVersion().isOlderThan(ServerVersion.V_1_19)) {
         this.writeItemStack(buyItemSecondary);
      } else {
         this.writeOptional(buyItemSecondary, MerchantItemCost::writeItem);
      }

      this.writeBoolean(data.getUses() >= data.getMaxUses());
      this.writeInt(data.getUses());
      this.writeInt(data.getMaxUses());
      this.writeInt(data.getXp());
      this.writeInt(data.getSpecialPrice());
      this.writeFloat(data.getPriceMultiplier());
      this.writeInt(data.getDemand());
   }

   public ChatType.Bound readChatTypeBoundNetwork() {
      ChatType type = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21) ? (ChatType)this.readMappedEntityOrDirect((IRegistry)ChatTypes.getRegistry(), ChatType::readDirect) : (ChatType)this.readMappedEntity((IRegistry)ChatTypes.getRegistry());
      Component name = this.readComponent();
      Component targetName = (Component)this.readOptional(PacketWrapper::readComponent);
      return new ChatType.Bound(type, name, targetName);
   }

   public void writeChatTypeBoundNetwork(ChatType.Bound chatFormatting) {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
         this.writeMappedEntityOrDirect(chatFormatting.getType(), ChatType::writeDirect);
      } else {
         this.writeMappedEntity(chatFormatting.getType());
      }

      this.writeComponent(chatFormatting.getName());
      this.writeOptional(chatFormatting.getTargetName(), PacketWrapper::writeComponent);
   }

   public Node readNode() {
      byte flags = this.readByte();
      int nodeType = flags & 3;
      List<Integer> children = this.readList(PacketWrapper::readVarInt);
      int redirectNodeIndex = (flags & 8) != 0 ? this.readVarInt() : 0;
      String name;
      if (nodeType == 2) {
         name = this.readString();
         Parsers.Parser parser = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19) ? (Parsers.Parser)this.readMappedEntity(Parsers::getById) : Parsers.getByName(this.readIdentifier().toString());
         List<Object> properties = (List)parser.readProperties(this).orElse((Object)null);
         ResourceLocation suggestionType = (flags & 16) != 0 ? this.readIdentifier() : null;
         return new Node(flags, children, redirectNodeIndex, name, parser, properties, suggestionType);
      } else if (nodeType == 1) {
         name = this.readString();
         return new Node(flags, children, redirectNodeIndex, name, (Parsers.Parser)null, (List)null, (ResourceLocation)null);
      } else {
         return new Node(flags, children, redirectNodeIndex, (String)null, (Parsers.Parser)null, (List)null, (ResourceLocation)null);
      }
   }

   public void writeNode(Node node) {
      this.writeByte(node.getFlags());
      this.writeList(node.getChildren(), PacketWrapper::writeVarInt);
      if ((node.getFlags() & 8) != 0) {
         this.writeVarInt(node.getRedirectNodeIndex());
      }

      node.getName().ifPresent(this::writeString);
      if (node.getParser().isPresent()) {
         Parsers.Parser parser = (Parsers.Parser)node.getParser().get();
         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            this.writeMappedEntity(parser);
         } else {
            this.writeIdentifier(parser.getName());
         }

         if (node.getProperties().isPresent()) {
            parser.writeProperties(this, (List)node.getProperties().get());
         }
      }

      node.getSuggestionsType().ifPresent(this::writeIdentifier);
   }

   public KnownPack readKnownPack() {
      String namespace = this.readString();
      String id = this.readString();
      String version = this.readString();
      return new KnownPack(namespace, id, version);
   }

   public void writeKnownPack(KnownPack knownPack) {
      this.writeString(knownPack.getNamespace());
      this.writeString(knownPack.getId());
      this.writeString(knownPack.getVersion());
   }

   public <T extends Enum<T>> EnumSet<T> readEnumSet(Class<T> enumClazz) {
      T[] values = (Enum[])enumClazz.getEnumConstants();
      byte[] bytes = new byte[-Math.floorDiv(-values.length, 8)];
      ByteBufHelper.readBytes(this.getBuffer(), bytes);
      BitSet bitSet = BitSet.valueOf(bytes);
      EnumSet<T> set = EnumSet.noneOf(enumClazz);

      for(int i = 0; i < values.length; ++i) {
         if (bitSet.get(i)) {
            set.add(values[i]);
         }
      }

      return set;
   }

   public <T extends Enum<T>> void writeEnumSet(EnumSet<T> set, Class<T> enumClazz) {
      T[] values = (Enum[])enumClazz.getEnumConstants();
      BitSet bitSet = new BitSet(values.length);

      for(int i = 0; i < values.length; ++i) {
         if (set.contains(values[i])) {
            bitSet.set(i);
         }
      }

      this.writeBytes(Arrays.copyOf(bitSet.toByteArray(), -Math.floorDiv(-values.length, 8)));
   }

   @ApiStatus.Experimental
   public <U, V, R> U readMultiVersional(VersionComparison version, ServerVersion target, PacketWrapper.Reader<V> first, PacketWrapper.Reader<R> second) {
      return this.serverVersion.is(version, target) ? first.apply(this) : second.apply(this);
   }

   @ApiStatus.Experimental
   public <V> void writeMultiVersional(VersionComparison version, ServerVersion target, V value, PacketWrapper.Writer<V> first, PacketWrapper.Writer<V> second) {
      if (this.serverVersion.is(version, target)) {
         first.accept(this, value);
      } else {
         second.accept(this, value);
      }

   }

   @Nullable
   public <R> R readOptional(PacketWrapper.Reader<R> reader) {
      return this.readBoolean() ? reader.apply(this) : null;
   }

   public <V> void writeOptional(@Nullable V value, PacketWrapper.Writer<V> writer) {
      if (value != null) {
         this.writeBoolean(true);
         writer.accept(this, value);
      } else {
         this.writeBoolean(false);
      }

   }

   public <R> Optional<R> readJavaOptional(PacketWrapper.Reader<R> reader) {
      return this.readBoolean() ? Optional.of(reader.apply(this)) : Optional.empty();
   }

   public <V> void writeJavaOptional(Optional<V> value, PacketWrapper.Writer<V> writer) {
      if (value.isPresent()) {
         this.writeBoolean(true);
         writer.accept(this, value.get());
      } else {
         this.writeBoolean(false);
      }

   }

   public <K, C extends Collection<K>> C readCollection(IntFunction<C> function, PacketWrapper.Reader<K> reader) {
      int size = this.readVarInt();
      return this._readCollection(function, reader, size);
   }

   public <K, C extends Collection<K>> C readCollection(IntFunction<C> function, PacketWrapper.Reader<K> reader, int maxSize) {
      int size = this.readVarInt();
      if (size > maxSize) {
         throw new RuntimeException(size + " elements exceeded max size of: " + maxSize);
      } else {
         return this._readCollection(function, reader, size);
      }
   }

   private <K, C extends Collection<K>> C _readCollection(IntFunction<C> function, PacketWrapper.Reader<K> reader, int size) {
      Collection<K> collection = (Collection)function.apply(size);

      for(int i = 0; i < size; ++i) {
         collection.add(reader.apply(this));
      }

      return collection;
   }

   public <K> void writeCollection(Collection<K> collection, PacketWrapper.Writer<K> writer) {
      this.writeVarInt(collection.size());
      Iterator var3 = collection.iterator();

      while(var3.hasNext()) {
         K key = var3.next();
         writer.accept(this, key);
      }

   }

   public <K> List<K> readList(PacketWrapper.Reader<K> reader) {
      return (List)this.readCollection(ArrayList::new, reader);
   }

   public <K> List<K> readList(PacketWrapper.Reader<K> reader, int maxSize) {
      return (List)this.readCollection(ArrayList::new, reader, maxSize);
   }

   public <K> void writeList(List<K> list, PacketWrapper.Writer<K> writer) {
      this.writeVarInt(list.size());
      Iterator var3 = list.iterator();

      while(var3.hasNext()) {
         K key = var3.next();
         writer.accept(this, key);
      }

   }

   public <K> Set<K> readSet(PacketWrapper.Reader<K> reader) {
      return (Set)this.readCollection(HashSet::new, reader);
   }

   public <K> Set<K> readSet(PacketWrapper.Reader<K> reader, int maxSize) {
      return (Set)this.readCollection(HashSet::new, reader, maxSize);
   }

   public <K> void writeSet(Set<K> set, PacketWrapper.Writer<K> writer) {
      this.writeVarInt(set.size());
      Iterator var3 = set.iterator();

      while(var3.hasNext()) {
         K key = var3.next();
         writer.accept(this, key);
      }

   }

   public <K> K[] readArray(PacketWrapper.Reader<K> reader, Class<K> clazz) {
      int length = this.readVarInt();
      K[] array = (Object[])Array.newInstance(clazz, length);

      for(int i = 0; i < length; ++i) {
         array[i] = reader.apply(this);
      }

      return array;
   }

   public <K> void writeArray(K[] array, PacketWrapper.Writer<K> writer) {
      this.writeVarInt(array.length);
      Object[] var3 = array;
      int var4 = array.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         K element = var3[var5];
         writer.accept(this, element);
      }

   }

   public <Z extends Enum<?>> Z readEnum(Class<Z> clazz) {
      return this.readEnum((Enum[])clazz.getEnumConstants());
   }

   public <Z extends Enum<?>> Z readEnum(Z[] values) {
      return values[this.readVarInt()];
   }

   public <Z extends Enum<?>> Z readEnum(Class<Z> clazz, Z fallback) {
      return this.readEnum((Enum[])clazz.getEnumConstants(), fallback);
   }

   public <Z extends Enum<?>> Z readEnum(Z[] values, Z fallback) {
      int id = this.readVarInt();
      return id >= 0 && id < values.length ? values[id] : fallback;
   }

   public void writeEnum(Enum<?> value) {
      this.writeVarInt(value.ordinal());
   }

   public <Z extends MappedEntity> Z readMappedEntity(BiFunction<ClientVersion, Integer, Z> getter) {
      int id = this.readVarInt();
      Z entity = (MappedEntity)getter.apply(this.serverVersion.toClientVersion(), id);
      if (entity == null) {
         throw new IllegalStateException("Can't find mapped entity with id " + id + " using " + getter);
      } else {
         return entity;
      }
   }

   public <Z extends MappedEntity> IRegistry<Z> replaceRegistry(IRegistry<Z> registry) {
      return this.getRegistryHolder().getRegistryOr(registry, this.serverVersion.toClientVersion());
   }

   public IRegistryHolder getRegistryHolder() {
      return (IRegistryHolder)(this.user != null ? this.user : GlobalRegistryHolder.INSTANCE);
   }

   public <Z extends MappedEntity> Z readMappedEntityOrDirect(BiFunction<ClientVersion, Integer, Z> getter, PacketWrapper.Reader<Z> directReader) {
      int id = this.readVarInt();
      if (id == 0) {
         return (MappedEntity)directReader.apply(this);
      } else {
         Z entity = (MappedEntity)getter.apply(this.serverVersion.toClientVersion(), id - 1);
         if (entity == null) {
            throw new IllegalStateException("Can't find mapped entity with id " + id + " using " + getter);
         } else {
            return entity;
         }
      }
   }

   public <Z extends MappedEntity> Z readMappedEntity(IRegistry<Z> registry) {
      IRegistry<Z> replacedRegistry = this.getRegistryHolder().getRegistryOr(registry, this.serverVersion.toClientVersion());
      return this.readMappedEntity((BiFunction)replacedRegistry);
   }

   public <Z extends MappedEntity> Z readMappedEntityOrDirect(IRegistry<Z> registry, PacketWrapper.Reader<Z> directReader) {
      IRegistry<Z> replacedRegistry = this.getRegistryHolder().getRegistryOr(registry, this.serverVersion.toClientVersion());
      return this.readMappedEntityOrDirect((BiFunction)replacedRegistry, directReader);
   }

   public void writeMappedEntity(MappedEntity entity) {
      if (!entity.isRegistered()) {
         throw new IllegalArgumentException("Can't write id of unregistered entity " + entity.getName() + " (" + entity + ")");
      } else {
         this.writeVarInt(entity.getId(this.serverVersion.toClientVersion()));
      }
   }

   public <Z extends MappedEntity> void writeMappedEntityOrDirect(Z entity, PacketWrapper.Writer<Z> writer) {
      if (!entity.isRegistered()) {
         this.writeVarInt(0);
         writer.accept(this, entity);
      } else {
         int id = entity.getId(this.serverVersion.toClientVersion());
         this.writeVarInt(id + 1);
      }
   }

   public int readContainerId() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readVarInt() : this.readUnsignedByte();
   }

   public void writeContainerId(int containerId) {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeVarInt(containerId);
      } else {
         this.writeByte(containerId);
      }

   }

   public <L, R> Either<L, R> readEither(PacketWrapper.Reader<L> leftReader, PacketWrapper.Reader<R> rightReader) {
      return this.readBoolean() ? Either.createLeft(leftReader.apply(this)) : Either.createRight(rightReader.apply(this));
   }

   public <L, R> void writeEither(Either<L, R> either, PacketWrapper.Writer<L> leftWriter, PacketWrapper.Writer<R> rightWriter) {
      if (either.isLeft()) {
         this.writeBoolean(true);
         leftWriter.accept(this, either.getLeft());
      } else {
         this.writeBoolean(false);
         rightWriter.accept(this, either.getRight());
      }

   }

   public void writeRotation(float rotation) {
      this.writeByte((byte)MathUtil.floor(rotation * 256.0F / 360.0F));
   }

   public float readRotation() {
      return (float)(this.readByte() * 360) / 256.0F;
   }

   @Nullable
   public Integer readNullableVarInt() {
      int i = this.readVarInt();
      return i == 0 ? null : i - 1;
   }

   public void writeNullableVarInt(@Nullable Integer i) {
      this.writeVarInt(i == null ? 0 : i + 1);
   }

   public <Z> Z readLengthPrefixed(int maxLength, PacketWrapper.Reader<Z> reader) {
      int length = this.readVarInt();
      if (length > maxLength) {
         throw new RuntimeException("Buffer size " + length + " is larger than allowed limit of " + maxLength);
      } else {
         Object prevBuffer = this.buffer;

         Object var5;
         try {
            this.buffer = ByteBufHelper.readSlice(prevBuffer, length);
            var5 = reader.apply(this);
         } finally {
            this.buffer = prevBuffer;
         }

         return var5;
      }
   }

   public <Z> void writeLengthPrefixed(Z value, PacketWrapper.Writer<Z> writer) {
      Object payloadBuffer = ByteBufHelper.allocateNewBuffer(this.buffer);
      Object prevBuffer = this.buffer;

      try {
         this.buffer = payloadBuffer;
         writer.accept(this, value);
      } finally {
         this.buffer = prevBuffer;
      }

      this.writeVarInt(ByteBufHelper.readableBytes(payloadBuffer));
      ByteBufHelper.writeBytes(prevBuffer, payloadBuffer);
   }

   @FunctionalInterface
   public interface Reader<T> extends Function<PacketWrapper<?>, T> {
   }

   @FunctionalInterface
   public interface Writer<T> extends BiConsumer<PacketWrapper<?>, T> {
   }
}
