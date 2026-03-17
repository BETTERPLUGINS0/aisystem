package com.bergerkiller.bukkit.tc.dep.neznamytabnametaghider;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.nametag.NameTagManager;
import org.bukkit.entity.Player;

public class TabNameTagHiderImpl_4_0_3 implements TabNameTagHider.TabPlayerNameTagHider {
   private final TabPlayer player;
   private final NameTagManager nametagManager;
   private boolean needToRestoreNametag = false;

   private TabNameTagHiderImpl_4_0_3(TabAPI tab, Player player) {
      this.player = tab.getPlayer(player.getUniqueId());
      this.nametagManager = tab.getNameTagManager();
   }

   public void hide() {
      if (!this.nametagManager.hasHiddenNameTag(this.player)) {
         this.nametagManager.hideNameTag(this.player);
         this.needToRestoreNametag = true;
      }

   }

   public void show() {
      if (this.needToRestoreNametag) {
         this.needToRestoreNametag = false;
         this.nametagManager.showNameTag(this.player);
      }

   }

   public static TabNameTagHider create() {
      TabAPI tab = TabAPI.getInstance();
      tab.getNameTagManager();
      return (player) -> {
         return new TabNameTagHiderImpl_4_0_3(tab, player);
      };
   }
}
