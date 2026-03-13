package me.ag4.playershop.hooks;

import java.util.Objects;
import me.ag4.playershop.Utils;
import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.files.Lang;
import me.ag4.playershop.objects.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.CustomForm.Builder;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class FloodgateAPI {
   public static void openSetPriceMenu(Player player, Shop shop) {
      FloodgatePlayer pePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
      if (pePlayer != null) {
         openForm(player, shop);
      } else {
         PlayerShopAPI.openSign(player, shop);
      }

   }

   public static void openForm(Player player, Shop shop) {
      try {
         CustomForm form = (CustomForm)((Builder)((Builder)CustomForm.builder().title(Lang.Form_Title.toString())).input(Lang.Form_Input_1.toString(), Lang.Form_Input_2.toString()).validResultHandler((customFormResponse) -> {
            try {
               double price = Double.parseDouble((String)Objects.requireNonNull(customFormResponse.asInput(0)));
               if (PlayerShopAPI.checkPrice(price)) {
                  if (price >= 0.01D) {
                     shop.setPrice(price);
                     player.sendMessage(Lang.Price_Set.toString().replace("{price}", Utils.formatPrice(price)));
                     player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                  } else {
                     player.sendMessage(Lang.Error_Price_0.toString());
                  }
               } else {
                  player.sendMessage(Lang.Error_Price_Enter.toString());
               }
            } catch (NumberFormatException var5) {
               player.sendMessage(Lang.Error_Price_Enter.toString());
            }

         })).build();
         FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
      } catch (Exception var3) {
         Bukkit.getConsoleSender().sendMessage(Utils.hex("&cFloodgate not Found!"));
      }

   }
}
