package es.outlook.adriansrj.spigui;

import es.outlook.adriansrj.spigui.menu.SGMenu;
import es.outlook.adriansrj.spigui.menu.SGMenuListener;
import es.outlook.adriansrj.spigui.menu.SGOpenMenu;
import es.outlook.adriansrj.spigui.toolbar.SGDefaultToolbarBuilder;
import es.outlook.adriansrj.spigui.toolbar.SGToolbarBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class SpiGUI {
   private final JavaPlugin plugin;
   private boolean blockDefaultInteractions = true;
   private boolean enableAutomaticPagination = true;
   private SGToolbarBuilder defaultToolbarBuilder = new SGDefaultToolbarBuilder();

   public SpiGUI(JavaPlugin var1) {
      this.plugin = var1;
      var1.getServer().getPluginManager().registerEvents(new SGMenuListener(var1, this), var1);
   }

   public SGMenu create(String var1, int var2) {
      return this.create(var1, var2, (String)null);
   }

   public SGMenu create(String var1, int var2, String var3) {
      return new SGMenu(this.plugin, this, var1, var2, var3);
   }

   public void setBlockDefaultInteractions(boolean var1) {
      this.blockDefaultInteractions = var1;
   }

   public boolean areDefaultInteractionsBlocked() {
      return this.blockDefaultInteractions;
   }

   public void setEnableAutomaticPagination(boolean var1) {
      this.enableAutomaticPagination = var1;
   }

   public boolean isAutomaticPaginationEnabled() {
      return this.enableAutomaticPagination;
   }

   public void setDefaultToolbarBuilder(SGToolbarBuilder var1) {
      this.defaultToolbarBuilder = var1;
   }

   public SGToolbarBuilder getDefaultToolbarBuilder() {
      return this.defaultToolbarBuilder;
   }

   public List<SGOpenMenu> findOpenWithTag(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.plugin.getServer().getOnlinePlayers().iterator();

      while(var3.hasNext()) {
         Player var4 = (Player)var3.next();
         if (var4.getOpenInventory().getTopInventory() != null) {
            Inventory var5 = var4.getOpenInventory().getTopInventory();
            if (var5.getHolder() != null && var5.getHolder() instanceof SGMenu) {
               SGMenu var6 = (SGMenu)var5.getHolder();
               if (Objects.equals(var6.getTag(), var1)) {
                  var2.add(new SGOpenMenu(var6, var4));
               }
            }
         }
      }

      return var2;
   }
}
