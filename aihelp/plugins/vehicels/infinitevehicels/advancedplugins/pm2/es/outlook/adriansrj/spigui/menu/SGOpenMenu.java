package es.outlook.adriansrj.spigui.menu;

import org.bukkit.entity.Player;

public class SGOpenMenu {
   private final SGMenu gui;
   private final Player player;

   public SGOpenMenu(SGMenu var1, Player var2) {
      this.gui = var1;
      this.player = var2;
   }

   public SGMenu getMenu() {
      return this.gui;
   }

   public Player getPlayer() {
      return this.player;
   }
}
