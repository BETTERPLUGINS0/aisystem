package advancedplugins.pm2.cv.models.v1_21_R5.entity.fake;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeItemDisplayEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Display.ItemDisplay;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
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
      this.itemDisplay.setPos(var1.getX(), var1.getY(), var1.getZ());
   }

   protected EntityType<?> getEntityType() {
      return EntityType.ITEM_DISPLAY;
   }

   public List<UUID> getViewers() {
      return this.viewers;
   }

   public void spawnInternal(Player var1) {
      super.spawnInternal(var1);
      this.packAndSend(var1);
      this.getItemDisplay().resendPossiblyDesyncedEntityData(this.getNmsPlayer(var1));
   }

   protected int getEntityId() {
      return this.getItemDisplay().getId();
   }

   public void setTransformation(Transformation var1) {
      com.mojang.math.Transformation var2 = new com.mojang.math.Transformation(var1.getTranslation(), var1.getLeftRotation(), var1.getScale(), var1.getRightRotation());
      this.itemDisplay.setTransformation(var2);
   }

   public void positionSync(double var1, double var3, double var5, float var7, float var8) {
      this.itemDisplay.setPos(var1, var3, var5);
      this.itemDisplay.setRot(var7, var8);
      float var9 = (float)var1 - this.getPosition().x;
      float var10 = (float)var3 - this.getPosition().y;
      float var11 = (float)var5 - this.getPosition().z;
      byte var12 = (byte)((int)(var7 - this.getYaw()));
      byte var13 = (byte)((int)(var8 - this.getPitch()));
      boolean var14 = Math.abs(var9) > 8.0F || Math.abs(var10) > 8.0F || Math.abs(var11) > 8.0F;
      Object var15;
      if (var14) {
         var15 = ClientboundEntityPositionSyncPacket.of(this.itemDisplay);
      } else {
         var15 = new PosRot(this.itemDisplay.getId(), (short)((int)var9), (short)((int)var10), (short)((int)var11), var12, var13, true);
      }

      this.getOnlineViewers().forEach((var2) -> {
         this.sendPacket(var2, var15);
      });
   }

   public Vector3f getPosition() {
      return new Vector3f((float)this.itemDisplay.getX(), (float)this.itemDisplay.getY(), (float)this.itemDisplay.getZ());
   }

   public float getYaw() {
      return this.itemDisplay.getBukkitYaw();
   }

   public float getPitch() {
      return this.itemDisplay.getYRot();
   }

   public void setItemStack(ItemStack var1) {
      this.itemDisplay.setItemStack(CraftItemStack.asNMSCopy(var1));
   }

   public void packAndSend(Player var1) {
      List var2 = this.itemDisplay.getEntityData().getNonDefaultValues();
      if (var2 != null && !var2.isEmpty()) {
         this.sendPacket(var1, new ClientboundSetEntityDataPacket(this.itemDisplay.getId(), var2));
      }

   }

   @Generated
   public ItemDisplay getItemDisplay() {
      return this.itemDisplay;
   }
}
