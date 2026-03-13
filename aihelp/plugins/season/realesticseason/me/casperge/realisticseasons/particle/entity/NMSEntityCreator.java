package me.casperge.realisticseasons.particle.entity;

import me.casperge.interfaces.FakeArmorStand;
import me.casperge.realisticseasons.Version;
import org.bukkit.World;

public class NMSEntityCreator {
   public static FakeArmorStand createFakeArmorStand(World var0, double var1, double var3, double var5, double var7, boolean var9, boolean var10, boolean var11, boolean var12) {
      return Version.createFakeArmorStand(var0, var1, var3, var5, var7, var9, var10, var11, var12);
   }
}
