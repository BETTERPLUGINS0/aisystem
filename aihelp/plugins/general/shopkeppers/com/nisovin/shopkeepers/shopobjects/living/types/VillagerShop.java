package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.shopobjects.living.types.villager.VillagerEditorItems;
import com.nisovin.shopkeepers.shopobjects.living.types.villager.VillagerSounds;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.ui.lib.ViewProvider;
import com.nisovin.shopkeepers.ui.trading.TradingViewProvider;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.validation.java.IntegerValidators;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.KeyedSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.NumberSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class VillagerShop extends BabyableShop<Villager> {
   public static final Property<Profession> PROFESSION;
   public static final Property<Type> VILLAGER_TYPE;
   public static final int MIN_VILLAGER_LEVEL = 1;
   public static final int MAX_VILLAGER_LEVEL = 5;
   public static final Property<Integer> VILLAGER_LEVEL;
   private final PropertyValue<Profession> professionProperty;
   private final PropertyValue<Type> villagerTypeProperty;
   private final PropertyValue<Integer> villagerLevelProperty;
   private final VillagerSounds villagerSounds;

   public VillagerShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<VillagerShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(PROFESSION);
      VillagerShop var10002 = (VillagerShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.professionProperty = var10001.onValueChanged(var10002::applyProfession).build(this.properties);
      var10001 = new PropertyValue(VILLAGER_TYPE);
      var10002 = (VillagerShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.villagerTypeProperty = var10001.onValueChanged(var10002::applyVillagerType).build(this.properties);
      var10001 = new PropertyValue(VILLAGER_LEVEL);
      var10002 = (VillagerShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.villagerLevelProperty = var10001.onValueChanged(var10002::applyVillagerLevel).build(this.properties);
      this.villagerSounds = new VillagerSounds((SKLivingShopObject)Unsafe.initialized(this));
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.professionProperty.load(shopObjectData);
      this.villagerTypeProperty.load(shopObjectData);
      this.villagerLevelProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.professionProperty.save(shopObjectData);
      this.villagerTypeProperty.save(shopObjectData);
      this.villagerLevelProperty.save(shopObjectData);
   }

   public void setup() {
      super.setup();
      if (Settings.simulateVillagerTradingSounds) {
         ViewProvider viewProvider = this.shopkeeper.getViewProvider(DefaultUITypes.TRADING());
         if (viewProvider instanceof TradingViewProvider) {
            TradingViewProvider tradingViewProvider = (TradingViewProvider)viewProvider;
            tradingViewProvider.addListener(this.villagerSounds);
         }
      }

   }

   protected void onSpawn() {
      super.onSpawn();
      Villager entity = (Villager)Unsafe.assertNonNull((Villager)this.getEntity());
      entity.setVillagerExperience(1);
      if (Settings.simulateVillagerTradingSounds || Settings.simulateVillagerAmbientSounds) {
         entity.setSilent(true);
      }

      this.applyProfession();
      this.applyVillagerType();
      this.applyVillagerLevel();
   }

   public void onTick() {
      super.onTick();
      if (Settings.simulateVillagerAmbientSounds) {
         this.villagerSounds.tick();
      }

   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getProfessionEditorButton());
      editorButtons.add(this.getVillagerTypeEditorButton());
      editorButtons.add(this.getVillagerLevelEditorButton());
      return editorButtons;
   }

   public Profession getProfession() {
      return (Profession)this.professionProperty.getValue();
   }

   public void setProfession(Profession profession) {
      this.professionProperty.setValue(profession);
   }

   public void cycleProfession(boolean backwards) {
      this.setProfession((Profession)RegistryUtils.cycleKeyed(Profession.class, this.getProfession(), backwards));
   }

   private void applyProfession() {
      Villager entity = (Villager)this.getEntity();
      if (entity != null) {
         entity.setProfession(this.getProfession());
      }
   }

   private ItemStack getProfessionEditorItem() {
      ItemStack iconItem = VillagerEditorItems.getProfessionEditorItem(this.getProfession());
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonVillagerProfession, Messages.buttonVillagerProfessionLore);
      return iconItem;
   }

   private Button getProfessionEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return VillagerShop.this.getProfessionEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            VillagerShop.this.cycleProfession(backwards);
            return true;
         }
      };
   }

   public Type getVillagerType() {
      return (Type)this.villagerTypeProperty.getValue();
   }

   public void setVillagerType(Type villagerType) {
      this.villagerTypeProperty.setValue(villagerType);
   }

   public void cycleVillagerType(boolean backwards) {
      this.setVillagerType((Type)RegistryUtils.cycleKeyed(Type.class, this.getVillagerType(), backwards));
   }

   private void applyVillagerType() {
      Villager entity = (Villager)this.getEntity();
      if (entity != null) {
         entity.setVillagerType(this.getVillagerType());
      }
   }

   private ItemStack getVillagerTypeEditorItem() {
      ItemStack iconItem = VillagerEditorItems.getVillagerTypeEditorItem(this.getVillagerType());
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonVillagerVariant, Messages.buttonVillagerVariantLore);
      return iconItem;
   }

   private Button getVillagerTypeEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return VillagerShop.this.getVillagerTypeEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            VillagerShop.this.cycleVillagerType(backwards);
            return true;
         }
      };
   }

   public int getVillagerLevel() {
      return (Integer)this.villagerLevelProperty.getValue();
   }

   public void setVillagerLevel(int villagerLevel) {
      this.villagerLevelProperty.setValue(villagerLevel);
   }

   public void cycleVillagerLevel(boolean backwards) {
      int villagerLevel = this.getVillagerLevel();
      int nextLevel;
      if (backwards) {
         nextLevel = villagerLevel - 1;
      } else {
         nextLevel = villagerLevel + 1;
      }

      nextLevel = MathUtils.rangeModulo(nextLevel, 1, 5);
      this.setVillagerLevel(nextLevel);
   }

   private void applyVillagerLevel() {
      Villager entity = (Villager)this.getEntity();
      if (entity != null) {
         entity.setVillagerLevel(this.getVillagerLevel());
      }
   }

   private ItemStack getVillagerLevelEditorItem() {
      ItemStack iconItem;
      switch(this.getVillagerLevel()) {
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

   private Button getVillagerLevelEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return VillagerShop.this.getVillagerLevelEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            VillagerShop.this.cycleVillagerLevel(backwards);
            return true;
         }
      };
   }

   static {
      PROFESSION = (new BasicProperty()).dataKeyAccessor("profession", KeyedSerializers.forRegistry(Profession.class)).defaultValue(Profession.NONE).build();
      VILLAGER_TYPE = (new BasicProperty()).dataKeyAccessor("villagerType", KeyedSerializers.forRegistry(Type.class)).defaultValue(Type.PLAINS).build();
      VILLAGER_LEVEL = (new BasicProperty()).dataKeyAccessor("villagerLevel", NumberSerializers.INTEGER).validator(IntegerValidators.bounded(1, 5)).defaultValue(1).build();
   }
}
