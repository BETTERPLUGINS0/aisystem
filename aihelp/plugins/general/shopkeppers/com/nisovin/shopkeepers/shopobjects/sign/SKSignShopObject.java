package com.nisovin.shopkeepers.shopobjects.sign;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopobjects.sign.SignShopObject;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.ShopkeeperData;
import com.nisovin.shopkeepers.shopkeeper.migration.Migration;
import com.nisovin.shopkeepers.shopkeeper.migration.MigrationPhase;
import com.nisovin.shopkeepers.shopkeeper.migration.ShopkeeperDataMigrator;
import com.nisovin.shopkeepers.shopobjects.SKDefaultShopObjectTypes;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.block.base.BaseBlockShopObject;
import com.nisovin.shopkeepers.shopobjects.block.base.BaseBlockShops;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.bukkit.BlockFaceUtils;
import com.nisovin.shopkeepers.util.bukkit.SignUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.BooleanSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SKSignShopObject extends BaseBlockShopObject implements SignShopObject {
   private static final String DATA_KEY_SIGN_TYPE = "signType";
   public static final Property<SignType> SIGN_TYPE;
   public static final Property<Boolean> WALL_SIGN;
   public static final Property<Boolean> GLOWING_TEXT;
   private final PropertyValue<SignType> signTypeProperty;
   private final PropertyValue<Boolean> wallSignProperty;
   private final PropertyValue<Boolean> glowingTextProperty;

   protected SKSignShopObject(BaseBlockShops blockShops, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(blockShops, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(SIGN_TYPE);
      SKSignShopObject var10002 = (SKSignShopObject)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.signTypeProperty = var10001.onValueChanged(var10002::applySignType).build(this.properties);
      var10001 = new PropertyValue(WALL_SIGN);
      var10002 = (SKSignShopObject)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.wallSignProperty = var10001.onValueChanged(var10002::respawn).build(this.properties);
      var10001 = new PropertyValue(GLOWING_TEXT);
      var10002 = (SKSignShopObject)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.glowingTextProperty = var10001.onValueChanged(var10002::applyGlowingText).build(this.properties);
      if (creationData != null) {
         BlockFace targetedBlockFace = creationData.getTargetedBlockFace();
         if (targetedBlockFace == BlockFace.UP) {
            this.wallSignProperty.setValue(false, Collections.emptySet());
         }
      }

   }

   public SKSignShopObjectType getType() {
      return SKDefaultShopObjectTypes.SIGN();
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.signTypeProperty.load(shopObjectData);
      this.wallSignProperty.load(shopObjectData);
      this.glowingTextProperty.load(shopObjectData);
      this.setGlowingText(this.isGlowingText());
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.signTypeProperty.save(shopObjectData);
      this.wallSignProperty.save(shopObjectData);
      this.glowingTextProperty.save(shopObjectData);
   }

   protected boolean isValidBlockType(Material blockType) {
      return ItemUtils.isSign(blockType);
   }

   @Nullable
   public Sign getSign() {
      if (!this.isActive()) {
         return null;
      } else {
         Block block = (Block)Unsafe.assertNonNull(this.getBlock());

         assert this.isValidBlockType(block.getType());

         return (Sign)block.getState();
      }
   }

   @Nullable
   protected BlockData createBlockData() {
      SignType signType = this.getSignType();

      assert signType.isSupported();

      boolean wallSign = this.isWallSign();
      Material blockMaterial = (Material)Unsafe.assertNonNull(signType.getSignMaterial(wallSign));

      assert this.isValidBlockType(blockMaterial);

      Object blockData;
      if (wallSign) {
         WallSign wallSignData = (WallSign)Bukkit.createBlockData(blockMaterial);
         wallSignData.setFacing(this.getSignFacing());
         blockData = wallSignData;
      } else {
         org.bukkit.block.data.type.Sign signPostData = (org.bukkit.block.data.type.Sign)Unsafe.castNonNull(Bukkit.createBlockData(blockMaterial));
         signPostData.setRotation(this.getSignFacing());
         blockData = signPostData;
      }

      return (BlockData)blockData;
   }

   protected void updateBlock() {
      Sign sign = this.getSign();
      if (sign != null) {
         SignShops.updateShopSign(sign, this.shopkeeper);
         this.applyGlowingText(sign);
         sign.update(false, false);
      }
   }

   @Nullable
   public Location getTickVisualizationParticleLocation() {
      Location location = this.getLocation();
      if (location == null) {
         return null;
      } else {
         return this.isWallSign() ? location.add(0.5D, 0.5D, 0.5D) : location.add(0.5D, 1.3D, 0.5D);
      }
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getSignTypeEditorButton());
      if (Settings.enableGlowingSignText) {
         editorButtons.add(this.getGlowingTextEditorButton());
      }

      return editorButtons;
   }

   public void setAttachedBlockFace(BlockFace attachedBlockFace) {
      super.setAttachedBlockFace(attachedBlockFace);
      Validate.isTrue(attachedBlockFace != BlockFace.DOWN, "Invalid sign block face: DOWN.");
      if (attachedBlockFace == BlockFace.UP) {
         this.wallSignProperty.setValue(false);
      } else {
         this.shopkeeper.setYaw(BlockFaceUtils.getYaw(attachedBlockFace));
         this.wallSignProperty.setValue(true);
      }

   }

   @Nullable
   public BlockFace getAttachedBlockFace() {
      return this.isWallSign() ? this.getSignFacing() : BlockFace.UP;
   }

   public boolean isWallSign() {
      return (Boolean)this.wallSignProperty.getValue();
   }

   public BlockFace getSignFacing() {
      return this.isWallSign() ? BlockFaceUtils.getWallSignFacings().fromYaw(this.shopkeeper.getYaw()) : BlockFaceUtils.getSignPostFacings().fromYaw(this.shopkeeper.getYaw());
   }

   public SignType getSignType() {
      return (SignType)this.signTypeProperty.getValue();
   }

   public void setSignType(SignType signType) {
      this.signTypeProperty.setValue(signType);
   }

   protected void applySignType() {
      Sign sign = this.getSign();
      if (sign != null) {
         BlockData blockData = (BlockData)Unsafe.assertNonNull(this.createBlockData());
         sign.setBlockData(blockData);
         sign.update(true, false);
      }
   }

   public void cycleSignType(boolean backwards) {
      this.setSignType((SignType)EnumUtils.cycleEnumConstant(SignType.class, this.getSignType(), backwards, SignType.IS_SUPPORTED));
   }

   private ItemStack getSignTypeEditorItem() {
      Material signMaterial = (Material)Unsafe.assertNonNull(this.getSignType().getSignMaterial());
      ItemStack iconItem = new ItemStack(signMaterial);
      return ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSignVariant, Messages.buttonSignVariantLore);
   }

   private Button getSignTypeEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SKSignShopObject.this.getSignTypeEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            SKSignShopObject.this.cycleSignType(backwards);
            return true;
         }
      };
   }

   public boolean isGlowingText() {
      return (Boolean)this.glowingTextProperty.getValue();
   }

   public void setGlowingText(boolean glowing) {
      if (glowing && !Settings.enableGlowingSignText) {
         Log.warning(this.shopkeeper.getLogPrefix() + "Disabling glowing sign text.");
         glowing = false;
      }

      this.glowingTextProperty.setValue(glowing);
   }

   protected void applyGlowingText() {
      Sign sign = this.getSign();
      if (sign != null) {
         this.applyGlowingText(sign);
         sign.update(false, false);
      }
   }

   private void applyGlowingText(Sign sign) {
      SignUtils.setBothSidesGlowingText(sign, this.isGlowingText());
   }

   public void cycleGlowingText(boolean backwards) {
      this.setGlowingText(!this.isGlowingText());
   }

   private ItemStack getGlowingTextEditorItem() {
      ItemStack iconItem;
      if (this.isGlowingText()) {
         iconItem = new ItemStack(Material.GLOW_INK_SAC);
      } else {
         iconItem = new ItemStack(Material.INK_SAC);
      }

      return ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonSignGlowingText, Messages.buttonSignGlowingTextLore);
   }

   private Button getGlowingTextEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return SKSignShopObject.this.getGlowingTextEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            SKSignShopObject.this.cycleGlowingText(backwards);
            return true;
         }
      };
   }

   static {
      SIGN_TYPE = (new BasicProperty()).dataKeyAccessor("signType", EnumSerializers.lenient(SignType.class)).validator((value) -> {
         Validate.isTrue(value.isSupported(), () -> {
            return "Unsupported sign type: '" + value.name() + "'.";
         });
      }).defaultValue(SignType.OAK).build();
      WALL_SIGN = (new BasicProperty()).dataKeyAccessor("wallSign", BooleanSerializers.LENIENT).defaultValue(true).build();
      GLOWING_TEXT = (new BasicProperty()).dataKeyAccessor("glowingText", BooleanSerializers.LENIENT).defaultValue(false).build();
      ShopkeeperDataMigrator.registerMigration(new Migration("sign-type", MigrationPhase.ofShopObjectClass(SKSignShopObject.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            boolean migrated = false;
            ShopObjectData shopObjectData = (ShopObjectData)shopkeeperData.get(AbstractShopkeeper.SHOP_OBJECT_DATA);
            String signTypeName = shopObjectData.getString("signType");
            if ("GENERIC".equals(signTypeName)) {
               Log.warning(logPrefix + "Migrating sign type from '" + signTypeName + "' to '" + String.valueOf(SignType.OAK) + "'.");
               shopObjectData.set(SKSignShopObject.SIGN_TYPE, SignType.OAK);
               migrated = true;
            } else if ("REDWOOD".equals(signTypeName)) {
               Log.warning(logPrefix + "Migrating sign type from '" + signTypeName + "' to '" + String.valueOf(SignType.SPRUCE) + "'.");
               shopObjectData.set(SKSignShopObject.SIGN_TYPE, SignType.SPRUCE);
               migrated = true;
            }

            return migrated;
         }
      });
      ShopkeeperDataMigrator.registerMigration(new Migration("sign-facing-to-yaw", MigrationPhase.ofShopObjectClass(SKSignShopObject.class)) {
         public boolean migrate(ShopkeeperData shopkeeperData, String logPrefix) throws InvalidDataException {
            boolean migrated = false;
            ShopObjectData shopObjectData = (ShopObjectData)shopkeeperData.get(AbstractShopkeeper.SHOP_OBJECT_DATA);
            String signFacingName = shopObjectData.getString("signFacing");
            if (signFacingName != null) {
               BlockFace signFacing = BlockFace.SOUTH;

               try {
                  signFacing = BlockFace.valueOf(signFacingName);
               } catch (IllegalArgumentException var8) {
                  Log.warning(logPrefix + "Could not parse sign facing '" + signFacingName + "'. Falling back to SOUTH.");
               }

               if (!this.isValidSignFacing(shopObjectData, signFacing)) {
                  Log.warning(logPrefix + "Invalid sign facing '" + signFacingName + "'. Falling back to SOUTH.");
                  signFacing = BlockFace.SOUTH;
               }

               float yaw = BlockFaceUtils.getYaw(signFacing);
               Log.warning(logPrefix + "Migrating sign facing '" + String.valueOf(signFacing) + "' to yaw " + TextUtils.format((double)yaw));
               shopkeeperData.set(AbstractShopkeeper.YAW, yaw);
               migrated = true;
            }

            return migrated;
         }

         private boolean isValidSignFacing(ShopObjectData shopObjectData, BlockFace signFacing) throws InvalidDataException {
            Boolean wallSign = (Boolean)shopObjectData.getOrNullIfMissing(SKSignShopObject.WALL_SIGN);
            if (wallSign == null) {
               return true;
            } else {
               return wallSign ? BlockFaceUtils.isWallSignFacing(signFacing) : BlockFaceUtils.isSignPostFacing(signFacing);
            }
         }
      });
   }
}
