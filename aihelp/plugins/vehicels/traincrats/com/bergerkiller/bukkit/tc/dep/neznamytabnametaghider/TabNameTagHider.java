package com.bergerkiller.bukkit.tc.dep.neznamytabnametaghider;

import org.bukkit.entity.Player;

public interface TabNameTagHider {
   TabNameTagHider NONE = (player) -> {
      return TabNameTagHider.TabPlayerNameTagHider.NONE;
   };

   TabNameTagHider.TabPlayerNameTagHider get(Player var1);

   public interface TabPlayerNameTagHider {
      TabNameTagHider.TabPlayerNameTagHider NONE = new TabNameTagHider.TabPlayerNameTagHider() {
         public void hide() {
         }

         public void show() {
         }
      };

      void hide();

      void show();
   }
}
