package com.nisovin.shopkeepers.shopobjects.living.types;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.ShopkeeperEditedEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.input.InputRequest;
import com.nisovin.shopkeepers.input.chat.ChatInput;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.ShopObjectData;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectCreationContext;
import com.nisovin.shopkeepers.shopobjects.entity.base.BaseEntityShopObjectType;
import com.nisovin.shopkeepers.shopobjects.living.SKLivingShopObject;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.ui.editor.Button;
import com.nisovin.shopkeepers.ui.editor.EditorView;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperActionButton;
import com.nisovin.shopkeepers.util.bukkit.EntityUtils;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.property.BasicProperty;
import com.nisovin.shopkeepers.util.data.property.Property;
import com.nisovin.shopkeepers.util.data.property.value.PropertyValue;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.ConfigSerializableSerializers;
import com.nisovin.shopkeepers.util.data.serialization.java.EnumSerializers;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.profile.PlayerProfile;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MannequinShop extends SKLivingShopObject<LivingEntity> {
   public static final Property<MainHand> MAIN_HAND;
   public static final Property<Pose> POSE;
   public static final Property<PlayerProfile> PROFILE;
   private final PropertyValue<MainHand> mainHandProperty;
   private final PropertyValue<Pose> poseProperty;
   private final PropertyValue<PlayerProfile> profileProperty;

   public MannequinShop(BaseEntityShopObjectCreationContext context, BaseEntityShopObjectType<MannequinShop> shopObjectType, AbstractShopkeeper shopkeeper, @Nullable ShopCreationData creationData) {
      super(context, shopObjectType, shopkeeper, creationData);
      PropertyValue var10001 = new PropertyValue(MAIN_HAND);
      MannequinShop var10002 = (MannequinShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.mainHandProperty = var10001.onValueChanged(var10002::applyMainHand).build(this.properties);
      var10001 = new PropertyValue(POSE);
      var10002 = (MannequinShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.poseProperty = var10001.onValueChanged(var10002::applyPose).build(this.properties);
      var10001 = new PropertyValue(PROFILE);
      var10002 = (MannequinShop)Unsafe.initialized(this);
      Objects.requireNonNull(var10002);
      this.profileProperty = var10001.onValueChanged(var10002::applyProfile).build(this.properties);
   }

   public void load(ShopObjectData shopObjectData) throws InvalidDataException {
      super.load(shopObjectData);
      this.mainHandProperty.load(shopObjectData);
      this.poseProperty.load(shopObjectData);
      this.profileProperty.load(shopObjectData);
   }

   public void save(ShopObjectData shopObjectData, boolean saveAll) {
      super.save(shopObjectData, saveAll);
      this.mainHandProperty.save(shopObjectData);
      this.poseProperty.save(shopObjectData);
      this.profileProperty.save(shopObjectData);
   }

   protected void onSpawn() {
      super.onSpawn();
      this.applyDescription();
      this.applyMainHand();
      this.applyPose();
      this.applyProfile();
   }

   public List<Button> createEditorButtons() {
      List<Button> editorButtons = super.createEditorButtons();
      editorButtons.add(this.getMainHandEditorButton());
      editorButtons.add(this.getPoseEditorButton());
      editorButtons.add(this.getProfileEditorButton());
      return editorButtons;
   }

   private void applyDescription() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Compat.getProvider().setMannequinHideDescription(entity, true);
      }
   }

   public MainHand getMainHand() {
      return (MainHand)this.mainHandProperty.getValue();
   }

   public void setMainHand(MainHand mainHand) {
      this.mainHandProperty.setValue(mainHand);
   }

   public void cycleMainHand(boolean backwards) {
      this.setMainHand((MainHand)EnumUtils.cycleEnumConstant(MainHand.class, this.getMainHand(), backwards));
   }

   private void applyMainHand() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Compat.getProvider().setMannequinMainHand(entity, this.getMainHand());
      }
   }

   private ItemStack getMainHandEditorItem() {
      ItemStack iconItem = this.getMainHand() == MainHand.LEFT ? ItemUtils.getSkull_MHF_ArrowLeft() : ItemUtils.getSkull_MHF_ArrowRight();
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonMannequinMainHand, Messages.buttonMannequinMainHandLore);
      return iconItem;
   }

   private Button getMainHandEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return MannequinShop.this.getMainHandEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            MannequinShop.this.cycleMainHand(backwards);
            return true;
         }
      };
   }

   public Pose getPose() {
      return (Pose)this.poseProperty.getValue();
   }

   public void setPose(Pose pose) {
      this.poseProperty.setValue(pose);
   }

   public void cyclePose(boolean backwards) {
      this.setPose((Pose)EnumUtils.cycleEnumConstant(Pose.class, this.getPose(), backwards, EntityUtils::isValidMannequinPose));
   }

   private void applyPose() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Compat.getProvider().setMannequinPose(entity, this.getPose());
      }
   }

   private ItemStack getPoseEditorItem() {
      ItemStack iconItem = new ItemStack(Material.LEATHER_BOOTS);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonMannequinPose, Messages.buttonMannequinPoseLore);
      return iconItem;
   }

   private Button getPoseEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return MannequinShop.this.getPoseEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            boolean backwards = clickEvent.isRightClick();
            MannequinShop.this.cyclePose(backwards);
            return true;
         }
      };
   }

   @Nullable
   public PlayerProfile getProfile() {
      return (PlayerProfile)this.profileProperty.getValue();
   }

   public void setProfile(@Nullable PlayerProfile profile) {
      this.profileProperty.setValue(profile);
   }

   private void applyProfile() {
      LivingEntity entity = (LivingEntity)this.getEntity();
      if (entity != null) {
         Compat.getProvider().setMannequinProfile(entity, this.getProfile());
      }
   }

   private ItemStack getProfileEditorItem() {
      ItemStack iconItem = new ItemStack(Material.PLAYER_HEAD);
      ItemUtils.setDisplayNameAndLore(iconItem, Messages.buttonMannequinProfile, Messages.buttonMannequinProfileLore);
      return iconItem;
   }

   private Button getProfileEditorButton() {
      return new ShopkeeperActionButton() {
         @Nullable
         public ItemStack getIcon(EditorView editorView) {
            return MannequinShop.this.getProfileEditorItem();
         }

         protected boolean runAction(EditorView editorView, InventoryClickEvent clickEvent) {
            editorView.closeDelayed();
            Player player = editorView.getPlayer();
            ChatInput chatInput = SKShopkeepersPlugin.getInstance().getChatInput();
            chatInput.request(player, new InputRequest<String>(player) {
               // $FF: synthetic field
               final Player val$player;
               // $FF: synthetic field
               final <undefinedtype> this$1;

               {
                  this.this$1 = this$1;
                  this.val$player = var2;
               }

               public void onInput(String input) {
                  this.this$1.this$0.handleProfileInput(this.val$player, input);
               }
            });
            TextUtils.sendMessage(player, (Text)Messages.mannequinEnterProfile);
            return true;
         }
      };
   }

   private void handleProfileInput(Player player, String input) {
      String preparedInput = input.trim();
      if (this.shopkeeper.isValid() && !preparedInput.isEmpty() && !preparedInput.equals("!")) {
         if (preparedInput.equals("-")) {
            this.updateProfile(player, preparedInput, (PlayerProfile)null);
         } else {
            UUID uuid = ConversionUtils.parseUUID(preparedInput);
            if (uuid == null && !Compat.getProvider().getCompatVersion().isPaper()) {
               OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(preparedInput);
               uuid = offlinePlayer.getUniqueId();
            }

            PlayerProfile profile = uuid != null ? Bukkit.createPlayerProfile(uuid, "") : Bukkit.createPlayerProfile(preparedInput);
            SKShopkeepersPlugin plugin = SKShopkeepersPlugin.getInstance();
            profile.update().whenComplete((updatedProfile, e) -> {
               if (e != null) {
                  Log.debug("Failed to update player profile: " + preparedInput, e);
               } else {
                  SchedulerUtils.runOnMainThreadOrOmit(plugin, () -> {
                     this.updateProfile(player, preparedInput, updatedProfile);
                  });
               }
            });
         }
      } else {
         TextUtils.sendMessage(player, (Text)Messages.mannequinEnterProfileCanceled);
      }
   }

   private void updateProfile(Player player, String input, @Nullable PlayerProfile profile) {
      if (!this.shopkeeper.isValid()) {
         TextUtils.sendMessage(player, (Text)Messages.mannequinEnterProfileCanceled);
      } else if (profile != null && !profile.isComplete()) {
         TextUtils.sendMessage(player, (Text)Messages.mannequinProfileInvalid, (Object[])("input", input));
      } else {
         this.setProfile(profile);
         if (profile == null) {
            TextUtils.sendMessage(player, (Text)Messages.mannequinProfileCleared);
         } else {
            String profileName = (String)Unsafe.assertNonNull(profile.getName());
            TextUtils.sendMessage(player, (Text)Messages.mannequinProfileSet, (Object[])("profileName", profileName));
         }

         Bukkit.getPluginManager().callEvent(new ShopkeeperEditedEvent(this.shopkeeper, player));
         this.shopkeeper.save();
      }
   }

   static {
      MAIN_HAND = (new BasicProperty()).dataKeyAccessor("mainHand", EnumSerializers.lenient(MainHand.class)).useDefaultIfMissing().defaultValue(MainHand.RIGHT).build();
      POSE = (new BasicProperty()).dataKeyAccessor("pose", EnumSerializers.lenient(Pose.class)).validator((pose) -> {
         Validate.isTrue(EntityUtils.isValidMannequinPose(pose), "pose is invalid for mannequin: " + pose.name());
      }).useDefaultIfMissing().defaultValue(Pose.STANDING).build();
      PROFILE = (new BasicProperty()).dataKeyAccessor("profile", ConfigSerializableSerializers.strict(PlayerProfile.class)).nullable().build();
   }
}
