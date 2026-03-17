package advancedplugins.pm2.cv.models.v1_20_R4.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeBlockDisplayEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.BlockDisplay;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.block.CraftBlockState;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class FakeBlockDisplayEntityImpl extends FakeDisplayEntityImpl implements FakeBlockDisplayEntity {
   private final BlockDisplay blockDisplay;
   private final List<UUID> viewers = new ArrayList();

   public FakeBlockDisplayEntityImpl(@NotNull Location initialSpawnLocation) {
      super(var1);
      CraftWorld var2 = (CraftWorld)var1.getWorld();
      this.blockDisplay = new BlockDisplay(this.getEntityType(), var2.getHandle());
      this.blockDisplay.a_(var1.getX(), var1.getY(), var1.getZ());
   }

   protected EntityTypes<?> getEntityType() {
      return EntityTypes.af;
   }

   public List<UUID> getViewers() {
      return this.viewers;
   }

   public void setTransformation(Transformation transformation) {
      com.mojang.math.Transformation var2 = new com.mojang.math.Transformation(var1.getTranslation(), var1.getLeftRotation(), var1.getScale(), var1.getRightRotation());
      this.blockDisplay.a(var2);
   }

   public void positionSync(double x, double y, double z, float yaw, float pitch) {
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
         var15 = new PacketPlayOutEntityTeleport(this.blockDisplay);
      } else {
         var15 = new PacketPlayOutRelEntityMoveLook(this.blockDisplay.aj(), (short)((int)var9), (short)((int)var10), (short)((int)var11), var12, var13, true);
      }

      this.getOnlineViewers().forEach((var2) -> {
         this.sendPacket(var2, var15);
      });
   }

   public Vector3f getPosition() {
      return new Vector3f((float)this.blockDisplay.dr(), (float)this.blockDisplay.dt(), (float)this.blockDisplay.dx());
   }

   public float getYaw() {
      return this.blockDisplay.getBukkitYaw();
   }

   public float getPitch() {
      return this.blockDisplay.dC();
   }

   public void setBlock(BlockData blockData) {
      CraftBlockState var2 = (CraftBlockState)var1.createBlockState();
      this.blockDisplay.c(var2.getHandle());
   }

   @Generated
   public BlockDisplay getBlockDisplay() {
      return this.blockDisplay;
   }
}
