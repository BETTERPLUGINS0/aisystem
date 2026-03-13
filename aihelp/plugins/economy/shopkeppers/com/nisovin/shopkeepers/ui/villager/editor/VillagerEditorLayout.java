package com.nisovin.shopkeepers.ui.villager.editor;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopobjects.living.types.villager.VillagerEditorItems;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.confirmations.ConfirmationUI;
import com.nisovin.shopkeepers.ui.confirmations.ConfirmationUIState;
import com.nisovin.shopkeepers.ui.editor.ActionButton;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorLayout;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.lib.UISessionManager;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.ui.villager.equipmentEditor.VillagerEquipmentEditorUI;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class VillagerEditorLayout extends EditorLayout {
   private final AbstractVillager villager;
   private static final int MAX_NAME_LENGTH = 256;

   public VillagerEditorLayout(AbstractVillager villager) {
      Validate.notNull(villager, (String)"villager is null");
      this.villager = villager;
   }

   protected AbstractVillager getVillager() {
      return this.villager;
   }

   protected ItemStack createShopInformationIcon() {
      String itemName = Messages.villagerEditorInformationHeader;
      List<String> itemLore = StringUtils.replaceArguments((Collection)Messages.villagerEditorInformation, (Object[])("entity_id", this.villager.getEntityId(), "entity_uuid", this.villager.getUniqueId(), "entity_name", StringUtils.getOrEmpty(this.villager.getCustomName()), "entity_location", TextUtils.getLocationString(this.villager.getLocation())));
      TextUtils.wrap(itemLore, 32);
      return ItemUtils.setDisplayNameAndLore(Settings.shopInformationItem.createItemStack(), itemName, itemLore);
   }

   protected ItemStack createTradeSetupIcon() {
      String villagerName = this.villager.getName();
      String itemName = StringUtils.replaceArguments(Messages.villagerEditorDescriptionHeader, "villagerName", villagerName);
      List<? extends String> itemLore = Messages.villagerEditorDescription;
      return ItemUtils.setDisplayNameAndLore(Settings.tradeSetupItem.createItemStack(), itemName, itemLore);
   }

   protected void setupVillagerButtons() {
      this.addButtonOrIgnore(this.createDeleteButton());
      this.addButtonOrIgnore(this.createNamingButton());
      this.addButtonOrIgnore(this.createEquipmentButton());
      this.addButtonOrIgnore(this.createContainerButton());
      if (this.villager instanceof Villager) {
         this.addButtonOrIgnore(this.getBabyEditorButton());
      }

      this.addButtonOrIgnore(this.getProfessionEditorButton());
      this.addButtonOrIgnore(this.getVillagerTypeEditorButton());
      this.addButtonOrIgnore(this.getVillagerLevelEditorButton());
      this.addButtonOrIgnore(this.getAIButton());
      this.addButtonOrIgnore(this.getInvulnerabilityButton());
   }

   protected Button createDeleteButton() {
      return new ActionButton(true) {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.deleteVillagerButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            UIState capturedUIState = editorView.captureState();
            editorView.closeDelayedAndRunTask(() -> {
               VillagerEditorLayout.this.requestConfirmationDeleteVillager(editorView, capturedUIState);
            });
            return true;
         }
      };
   }

   private void requestConfirmationDeleteVillager(EditorView editorView, UIState previousUIState) {
      Player player = editorView.getPlayer();
      ConfirmationUIState config = new ConfirmationUIState(Messages.confirmationUiDeleteVillagerTitle, Messages.confirmationUiDeleteVillagerConfirmLore, () -> {
         if (player.isValid()) {
            if (!editorView.abortIfContextInvalid()) {
               this.villager.remove();
               TextUtils.sendMessage(player, (Text)Messages.villagerRemoved);
            }
         }
      }, () -> {
         if (player.isValid()) {
            if (!editorView.abortIfContextInvalid()) {
               VillagerEditorViewProvider viewProvider = new VillagerEditorViewProvider(this.villager);
               UISessionManager.getInstance().requestUI(viewProvider, player, previousUIState);
            }
         }
      });
      ConfirmationUI.requestConfirmation(player, config);
   }

   protected Button createNamingButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.nameVillagerButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayedAndRunTask(() -> {
               Player player = editorView.getPlayer();
               if (player.isValid()) {
                  if (!editorView.abortIfContextInvalid()) {
                     SKShopkeepersPlugin.getInstance().getChatInput().request(player, (message) -> {
                        VillagerEditorLayout.this.renameVillager(editorView, message);
                     });
                     TextUtils.sendMessage(player, (Text)Messages.typeNewVillagerName);
                  }
               }
            });
            return true;
         }
      };
   }

   private void renameVillager(EditorView editorView, String newName) {
      assert editorView != null && newName != null;

      if (!editorView.abortIfContextInvalid()) {
         Player player = editorView.getPlayer();
         String preparedName = newName.trim();
         preparedName = TextUtils.convertHexColorsToBukkit(preparedName);
         preparedName = TextUtils.colorize(preparedName);
         if (!preparedName.isEmpty() && !preparedName.equals("-")) {
            if (!this.isValidName(preparedName)) {
               TextUtils.sendMessage(player, (Text)Messages.villagerNameInvalid);
               return;
            }
         } else {
            preparedName = "";
         }

         if (preparedName.isEmpty()) {
            this.villager.setCustomName((String)null);
         } else {
            this.villager.setCustomName(preparedName);
         }

         TextUtils.sendMessage(player, (Text)Messages.villagerNameSet);
      }
   }

   private boolean isValidName(String name) {
      assert name != null;

      return name.length() <= 256;
   }

   protected Button createEquipmentButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ItemUtils.setDisplayNameAndLore(new ItemStack(Material.ARMOR_STAND), Messages.buttonEquipment, Messages.buttonEquipmentLore);
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayedAndRunTask(() -> {
               Player player = editorView.getPlayer();
               if (player.isValid()) {
                  if (!editorView.abortIfContextInvalid()) {
                     VillagerEquipmentEditorUI.request(VillagerEditorLayout.this.villager, player);
                  }
               }
            });
            return true;
         }
      };
   }

   protected Button createContainerButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return Settings.DerivedSettings.villagerInventoryButtonItem.createItemStack();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayedAndRunTask(() -> {
               Player player = editorView.getPlayer();
               if (player.isValid()) {
                  if (!editorView.abortIfContextInvalid()) {
                     Inventory villagerInventory = VillagerEditorLayout.this.villager.getInventory();
                     int inventorySize = (int)Math.ceil((double)villagerInventory.getSize() / 9.0D) * 9;
                     String villagerName = VillagerEditorLayout.this.villager.getName();
                     String inventoryTitle = StringUtils.replaceArguments(Messages.villagerInventoryTitle, "villagerName", villagerName);
                     Inventory customInventory = Bukkit.createInventory((InventoryHolder)null, inventorySize, inventoryTitle);
                     customInventory.setStorageContents(villagerInventory.getStorageContents());
                     player.openInventory(customInventory);
                  }
               }
            });
            return true;
         }
      };
   }

   protected Button getBabyEditorButton() {
      return new ActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            ItemStack iconItem = new ItemStack(Material.EGG);
            ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonBaby, Messages.buttonBabyLore);
            return iconItem;
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            if (VillagerEditorLayout.this.villager.isAdult()) {
               VillagerEditorLayout.this.villager.setBaby();
            } else {
               Location location = VillagerEditorLayout.this.villager.getLocation();
               VillagerEditorLayout.this.villager.setAdult();
               SKShopkeepersPlugin.getInstance().getForcingEntityTeleporter().teleport(VillagerEditorLayout.this.villager, location);
            }

            return true;
         }
      };
   }

   @Nullable
   protected Button getProfessionEditorButton() {
      if (!(this.villager instanceof Villager)) {
         return null;
      } else {
         final Villager regularVillager = (Villager)this.villager;
         return new ActionButton() {
            private Profession profession = regularVillager.getProfession();

            @Nullable
            public ItemStack getIcon(EditorView editorView) {
               ItemStack iconItem = VillagerEditorItems.getProfessionEditorItem(this.profession);
               ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonVillagerProfession, Messages.buttonVillagerProfessionLore);
               return iconItem;
            }

            protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
               boolean backwards = clickEvent.isRightClick();
               List<MerchantRecipe> previousRecipes = VillagerEditorLayout.this.villager.getRecipes();
               this.profession = (Profession)RegistryUtils.cycleKeyed(Profession.class, this.profession, backwards);
               regularVillager.setProfession(this.profession);
               VillagerEditorLayout.this.villager.setRecipes(previousRecipes);
               if (regularVillager.getVillagerExperience() == 0) {
                  regularVillager.setVillagerExperience(1);
                  Player player = editorView.getPlayer();
                  TextUtils.sendMessage(player, (String)Messages.setVillagerXp, (Object[])("xp", 1));
               }

               return true;
            }
         };
      }
   }

   @Nullable
   protected Button getVillagerTypeEditorButton() {
      if (!(this.villager instanceof Villager)) {
         return null;
      } else {
         final Villager regularVillager = (Villager)this.villager;
         return new ActionButton(this) {
            private Type villagerType = regularVillager.getVillagerType();

            @Nullable
            public ItemStack getIcon(EditorView editorView) {
               ItemStack iconItem = VillagerEditorItems.getVillagerTypeEditorItem(this.villagerType);
               ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonVillagerVariant, Messages.buttonVillagerVariantLore);
               return iconItem;
            }

            protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
               boolean backwards = clickEvent.isRightClick();
               this.villagerType = (Type)RegistryUtils.cycleKeyed(Type.class, this.villagerType, backwards);
               regularVillager.setVillagerType(this.villagerType);
               return true;
            }
         };
      }
   }

   @Nullable
   protected Button getVillagerLevelEditorButton() {
      if (!(this.villager instanceof Villager)) {
         return null;
      } else {
         final Villager regularVillager = (Villager)this.villager;
         return new ActionButton(this) {
            private int villagerLevel = regularVillager.getVillagerLevel();

            @Nullable
            public ItemStack getIcon(EditorView editorView) {
               ItemStack iconItem;
               switch(regularVillager.getVillagerLevel()) {
               case 1:
               default:
                  iconItem = new ItemStack(Material.COBBLESTONE);
                  break;
               case 2:
                  iconItem = new ItemStack(Material.IRON_INGOT);
                  break;
               case 3:
                  iconItem = new ItemStack(Material.GOLD_INGOT);
                  break;
               case 4:
                  iconItem = new ItemStack(Material.EMERALD);
                  break;
               case 5:
                  iconItem = new ItemStack(Material.DIAMOND);
               }

               assert iconItem != null;

               ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonVillagerLevel, Messages.buttonVillagerLevelLore);
               return iconItem;
            }

            protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
               boolean backwards = clickEvent.isRightClick();
               if (backwards) {
                  --this.villagerLevel;
               } else {
                  ++this.villagerLevel;
               }

               this.villagerLevel = MathUtils.rangeModulo(this.villagerLevel, 1, 5);
               regularVillager.setVillagerLevel(this.villagerLevel);
               return true;
            }
         };
      }
   }

   protected Button getAIButton() {
      return new ActionButton() {
         private boolean hasAI;

         {
            this.hasAI = VillagerEditorLayout.this.villager.hasAI();
         }

         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            ItemStack iconItem;
            if (this.hasAI) {
               iconItem = new ItemStack(Material.JACK_O_LANTERN);
            } else {
               iconItem = new ItemStack(Material.CARVED_PUMPKIN);
            }

            assert iconItem != null;

            ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonMobAi, Messages.buttonMobAiLore);
            return iconItem;
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            this.hasAI = !this.hasAI;
            VillagerEditorLayout.this.villager.setAI(this.hasAI);
            return true;
         }
      };
   }

   protected Button getInvulnerabilityButton() {
      return new ActionButton() {
         private boolean invulnerable;

         {
            this.invulnerable = VillagerEditorLayout.this.villager.isInvulnerable();
         }

         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            ItemStack iconItem;
            if (this.invulnerable) {
               iconItem = new ItemStack(Material.POTION);
               PotionMeta potionMeta = (PotionMeta)Unsafe.castNonNull(iconItem.getItemMeta());
               potionMeta.setBasePotionType(PotionType.HEALING);
               iconItem.setItemMeta(potionMeta);
            } else {
               iconItem = new ItemStack(Material.GLASS_BOTTLE);
            }

            assert iconItem != null;

            ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonInvulnerability, Messages.buttonInvulnerabilityLore);
            return iconItem;
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            this.invulnerable = !this.invulnerable;
            VillagerEditorLayout.this.villager.setInvulnerable(this.invulnerable);
            return true;
         }
      };
   }
}
