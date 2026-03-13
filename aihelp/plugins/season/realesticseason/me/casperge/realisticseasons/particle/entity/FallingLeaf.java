package me.casperge.realisticseasons.particle.entity;

import java.util.List;
import me.casperge.enums.ArmorStandPart;
import me.casperge.interfaces.FakeArmorStand;
import me.casperge.realisticseasons.utils.BlockUtils;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class FallingLeaf implements SeasonEntity {
   private int counter = 0;
   private Location currentLocation;
   private FakeArmorStand fakearm;
   private double FallingSpeed = 0.1D;
   private Vector wind;
   private boolean isDestroyed = false;
   boolean yawIncreasing = true;
   Vector startVector;
   boolean isOnGround = false;
   private double maxArmYaw = JavaUtils.randomDouble(25.0D, 35.0D);
   private int blockCheckCount = 0;

   public FallingLeaf(Location var1, List<Player> var2, Vector var3, Material var4, Integer var5) {
      this.FallingSpeed = 0.1D + (0.01D - Math.random() / 50.0D);
      double var6 = Math.random() * 360.0D;
      this.currentLocation = var1.clone();
      this.wind = var3;
      this.fakearm = NMSEntityCreator.createFakeArmorStand(var1.getWorld(), var1.getX(), var1.getY(), var1.getZ(), var6, true, false, false, true);
      ItemStack var8 = new ItemStack(var4);
      if (var5 != 0) {
         ItemMeta var9 = var8.getItemMeta();
         var9.setCustomModelData(var5);
         var8.setItemMeta(var9);
      }

      this.fakearm.setItemSlot(0, var8);
      this.fakearm.sendSpawnPacket(var2);
      this.currentLocation.setYaw((float)var6);
      this.startVector = this.currentLocation.getDirection().normalize();
      this.startVector = new Vector(-this.startVector.getZ(), 0.0D, this.startVector.getX());
   }

   private boolean isPlaceable(Block var1) {
      return var1.getType().equals(Material.AIR) || ChunkUtils.naturalplants.contains(var1.getType());
   }

   public void tick(List<Player> var1) {
      if (this.blockCheckCount > 4) {
         this.blockCheckCount = 0;
         Block var2 = this.currentLocation.getBlock();
         Material var3 = var2.getType();
         if (!var3.equals(Material.AIR) && !ChunkUtils.naturalplants.contains(var3) && !this.isOnGround) {
            if (!var3.equals(Material.WATER) && !var3.equals(Material.LAVA) && BlockUtils.canLeafDrop(var2)) {
               if (!var2.getRelative(BlockFace.UP).getType().equals(Material.AIR) && !ChunkUtils.naturalplants.contains(var2.getRelative(BlockFace.UP).getType())) {
                  if (!this.isPlaceable(var2.getRelative(BlockFace.NORTH)) && !this.isPlaceable(var2.getRelative(BlockFace.SOUTH)) && !this.isPlaceable(var2.getRelative(BlockFace.WEST)) && !this.isPlaceable(var2.getRelative(BlockFace.EAST))) {
                     this.fakearm.destroy(var1);
                     this.isDestroyed = true;
                  }
               } else {
                  this.setOnGround(var1);
               }
            } else {
               this.fakearm.destroy(var1);
               this.isDestroyed = true;
            }
         }
      }

      ++this.blockCheckCount;
      if (!this.isOnGround && !this.isDestroyed) {
         float var5 = this.fakearm.getYaw(ArmorStandPart.RIGHT_ARM);
         float var6 = (float)((this.maxArmYaw + 10.0D - (double)Math.abs(var5)) / 4.0D);
         float var4 = 0.0F;
         if (this.yawIncreasing) {
            var4 = var5 + var6;
            if ((double)var4 > this.maxArmYaw) {
               this.yawIncreasing = false;
            }
         } else {
            var4 = var5 - var6;
            if ((double)var4 < -this.maxArmYaw) {
               this.yawIncreasing = true;
            }

            var6 = -var6;
         }

         this.currentLocation.add(this.wind.getX() / 10.0D, -this.FallingSpeed, this.wind.getZ() / 10.0D);
         this.currentLocation.add(this.startVector.clone().multiply(var6 / 50.0F));
         this.fakearm.move(this.currentLocation.clone(), var1);
         this.fakearm.updatePose(ArmorStandPart.RIGHT_ARM, 0.0F, this.fakearm.getRoll(ArmorStandPart.RIGHT_ARM) + (float)(-3.0D + JavaUtils.randomDouble(0.0D, 6.0D)), var4, var1);
      }

      if (this.counter > 300) {
         this.fakearm.destroy(var1);
         this.isDestroyed = true;
      }

      ++this.counter;
   }

   public boolean isDestroyed() {
      return this.isDestroyed;
   }

   public Location getLocation() {
      return this.fakearm.getLocation();
   }

   public void setOnGround(List<Player> var1) {
      this.fakearm.updatePose(ArmorStandPart.RIGHT_ARM, 0.0F, 0.0F, 0.0F, var1);
      Location var2 = this.fakearm.getLocation().clone();
      var2.setY((double)((float)this.fakearm.getLocation().getBlockY() + 0.2F));
      this.isOnGround = true;
      this.fakearm.move(var2, var1);
      this.counter = 280;
   }
}
