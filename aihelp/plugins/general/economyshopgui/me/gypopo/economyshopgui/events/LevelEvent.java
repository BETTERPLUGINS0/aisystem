package me.gypopo.economyshopgui.events;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.EcoType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class LevelEvent implements Listener {
   @EventHandler
   public void onPlayerLevelChange(PlayerLevelChangeEvent e) {
      Player player = e.getPlayer();
      double levelmoney5 = ConfigManager.getConfig().getDouble("leveleventmoney5/10/15");
      double levelmoney30 = ConfigManager.getConfig().getDouble("leveleventmoney20/25/30");
      Translatable message5 = Lang.LEVEL_EVENT_MESSAGE.get().replace("%amount%", EconomyShopGUI.getInstance().formatPrice((EcoType)null, levelmoney5));
      Translatable message30 = Lang.LEVEL_EVENT_MESSAGE.get().replace("%amount%", EconomyShopGUI.getInstance().formatPrice((EcoType)null, levelmoney30));
      if (ConfigManager.getConfig().getBoolean("enable-levelevent")) {
         if (player.getLevel() == 5) {
            EconomyShopGUI.getInstance().getEcoHandler().getDefaultProvider().depositBalance(player, levelmoney5);
            SendMessage.chatToPlayer(player, message5);
         } else if (player.getLevel() == 10) {
            EconomyShopGUI.getInstance().getEcoHandler().getDefaultProvider().depositBalance(player, levelmoney5);
            SendMessage.chatToPlayer(player, message5);
         } else if (player.getLevel() == 15) {
            EconomyShopGUI.getInstance().getEcoHandler().getDefaultProvider().depositBalance(player, levelmoney5);
            SendMessage.chatToPlayer(player, message5);
         } else if (player.getLevel() == 20) {
            EconomyShopGUI.getInstance().getEcoHandler().getDefaultProvider().depositBalance(player, levelmoney30);
            SendMessage.chatToPlayer(player, message30);
         } else if (player.getLevel() == 25) {
            EconomyShopGUI.getInstance().getEcoHandler().getDefaultProvider().depositBalance(player, levelmoney30);
            SendMessage.chatToPlayer(player, message30);
         } else if (player.getLevel() == 30) {
            EconomyShopGUI.getInstance().getEcoHandler().getDefaultProvider().depositBalance(player, levelmoney30);
            SendMessage.chatToPlayer(player, message30);
         }
      }

   }
}
