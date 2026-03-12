package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.JointType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Optional;

public class WrapperPlayClientUpdateJigsawBlock extends PacketWrapper<WrapperPlayClientUpdateJigsawBlock> {
   private Vector3i position;
   private ResourceLocation name;
   @Nullable
   private ResourceLocation target;
   private ResourceLocation pool;
   private String finalState;
   @Nullable
   private JointType jointType;
   private int selectionPriority;
   private int placementPriority;

   public WrapperPlayClientUpdateJigsawBlock(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientUpdateJigsawBlock(Vector3i position, ResourceLocation name, ResourceLocation pool, String finalState) {
      this(position, name, (ResourceLocation)null, pool, finalState, (JointType)null);
   }

   public WrapperPlayClientUpdateJigsawBlock(Vector3i position, ResourceLocation name, @Nullable ResourceLocation target, ResourceLocation pool, String finalState, @Nullable JointType jointType) {
      this(position, name, target, pool, finalState, jointType, 0, 0);
   }

   public WrapperPlayClientUpdateJigsawBlock(Vector3i position, ResourceLocation name, @Nullable ResourceLocation target, ResourceLocation pool, String finalState, @Nullable JointType jointType, int selectionPriority, int placementPriority) {
      super((PacketTypeCommon)PacketType.Play.Client.UPDATE_JIGSAW_BLOCK);
      this.position = position;
      this.name = name;
      this.target = target;
      this.pool = pool;
      this.finalState = finalState;
      this.jointType = jointType;
      this.selectionPriority = selectionPriority;
      this.placementPriority = placementPriority;
   }

   public void read() {
      this.position = this.readBlockPosition();
      this.name = this.readIdentifier();
      if (this.v1_16()) {
         this.target = this.readIdentifier();
      }

      this.pool = this.readIdentifier();
      this.finalState = this.readString();
      if (this.v1_16()) {
         this.jointType = (JointType)JointType.byName(this.readString()).orElse(JointType.ALIGNED);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.selectionPriority = this.readVarInt();
         this.placementPriority = this.readVarInt();
      }

   }

   public void write() {
      this.writeBlockPosition(this.position);
      this.writeIdentifier(this.name);
      if (this.v1_16()) {
         this.writeIdentifier(this.target);
      }

      this.writeIdentifier(this.pool);
      this.writeString(this.finalState);
      if (this.v1_16()) {
         this.writeString(this.jointType.getSerializedName());
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         this.writeVarInt(this.selectionPriority);
         this.writeVarInt(this.placementPriority);
      }

   }

   public void copy(WrapperPlayClientUpdateJigsawBlock wrapper) {
      this.position = wrapper.position;
      this.name = wrapper.name;
      this.target = wrapper.target;
      this.pool = wrapper.pool;
      this.finalState = wrapper.finalState;
      this.jointType = wrapper.jointType;
      this.selectionPriority = wrapper.selectionPriority;
      this.placementPriority = wrapper.placementPriority;
   }

   private boolean v1_16() {
      return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   public ResourceLocation getName() {
      return this.name;
   }

   public void setName(ResourceLocation name) {
      this.name = name;
   }

   public Optional<ResourceLocation> getTarget() {
      return Optional.ofNullable(this.target);
   }

   public void setTarget(@Nullable ResourceLocation target) {
      this.target = target;
   }

   public ResourceLocation getPool() {
      return this.pool;
   }

   public void setPool(ResourceLocation pool) {
      this.pool = pool;
   }

   public String getFinalState() {
      return this.finalState;
   }

   public void setFinalState(String finalState) {
      this.finalState = finalState;
   }

   public Optional<JointType> getJointType() {
      return Optional.ofNullable(this.jointType);
   }

   public void setJointType(@Nullable JointType jointType) {
      this.jointType = jointType;
   }

   public int getSelectionPriority() {
      return this.selectionPriority;
   }

   public void setSelectionPriority(int selectionPriority) {
      this.selectionPriority = selectionPriority;
   }

   public int getPlacementPriority() {
      return this.placementPriority;
   }

   public void setPlacementPriority(int placementPriority) {
      this.placementPriority = placementPriority;
   }
}
