package me.casperge.interfaces;

import me.casperge.enums.GameRuleType;
import org.bukkit.World;

public interface GameRuleGetter {
   Integer GetIntegerGameRule(GameRuleType var1, World var2);

   Boolean GetBooleanGameRule(GameRuleType var1, World var2);

   void SetIntegerGameRule(GameRuleType var1, World var2, Integer var3);

   void SetBooleanGameRule(GameRuleType var1, World var2, Boolean var3);
}
