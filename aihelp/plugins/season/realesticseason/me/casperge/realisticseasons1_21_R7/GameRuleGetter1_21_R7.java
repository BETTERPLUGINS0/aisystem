package me.casperge.realisticseasons1_21_R7;

import me.casperge.enums.GameRuleType;
import me.casperge.interfaces.GameRuleGetter;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.gamerules.GameRules;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R7.CraftWorld;

public class GameRuleGetter1_21_R7 implements GameRuleGetter {
   public Integer GetIntegerGameRule(GameRuleType var1, World var2) {
      WorldServer var3;
      if (var1 == GameRuleType.SNOW_ACCUMULATION_HEIGHT) {
         var3 = ((CraftWorld)var2).getHandle();
         return (Integer)var3.U().a(GameRules.C);
      } else if (var1 == GameRuleType.PLAYER_SLEEPING_PERCENTAGE) {
         var3 = ((CraftWorld)var2).getHandle();
         return (Integer)var3.U().a(GameRules.K);
      } else {
         return null;
      }
   }

   public Boolean GetBooleanGameRule(GameRuleType var1, World var2) {
      WorldServer var3;
      if (var1 == GameRuleType.DO_MOB_SPAWNING) {
         var3 = ((CraftWorld)var2).getHandle();
         return (Boolean)var3.U().a(GameRules.V);
      } else if (var1 == GameRuleType.DO_DAYLIGHT_CYCLE) {
         var3 = ((CraftWorld)var2).getHandle();
         return (Boolean)var3.U().a(GameRules.a);
      } else {
         return null;
      }
   }

   public void SetIntegerGameRule(GameRuleType var1, World var2, Integer var3) {
      WorldServer var4;
      if (var1 == GameRuleType.SNOW_ACCUMULATION_HEIGHT) {
         var4 = ((CraftWorld)var2).getHandle();
         var4.U().set(GameRules.C, var3, var4);
      } else if (var1 == GameRuleType.PLAYER_SLEEPING_PERCENTAGE) {
         var4 = ((CraftWorld)var2).getHandle();
         var4.U().set(GameRules.K, var3, var4);
      }

   }

   public void SetBooleanGameRule(GameRuleType var1, World var2, Boolean var3) {
      WorldServer var4;
      if (var1 == GameRuleType.DO_MOB_SPAWNING) {
         var4 = ((CraftWorld)var2).getHandle();
         var4.U().set(GameRules.V, var3, var4);
      } else if (var1 == GameRuleType.DO_DAYLIGHT_CYCLE) {
         var4 = ((CraftWorld)var2).getHandle();
         var4.U().set(GameRules.a, var3, var4);
      }

   }
}
