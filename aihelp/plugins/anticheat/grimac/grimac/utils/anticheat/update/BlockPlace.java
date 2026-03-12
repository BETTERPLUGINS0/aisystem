package ac.grim.grimac.utils.anticheat.update;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.East;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Half;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.North;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.South;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.Type;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.enums.West;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateValue;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.fastutil.objects.ObjectIterator;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.collisions.AxisSelect;
import ac.grim.grimac.utils.collisions.CollisionData;
import ac.grim.grimac.utils.collisions.blocks.DoorHandler;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.grim.grimac.utils.data.HitData;
import ac.grim.grimac.utils.data.packetentity.PacketEntity;
import ac.grim.grimac.utils.latency.CompensatedWorld;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BoundingBoxSize;
import ac.grim.grimac.utils.nmsutil.GetBoundingBox;
import ac.grim.grimac.utils.nmsutil.Materials;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;

public class BlockPlace {
   private static final BlockFace[] BY_3D;
   public static final BlockFace[] BY_2D;
   public final boolean isBlock;
   private final SimpleCollisionBox[] collisions = new SimpleCollisionBox[15];
   public Vector3i position;
   public final InteractionHand hand;
   public boolean replaceClicked;
   private boolean isCancelled;
   private final GrimPlayer player;
   public final ItemStack itemStack;
   public final StateType material;
   @Nullable
   public final HitData hitData;
   private int faceId;
   private BlockFace face;
   public boolean isInside;
   public Vector3f cursor;
   public final int sequence;

   public BlockPlace(GrimPlayer player, InteractionHand hand, Vector3i position, int faceId, BlockFace face, ItemStack itemStack, @Nullable HitData hitData, int sequence) {
      this.player = player;
      this.hand = hand;
      this.position = position;
      this.faceId = faceId;
      this.face = face;
      this.itemStack = itemStack;
      if (itemStack.getType().getPlacedType() == null) {
         this.material = StateTypes.FIRE;
         this.isBlock = false;
      } else {
         this.material = itemStack.getType().getPlacedType();
         this.isBlock = true;
      }

      this.hitData = hitData;
      WrappedBlockState state = player.compensatedWorld.getBlock(position);
      this.replaceClicked = this.canBeReplaced(this.material, state, face);
      this.sequence = sequence;
   }

   public WrappedBlockState getExistingBlockData() {
      return this.player.compensatedWorld.getBlock(this.getPlacedBlockPos());
   }

   public StateType getPlacedAgainstMaterial() {
      return this.player.compensatedWorld.getBlock(this.position).getType();
   }

   public WrappedBlockState getBelowState() {
      Vector3i pos = this.getPlacedBlockPos();
      pos = pos.withY(pos.getY() - 1);
      return this.player.compensatedWorld.getBlock(pos);
   }

   public WrappedBlockState getAboveState() {
      Vector3i pos = this.getPlacedBlockPos();
      pos = pos.withY(pos.getY() + 1);
      return this.player.compensatedWorld.getBlock(pos);
   }

   public WrappedBlockState getDirectionalState(BlockFace facing) {
      Vector3i pos = this.getPlacedBlockPos();
      pos = pos.add(facing.getModX(), facing.getModY(), facing.getModZ());
      return this.player.compensatedWorld.getBlock(pos);
   }

   public boolean isSolidBlocking(BlockFace relative) {
      WrappedBlockState state = this.getDirectionalState(relative);
      return state.getType().isBlocking();
   }

   private boolean canBeReplaced(StateType heldItem, WrappedBlockState state, BlockFace face) {
      StateType currentType = state.getType();
      if (BlockTags.SLABS.contains(currentType)) {
         Type typeData = state.getTypeData();
         if (typeData != Type.DOUBLE && currentType == heldItem) {
            boolean isHighClick = this.getClickedLocation().getY() > 0.5D;
            if (typeData == Type.BOTTOM) {
               return this.getFace() == BlockFace.UP || isHighClick && this.isFaceHorizontal();
            } else {
               return this.getFace() == BlockFace.DOWN || !isHighClick && this.isFaceHorizontal();
            }
         } else {
            return false;
         }
      } else if (currentType == StateTypes.SNOW) {
         int layers = state.getLayers();
         if (heldItem == currentType && layers < 8) {
            return face == BlockFace.UP;
         } else {
            return layers == 1;
         }
      } else if (currentType == StateTypes.VINE) {
         boolean baseReplaceable = currentType != heldItem && currentType.isReplaceable();
         if (baseReplaceable) {
            return true;
         } else if (heldItem != currentType) {
            return false;
         } else if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && !state.isUp()) {
            return true;
         } else {
            return state.getNorth() == North.FALSE || state.getSouth() == South.FALSE || state.getEast() == East.FALSE || state.getWest() == West.FALSE;
         }
      } else if (currentType == StateTypes.LADDER) {
         if (this.player.getClientVersion().isOlderThan(ClientVersion.V_1_13)) {
            return true;
         } else {
            return currentType != heldItem && currentType.isReplaceable();
         }
      } else if (currentType != StateTypes.GLOW_LICHEN && currentType != StateTypes.SCULK_VEIN) {
         if (currentType == StateTypes.SCAFFOLDING) {
            return heldItem == StateTypes.SCAFFOLDING;
         } else if (BlockTags.CANDLES.contains(currentType)) {
            return heldItem == currentType && state.getCandles() < 4 && !this.isSecondaryUse();
         } else if (currentType == StateTypes.SEA_PICKLE) {
            return heldItem == currentType && state.getPickles() < 4 && !this.isSecondaryUse();
         } else if (currentType == StateTypes.TURTLE_EGG) {
            return heldItem == currentType && state.getEggs() < 4 && !this.isSecondaryUse();
         } else {
            return currentType != heldItem && currentType.isReplaceable();
         }
      } else {
         return heldItem != currentType || !state.isUp() || !state.isDown() || state.getNorth() == North.FALSE || state.getSouth() == South.FALSE || state.getEast() == East.FALSE || state.getWest() == West.FALSE;
      }
   }

   public boolean isFaceFullCenter(BlockFace facing) {
      WrappedBlockState data = this.getDirectionalState(facing);
      CollisionBox box = CollisionData.getData(data.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), data);
      if (box.isNull()) {
         return false;
      } else if (this.isFullFace(facing)) {
         return true;
      } else if (BlockTags.LEAVES.contains(data.getType())) {
         return false;
      } else if (BlockTags.FENCE_GATES.contains(data.getType())) {
         return false;
      } else {
         int size = box.downCast(this.collisions);
         AxisSelect axis = AxisSelect.byFace(facing.getOppositeFace());

         for(int i = 0; i < size; ++i) {
            SimpleCollisionBox simpleBox = this.collisions[i];
            simpleBox = axis.modify(simpleBox);
            if (simpleBox.minX <= 0.4375D && simpleBox.maxX >= 0.4375D && simpleBox.minY <= 0.0D && simpleBox.maxY >= 0.625D && simpleBox.minZ <= 0.4375D && simpleBox.maxZ >= 0.5625D) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isFaceRigid(BlockFace facing) {
      WrappedBlockState data = this.getDirectionalState(facing);
      CollisionBox box = CollisionData.getData(data.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), data);
      if (box.isNull()) {
         return false;
      } else if (this.isFullFace(facing)) {
         return true;
      } else if (BlockTags.LEAVES.contains(data.getType())) {
         return false;
      } else {
         int size = box.downCast(this.collisions);
         AxisSelect axis = AxisSelect.byFace(facing.getOppositeFace());

         for(int i = 0; i < size; ++i) {
            SimpleCollisionBox simpleBox = this.collisions[i];
            simpleBox = axis.modify(simpleBox);
            if (simpleBox.minX <= 0.125D && simpleBox.maxX >= 0.875D && simpleBox.minY <= 0.0D && simpleBox.maxY >= 1.0D && simpleBox.minZ <= 0.125D && simpleBox.maxZ >= 0.875D) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isFullFace(BlockFace relative) {
      WrappedBlockState state = this.getDirectionalState(relative);
      BlockFace face = relative.getOppositeFace();
      BlockFace bukkitFace = BlockFace.valueOf(face.name());
      AxisSelect axis = AxisSelect.byFace(face);
      CollisionBox box = CollisionData.getData(state.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), state);
      StateType blockMaterial = state.getType();
      if (BlockTags.LEAVES.contains(blockMaterial)) {
         return false;
      } else if (blockMaterial == StateTypes.SNOW) {
         return state.getLayers() == 8 || face == BlockFace.DOWN;
      } else if (BlockTags.STAIRS.contains(blockMaterial)) {
         if (face == BlockFace.UP) {
            return state.getHalf() == Half.TOP;
         } else if (face == BlockFace.DOWN) {
            return state.getHalf() == Half.BOTTOM;
         } else {
            return state.getFacing() == bukkitFace;
         }
      } else if (blockMaterial == StateTypes.COMPOSTER) {
         return face != BlockFace.UP;
      } else if (blockMaterial == StateTypes.SOUL_SAND) {
         return true;
      } else if (blockMaterial == StateTypes.LADDER) {
         return state.getFacing().getOppositeFace() == bukkitFace;
      } else if (!BlockTags.TRAPDOORS.contains(blockMaterial)) {
         if (BlockTags.DOORS.contains(blockMaterial)) {
            CollisionData data = CollisionData.getData(blockMaterial);
            CollisionFactory var10 = data.dynamic;
            if (var10 instanceof DoorHandler) {
               DoorHandler doorHandler = (DoorHandler)var10;
               return doorHandler.fetchDirection(this.player, this.player.getClientVersion(), state, this.position.x, this.position.y, this.position.z).getOppositeFace() == bukkitFace;
            }
         }

         int size = box.downCast(this.collisions);

         for(int i = 0; i < size; ++i) {
            SimpleCollisionBox simpleBox = this.collisions[i];
            if (axis.modify(simpleBox).isFullBlockNoCache()) {
               return true;
            }
         }

         return false;
      } else {
         return state.getFacing().getOppositeFace() == bukkitFace && state.isOpen() || state.getHalf() == Half.TOP && !state.isOpen() && bukkitFace == BlockFace.UP || state.getHalf() == Half.BOTTOM && !state.isOpen() && bukkitFace == BlockFace.DOWN;
      }
   }

   public boolean isBlockFaceOpen(BlockFace facing) {
      Vector3i pos = this.getPlacedBlockPos();
      pos = pos.add(facing.getModX(), facing.getModY(), facing.getModZ());
      return pos.getY() >= this.player.compensatedWorld.getMaxHeight() ? false : this.player.compensatedWorld.getBlock(pos).getType().isReplaceable();
   }

   public boolean isFaceEmpty(BlockFace facing) {
      WrappedBlockState data = this.getDirectionalState(facing);
      CollisionBox box = CollisionData.getData(data.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), data);
      if (box.isNull()) {
         return false;
      } else if (this.isFullFace(facing)) {
         return true;
      } else if (BlockTags.LEAVES.contains(data.getType())) {
         return false;
      } else {
         int size = box.downCast(this.collisions);
         AxisSelect axis = AxisSelect.byFace(facing.getOppositeFace());

         for(int i = 0; i < size; ++i) {
            SimpleCollisionBox simpleBox = this.collisions[i];
            simpleBox = axis.modify(simpleBox);
            switch(facing) {
            case NORTH:
               if (simpleBox.minZ == 0.0D) {
                  return false;
               }
               break;
            case SOUTH:
               if (simpleBox.maxZ == 1.0D) {
                  return false;
               }
               break;
            case EAST:
               if (simpleBox.maxX == 1.0D) {
                  return false;
               }
               break;
            case WEST:
               if (simpleBox.minX == 0.0D) {
                  return false;
               }
               break;
            case UP:
               if (simpleBox.maxY == 1.0D) {
                  return false;
               }
               break;
            case DOWN:
               if (simpleBox.minY == 0.0D) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean isLava(BlockFace facing) {
      Vector3i pos = this.getPlacedBlockPos();
      pos = pos.add(facing.getModX(), facing.getModY(), facing.getModZ());
      return this.player.compensatedWorld.getBlock(pos).getType() == StateTypes.LAVA;
   }

   public boolean isSecondaryUse() {
      return this.player.isSneaking;
   }

   public boolean isInWater() {
      Vector3i pos = this.getPlacedBlockPos();
      return this.player.compensatedWorld.isWaterSourceBlock(pos.getX(), pos.getY(), pos.getZ());
   }

   public boolean isInLiquid() {
      Vector3i pos = this.getPlacedBlockPos();
      WrappedBlockState data = this.player.compensatedWorld.getBlock(pos);
      return Materials.isWater(this.player.getClientVersion(), data) || data.getType() == StateTypes.LAVA;
   }

   public StateType getBelowMaterial() {
      return this.getBelowState().getType();
   }

   public boolean isOn(StateType... mat) {
      StateType lookingFor = this.getBelowMaterial();
      return Arrays.stream(mat).anyMatch((m) -> {
         return m == lookingFor;
      });
   }

   public boolean isOnDirt() {
      return this.isOn(StateTypes.DIRT, StateTypes.GRASS_BLOCK, StateTypes.PODZOL, StateTypes.COARSE_DIRT, StateTypes.MYCELIUM, StateTypes.ROOTED_DIRT, StateTypes.MOSS_BLOCK);
   }

   public boolean isBlockPlacedPowered() {
      Vector3i placed = this.getPlacedBlockPos();
      BlockFace[] var2 = BY_3D;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockFace face = var2[var4];
         Vector3i modified = placed.add(face.getModX(), face.getModY(), face.getModZ());
         if (this.player.compensatedWorld.getRawPowerAtState(face, modified.getX(), modified.getY(), modified.getZ()) > 0) {
            return true;
         }

         WrappedBlockState state = this.player.compensatedWorld.getBlock(modified);
         boolean isByDefaultConductive = !Materials.isSolidBlockingBlacklist(state.getType(), this.player.getClientVersion()) && CollisionData.getData(state.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), state).isFullBlock();
         if ((state.getType() == StateTypes.SOUL_SAND || !BlockTags.GLASS_BLOCKS.contains(state.getType())) && state.getType() != StateTypes.MOVING_PISTON && state.getType() != StateTypes.BEACON && state.getType() != StateTypes.REDSTONE_BLOCK && state.getType() != StateTypes.OBSERVER && isByDefaultConductive) {
            BlockFace[] var9 = BY_3D;
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               BlockFace recursive = var9[var11];
               Vector3i poweredRecursive = placed.add(recursive.getModX(), recursive.getModY(), recursive.getModZ());
               if (this.player.compensatedWorld.getDirectSignalAtState(recursive, poweredRecursive.getX(), poweredRecursive.getY(), poweredRecursive.getZ()) > 0) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void setFace(BlockFace face) {
      this.face = face;
      this.faceId = face.getFaceValue();
   }

   public void setFaceId(int face) {
      this.faceId = face;
      this.face = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? BlockFace.getBlockFaceByValue(this.faceId) : BlockFace.getLegacyBlockFaceByValue(this.faceId);
   }

   private List<BlockFace> getNearestLookingDirections() {
      float pitch = GrimMath.radians(this.player.pitch);
      float yaw = GrimMath.radians(-this.player.yaw);
      float y = this.player.trigHandler.sin(pitch);
      float cosPitch = this.player.trigHandler.cos(pitch);
      float x = this.player.trigHandler.sin(yaw);
      float z = this.player.trigHandler.cos(yaw);
      boolean isPositiveX = x > 0.0F;
      boolean isNegativeY = y < 0.0F;
      boolean isPositiveZ = z > 0.0F;
      float absX = isPositiveX ? x : -x;
      float absY = isNegativeY ? -y : y;
      float absZ = isPositiveZ ? z : -z;
      float modifiedX = absX * cosPitch;
      float modifiedZ = absZ * cosPitch;
      BlockFace xDir = isPositiveX ? BlockFace.EAST : BlockFace.WEST;
      BlockFace yDir = isNegativeY ? BlockFace.UP : BlockFace.DOWN;
      BlockFace zDir = isPositiveZ ? BlockFace.SOUTH : BlockFace.NORTH;
      if (absX > absZ) {
         if (absY > modifiedX) {
            return this.makeDirList(yDir, xDir, zDir);
         } else {
            return modifiedZ > absY ? this.makeDirList(xDir, zDir, yDir) : this.makeDirList(xDir, yDir, zDir);
         }
      } else if (absY > modifiedZ) {
         return this.makeDirList(yDir, zDir, xDir);
      } else {
         return modifiedX > absY ? this.makeDirList(zDir, xDir, yDir) : this.makeDirList(zDir, yDir, xDir);
      }
   }

   private List<BlockFace> makeDirList(BlockFace one, BlockFace two, BlockFace three) {
      return Arrays.asList(one, two, three, three.getOppositeFace(), two.getOppositeFace(), one.getOppositeFace());
   }

   public BlockFace getNearestVerticalDirection() {
      return this.player.pitch < 0.0F ? BlockFace.UP : BlockFace.DOWN;
   }

   public List<BlockFace> getNearestPlacingDirections() {
      BlockFace[] faces = (BlockFace[])this.getNearestLookingDirections().toArray(new BlockFace[0]);
      if (!this.replaceClicked) {
         BlockFace direction = this.getFace();

         int i;
         for(i = 0; i < faces.length && faces[i] != direction.getOppositeFace(); ++i) {
         }

         if (i > 0) {
            System.arraycopy(faces, 0, faces, 1, i);
            faces[0] = direction.getOppositeFace();
         }
      }

      return Arrays.asList(faces);
   }

   public boolean isFaceVertical() {
      return !this.isFaceHorizontal();
   }

   public boolean isFaceHorizontal() {
      BlockFace face = this.getFace();
      return face == BlockFace.NORTH || face == BlockFace.EAST || face == BlockFace.SOUTH || face == BlockFace.WEST;
   }

   public boolean isXAxis() {
      BlockFace face = this.getFace();
      return face == BlockFace.WEST || face == BlockFace.EAST;
   }

   public Vector3i getPlacedBlockPos() {
      if (this.replaceClicked) {
         return this.position;
      } else {
         int x = this.position.getX() + this.getNormalBlockFace().getX();
         int y = this.position.getY() + this.getNormalBlockFace().getY();
         int z = this.position.getZ() + this.getNormalBlockFace().getZ();
         return new Vector3i(x, y, z);
      }
   }

   public Vector3i getNormalBlockFace() {
      Vector3i var10000;
      switch(this.face) {
      case NORTH:
         var10000 = new Vector3i(0, 0, -1);
         break;
      case SOUTH:
         var10000 = new Vector3i(0, 0, 1);
         break;
      case EAST:
         var10000 = new Vector3i(1, 0, 0);
         break;
      case WEST:
         var10000 = new Vector3i(-1, 0, 0);
         break;
      case UP:
      default:
         var10000 = new Vector3i(0, 1, 0);
         break;
      case DOWN:
         var10000 = new Vector3i(0, -1, 0);
      }

      return var10000;
   }

   public void set(StateType material) {
      this.set(material.createBlockState(CompensatedWorld.blockVersion));
   }

   public void set(BlockFace face, WrappedBlockState state) {
      Vector3i blockPos = this.getPlacedBlockPos().add(face.getModX(), face.getModY(), face.getModZ());
      this.set(blockPos, state);
   }

   public void set(Vector3i position, WrappedBlockState state) {
      CollisionBox box = CollisionData.getData(state.getType()).getMovementCollisionBox(this.player, this.player.getClientVersion(), state, position.getX(), position.getY(), position.getZ());
      if (state.getType() != StateTypes.SCAFFOLDING) {
         if (box.isIntersected(this.player.boundingBox)) {
            return;
         }

         if (this.player.getClientVersion().isNewerThan(ClientVersion.V_1_8)) {
            ObjectIterator var4 = this.player.compensatedEntities.entityMap.values().iterator();

            label60:
            while(true) {
               PacketEntity entity;
               do {
                  if (!var4.hasNext()) {
                     break label60;
                  }

                  entity = (PacketEntity)var4.next();
               } while(!entity.canHit());

               SimpleCollisionBox interpBox = entity.getPossibleCollisionBoxes();
               double scale = entity.getAttributeValue(Attributes.SCALE);
               double width = (double)BoundingBoxSize.getWidth(this.player, entity) * scale;
               double height = (double)BoundingBoxSize.getHeight(this.player, entity) * scale;
               double interpWidth = Math.max(interpBox.maxX - interpBox.minX, interpBox.maxZ - interpBox.minZ);
               double interpHeight = interpBox.maxY - interpBox.minY;
               if (interpWidth - width > 0.05D || interpHeight - height > 0.05D) {
                  Vector3d entityPos = entity.trackedServerPosition.getPos();
                  interpBox = GetBoundingBox.getPacketEntityBoundingBox(this.player, entityPos.getX(), entityPos.getY(), entityPos.getZ(), entity);
               }

               if (box.isIntersected(interpBox)) {
                  return;
               }
            }
         }
      }

      WrappedBlockState existingState = this.player.compensatedWorld.getBlock(position);
      if (this.replaceClicked || this.canBeReplaced(this.material, existingState, this.face)) {
         if (this.player.compensatedWorld.getMaxHeight() > position.getY() && position.getY() >= this.player.compensatedWorld.getMinHeight()) {
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && state.hasProperty(StateValue.WATERLOGGED)) {
               state.setWaterlogged(existingState.getType() == StateTypes.WATER && existingState.getLevel() == 0);
            }

            this.player.inventory.onBlockPlace(this);
            this.player.compensatedWorld.updateBlock(position.getX(), position.getY(), position.getZ(), state.getGlobalId());
         }
      }
   }

   public boolean isZAxis() {
      BlockFace face = this.getFace();
      return face == BlockFace.NORTH || face == BlockFace.SOUTH;
   }

   public void tryCascadeBlockUpdates(Vector3i pos) {
      if (!this.player.getClientVersion().isOlderThanOrEquals(ClientVersion.V_1_12_2)) {
         this.cascadeBlockUpdates(pos);
      }
   }

   private void cascadeBlockUpdates(Vector3i pos) {
   }

   public void set(WrappedBlockState state) {
      this.set(this.getPlacedBlockPos(), state);
   }

   public void resync() {
      this.isCancelled = true;
   }

   public Vector3dm getClickedLocation() {
      SimpleCollisionBox box = new SimpleCollisionBox(this.position);
      Vector3dm look = ReachUtils.getLook(this.player, this.player.yaw, this.player.pitch);
      double distance = this.player.compensatedEntities.self.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 3.0D;
      Vector3dm eyePos = new Vector3dm(this.player.x, this.player.y + this.player.getEyeHeight(), this.player.z);
      Vector3dm endReachPos = eyePos.clone().add(new Vector3dm(look.getX() * distance, look.getY() * distance, look.getZ() * distance));
      Vector3dm intercept = (Vector3dm)ReachUtils.calculateIntercept(box, eyePos, endReachPos).first();
      if (intercept == null) {
         return new Vector3dm();
      } else {
         intercept.setX(intercept.getX() - box.minX);
         intercept.setY(intercept.getY() - box.minY);
         intercept.setZ(intercept.getZ() - box.minZ);
         return intercept;
      }
   }

   public BlockFace getPlayerFacing() {
      return BY_2D[GrimMath.floor((double)this.player.yaw / 90.0D + 0.5D) & 3];
   }

   public void set() {
      if (this.material == null) {
         LogUtil.warn(makeConcatWithConstants<invokedynamic>());
      } else {
         this.set(this.material);
      }
   }

   public void setAbove() {
      Vector3i placed = this.getPlacedBlockPos();
      placed = placed.add(0, 1, 0);
      this.set(placed, this.material.createBlockState(CompensatedWorld.blockVersion));
   }

   public void setAbove(WrappedBlockState toReplaceWith) {
      Vector3i placed = this.getPlacedBlockPos();
      placed = placed.add(0, 1, 0);
      this.set(placed, toReplaceWith);
   }

   @Generated
   public boolean isCancelled() {
      return this.isCancelled;
   }

   @Generated
   public int getFaceId() {
      return this.faceId;
   }

   @Generated
   public BlockFace getFace() {
      return this.face;
   }

   static {
      BY_3D = new BlockFace[]{BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};
      BY_2D = new BlockFace[]{BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST};
   }
}
