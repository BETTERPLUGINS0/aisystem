package advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeBlockDisplayEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.BlockDisplay;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R5.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R5.block.data.CraftBlockData;
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
      this.blockDisplay.a_(var1.getX(), var1.getY(), var1.getZ());
      this.blockDisplay.valid = true;
   }

   protected EntityTypes<?> getEntityType() {
      return EntityTypes.q;
   }

   public List<UUID> getViewers() {
      return this.viewers;
   }

   public void spawnInternal(Player var1) {
      super.spawnInternal(var1);
      this.packAndSend(var1);
   }

   protected int getEntityId() {
      return this.getBlockDisplay().ar();
   }

   public void setTransformation(Transformation var1) {
      com.mojang.math.Transformation var2 = new com.mojang.math.Transformation(var1.getTranslation(), var1.getLeftRotation(), var1.getScale(), var1.getRightRotation());
      this.blockDisplay.a(var2);
   }

   public void positionSync(double var1, double var3, double var5, float var7, float var8) {
      this.blockDisplay.a_(var1, var3, var5);
      this.blockDisplay.a(var7, var8);
      float var9 = (float)var1 - this.getPosition().x;
      float var10 = (float)var3 - this.getPosition().y;
      float var11 = (float)var5 - this.getPosition().z;
      byte var12 = (byte)((int)(var7 - this.getYaw()));
      byte var13 = (byte)((int)(var8 - this.getPitch()));
      boolean var14 = Math.abs(var9) > 8.0F || Math.abs(var10) > 8.0F || Math.abs(var11) > 8.0F;
      Object var15;
      if (var14) {
         var15 = ClientboundEntityPositionSyncPacket.a(this.blockDisplay);
      } else {
         var15 = new PacketPlayOutRelEntityMoveLook(this.blockDisplay.ar(), (short)((int)var9), (short)((int)var10), (short)((int)var11), var12, var13, true);
      }

      this.getOnlineViewers().forEach((var2) -> {
         this.sendPacket(var2, var15);
      });
   }

   public Vector3f getPosition() {
      return new Vector3f((float)this.blockDisplay.dC(), (float)this.blockDisplay.dE(), (float)this.blockDisplay.dI());
   }

   public float getYaw() {
      return this.blockDisplay.getBukkitYaw();
   }

   public float getPitch() {
      return this.blockDisplay.dP();
   }

   public void setBlock(BlockData var1) {
      CraftBlockData var2 = (CraftBlockData)var1;
      this.blockDisplay.c(var2.getState());
   }

   public void packAndSend(Player var1) {
      List var2 = this.blockDisplay.au().c();
      if (var2 != null && !var2.isEmpty()) {
         this.sendPacket(var1, new PacketPlayOutEntityMetadata(this.blockDisplay.ar(), var2));
      }

   }

   @Generated
   public BlockDisplay getBlockDisplay() {
      return this.blockDisplay;
   }
}
