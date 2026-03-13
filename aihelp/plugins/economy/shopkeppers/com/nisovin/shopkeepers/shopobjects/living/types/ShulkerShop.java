package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import java.util.Objects;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ShulkerShop extends SKLivingShopObject<Shulker> {
   private static final float PEEK_EPSILON = 0.01F;
   public static final Property<DyeColor> COLOR = (new BasicProperty()).dataKeyAccessor("color", EnumSerializers.lenient(DyeColor.class)).nullable().defaultValue((Object)null).build();
   public static final Property<BlockFace> ATTACHED_FACE;
   private final PropertyValue<DyeColor> colorProperty;
   private final PropertyValue<BlockFace> attachedFaceProperty;

   public ShulkerShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<ShulkerShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(COLOR);
      ShulkerShop var10002 = (ShulkerShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.colorProperty = var10001.onValueChanged(var10002::applyColor).build(this.properties);
      var10001 = new PropertyValue(ATTACHED_FACE);
      var10002 = (ShulkerShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.attachedFaceProperty = var10001.onValueChanged(var10002::applyAttachedFace).build(this.properties);
      if (creationData != null) {
         BlockFace targetedBlockFace = creationData.getTargetedBlockFace();
         if (targetedBlockFace != null) {
            ((ShulkerShop)Unsafe.initialized(this)).setAttachedBlockFace(targetedBlockFace);
         }
      }

   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.colorProperty.load(shopObjectData);
      this.attachedFaceProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.colorProperty.save(shopObjectData);
      this.attachedFaceProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyColor();
   }

   protected void prepareEntity(Shulker entity) {
      super.prepareEntity((LivingEntity)entity);
      this.applyAttachedFace(entity);
   }

   protected boolean shallAdjustSpawnLocation() {
      return this.getAttachedFace() == BlockFace.DOWN;
   }

   public void tickAI() {
      super.tickAI();
      this.peekIfPlayerNearby();
   }

   private void peekIfPlayerNearby() {
      Shulker entity = (Shulker)this.getEntity();
      if (entity != null) {
         if (Settings.shulkerPeekIfPlayerNearby) {
            Location entityLocation = entity.getLocation();
            Player nearestPlayer = EntityUtils.getNearestPlayer(entityLocation, 6.0D);
            if (nearestPlayer != null) {
               if (!MathUtils.fuzzyEquals(entity.getPeek(), Settings.shulkerPeekHeight, 0.01F)) {
                  entity.setPeek(Settings.shulkerPeekHeight);
               }
            } else if (entity.getPeek() > 0.0F) {
               entity.setPeek(0.0F);
            }

         }
      }
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getColorEditorButton());
      return editorButtons;
   }

   @Nullable
   public DyeColor getColor() {
      return (DyeColor)this.colorProperty.getValue();
   }

   public void setColor(@Nullable DyeColor color) {
      this.colorProperty.setValue(color);
   }

   public void cycleColor(boolean backwards) {
      this.setColor((DyeColor)EnumUtils.cycleEnumConstantNullable(DyeColor.class, this.getColor(), backwards));
   }

   private void applyColor() {
      Shulker entity = (Shulker)this.getEntity();
      if (entity != null) {
         entity.setColor((DyeColor)Unsafe.nullableAsNonNull(this.getColor()));
      }
   }

   private ItemStack getColorEditorItem() {
      DyeColor color = this.getColor();
      ItemStack iconItem;
      if (color == null) {
         iconItem = new ItemStack(Material.PURPUR_BLOCK);
      } else {
         iconItem = new ItemStack(ItemUtils.getWoolType(color));
      }

      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonShulkerColor, Messages.buttonShulkerColorLore);
      return iconItem;
   }

   private Button getColorEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return ShulkerShop.this.getColorEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            ShulkerShop.this.cycleColor(backwards);
            return true;
         }
      };
   }

   public void setAttachedBlockFace(BlockFace attachedBlockFace) {
      super.setAttachedBlockFace(attachedBlockFace);
      this.setAttachedFace(attachedBlockFace.getOppositeFace());
   }

   public void setAttachedFace(BlockFace attachedFace) {
      this.attachedFaceProperty.setValue(attachedFace);
   }

   public BlockFace getAttachedFace() {
      return (BlockFace)this.attachedFaceProperty.getValue();
   }

   private void applyAttachedFace() {
      Shulker entity = (Shulker)this.getEntity();
      if (entity != null) {
         this.applyAttachedFace(entity);
      }
   }

   private void applyAttachedFace(Shulker entity) {
      entity.setAttachedFace(this.getAttachedFace());
   }

   static {
      ATTACHED_FACE = (new BasicProperty()).dataKeyAccessor("attachedFace", EnumSerializers.lenient(BlockFace.class)).validator((value) -> {
         Validate.isTrue(BlockFaceUtils.isBlockSide(value), "Not a valid block side: '" + String.valueOf(value) + "'.");
      }).useDefaultIfMissing().defaultValue(BlockFace.DOWN).build();
   }
}
