package me.gypopo.economyshopgui.providers.requirements;

import org.bukkit.entity.Player;

public interface ItemRequirement {
   boolean isMet(Player var1);

   String[] getLore(Player var1, boolean var2, boolean var3);

   void sendNotMetMessage(Player var1);
}
