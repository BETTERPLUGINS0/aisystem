package emanondev.itemedit.command.itemedit;

import emanondev.itemedit.Util;
import emanondev.itemedit.command.ItemEditCommand;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jetbrains.annotations.NotNull;

public class ColorOld extends SubCmd {
   private final String leatherPerm = this.getPermission() + ".leather";
   private final String starsPerm = this.getPermission() + ".firework_star";

   public ColorOld(ItemEditCommand cmd) {
      super("color", cmd, true, true);
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);
      if (item.getItemMeta() instanceof LeatherArmorMeta) {
         if (!sender.hasPermission(this.leatherPerm)) {
            this.getCommand().sendPermissionLackMessage(this.leatherPerm, sender);
         } else {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta)ItemUtils.getMeta(item);

            try {
               if (args.length != 4) {
                  throw new IllegalArgumentException("Wrong param number");
               }

               Color color = Color.fromRGB(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
               leatherMeta.setColor(color);
               item.setItemMeta(leatherMeta);
               this.updateView(p);
            } catch (Exception var11) {
               this.onFail(p, alias);
            }

         }
      } else {
         ItemMeta rawMeta = ItemUtils.getMeta(item);
         if (!(rawMeta instanceof FireworkEffectMeta)) {
            Util.sendMessage(p, (String)this.getLanguageString("wrong-type", (String)null, sender, new String[0]));
         } else if (!sender.hasPermission(this.starsPerm)) {
            this.getCommand().sendPermissionLackMessage(this.starsPerm, sender);
         } else {
            FireworkEffectMeta starMeta = (FireworkEffectMeta)rawMeta;

            try {
               if (args.length != 4) {
                  throw new IllegalArgumentException("Wrong param number");
               }

               Color color = Color.fromRGB(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
               FireworkEffect oldEffect = starMeta.getEffect();
               Builder newEffect = FireworkEffect.builder().flicker(oldEffect != null && oldEffect.hasFlicker()).trail(oldEffect != null && oldEffect.hasTrail()).withColor(color);
               if (oldEffect != null && oldEffect.getFadeColors() != null) {
                  newEffect.withFade(oldEffect.getFadeColors());
               }

               starMeta.setEffect(newEffect.build());
               item.setItemMeta(starMeta);
               this.updateView(p);
            } catch (Exception var12) {
               this.onFail(p, alias);
            }

         }
      }
   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      return Collections.emptyList();
   }
}
