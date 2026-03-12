package com.nisovin.shopkeepers.naming;

import com.nisovin.shopkeepers.api.events.ShopkeeperEditedEvent;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.input.InputRequest;
import com.nisovin.shopkeepers.input.chat.ChatInput;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ShopkeeperNaming {
   private final ChatInput chatInput;

   public ShopkeeperNaming(ChatInput chatInput) {
      Validate.notNull(chatInput, (String)"chatInput is null");
      this.chatInput = chatInput;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void startNaming(Player player, Shopkeeper shopkeeper) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.chatInput.request(player, new ShopkeeperNaming.ShopkeeperNameRequest(player, shopkeeper));
   }

   public void abortNaming(Player player) {
      Validate.notNull(player, (String)"player is null");
      InputRequest<String> request = this.chatInput.getRequest(player);
      if (request instanceof ShopkeeperNaming.ShopkeeperNameRequest) {
         this.chatInput.abortRequest(player, request);
      }

   }

   public boolean requestNameChange(Player player, Shopkeeper shopkeeper, String newName) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.notNull(newName, (String)"newName is null");
      if (!shopkeeper.isValid()) {
         return false;
      } else {
         String preparedName = newName.trim();
         preparedName = TextUtils.convertHexColorsToBukkit(preparedName);
         if (!preparedName.isEmpty() && !preparedName.equals("-")) {
            if (!((AbstractShopkeeper)shopkeeper).isValidName(preparedName)) {
               Log.debug(() -> {
                  String var10000 = shopkeeper.getLogPrefix();
                  return var10000 + "Player " + player.getName() + " tried to set an invalid name: '" + preparedName + "'";
               });
               TextUtils.sendMessage(player, (Text)Messages.nameInvalid, (Object[])("name", preparedName));
               return false;
            }
         } else {
            preparedName = "";
         }

         String oldName = shopkeeper.getName();
         shopkeeper.setName(preparedName);
         String actualNewName = shopkeeper.getName();
         if (oldName.equals(actualNewName)) {
            TextUtils.sendMessage(player, (Text)Messages.nameHasNotChanged, (Object[])("name", Text.parse(actualNewName)));
            return false;
         } else {
            TextUtils.sendMessage(player, (Text)Messages.nameSet, (Object[])("name", Text.parse(actualNewName)));
            shopkeeper.abortUISessionsDelayed();
            Bukkit.getPluginManager().callEvent(new ShopkeeperEditedEvent(shopkeeper, player));
            shopkeeper.save();
            return true;
         }
      }
   }

   private class ShopkeeperNameRequest implements InputRequest<String> {
      private final Player player;
      private final Shopkeeper shopkeeper;

      ShopkeeperNameRequest(Player param2, Shopkeeper param3) {
         assert player != null && shopkeeper != null;

         this.player = player;
         this.shopkeeper = shopkeeper;
      }

      public void onInput(String message) {
         ShopkeeperNaming.this.requestNameChange(this.player, this.shopkeeper, message);
      }
   }
}
