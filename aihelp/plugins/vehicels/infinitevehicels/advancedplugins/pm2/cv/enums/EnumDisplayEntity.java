package advancedplugins.pm2.cv.enums;

import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public enum EnumDisplayEntity {
   ITEM,
   BLOCK,
   TEXT;

   public static EnumDisplayEntity fromMaterial(@NotNull Material material) {
      if (!ItemStackUtil.isHead(var0) && !ItemStackUtil.isBanner(var0)) {
         if (var0 == Material.PISTON_HEAD) {
            return BLOCK;
         } else {
            return var0.isBlock() ? BLOCK : ITEM;
         }
      } else {
         return ITEM;
      }
   }

   // $FF: synthetic method
   private static EnumDisplayEntity[] $values() {
      return new EnumDisplayEntity[]{ITEM, BLOCK, TEXT};
   }
}
