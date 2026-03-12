package me.gypopo.economyshopgui.objects;

import java.util.UUID;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.events.MenuHandler;
import org.bukkit.entity.Player;

public class User {
   private final UUID uuid;
   private boolean bedrock;
   private boolean pr;
   private boolean openNewGUI;

   public User(UUID uuid) {
      this.uuid = uuid;
   }

   public User(Player player) {
      this.uuid = player.getUniqueId();
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public boolean isBedrock() {
      return this.bedrock;
   }

   public boolean isPr() {
      return this.pr;
   }

   public void setBedrock(boolean bedrock) {
      this.bedrock = bedrock;
   }

   public void setPr(boolean pr) {
      this.pr = pr;
   }

   public void setOpenNewGUI(boolean openNewGUI) {
      this.openNewGUI = openNewGUI;
      if (MenuHandler.backOnClose && openNewGUI) {
         EconomyShopGUI.getInstance().runTaskLater(() -> {
            this.setOpenNewGUI(false);
         }, 1L);
      }

   }

   public boolean isOpenNewGUI() {
      return this.openNewGUI;
   }
}
