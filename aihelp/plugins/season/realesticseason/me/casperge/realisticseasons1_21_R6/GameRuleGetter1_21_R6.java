package me.casperge.realisticseasons1_21_R6;

import me.casperge.enums.GameRuleType;
import me.casperge.interfaces.GameRuleGetter;
import org.bukkit.GameRule;
import org.bukkit.World;

public class GameRuleGetter1_21_R6 implements GameRuleGetter {
   public Integer GetIntegerGameRule(GameRuleType var1, World var2) {
      if (var1 == GameRuleType.SNOW_ACCUMULATION_HEIGHT) {
         return Integer.valueOf(var2.getGameRuleValue("snowAccumulationHeight"));
      } else {
         return var1 == GameRuleType.PLAYER_SLEEPING_PERCENTAGE ? Integer.valueOf(var2.getGameRuleValue("playersSleepingPercentage")) : null;
      }
   }

   public Boolean GetBooleanGameRule(GameRuleType var1, World var2) {
      if (var1 == GameRuleType.DO_MOB_SPAWNING) {
         return (Boolean)var2.getGameRuleValue(GameRule.DO_MOB_SPAWNING);
      } else {
         return var1 == GameRuleType.DO_DAYLIGHT_CYCLE ? (Boolean)var2.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE) : null;
      }
   }

   public void SetIntegerGameRule(GameRuleType var1, World var2, Integer var3) {
      if (var1 == GameRuleType.SNOW_ACCUMULATION_HEIGHT) {
         var2.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, var3);
      } else if (var1 == GameRuleType.PLAYER_SLEEPING_PERCENTAGE) {
         var2.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, var3);
      }

   }

   public void SetBooleanGameRule(GameRuleType var1, World var2, Boolean var3) {
      if (var1 == GameRuleType.DO_MOB_SPAWNING) {
         var2.setGameRule(GameRule.DO_MOB_SPAWNING, var3);
      } else if (var1 == GameRuleType.DO_DAYLIGHT_CYCLE) {
         var2.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, var3);
      }

   }
}
