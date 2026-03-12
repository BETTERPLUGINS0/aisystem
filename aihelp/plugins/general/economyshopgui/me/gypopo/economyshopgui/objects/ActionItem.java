package me.gypopo.economyshopgui.objects;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.providers.requirements.RequirementType;
import me.gypopo.economyshopgui.providers.requirements.Requirements;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.SkullUtil;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ActionItem implements DisplayItem {
   private static final String[] actionPlaceholders = new String[]{"%player_name%", "%player_displayname%", "%player_level%", "%player_balance%", "%page%", "%pages%"};
   private ItemStack actionItem;
   private ActionItem.Action action;
   private boolean error = false;
   private boolean clickCommand;
   private Requirements requirements;
   private DisplayItem.DynamicLore lore;
   private final List<Integer> placeholders = new ArrayList();
   private String displayname;
   private final String itemPath;
   public final String section;
   public final String itemLoc;

   public ActionItem(String section, String itemLoc) {
      this.section = section;
      this.itemLoc = itemLoc;
      this.itemPath = this.section + "." + this.itemLoc;
      ConfigurationSection conf = ConfigManager.getShop(this.section).getConfigurationSection("pages." + this.itemLoc);

      try {
         this.requirements = EconomyShopGUI.getInstance().getRequirementManager().load(this.section, "pages." + this.itemLoc);

         try {
            this.action = ActionItem.Action.getFromString(conf.getString("action", "NONE"));
         } catch (IllegalArgumentException var5) {
            throw new ItemLoadException(Lang.INVALID_ITEM_ACTION.get());
         }

         this.actionItem = EconomyShopGUI.getInstance().createItem.loadActionItem(this);
         if (this.actionItem == null) {
            this.actionItem = EconomyShopGUI.getInstance().createItem.getInvalidShopItem(this.section, this.itemLoc);
            this.error = true;
         } else if (this.actionItem.getType() != Material.AIR) {
            this.displayname = EconomyShopGUI.getInstance().useItemName ? (this.actionItem.getItemMeta().hasDisplayName() ? EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(this.actionItem.getItemMeta()) : EconomyShopGUI.getInstance().getMaterialName(this.actionItem.getType().name())) : EconomyShopGUI.getInstance().getMaterialName(this.actionItem.getType().name());
            List<String> list = this.actionItem.hasItemMeta() ? EconomyShopGUI.getInstance().getMetaUtils().getRawLore(this.actionItem.getItemMeta()) : null;
            this.lore = this.getDynamicLore(EconomyShopGUI.getInstance(), list);
         }
      } catch (ItemLoadException var6) {
         this.actionItem = EconomyShopGUI.getInstance().createItem.getInvalidShopItem(this.section, this.itemLoc);
         this.error = true;
         SendMessage.warnMessage(var6.getMessage());
         SendMessage.errorShops(this.section, this.itemLoc);
      } catch (Exception var7) {
         this.actionItem = EconomyShopGUI.getInstance().createItem.getInvalidShopItem(this.section, this.itemLoc);
         this.error = true;
         SendMessage.warnMessage("A unknown error occurred while loading item: " + this.itemPath);
         var7.printStackTrace();
      }

   }

   public ActionItem(ActionItem actionItem) {
      this.itemPath = actionItem.itemPath;
      this.section = actionItem.section;
      this.itemLoc = actionItem.itemLoc;
      this.actionItem = actionItem.actionItem;
      this.action = actionItem.action;
      this.displayname = actionItem.displayname;
      this.clickCommand = actionItem.clickCommand;
      this.error = actionItem.error;
      this.lore = actionItem.lore;
      this.requirements = actionItem.requirements;
   }

   public DisplayItem.DynamicLore getLore() {
      return this.lore;
   }

   public String section() {
      return this.section;
   }

   public String itemLoc() {
      return this.itemLoc;
   }

   public String getItemPath() {
      return this.itemPath;
   }

   public ItemStack getShopItem() {
      return this.actionItem;
   }

   public ActionItem.Action getAction() {
      return this.action;
   }

   public boolean hasPlaceholders() {
      return !this.placeholders.isEmpty();
   }

   public List<Integer> getPlaceholders() {
      return this.placeholders;
   }

   public boolean meetsRequirements(Player p, boolean silent) {
      if (this.requirements == null) {
         return true;
      } else {
         Iterator var3 = this.requirements.iterator();

         ItemRequirement requirement;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            requirement = (ItemRequirement)var3.next();
         } while(requirement.isMet(p));

         if (!silent) {
            requirement.sendNotMetMessage(p);
         }

         return false;
      }
   }

   public void addItemRequirement(ItemRequirement requirement) {
      this.requirements.add(requirement);
   }

   public void addItemRequirements(Collection<? extends ItemRequirement> requirements) {
      this.requirements.addAll(requirements);
   }

   public Requirements getRequirements() {
      return this.requirements;
   }

   public boolean hasItemRequirement(RequirementType type) {
      return this.requirements.hasType(type);
   }

   public void removeItemRequirement(ItemRequirement requirement) {
      this.requirements.remove(requirement);
   }

   public void removeItemRequirements(Collection<? extends ItemRequirement> requirements) {
      this.requirements.removeAll(requirements);
   }

   public String getDisplayname() {
      return this.displayname;
   }

   public boolean hasItemError() {
      return this.error;
   }

   public void updateSkullTexture(GameProfile profile, boolean itg) {
      SkullMeta meta = (SkullMeta)this.actionItem.getItemMeta();
      SkullUtil.applySkullProfile(profile, meta);
      this.actionItem.setItemMeta(meta);
   }

   private DisplayItem.DynamicLore getDynamicLore(EconomyShopGUI plugin, List<String> lore) {
      return (DisplayItem.DynamicLore)(plugin.paperMeta ? new ActionItem.DynamicCompLore(lore) : new ActionItem.DynamicBukkitLore(plugin, lore));
   }

   public static enum Action {
      BACK,
      CLOSE,
      PAGE_BACK,
      PAGE_NEXT,
      SEARCH;

      public static ActionItem.Action getFromString(String s) throws IllegalArgumentException {
         if (s == null) {
            throw new IllegalArgumentException();
         } else {
            return valueOf(s.toUpperCase(Locale.ENGLISH).replace(" ", "_"));
         }
      }

      // $FF: synthetic method
      private static ActionItem.Action[] $values() {
         return new ActionItem.Action[]{BACK, CLOSE, PAGE_BACK, PAGE_NEXT, SEARCH};
      }
   }

   public final class DynamicCompLore implements DisplayItem.DynamicLore {
      private final String[] lore;
      private final String[] compLore;

      public DynamicCompLore(List<String> param2) {
         this.lore = list != null && !list.isEmpty() ? (String[])list.toArray(new String[0]) : new String[0];
         this.compLore = (String[])Arrays.stream(this.lore).map(ChatUtil::getGsonComponent).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var4 = this.compLore;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            Stream var10000 = Arrays.stream(ActionItem.actionPlaceholders);
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               ActionItem.this.placeholders.add(i);
            }

            ++i;
         }

      }

      public String[] get(boolean fast, boolean pr) {
         return (String[])(fast ? this.compLore : this.lore).clone();
      }
   }

   private final class DynamicBukkitLore implements DisplayItem.DynamicLore {
      private final String[] lore;
      private final String[] fastLore;

      public DynamicBukkitLore(EconomyShopGUI param2, List<String> param3) {
         this.lore = list != null && !list.isEmpty() ? (String[])list.stream().map(ChatUtil::formatColors).toArray((x$0) -> {
            return new String[x$0];
         }) : new String[0];
         this.fastLore = (String[])Arrays.stream(this.lore).map((sx) -> {
            return plugin.versionHandler.toNBT(sx);
         }).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var5 = this.lore;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            Stream var10000 = Arrays.stream(ActionItem.actionPlaceholders);
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               ActionItem.this.placeholders.add(i);
            }

            ++i;
         }

      }

      public String[] get(boolean fast, boolean pr) {
         return (String[])(fast && !pr ? this.fastLore : this.lore).clone();
      }
   }
}
