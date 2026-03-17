package com.bergerkiller.bukkit.tc.dep.neznamytabnametaghider;

import com.bergerkiller.bukkit.tc.dep.softdependency.SoftDependency;
import org.bukkit.plugin.Plugin;

public class TabNameTagHiderDependency extends SoftDependency<TabNameTagHider> {
   public TabNameTagHiderDependency(Plugin owningPlugin) {
      super(owningPlugin, "TAB", TabNameTagHider.NONE);
   }

   protected TabNameTagHider initialize(Plugin plugin) throws Error, Exception {
      ClassLoader loader = plugin.getClass().getClassLoader();
      Class.forName("me.neznamy.tab.api.TabAPI", false, loader);
      boolean hasNameTagManager = false;

      try {
         Class.forName("me.neznamy.tab.api.nametag.NameTagManager", false, loader);
         hasNameTagManager = true;
      } catch (Throwable var5) {
      }

      return !hasNameTagManager ? create_3_1_4() : create_4_0_3();
   }

   private static TabNameTagHider create_4_0_3() {
      return TabNameTagHiderImpl_4_0_3.create();
   }

   private static TabNameTagHider create_3_1_4() {
      return TabNameTagHiderImpl_3_1_4.create();
   }
}
