package emanondev.itemedit.command;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.command.itemedit.Amount;
import emanondev.itemedit.command.itemedit.Attribute;
import emanondev.itemedit.command.itemedit.AxolotlVariant;
import emanondev.itemedit.command.itemedit.Banner;
import emanondev.itemedit.command.itemedit.BookAuthor;
import emanondev.itemedit.command.itemedit.BookEnchant;
import emanondev.itemedit.command.itemedit.BookType;
import emanondev.itemedit.command.itemedit.ColorOld;
import emanondev.itemedit.command.itemedit.ColorSubcommand;
import emanondev.itemedit.command.itemedit.Compass;
import emanondev.itemedit.command.itemedit.CustomModelData;
import emanondev.itemedit.command.itemedit.Damage;
import emanondev.itemedit.command.itemedit.Enchant;
import emanondev.itemedit.command.itemedit.Equipment;
import emanondev.itemedit.command.itemedit.FireResistent;
import emanondev.itemedit.command.itemedit.Firework;
import emanondev.itemedit.command.itemedit.FireworkPower;
import emanondev.itemedit.command.itemedit.Food;
import emanondev.itemedit.command.itemedit.Glider;
import emanondev.itemedit.command.itemedit.Glow;
import emanondev.itemedit.command.itemedit.GoatHornSound;
import emanondev.itemedit.command.itemedit.Hide;
import emanondev.itemedit.command.itemedit.HideAll;
import emanondev.itemedit.command.itemedit.HideToolTip;
import emanondev.itemedit.command.itemedit.ItemModel;
import emanondev.itemedit.command.itemedit.ListAliases;
import emanondev.itemedit.command.itemedit.Lore;
import emanondev.itemedit.command.itemedit.MaxDurability;
import emanondev.itemedit.command.itemedit.MaxStackSize;
import emanondev.itemedit.command.itemedit.PotionEffectEditor;
import emanondev.itemedit.command.itemedit.Rarity;
import emanondev.itemedit.command.itemedit.Rename;
import emanondev.itemedit.command.itemedit.RepairCost;
import emanondev.itemedit.command.itemedit.SkullOwner;
import emanondev.itemedit.command.itemedit.SpawnerEggType;
import emanondev.itemedit.command.itemedit.ToolTipStyle;
import emanondev.itemedit.command.itemedit.Trim;
import emanondev.itemedit.command.itemedit.TropicalFish;
import emanondev.itemedit.command.itemedit.Type;
import emanondev.itemedit.command.itemedit.Unbreakable;
import emanondev.itemedit.utility.VersionUtils;

public class ItemEditCommand extends AbstractCommand {
   public static ItemEditCommand instance;

   public ItemEditCommand() {
      super("itemedit", ItemEdit.get(), true);
      instance = this;
      this.registerSubCommand(() -> {
         return new Rename(this);
      });
      this.registerSubCommand(() -> {
         return new Lore(this);
      });
      this.registerSubCommand(() -> {
         return new Enchant(this);
      });
      this.registerSubCommand(() -> {
         return new Hide(this);
      });
      this.registerSubCommand(() -> {
         return new HideAll(this);
      });
      this.registerSubCommand(() -> {
         return new HideToolTip(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new Unbreakable(this);
      });
      this.registerSubCommand(() -> {
         return new Equipment(this);
      }, VersionUtils.isVersionAfter(1, 21, 2));
      this.registerSubCommand(() -> {
         return new RepairCost(this);
      });
      this.registerSubCommand(() -> {
         return new Food(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new MaxStackSize(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new MaxDurability(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new FireResistent(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new Glider(this);
      }, VersionUtils.isVersionAfter(1, 21, 2));
      this.registerSubCommand(() -> {
         return new Glow(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new Rarity(this);
      }, VersionUtils.isVersionAfter(1, 20, 5));
      this.registerSubCommand(() -> {
         return new Amount(this);
      });
      this.registerSubCommand(() -> {
         return new Damage(this);
      });
      this.registerSubCommand(() -> {
         return new Banner(this);
      });
      this.registerSubCommand(() -> {
         return (SubCmd)(VersionUtils.isVersionUpTo(1, 10) ? new ColorOld(this) : new ColorSubcommand(this));
      });
      this.registerSubCommand(() -> {
         return new SkullOwner(this);
      });
      this.registerSubCommand(() -> {
         return new FireworkPower(this);
      });
      this.registerSubCommand(() -> {
         return new Firework(this);
      });
      this.registerSubCommand(() -> {
         return new PotionEffectEditor(this);
      });
      this.registerSubCommand(() -> {
         return new BookAuthor(this);
      });
      this.registerSubCommand(() -> {
         return new BookType(this);
      }, VersionUtils.isVersionAfter(1, 10));
      this.registerSubCommand(() -> {
         return new SpawnerEggType(this);
      }, VersionUtils.isVersionInRange(1, 11, 1, 12));
      this.registerSubCommand(() -> {
         return new Attribute(this);
      }, VersionUtils.isVersionAfter(1, 13));
      this.registerSubCommand(() -> {
         return new TropicalFish(this);
      }, VersionUtils.isVersionAfter(1, 13));
      this.registerSubCommand(() -> {
         return new CustomModelData(this);
      }, VersionUtils.isVersionAfter(1, 14));
      this.registerSubCommand(() -> {
         return new ItemModel(this);
      }, VersionUtils.isVersionAfter(1, 21, 2));
      this.registerSubCommand(() -> {
         return new ToolTipStyle(this);
      }, VersionUtils.isVersionAfter(1, 21, 2));
      this.registerSubCommand(() -> {
         return new Compass(this);
      }, VersionUtils.isVersionAfter(1, 16));
      this.registerSubCommand(() -> {
         return new AxolotlVariant(this);
      }, VersionUtils.isVersionAfter(1, 17));
      this.registerSubCommand(() -> {
         return new GoatHornSound(this);
      }, VersionUtils.isVersionAfter(1, 19, 3));
      this.registerSubCommand(() -> {
         return new Trim(this);
      }, VersionUtils.isVersionAfter(1, 20));
      this.registerSubCommand(() -> {
         return new BookEnchant(this);
      });
      this.registerSubCommand(() -> {
         return new Type(this);
      });
      this.registerSubCommand(() -> {
         return new ListAliases(this);
      });
   }

   public static ItemEditCommand get() {
      return instance;
   }
}
