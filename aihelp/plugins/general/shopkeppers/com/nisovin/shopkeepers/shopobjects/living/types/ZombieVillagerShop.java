package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.types.villager.VillagerEditorItems;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.KeyedSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ZombieVillagerShop extends ZombieShop<ZombieVillager> {
   public static final Property<Profession> PROFESSION;
   private final PropertyValue<Profession> professionProperty;

   public ZombieVillagerShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<ZombieVillagerShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(PROFESSION);
      ZombieVillagerShop var10002 = (ZombieVillagerShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.professionProperty = var10001.onValueChanged(var10002::applyProfession).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.professionProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.professionProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyProfession();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getProfessionEditorButton());
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
      ZombieVillager entity = (ZombieVillager)this.getEntity();
      if (entity != null) {
         entity.setVillagerProfession(this.getProfession());
      }
   }

   private ItemStack getProfessionEditorItem() {
      ItemStack iconItem = VillagerEditorItems.getProfessionEditorItem(this.getProfession());
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonZombieVillagerProfession, Messages.buttonZombieVillagerProfessionLore);
      return iconItem;
   }

   private Button getProfessionEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ZombieVillagerShop.this.getProfessionEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ZombieVillagerShop.this.cycleProfession(backwards);
            return true;
         }
      };
   }

   static {
      PROFESSION = (new BasicProperty()).dataKeyAccessor("profession", KeyedSerializers.forRegistry(Profession.class)).defaultValue(Profession.NONE).build();
   }
}
