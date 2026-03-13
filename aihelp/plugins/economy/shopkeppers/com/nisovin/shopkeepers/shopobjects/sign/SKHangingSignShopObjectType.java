package com.nisovin.shopkeepers.shopobjects.sign;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.sign.HangingSignShopObjectType;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.block.base.BaseBlockShopObjectType;
import com.nisovin.shopkeepers.shopobjects.block.base.BaseBlockShops;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SKHangingSignShopObjectType extends BaseBlockShopObjectType<SKHangingSignShopObject> implements HangingSignShopObjectType<SKHangingSignShopObject> {
   private final BaseBlockShops blockShops;

   public SKHangingSignShopObjectType(BaseBlockShops blockShops) {
      super("hanging-sign", Collections.emptyList(), "shopkeeper.hanging-sign", SKHangingSignShopObject.class);
      this.blockShops = blockShops;
   }

   public boolean isEnabled() {
      return Settings.enableHangingSignShops;
   }

   public String getDisplayName() {
      return Messages.shopObjectTypeHangingSign;
   }

   public boolean mustBeSpawned() {
      return true;
   }

   public boolean validateSpawnLocation(@Nullable Player creator, @Nullable Location spawnLocation, @Nullable BlockFace attachedBlockFace) {
      if (!super.validateSpawnLocation(creator, spawnLocation, attachedBlockFace)) {
         return false;
      } else {
         assert spawnLocation != null;

         Block spawnBlock = spawnLocation.getBlock();
         if (!spawnBlock.isEmpty()) {
            if (creator != null) {
               TextUtils.sendMessage(creator, (Text)Messages.spawnBlockNotEmpty);
            }

            return false;
         } else if (attachedBlockFace == BlockFace.UP || attachedBlockFace != null && !BlockFaceUtils.isBlockSide(attachedBlockFace)) {
            if (creator != null) {
               TextUtils.sendMessage(creator, (Text)Messages.invalidSpawnBlockFace);
            }

            return false;
         } else {
            return true;
         }
      }
   }

   public SKHangingSignShopObject createObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      return new SKHangingSignShopObject(this.blockShops, shopkeeper, creationData);
   }
}
