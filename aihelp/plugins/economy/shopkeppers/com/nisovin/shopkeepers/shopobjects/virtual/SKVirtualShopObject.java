package com.nisovin.shopkeepers.shopobjects.virtual;

import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.virtual.VirtualShopObject;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObject;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import java.util.List;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKVirtualShopObject extends AbstractShopObject implements VirtualShopObject {
   protected final VirtualShops virtualShops;

   protected SKVirtualShopObject(VirtualShops virtualShops, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(shopkeeper, creationData);
      this.virtualShops = virtualShops;
   }

   public SKVirtualShopObjectType getType() {
      return this.virtualShops.getSignShopObjectType();
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
   }

   public boolean isSpawned() {
      return false;
   }

   public boolean isActive() {
      return false;
   }

   @Nullable
   public String getId() {
      return null;
   }

   public boolean spawn() {
      return false;
   }

   public void despawn() {
   }

   @Nullable
   public Location getLocation() {
      return null;
   }

   public boolean move() {
      return false;
   }

   public void onTick() {
      super.onTick();
   }

   @Nullable
   public Location getTickVisualizationParticleLocation() {
      return null;
   }

   public void setName(@Nullable String name) {
   }

   @Nullable
   public String getName() {
      return null;
   }

   public List<Button> createEditorButtons() {
      return super.createEditorButtons();
   }
}
