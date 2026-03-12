package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.GameMode;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Difficulty;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Dimension;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.WorldType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypeRef;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;

public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {
   public static final byte KEEP_NOTHING = 0;
   public static final byte KEEP_ATTRIBUTES = 1;
   public static final byte KEEP_ENTITY_DATA = 2;
   public static final byte KEEP_ALL_DATA = 3;
   static final int FALLBACK_SEA_LEVEL = 62;
   private DimensionTypeRef dimensionTypeRef;
   private Optional<String> worldName;
   private Difficulty difficulty;
   private long hashedSeed;
   private GameMode gameMode;
   @Nullable
   private GameMode previousGameMode;
   private boolean worldDebug;
   private boolean worldFlat;
   private byte keptData;
   private WorldBlockPosition lastDeathPosition;
   private Integer portalCooldown;
   private int seaLevel;
   private String levelType;

   public WrapperPlayServerRespawn(PacketSendEvent event) {
      super(event);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerRespawn(Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData, @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this(dimension.asDimensionTypeRef(), worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, keepingAllPlayerData, deathDimensionName, lastDeathPosition, portalCooldown);
   }

   /** @deprecated */
   @Deprecated
   public WrapperPlayServerRespawn(Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this(dimension.asDimensionTypeRef(), worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown);
   }

   public WrapperPlayServerRespawn(DimensionType dimensionType, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData, @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this((DimensionTypeRef)null, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, keepingAllPlayerData, deathDimensionName, lastDeathPosition, portalCooldown);
      this.dimensionTypeRef = dimensionType.asRef((PacketWrapper)this);
   }

   public WrapperPlayServerRespawn(DimensionType dimensionType, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this((DimensionType)dimensionType, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown, 62);
   }

   public WrapperPlayServerRespawn(DimensionType dimensionType, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown, int seaLevel) {
      this((DimensionTypeRef)null, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown, seaLevel);
      this.dimensionTypeRef = dimensionType.asRef((PacketWrapper)this);
   }

   public WrapperPlayServerRespawn(DimensionTypeRef dimensionTypeRef, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData, @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this((DimensionTypeRef)dimensionTypeRef, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, (byte)(keepingAllPlayerData ? 3 : 0), lastDeathPosition, portalCooldown);
   }

   public WrapperPlayServerRespawn(DimensionTypeRef dimensionTypeRef, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
      this((DimensionTypeRef)dimensionTypeRef, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown, 62);
   }

   public WrapperPlayServerRespawn(DimensionTypeRef dimensionTypeRef, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData, @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown, int seaLevel) {
      super((PacketTypeCommon)PacketType.Play.Server.RESPAWN);
      this.dimensionTypeRef = dimensionTypeRef;
      this.setWorldName(worldName);
      this.difficulty = difficulty;
      this.hashedSeed = hashedSeed;
      this.gameMode = gameMode;
      this.previousGameMode = previousGameMode;
      this.worldDebug = worldDebug;
      this.worldFlat = worldFlat;
      this.keptData = keptData;
      this.lastDeathPosition = lastDeathPosition;
      this.portalCooldown = portalCooldown;
      this.seaLevel = seaLevel;
   }

   public void read() {
      boolean v1_14 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
      boolean v1_15_0 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
      boolean v1_16_0 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      boolean v1_19_3 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3);
      boolean v1_20_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);
      this.dimensionTypeRef = DimensionTypeRef.read(this);
      if (v1_16_0) {
         this.worldName = Optional.of(this.readString());
         this.hashedSeed = this.readLong();
         if (v1_20_2) {
            this.gameMode = this.readGameMode();
         } else {
            this.gameMode = GameMode.getById(this.readUnsignedByte());
         }

         this.previousGameMode = this.readGameMode();
         this.worldDebug = this.readBoolean();
         this.worldFlat = this.readBoolean();
         if (v1_19_3) {
            if (!v1_20_2) {
               this.keptData = this.readByte();
            }
         } else {
            this.keptData = (byte)(this.readBoolean() ? 3 : 2);
         }

         if (v1_19) {
            this.lastDeathPosition = (WorldBlockPosition)this.readOptional(PacketWrapper::readWorldBlockPosition);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
            this.portalCooldown = this.readVarInt();
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.seaLevel = this.readVarInt();
         }

         if (v1_20_2) {
            this.keptData = this.readByte();
         }
      } else {
         this.worldName = Optional.empty();
         this.hashedSeed = 0L;
         if (v1_15_0) {
            this.hashedSeed = this.readLong();
         } else if (!v1_14) {
            this.difficulty = Difficulty.getById(this.readByte());
         }

         this.gameMode = GameMode.getById(this.readByte());
         this.levelType = this.readString(16);
         this.worldFlat = ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.DimensionType.isFlat(this.levelType);
         this.worldDebug = ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.DimensionType.isDebug(this.levelType);
      }

   }

   public void write() {
      boolean v1_14 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
      boolean v1_15_0 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
      boolean v1_16_0 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
      boolean v1_19 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
      boolean v1_19_3 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3);
      boolean v1_20_2 = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);
      DimensionTypeRef.write(this, this.dimensionTypeRef);
      int pCooldown;
      if (v1_16_0) {
         this.writeString((String)this.worldName.orElse(""));
         this.writeLong(this.hashedSeed);
         this.writeGameMode(this.gameMode);
         this.writeGameMode(this.previousGameMode);
         this.writeBoolean(this.worldDebug);
         this.writeBoolean(this.worldFlat);
         if (v1_19_3) {
            if (!v1_20_2) {
               this.writeByte(this.keptData);
            }
         } else {
            this.writeBoolean((this.keptData & 1) != 0);
         }

         if (v1_19) {
            this.writeOptional(this.lastDeathPosition, PacketWrapper::writeWorldBlockPosition);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
            pCooldown = this.portalCooldown != null ? this.portalCooldown : 0;
            this.writeVarInt(pCooldown);
         }

         if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeVarInt(this.seaLevel);
         }

         if (v1_20_2) {
            this.writeByte(this.keptData);
         }
      } else {
         if (v1_15_0) {
            this.writeLong(this.hashedSeed);
         } else if (!v1_14) {
            pCooldown = this.difficulty == null ? Difficulty.NORMAL.getId() : this.difficulty.getId();
            this.writeByte(pCooldown);
         }

         this.writeByte(this.gameMode.ordinal());
         if (this.worldFlat) {
            this.writeString(WorldType.FLAT.getName());
         } else if (this.worldDebug) {
            this.writeString(WorldType.DEBUG_ALL_BLOCK_STATES.getName());
         } else {
            this.writeString(this.levelType == null ? WorldType.DEFAULT.getName() : this.levelType, 16);
         }
      }

   }

   public void copy(WrapperPlayServerRespawn wrapper) {
      this.dimensionTypeRef = wrapper.dimensionTypeRef;
      this.worldName = wrapper.worldName;
      this.difficulty = wrapper.difficulty;
      this.hashedSeed = wrapper.hashedSeed;
      this.gameMode = wrapper.gameMode;
      this.previousGameMode = wrapper.previousGameMode;
      this.worldDebug = wrapper.worldDebug;
      this.worldFlat = wrapper.worldFlat;
      this.keptData = wrapper.keptData;
      this.lastDeathPosition = wrapper.lastDeathPosition;
      this.portalCooldown = wrapper.portalCooldown;
      this.seaLevel = wrapper.seaLevel;
      this.levelType = wrapper.levelType;
   }

   public DimensionTypeRef getDimensionTypeRef() {
      return this.dimensionTypeRef;
   }

   public void setDimensionTypeRef(DimensionTypeRef dimensionTypeRef) {
      this.dimensionTypeRef = dimensionTypeRef;
   }

   public DimensionType getDimensionType() {
      IRegistry<DimensionType> registry = this.replaceRegistry(DimensionTypes.getRegistry());
      return this.dimensionTypeRef.resolve(registry, (PacketWrapper)this);
   }

   public void setDimensionType(DimensionType dimensionType) {
      this.dimensionTypeRef = dimensionType.asRef((PacketWrapper)this);
   }

   /** @deprecated */
   @Deprecated
   public Dimension getDimension() {
      return Dimension.fromDimensionTypeRef(this.dimensionTypeRef);
   }

   /** @deprecated */
   @Deprecated
   public void setDimension(Dimension dimension) {
      this.dimensionTypeRef = dimension.asDimensionTypeRef();
   }

   public Optional<String> getWorldName() {
      return this.worldName;
   }

   public void setWorldName(@Nullable String worldName) {
      this.worldName = Optional.ofNullable(worldName);
   }

   @Nullable
   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   public void setDifficulty(Difficulty difficulty) {
      this.difficulty = difficulty;
   }

   public long getHashedSeed() {
      return this.hashedSeed;
   }

   public void setHashedSeed(long hashedSeed) {
      this.hashedSeed = hashedSeed;
   }

   public GameMode getGameMode() {
      return this.gameMode;
   }

   public void setGameMode(GameMode gameMode) {
      this.gameMode = gameMode;
   }

   @Nullable
   public GameMode getPreviousGameMode() {
      return this.previousGameMode;
   }

   public void setPreviousGameMode(@Nullable GameMode previousGameMode) {
      this.previousGameMode = previousGameMode;
   }

   public boolean isWorldDebug() {
      return this.worldDebug;
   }

   public void setWorldDebug(boolean worldDebug) {
      this.worldDebug = worldDebug;
   }

   public boolean isWorldFlat() {
      return this.worldFlat;
   }

   public void setWorldFlat(boolean worldFlat) {
      this.worldFlat = worldFlat;
   }

   public boolean isKeepingAllPlayerData() {
      return (this.keptData & 1) != 0;
   }

   public void setKeepingAllPlayerData(boolean keepAllPlayerData) {
      this.keptData = (byte)(keepAllPlayerData ? 3 : 2);
   }

   public byte getKeptData() {
      return this.keptData;
   }

   public void setKeptData(byte keptData) {
      this.keptData = keptData;
   }

   @Nullable
   public WorldBlockPosition getLastDeathPosition() {
      return this.lastDeathPosition;
   }

   public void setLastDeathPosition(@Nullable WorldBlockPosition lastDeathPosition) {
      this.lastDeathPosition = lastDeathPosition;
   }

   public Optional<Integer> getPortalCooldown() {
      return Optional.ofNullable(this.portalCooldown);
   }

   public void setPortalCooldown(int portalCooldown) {
      this.portalCooldown = portalCooldown;
   }

   public int getSeaLevel() {
      return this.seaLevel;
   }

   public void setSeaLevel(int seaLevel) {
      this.seaLevel = seaLevel;
   }
}
