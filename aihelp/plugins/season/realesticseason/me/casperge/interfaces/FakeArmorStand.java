package me.casperge.interfaces;

import java.util.List;
import me.casperge.enums.ArmorStandPart;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface FakeArmorStand {
   Location getLocation();

   void move(Location var1, List<Player> var2);

   void setItemSlot(int var1, ItemStack var2);

   void sendSpawnPacket(List<Player> var1);

   void updateMetaData(Player var1);

   float getPitch(ArmorStandPart var1);

   float getRoll(ArmorStandPart var1);

   float getYaw(ArmorStandPart var1);

   void updatePose(ArmorStandPart var1, float var2, float var3, float var4, List<Player> var5);

   void destroy(List<Player> var1);
}
