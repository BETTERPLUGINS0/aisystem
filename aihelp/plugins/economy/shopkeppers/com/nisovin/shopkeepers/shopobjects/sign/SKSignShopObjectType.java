package com.nisovin.shopkeepers.shopobjects.sign;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.sign.SignShopObjectType;
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

public final class SKSignShopObjectType extends BaseBlockShopObjectType<SKSignShopObject> implements SignShopObjectType<SKSignShopObject> {
   private final BaseBlockShops blockShops;

   public SKSignShopObjectType(BaseBlockShops blockShops) {
      super("sign", Collections.emptyList(), "shopkeeper.sign", SKSignShopObject.class);
      this.blockShops = blockShops;
   }

   public boolean isEnabled() {
      return Settings.enableSignShops;
   }

   public String getDisplayName() {
      return Messages.shopObjectTypeSign;
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
         } else if (attachedBlockFace == BlockFace.DOWN || attachedBlockFace == BlockFace.UP && !Settings.enableSignPostShops || attachedBlockFace != null && !BlockFaceUtils.isBlockSide(attachedBlockFace)) {
            if (creator != null) {
               TextUtils.sendMessage(creator, (Text)Messages.invalidSpawnBlockFace);
            }

            return false;
         } else {
            return true;
         }
      }
   }

   public SKSignShopObject createObject(AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      return new SKSignShopObject(this.blockShops, shopkeeper, creationData);
   }
}
