package com.nisovin.shopkeepers.shopobjects.sign;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.shopobjects.ShopObject;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.util.bukkit.SignUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.block.Sign;

final class SignShops {
   private static final String[] TEMP_SIGN_LINES = new String[4];

   static void updateShopSign(Sign sign, Shopkeeper shopkeeper) {
      if (shopkeeper instanceof PlayerShopkeeper) {
         updatePlayerShopSign(sign, (PlayerShopkeeper)shopkeeper);
      } else {
         assert shopkeeper instanceof AdminShopkeeper;

         updateAdminShopSign(sign, (AdminShopkeeper)shopkeeper);
      }

   }

   private static void updatePlayerShopSign(Sign sign, PlayerShopkeeper shop) {
      ShopObject shopObject = shop.getShopObject();
      HashMap<String, Object> arguments = new HashMap();
      arguments.put("shopName", Unsafe.assertNonNull(shopObject.prepareName(shop.getName())));
      arguments.put("owner", shop.getOwnerName());
      TEMP_SIGN_LINES[0] = StringUtils.replaceArguments((String)Messages.playerSignShopLine1, (Map)arguments);
      TEMP_SIGN_LINES[1] = StringUtils.replaceArguments((String)Messages.playerSignShopLine2, (Map)arguments);
      TEMP_SIGN_LINES[2] = StringUtils.replaceArguments((String)Messages.playerSignShopLine3, (Map)arguments);
      TEMP_SIGN_LINES[3] = StringUtils.replaceArguments((String)Messages.playerSignShopLine4, (Map)arguments);
      SignUtils.setBothSidesText(sign, TEMP_SIGN_LINES);
   }

   private static void updateAdminShopSign(Sign sign, AdminShopkeeper shop) {
      ShopObject shopObject = shop.getShopObject();
      HashMap<String, Object> arguments = new HashMap();
      arguments.put("shopName", Unsafe.assertNonNull(shopObject.prepareName(shop.getName())));
      TEMP_SIGN_LINES[0] = StringUtils.replaceArguments((String)Messages.adminSignShopLine1, (Map)arguments);
      TEMP_SIGN_LINES[1] = StringUtils.replaceArguments((String)Messages.adminSignShopLine2, (Map)arguments);
      TEMP_SIGN_LINES[2] = StringUtils.replaceArguments((String)Messages.adminSignShopLine3, (Map)arguments);
      TEMP_SIGN_LINES[3] = StringUtils.replaceArguments((String)Messages.adminSignShopLine4, (Map)arguments);
      SignUtils.setBothSidesText(sign, TEMP_SIGN_LINES);
   }

   private SignShops() {
   }
}
