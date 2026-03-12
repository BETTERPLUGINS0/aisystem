package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.compat.MC_1_21_9;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import java.util.List;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.entity.Golem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CopperGolemShop extends SKLivingShopObject<Golem> {
   public static final Property<String> WEATHER_STATE;
   private final PropertyValue<String> weatherStateProperty;

   public CopperGolemShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<CopperGolemShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(WEATHER_STATE);
      CopperGolemShop var10002 = (CopperGolemShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.weatherStateProperty = var10001.onValueChanged(var10002::applyWeatherState).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.weatherStateProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.weatherStateProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyWeatherState();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getWeatherStateEditorButton());
      return editorButtons;
   }

   public String getWeatherState() {
      return (String)this.weatherStateProperty.getValue();
   }

   public void setWeatherState(String weatherState) {
      this.weatherStateProperty.setValue(weatherState);
   }

   public void cycleWeatherState(boolean backwards) {
      this.setWeatherState((String)CollectionUtils.cycleValue(MC_1_21_9.COPPER_GOLEM_WEATHER_STATES, this.getWeatherState(), backwards));
   }

   private void applyWeatherState() {
      Golem entity = (Golem)this.getEntity();
      if (entity != null) {
         Compat.getProvider().setCopperGolemWeatherState(entity, this.getWeatherState());
         Compat.getProvider().setCopperGolemNextWeatheringTick(entity, -2);
      }
   }

   private ItemStack getWeatherStateEditorItem() {
      String weatherState = this.getWeatherState();
      byte var4 = -1;
      switch(weatherState.hashCode()) {
      case -1682160627:
         if (weatherState.equals("UNAFFECTED")) {
            var4 = 3;
         }
         break;
      case -1106000812:
         if (weatherState.equals("OXIDIZED")) {
            var4 = 2;
         }
         break;
      case -591073024:
         if (weatherState.equals("EXPOSED")) {
            var4 = 0;
         }
         break;
      case 1691756403:
         if (weatherState.equals("WEATHERED")) {
            var4 = 1;
         }
      }

      ItemStack iconItem;
      switch(var4) {
      case 0:
         iconItem = new ItemStack(Material.EXPOSED_COPPER);
         break;
      case 1:
         iconItem = new ItemStack(Material.WEATHERED_COPPER);
         break;
      case 2:
         iconItem = new ItemStack(Material.OXIDIZED_COPPER);
         break;
      case 3:
      default:
         iconItem = new ItemStack(Material.COPPER_BLOCK);
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonCopperGolemWeatherState, Messages.buttonCopperGolemWeatherStateLore);
      return iconItem;
   }

   private Button getWeatherStateEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return CopperGolemShop.this.getWeatherStateEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            CopperGolemShop.this.cycleWeatherState(backwards);
            return true;
         }
      };
   }

   static {
      WEATHER_STATE = (new BasicProperty()).dataKeyAccessor("weatherState", StringSerializers.STRICT_NON_EMPTY).useDefaultIfMissing().defaultValue("UNAFFECTED").build();
   }
}
