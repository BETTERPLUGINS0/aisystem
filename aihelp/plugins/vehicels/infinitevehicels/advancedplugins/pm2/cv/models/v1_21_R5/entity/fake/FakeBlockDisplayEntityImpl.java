package advancedplugins.pm2.cv.models.v1_21_R5.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeBlockDisplayEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.BlockDisplay;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class FakeBlockDisplayEntityImpl extends FakeDisplayEntityImpl implements FakeBlockDisplayEntity {
   private final BlockDisplay blockDisplay;
   private final List<UUID> viewers = new ArrayList();

   public FakeBlockDisplayEntityImpl(@NotNull Location var1) {
      super(var1);
      CraftWorld var2 = (CraftWorld)var1.getWorld();
      this.blockDisplay = new BlockDisplay(this.getEntityType(), var2.getHandle());
      this.blockDisplay.setPos(var1.getX(), var1.getY(), var1.getZ());
      this.blockDisplay.valid = true;
   }

   protected EntityType<?> getEntityType() {
      return EntityType.BLOCK_DISPLAY;
   }

   public List<UUID> getViewers() {
      return this.viewers;
   }

   public void spawnInternal(Player var1) {
      super.spawnInternal(var1);
      this.packAndSend(var1);
   }

   protected int getEntityId() {
      return this.getBlockDisplay().getId();
   }

   public void setTransformation(Transformation var1) {
      com.mojang.math.Transformation var2 = new com.mojang.math.Transformation(var1.getTranslation(), var1.getLeftRotation(), var1.getScale(), var1.getRightRotation());
      this.blockDisplay.setTransformation(var2);
   }

   public void positionSync(double var1, double var3, double var5, float var7, float var8) {
      this.blockDisplay.setPos(var1, var3, var5);
      this.blockDisplay.setRot(var7, var8);
      float var9 = (float)var1 - this.getPosition().x;
      float var10 = (float)var3 - this.getPosition().y;
      float var11 = (float)var5 - this.getPosition().z;
      byte var12 = (byte)((int)(var7 - this.getYaw()));
      byte var13 = (byte)((int)(var8 - this.getPitch()));
      boolean var14 = Math.abs(var9) > 8.0F || Math.abs(var10) > 8.0F || Math.abs(var11) > 8.0F;
      Object var15;
      if (var14) {
         var15 = ClientboundEntityPositionSyncPacket.of(this.blockDisplay);
      } else {
         var15 = new PosRot(this.blockDisplay.getId(), (short)((int)var9), (short)((int)var10), (short)((int)var11), var12, var13, true);
      }

      this.getOnlineViewers().forEach((var2) -> {
         this.sendPacket(var2, var15);
      });
   }

   public Vector3f getPosition() {
      return new Vector3f((float)this.blockDisplay.getX(), (float)this.blockDisplay.getY(), (float)this.blockDisplay.getZ());
   }

   public float getYaw() {
      return this.blockDisplay.getBukkitYaw();
   }

   public float getPitch() {
      return this.blockDisplay.getYRot();
   }

   public void setBlock(BlockData var1) {
      CraftBlockData var2 = (CraftBlockData)var1;
      this.blockDisplay.setBlockState(var2.getState());
   }

   public void packAndSend(Player var1) {
      List var2 = this.blockDisplay.getEntityData().getNonDefaultValues();
      if (var2 != null && !var2.isEmpty()) {
         this.sendPacket(var1, new ClientboundSetEntityDataPacket(this.blockDisplay.getId(), var2));
      }

   }

   @Generated
   public BlockDisplay getBlockDisplay() {
      return this.blockDisplay;
   }
}
