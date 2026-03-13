package me.casperge.realisticseasons1_21_R2;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.casperge.enums.ArmorStandPart;
import me.casperge.interfaces.FakeArmorStand;
import net.minecraft.core.Vector3f;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FakeArmorStand_1_21_R2 implements FakeArmorStand {
   private int ID;
   private DataWatcher dataWatcher;
   private ItemStack[] slots = new ItemStack[6];
   private HashMap<ArmorStandPart, Vector> poses = new HashMap();
   private Location loc;
   private EntityArmorStand armorstand;

   public FakeArmorStand_1_21_R2(World var1, double var2, double var4, double var6, double var8, boolean var10, boolean var11, boolean var12, boolean var13) {
      this.armorstand = new EntityArmorStand(((CraftWorld)var1).getHandle(), var2, var4, var6);
      this.ID = this.armorstand.ar();
      this.dataWatcher = this.armorstand.au();
      this.armorstand.k(var10);
      this.armorstand.v(var11);
      this.armorstand.u(var12);
      this.armorstand.a(var13);
      this.armorstand.b(var2, var4, var6, (float)var8, 0.0F);
      this.loc = new Location(var1, var2, var4, var6);
   }

   public Location getLocation() {
      return this.loc;
   }

   public void move(Location var1, List<Player> var2) {
      double var3 = var1.getX();
      double var5 = var1.getY();
      double var7 = var1.getZ();
      this.armorstand.b(var3, var5, var7, var1.getYaw(), var1.getPitch());
      short var9 = (short)((int)((var3 * 32.0D - this.loc.getX() * 32.0D) * 128.0D));
      short var10 = (short)((int)((var5 * 32.0D - this.loc.getY() * 32.0D) * 128.0D));
      short var11 = (short)((int)((var7 * 32.0D - this.loc.getZ() * 32.0D) * 128.0D));
      PacketPlayOutRelEntityMove var12 = new PacketPlayOutRelEntityMove(this.ID, var9, var10, var11, false);
      Iterator var13 = var2.iterator();

      while(var13.hasNext()) {
         Player var14 = (Player)var13.next();
         NmsCode_21_R2.sendPacket(var14, var12);
      }

      this.loc = var1;
   }

   public void setItemSlot(int var1, org.bukkit.inventory.ItemStack var2) {
      this.slots[var1] = CraftItemStack.asNMSCopy(var2);
      this.armorstand.a(EnumItemSlot.values()[var1], this.slots[var1]);
   }

   public void sendSpawnPacket(List<Player> var1) {
      PacketPlayOutSpawnEntity var2 = new PacketPlayOutSpawnEntity(this.armorstand.ar(), this.armorstand.cG(), this.armorstand.dB(), this.armorstand.dD(), this.armorstand.dH(), this.armorstand.dO(), this.armorstand.dM(), this.armorstand.aq(), 0, this.armorstand.dz(), (double)this.armorstand.cA());
      PacketPlayOutEntityMetadata var3 = new PacketPlayOutEntityMetadata(this.ID, this.dataWatcher.b());
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < 6; ++var5) {
         if (this.slots[var5] != null) {
            var4.add(new Pair(EnumItemSlot.values()[var5], this.slots[var5]));
         }
      }

      PacketPlayOutEntityEquipment var8 = new PacketPlayOutEntityEquipment(this.ID, var4);
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         Player var7 = (Player)var6.next();
         NmsCode_21_R2.sendPacket(var7, var2);
         NmsCode_21_R2.sendPacket(var7, var3);
         NmsCode_21_R2.sendPacket(var7, var8);
      }

   }

   public void updateMetaData(Player var1) {
      PacketPlayOutEntityMetadata var2 = new PacketPlayOutEntityMetadata(this.ID, this.dataWatcher.c());
      NmsCode_21_R2.sendPacket(var1, var2);
   }

   public float getPitch(ArmorStandPart var1) {
      return !this.poses.containsKey(var1) ? 0.0F : (float)((Vector)this.poses.get(var1)).getX();
   }

   public float getRoll(ArmorStandPart var1) {
      return !this.poses.containsKey(var1) ? 0.0F : (float)((Vector)this.poses.get(var1)).getY();
   }

   public float getYaw(ArmorStandPart var1) {
      return !this.poses.containsKey(var1) ? 0.0F : (float)((Vector)this.poses.get(var1)).getZ();
   }

   public void updatePose(ArmorStandPart var1, float var2, float var3, float var4, List<Player> var5) {
      Vector3f var6 = new Vector3f(var2, var3, var4);
      this.poses.put(var1, new Vector(var2, var3, var4));
      switch(var1) {
      case HEAD:
         this.armorstand.a(var6);
         break;
      case BODY:
         this.armorstand.b(var6);
         break;
      case LEFT_ARM:
         this.armorstand.c(var6);
         break;
      case RIGHT_ARM:
         this.armorstand.d(var6);
         break;
      case LEFT_LEG:
         this.armorstand.e(var6);
         break;
      case RIGHT_LEG:
         this.armorstand.f(var6);
      }

      Iterator var7 = var5.iterator();

      while(var7.hasNext()) {
         Player var8 = (Player)var7.next();
         this.updateMetaData(var8);
      }

   }

   public void destroy(List<Player> var1) {
      PacketPlayOutEntityDestroy var2 = new PacketPlayOutEntityDestroy(new int[]{this.ID});
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Player var4 = (Player)var3.next();
         NmsCode_21_R2.sendPacket(var4, var2);
      }

   }
}
