package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.aliases.AliasSet;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.command.SubCmd;
import emanondev.itemedit.gui.Gui;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.command.itemtag.customflags.ClickMove;
import emanondev.itemtag.command.itemtag.customflags.CraftRecipeIngredient;
import emanondev.itemtag.command.itemtag.customflags.CustomFlag;
import emanondev.itemtag.command.itemtag.customflags.Enchantable;
import emanondev.itemtag.command.itemtag.customflags.EntityFood;
import emanondev.itemtag.command.itemtag.customflags.EquipmentFlag;
import emanondev.itemtag.command.itemtag.customflags.FurnaceFuel;
import emanondev.itemtag.command.itemtag.customflags.Grindable;
import emanondev.itemtag.command.itemtag.customflags.Placeable;
import emanondev.itemtag.command.itemtag.customflags.Renamable;
import emanondev.itemtag.command.itemtag.customflags.RenamableOld;
import emanondev.itemtag.command.itemtag.customflags.Smelt;
import emanondev.itemtag.command.itemtag.customflags.SmithingTable;
import emanondev.itemtag.command.itemtag.customflags.Tradeable;
import emanondev.itemtag.command.itemtag.customflags.Usable;
import emanondev.itemtag.command.itemtag.customflags.VanishCurse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Flag extends SubCmd {
   private final TreeSet<CustomFlag> flags = new TreeSet();
   private final AliasSet<CustomFlag> FLAG_ALIASES = new AliasSet<CustomFlag>("custom_flags") {
      public String getName(CustomFlag customFlag) {
         return customFlag.getId();
      }

      public Collection<CustomFlag> getValues() {
         return Flag.this.flags;
      }
   };

   public Flag(ItemTagCommand cmd) {
      super("flag", cmd, true, true);
      this.registerFlag(new Placeable(this));
      this.registerFlag(new Usable(this));
      this.registerFlag(new CraftRecipeIngredient(this));
      this.registerFlag(new Smelt(this));
      this.registerFlag(new FurnaceFuel(this));
      this.registerFlag(new Enchantable(this));
      this.registerFlag(new EntityFood(this));
      if (VersionUtils.isVersionAfter(1, 9)) {
         this.registerFlag(new Renamable(this));
      } else {
         this.registerFlag(new RenamableOld(this));
      }

      if (VersionUtils.isVersionAfter(1, 14)) {
         this.registerFlag(new Grindable(this));
      }

      this.registerFlag(new EquipmentFlag(this));
      this.registerFlag(new VanishCurse(this));
      this.registerFlag(new ClickMove(this));
      this.registerFlag(new Tradeable(this));
      if (VersionUtils.isVersionAfter(1, 16, 5)) {
         this.registerFlag(new SmithingTable(this));
      }

      Aliases.registerAliasType(this.FLAG_ALIASES, true);
   }

   public void reload() {
      Iterator var1 = this.flags.iterator();

      while(var1.hasNext()) {
         CustomFlag flag = (CustomFlag)var1.next();
         flag.reload();
      }

   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      ItemStack item = this.getItemInHand(p);

      try {
         if (args.length == 1) {
            p.openInventory((new Flag.FlagGui(p, item)).getInventory());
            return;
         }

         TagItem tagItem = ItemTag.getTagItem(item);
         CustomFlag flag = (CustomFlag)this.FLAG_ALIASES.convertAlias(args[1]);
         if (flag == null) {
            this.onWrongAlias("wrong-flag", p, this.FLAG_ALIASES, new String[0]);
            this.onFail(p, alias);
            return;
         }

         boolean value = args.length >= 3 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[2]) : !flag.getValue(tagItem);
         flag.setValue(tagItem, value);
         this.sendLanguageString(flag.getId() + ".feedback." + (value == flag.defaultValue() ? "standard" : "custom"), (String)null, p, new String[0]);
      } catch (Exception var9) {
         var9.printStackTrace();
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      if (args.length == 2) {
         return CompleteUtility.complete(args[1], this.FLAG_ALIASES);
      } else {
         return args.length == 3 ? CompleteUtility.complete(args[2], Aliases.BOOLEAN) : Collections.emptyList();
      }
   }

   public void registerFlag(CustomFlag flag) {
      Iterator var2 = this.flags.iterator();

      CustomFlag f;
      do {
         if (!var2.hasNext()) {
            this.getPlugin().registerListener(flag);
            this.flags.add(flag);
            this.FLAG_ALIASES.reload();
            return;
         }

         f = (CustomFlag)var2.next();
      } while(!f.getId().equals(flag.getId()));

      throw new IllegalStateException("Id already used");
   }

   private class FlagGui implements Gui {
      private final TagItem tagItem;
      private final Inventory inventory;
      private final Player target;

      public FlagGui(Player param2, ItemStack param3) {
         String title = this.getLanguageMessage("gui.actions.title", new String[]{"%player_name%", target.getName()});
         this.inventory = Bukkit.createInventory(this, 18, title);
         this.target = target;
         this.tagItem = ItemTag.getTagItem(item);
         this.updateInventory();
      }

      public void onClose(InventoryCloseEvent event) {
      }

      public void onClick(InventoryClickEvent event) {
         if (event.getWhoClicked().equals(this.target)) {
            if (this.inventory.equals(event.getClickedInventory())) {
               if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                  CustomFlag flag = (CustomFlag)(new ArrayList(Flag.this.flags)).get(event.getSlot());
                  if (flag != null) {
                     flag.setValue(this.tagItem, !flag.getValue(this.tagItem));
                     this.updateInventory();
                  }
               }
            }
         }
      }

      public void onDrag(InventoryDragEvent inventoryDragEvent) {
      }

      public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
         this.updateInventory();
      }

      private void updateInventory() {
         List<CustomFlag> flags = new ArrayList(Flag.this.flags);

         for(int i = 0; i < flags.size(); ++i) {
            CustomFlag flag = (CustomFlag)flags.get(i);
            ItemStack item = flag.getGuiItem();
            ItemMeta meta = ItemUtils.getMeta(item);
            boolean value = flag.getValue(this.tagItem);
            this.loadLanguageDescription(meta, "gui.flags." + flag.getId(), new String[]{"%value%", Aliases.BOOLEAN.getName(value)});
            if (flag.defaultValue() != value) {
               meta.addEnchant(Enchantment.LURE, 1, true);
            }

            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
            this.inventory.setItem(i, item);
         }

      }

      @NotNull
      public Inventory getInventory() {
         return this.inventory;
      }

      public Player getTargetPlayer() {
         return this.target;
      }

      @NotNull
      public APlugin getPlugin() {
         return ItemTag.get();
      }
   }
}
