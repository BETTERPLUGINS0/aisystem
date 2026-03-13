package com.nisovin.shopkeepers.commands.util;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.admin.AdminShopkeeper;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.commands.lib.util.ObjectMatcher;
import com.nisovin.shopkeepers.container.ShopContainers;
import com.nisovin.shopkeepers.container.protection.ProtectedContainers;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ShopkeeperArgumentUtils {
   private static final int SHOPKEEPER_TARGET_RANGE = 10;

   public static ShopkeeperArgumentUtils.TargetShopkeepersResult findTargetedShopkeepers(Player player, ShopkeeperArgumentUtils.TargetShopkeeperFilter filter) {
      Validate.notNull(filter, (String)"filter is null");
      Location playerLoc = player.getEyeLocation();
      World world = (World)Unsafe.assertNonNull(playerLoc.getWorld());
      Vector viewDirection = playerLoc.getDirection();
      RayTraceResult rayTraceResult = world.rayTrace(playerLoc, viewDirection, 10.0D, FluidCollisionMode.NEVER, false, 0.0D, (entity) -> {
         return !entity.isDead() && !entity.equals(player);
      });
      if (rayTraceResult != null) {
         Block targetBlock = rayTraceResult.getHitBlock();
         if (targetBlock != null) {
            ShopkeeperArgumentUtils.TargetShopkeepersResult result = getTargetedShopkeeperByBlock(targetBlock, filter);
            if (result != null) {
               return result;
            }

            result = getTargetedShopkeepersByContainer(targetBlock, filter);
            if (result != null) {
               return result;
            }
         } else {
            Entity targetEntity = (Entity)Unsafe.assertNonNull(rayTraceResult.getHitEntity());
            ShopkeeperArgumentUtils.TargetShopkeepersResult result = getTargetedShopkeeperByEntity(targetEntity, filter);
            if (result != null) {
               return result;
            }
         }
      }

      return new ShopkeeperArgumentUtils.TargetShopkeepersResult(filter.getNoTargetErrorMsg());
   }

   @Nullable
   private static ShopkeeperArgumentUtils.TargetShopkeepersResult getTargetedShopkeeperByBlock(Block targetBlock, ShopkeeperArgumentUtils.TargetShopkeeperFilter filter) {
      Shopkeeper shopkeeper = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByBlock(targetBlock);
      if (shopkeeper == null) {
         return null;
      } else {
         return !filter.test(shopkeeper) ? new ShopkeeperArgumentUtils.TargetShopkeepersResult(filter.getInvalidTargetErrorMsg(shopkeeper)) : new ShopkeeperArgumentUtils.TargetShopkeepersResult(Collections.singletonList(shopkeeper));
      }
   }

   @Nullable
   private static ShopkeeperArgumentUtils.TargetShopkeepersResult getTargetedShopkeepersByContainer(Block targetBlock, ShopkeeperArgumentUtils.TargetShopkeeperFilter filter) {
      if (!ShopContainers.isSupportedContainer(targetBlock.getType())) {
         return null;
      } else {
         ProtectedContainers containerProtections = SKShopkeepersPlugin.getInstance().getProtectedContainers();
         List<? extends PlayerShopkeeper> shopsUsingContainer = containerProtections.getShopkeepersUsingContainer(targetBlock);
         if (shopsUsingContainer.isEmpty()) {
            return new ShopkeeperArgumentUtils.TargetShopkeepersResult(Messages.unusedContainer);
         } else {
            List<Shopkeeper> acceptedShops = new ArrayList();
            Iterator var5 = shopsUsingContainer.iterator();

            while(var5.hasNext()) {
               Shopkeeper shopUsingContainer = (Shopkeeper)var5.next();
               if (filter.test(shopUsingContainer)) {
                  acceptedShops.add(shopUsingContainer);
               }
            }

            if (acceptedShops.isEmpty()) {
               return new ShopkeeperArgumentUtils.TargetShopkeepersResult(filter.getInvalidTargetErrorMsg((Shopkeeper)shopsUsingContainer.get(0)));
            } else {
               return new ShopkeeperArgumentUtils.TargetShopkeepersResult(acceptedShops);
            }
         }
      }
   }

   @Nullable
   private static ShopkeeperArgumentUtils.TargetShopkeepersResult getTargetedShopkeeperByEntity(Entity targetEntity, ShopkeeperArgumentUtils.TargetShopkeeperFilter filter) {
      Shopkeeper shopkeeper = ShopkeepersAPI.getShopkeeperRegistry().getShopkeeperByEntity(targetEntity);
      if (shopkeeper == null) {
         return new ShopkeeperArgumentUtils.TargetShopkeepersResult(Messages.targetEntityIsNoShop);
      } else {
         return !filter.test(shopkeeper) ? new ShopkeeperArgumentUtils.TargetShopkeepersResult(filter.getInvalidTargetErrorMsg(shopkeeper)) : new ShopkeeperArgumentUtils.TargetShopkeepersResult(Collections.singletonList(shopkeeper));
      }
   }

   public static List<? extends Shopkeeper> getTargetedShopkeepers(Player player, ShopkeeperArgumentUtils.TargetShopkeeperFilter shopkeeperFilter) {
      ShopkeeperArgumentUtils.TargetShopkeepersResult result = findTargetedShopkeepers(player, shopkeeperFilter);
      if (result.isSuccess()) {
         assert !result.getShopkeepers().isEmpty();

         return result.getShopkeepers();
      } else {
         return Collections.emptyList();
      }
   }

   public static List<? extends Shopkeeper> getTargetedShopkeepers(CommandSender sender, ShopkeeperArgumentUtils.TargetShopkeeperFilter shopkeeperFilter) {
      return sender instanceof Player ? getTargetedShopkeepers((Player)sender, shopkeeperFilter) : Collections.emptyList();
   }

   public static ShopkeeperArgumentUtils.OwnedPlayerShopsResult getOwnedPlayerShops(@Nullable UUID targetPlayerUUID, @Nullable String targetPlayerName) {
      Validate.isTrue(targetPlayerUUID != null || targetPlayerName != null, "targetPlayerUUID and targetPlayerName are both null");
      String actualTargetPlayerName = targetPlayerName;
      Map<UUID, String> matchingShopOwners = new LinkedHashMap();
      List<PlayerShopkeeper> shops = new ArrayList();
      Iterator var5 = ShopkeepersAPI.getShopkeeperRegistry().getAllShopkeepers().iterator();

      while(var5.hasNext()) {
         Shopkeeper shopkeeper = (Shopkeeper)var5.next();
         if (shopkeeper instanceof PlayerShopkeeper) {
            PlayerShopkeeper playerShop = (PlayerShopkeeper)shopkeeper;
            UUID shopOwnerUUID = playerShop.getOwnerUUID();
            String shopOwnerName = playerShop.getOwnerName();
            if (targetPlayerUUID != null) {
               if (targetPlayerUUID.equals(shopOwnerUUID)) {
                  shops.add(playerShop);
                  actualTargetPlayerName = shopOwnerName;
               }
            } else {
               assert targetPlayerName != null;

               if (shopOwnerName.equalsIgnoreCase(targetPlayerName)) {
                  shops.add(playerShop);
                  actualTargetPlayerName = shopOwnerName;
                  matchingShopOwners.putIfAbsent(shopOwnerUUID, shopOwnerName);
               }
            }
         }
      }

      return new ShopkeeperArgumentUtils.OwnedPlayerShopsResult(targetPlayerUUID, actualTargetPlayerName, matchingShopOwners, shops);
   }

   private ShopkeeperArgumentUtils() {
   }

   public interface TargetShopkeeperFilter extends Predicate<Shopkeeper> {
      ShopkeeperArgumentUtils.TargetShopkeeperFilter ANY = new ShopkeeperArgumentUtils.TargetShopkeeperFilter() {
         public boolean test(@Nullable Shopkeeper shopkeeper) {
            return true;
         }

         public Text getNoTargetErrorMsg() {
            return Messages.mustTargetShop;
         }

         public Text getInvalidTargetErrorMsg(Shopkeeper shopkeeper) {
            return Text.EMPTY;
         }
      };
      ShopkeeperArgumentUtils.TargetShopkeeperFilter ADMIN = new ShopkeeperArgumentUtils.TargetShopkeeperFilter() {
         public boolean test(@Nullable Shopkeeper shopkeeper) {
            return shopkeeper instanceof AdminShopkeeper;
         }

         public Text getNoTargetErrorMsg() {
            return Messages.mustTargetAdminShop;
         }

         public Text getInvalidTargetErrorMsg(Shopkeeper shopkeeper) {
            return Messages.targetShopIsNoAdminShop;
         }
      };
      ShopkeeperArgumentUtils.TargetShopkeeperFilter PLAYER = new ShopkeeperArgumentUtils.TargetShopkeeperFilter() {
         public boolean test(@Nullable Shopkeeper shopkeeper) {
            return shopkeeper instanceof PlayerShopkeeper;
         }

         public Text getNoTargetErrorMsg() {
            return Messages.mustTargetPlayerShop;
         }

         public Text getInvalidTargetErrorMsg(Shopkeeper shopkeeper) {
            return Messages.targetShopIsNoPlayerShop;
         }
      };

      Text getNoTargetErrorMsg();

      Text getInvalidTargetErrorMsg(Shopkeeper var1);
   }

   public static final class TargetShopkeepersResult {
      private final List<? extends Shopkeeper> shopkeepers;
      @Nullable
      private final Text errorMessage;

      private TargetShopkeepersResult(List<? extends Shopkeeper> shopkeepers) {
         Validate.notNull(shopkeepers, (String)"shopkeepers is null");
         Validate.isTrue(!shopkeepers.isEmpty(), "shopkeepers is empty");
         Validate.noNullElements(shopkeepers, (String)"shopkeepers contains null");
         this.shopkeepers = shopkeepers;
         this.errorMessage = null;
      }

      private TargetShopkeepersResult(Text errorMessage) {
         Validate.notNull(errorMessage, (String)"errorMessage is null");
         Validate.isTrue(!errorMessage.isPlainTextEmpty());
         this.errorMessage = errorMessage;
         this.shopkeepers = Collections.emptyList();
      }

      public boolean isSuccess() {
         return this.errorMessage == null;
      }

      public List<? extends Shopkeeper> getShopkeepers() {
         return this.shopkeepers;
      }

      @Nullable
      public Text getErrorMessage() {
         return this.errorMessage;
      }
   }

   public static class OwnedPlayerShopsResult {
      @Nullable
      private final UUID playerUUID;
      @Nullable
      private final String playerName;
      private final Map<? extends UUID, ? extends String> matchingShopOwners;
      private final List<? extends PlayerShopkeeper> shops;

      public OwnedPlayerShopsResult(@Nullable UUID playerUUID, @Nullable String playerName, Map<? extends UUID, ? extends String> matchingShopOwners, List<? extends PlayerShopkeeper> shops) {
         Validate.isTrue(playerUUID != null || playerName != null, "playerUUID and playerName are both null");
         Validate.notNull(matchingShopOwners, (String)"matchingShopOwners is null");
         Validate.notNull(shops, (String)"shops is null");
         this.playerUUID = playerUUID;
         this.playerName = playerName;
         this.matchingShopOwners = matchingShopOwners;
         this.shops = shops;
      }

      @Nullable
      public UUID getPlayerUUID() {
         return this.playerUUID;
      }

      @Nullable
      public String getPlayerName() {
         return this.playerName;
      }

      public Map<? extends UUID, ? extends String> getMatchingShopOwners() {
         return this.matchingShopOwners;
      }

      public List<? extends PlayerShopkeeper> getShops() {
         return this.shops;
      }
   }

   public static final class ShopkeeperNameMatchers {
      public static final ObjectMatcher<Shopkeeper> DEFAULT = new ObjectMatcher<Shopkeeper>() {
         public Stream<? extends Shopkeeper> match(String input) {
            return StringUtils.isEmpty(input) ? Stream.empty() : ShopkeepersAPI.getShopkeeperRegistry().getShopkeepersByName(input);
         }
      };

      private ShopkeeperNameMatchers() {
      }
   }
}
