package emanondev.itemedit.command.itemedit;

import com.google.common.collect.Multimap;
import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class HideAll extends SubCmd {
   public HideAll(ItemEditCommand cmd) {
      super("hideall", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length != 1) {
            throw new IllegalArgumentException("Wrong param number");
         }

         ItemMeta itemMeta = ItemUtils.getMeta(item);
         this.handleFlagChange(item, itemMeta);
         itemMeta.addItemFlags(ItemFlag.values());
         item.setItemMeta(itemMeta);
         this.updateView(p);
      } catch (Exception var7) {
         this.onFail(p, alias);
      }

   }

   private void handleFlagChange(ItemStack item, ItemMeta meta) {
      if (VersionUtils.hasPaperAPI() && VersionUtils.isVersionAfter(1, 20, 5) && ItemEdit.get().getConfig().loadBoolean("itemedit.paper_hide_fix", true)) {
         if (meta.getAttributeModifiers() == null) {
            EquipmentSlot[] var3 = EquipmentSlot.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               EquipmentSlot slot = var3[var5];
               Multimap var10000 = item.getType().getDefaultAttributeModifiers(slot);
               Objects.requireNonNull(meta);
               var10000.forEach(meta::addAttributeModifier);
            }

         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return Collections.emptyList();
   }
}
