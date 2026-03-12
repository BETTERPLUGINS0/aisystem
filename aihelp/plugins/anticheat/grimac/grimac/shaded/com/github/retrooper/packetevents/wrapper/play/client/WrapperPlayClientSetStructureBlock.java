package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.StructureMirror;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.StructureRotation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetStructureBlock extends PacketWrapper<WrapperPlayClientSetStructureBlock> {
   private static final int LIMIT_PRE_1_16_2 = 32;
   private static final int LIMIT = 48;
   private Vector3i position;
   private WrapperPlayClientSetStructureBlock.UpdateType updateType;
   private WrapperPlayClientSetStructureBlock.StructureMode mode;
   private String name;
   private Vector3i offset;
   private Vector3i size;
   private StructureMirror mirror;
   private StructureRotation rotation;
   private String data;
   private boolean ignoreEntities;
   private boolean strict;
   private boolean showAir;
   private boolean showBoundingBox;
   private float integrity;
   private long seed;

   public WrapperPlayClientSetStructureBlock(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientSetStructureBlock(Vector3i position, WrapperPlayClientSetStructureBlock.UpdateType updateType, WrapperPlayClientSetStructureBlock.StructureMode mode, String name, Vector3i offset, Vector3i size, StructureMirror mirror, StructureRotation rotation, String data, boolean ignoreEntities, boolean showAir, boolean showBoundingBox, float integrity, long seed) {
      this(position, updateType, mode, name, offset, size, mirror, rotation, data, ignoreEntities, false, showAir, showBoundingBox, integrity, seed);
   }

   public WrapperPlayClientSetStructureBlock(Vector3i position, WrapperPlayClientSetStructureBlock.UpdateType updateType, WrapperPlayClientSetStructureBlock.StructureMode mode, String name, Vector3i offset, Vector3i size, StructureMirror mirror, StructureRotation rotation, String data, boolean ignoreEntities, boolean strict, boolean showAir, boolean showBoundingBox, float integrity, long seed) {
      super((PacketTypeCommon)PacketType.Play.Client.UPDATE_STRUCTURE_BLOCK);
      this.position = position;
      this.updateType = updateType;
      this.mode = mode;
      this.name = name;
      this.offset = offset;
      this.size = size;
      this.mirror = mirror;
      this.rotation = rotation;
      this.data = data;
      this.ignoreEntities = ignoreEntities;
      this.strict = strict;
      this.showAir = showAir;
      this.showBoundingBox = showBoundingBox;
      this.integrity = integrity;
      this.seed = seed;
   }

   public void read() {
      this.position = this.readBlockPosition();
      this.updateType = (WrapperPlayClientSetStructureBlock.UpdateType)this.readEnum(WrapperPlayClientSetStructureBlock.UpdateType.class);
      this.mode = (WrapperPlayClientSetStructureBlock.StructureMode)this.readEnum(WrapperPlayClientSetStructureBlock.StructureMode.class);
      this.name = this.readString();
      int limit = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2) ? 48 : 32;
      this.offset = new Vector3i(MathUtil.clamp(this.readByte(), -limit, limit), MathUtil.clamp(this.readByte(), -limit, limit), MathUtil.clamp(this.readByte(), -limit, limit));
      this.size = new Vector3i(MathUtil.clamp(this.readByte(), 0, limit), MathUtil.clamp(this.readByte(), 0, limit), MathUtil.clamp(this.readByte(), 0, limit));
      this.mirror = (StructureMirror)this.readEnum(StructureMirror.class);
      this.rotation = (StructureRotation)this.readEnum(StructureRotation.class);
      this.data = this.readString(this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? 128 : 12);
      this.integrity = MathUtil.clamp(this.readFloat(), 0.0F, 1.0F);
      this.seed = this.readVarLong();
      int flags = this.readByte();
      this.ignoreEntities = (flags & 1) != 0;
      this.strict = (flags & 8) != 0;
      this.showAir = (flags & 2) != 0;
      this.showBoundingBox = (flags & 4) != 0;
   }

   public void write() {
      this.writeBlockPosition(this.position);
      this.writeEnum(this.updateType);
      this.writeEnum(this.mode);
      this.writeString(this.name);
      this.writeByte(this.offset.x);
      this.writeByte(this.offset.y);
      this.writeByte(this.offset.z);
      this.writeByte(this.size.x);
      this.writeByte(this.size.y);
      this.writeByte(this.size.z);
      this.writeEnum(this.mirror);
      this.writeEnum(this.rotation);
      this.writeString(this.data);
      this.writeFloat(this.integrity);
      this.writeVarLong(this.seed);
      this.writeByte(0 | (this.ignoreEntities ? 1 : 0) | (this.showAir ? 2 : 0) | (this.showBoundingBox ? 4 : 0) | (this.strict ? 8 : 0));
   }

   public void copy(WrapperPlayClientSetStructureBlock wrapper) {
      this.position = wrapper.position;
      this.updateType = wrapper.updateType;
      this.mode = wrapper.mode;
      this.name = wrapper.name;
      this.offset = wrapper.offset;
      this.size = wrapper.size;
      this.mirror = wrapper.mirror;
      this.rotation = wrapper.rotation;
      this.data = wrapper.data;
      this.ignoreEntities = wrapper.ignoreEntities;
      this.strict = wrapper.strict;
      this.showAir = wrapper.showAir;
      this.showBoundingBox = wrapper.showBoundingBox;
      this.integrity = wrapper.integrity;
      this.seed = wrapper.seed;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public WrapperPlayClientSetStructureBlock.UpdateType getUpdateType() {
      return this.updateType;
   }

   public void setUpdateType(WrapperPlayClientSetStructureBlock.UpdateType updateType) {
      this.updateType = updateType;
   }

   public WrapperPlayClientSetStructureBlock.StructureMode getMode() {
      return this.mode;
   }

   public void setMode(WrapperPlayClientSetStructureBlock.StructureMode mode) {
      this.mode = mode;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Vector3i getOffset() {
      return this.offset;
   }

   public void setOffset(Vector3i offset) {
      this.offset = offset;
   }

   public Vector3i getSize() {
      return this.size;
   }

   public void setSize(Vector3i size) {
      this.size = size;
   }

   public StructureMirror getMirror() {
      return this.mirror;
   }

   public void setMirror(StructureMirror mirror) {
      this.mirror = mirror;
   }

   public StructureRotation getRotation() {
      return this.rotation;
   }

   public void setRotation(StructureRotation rotation) {
      this.rotation = rotation;
   }

   public String getData() {
      return this.data;
   }

   public void setData(String data) {
      this.data = data;
   }

   public boolean isIgnoreEntities() {
      return this.ignoreEntities;
   }

   public void setIgnoreEntities(boolean ignoreEntities) {
      this.ignoreEntities = ignoreEntities;
   }

   public boolean isStrict() {
      return this.strict;
   }

   public void setStrict(boolean strict) {
      this.strict = strict;
   }

   public boolean isShowAir() {
      return this.showAir;
   }

   public void setShowAir(boolean showAir) {
      this.showAir = showAir;
   }

   public boolean isShowBoundingBox() {
      return this.showBoundingBox;
   }

   public void setShowBoundingBox(boolean showBoundingBox) {
      this.showBoundingBox = showBoundingBox;
   }

   public float getIntegrity() {
      return this.integrity;
   }

   public void setIntegrity(float integrity) {
      this.integrity = integrity;
   }

   public long getSeed() {
      return this.seed;
   }

   public void setSeed(long seed) {
      this.seed = seed;
   }

   public static enum UpdateType {
      UPDATE_DATA,
      SAVE_AREA,
      LOAD_AREA,
      SCAN_AREA;

      // $FF: synthetic method
      private static WrapperPlayClientSetStructureBlock.UpdateType[] $values() {
         return new WrapperPlayClientSetStructureBlock.UpdateType[]{UPDATE_DATA, SAVE_AREA, LOAD_AREA, SCAN_AREA};
      }
   }

   public static enum StructureMode {
      SAVE,
      LOAD,
      CORNER,
      DATA;

      // $FF: synthetic method
      private static WrapperPlayClientSetStructureBlock.StructureMode[] $values() {
         return new WrapperPlayClientSetStructureBlock.StructureMode[]{SAVE, LOAD, CORNER, DATA};
      }
   }
}
