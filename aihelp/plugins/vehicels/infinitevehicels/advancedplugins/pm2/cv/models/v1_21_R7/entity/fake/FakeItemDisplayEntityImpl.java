package advancedplugins.pm2.cv.models.v1_21_R7.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeItemDisplayEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.ItemDisplay;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class FakeItemDisplayEntityImpl extends FakeDisplayEntityImpl implements FakeItemDisplayEntity {
   private final ItemDisplay itemDisplay;
   private final List<UUID> viewers = new ArrayList();

   public FakeItemDisplayEntityImpl(@NotNull Location var1) {
      super(var1);
      CraftWorld var2 = (CraftWorld)var1.getWorld();
      this.itemDisplay = new ItemDisplay(this.getEntityType(), var2.getHandle());
      this.itemDisplay.a_(var1.getX(), var1.getY(), var1.getZ());
   }

   protected EntityTypes<?> getEntityType() {
      return EntityTypes.av;
   }

   public List<UUID> getViewers() {
      return this.viewers;
   }

   public void spawnInternal(Player var1) {
      super.spawnInternal(var1);
      this.packAndSend(var1);
      this.getItemDisplay().refreshEntityData(this.getNmsPlayer(var1));
   }

   protected int getEntityId() {
      return this.getItemDisplay().az();
   }

   public void setTransformation(Transformation var1) {
      com.mojang.math.Transformation var2 = new com.mojang.math.Transformation(var1.getTranslation(), var1.getLeftRotation(), var1.getScale(), var1.getRightRotation());
      this.itemDisplay.a(var2);
   }

   public void positionSync(double var1, double var3, double var5, float var7, float var8) {
      this.itemDisplay.a_(var1, var3, var5);
      this.itemDisplay.a(var7, true, var8, true);
      float var9 = (float)var1 - this.getPosition().x;
      float var10 = (float)var3 - this.getPosition().y;
      float var11 = (float)var5 - this.getPosition().z;
      byte var12 = (byte)((int)(var7 - this.getYaw()));
      byte var13 = (byte)((int)(var8 - this.getPitch()));
      boolean var14 = Math.abs(var9) > 8.0F || Math.abs(var10) > 8.0F || Math.abs(var11) > 8.0F;
      Object var15;
      if (var14) {
         var15 = ClientboundEntityPositionSyncPacket.a(this.itemDisplay);
      } else {
         var15 = new PacketPlayOutRelEntityMoveLook(this.itemDisplay.az(), (short)((int)var9), (short)((int)var10), (short)((int)var11), var12, var13, true);
      }

      this.getOnlineViewers().forEach((var2) -> {
         this.sendPacket(var2, var15);
      });
   }

   public Vector3f getPosition() {
      return new Vector3f((float)this.itemDisplay.dK(), (float)this.itemDisplay.dM(), (float)this.itemDisplay.dQ());
   }

   public float getYaw() {
      return this.itemDisplay.getBukkitYaw();
   }

   public float getPitch() {
      return this.itemDisplay.dX();
   }

   public void setItemStack(ItemStack var1) {
      this.itemDisplay.a(CraftItemStack.asNMSCopy(var1));
   }

   public void packAndSend(Player var1) {
      List var2 = this.itemDisplay.aC().c();
      if (var2 != null && !var2.isEmpty()) {
         this.sendPacket(var1, new PacketPlayOutEntityMetadata(this.itemDisplay.az(), var2));
      }

   }

   @Generated
   public ItemDisplay getItemDisplay() {
      return this.itemDisplay;
   }
}
