package com.nisovin.shopkeepers.compat;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.inventory.ItemStackComponentsData;
import com.nisovin.shopkeepers.util.inventory.ItemStackMetaTag;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.bukkit.profile.PlayerProfile;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface CompatProvider {
   default void onEnable() {
   }

   default void onDisable() {
   }

   String getVersionId();

   default CompatVersion getCompatVersion() {
      return getCompatVersion(this.getVersionId());
   }

   static CompatVersion getCompatVersion(String versionId) {
      CompatVersion compatVersion = Compat.getCompatVersion(versionId);
      return (CompatVersion)Validate.State.notNull(compatVersion, (String)("Could not find CompatVersion for '" + versionId + "'!"));
   }

   void overwriteLivingEntityAI(LivingEntity var1);

   default boolean supportsCustomMobAI() {
      return true;
   }

   void tickAI(LivingEntity var1, int var2);

   void setOnGround(Entity var1, boolean var2);

   default boolean isNoAIDisablingGravity() {
      return true;
   }

   void setNoclip(Entity var1);

   default void prepareEntity(Entity entity) {
   }

   default void setupSpawnedEntity(Entity entity) {
   }

   default boolean matches(@Nullable @ReadOnly ItemStack provided, @Nullable UnmodifiableItemStack required) {
      return this.matches(provided, ItemUtils.asItemStackOrNull(required));
   }

   boolean matches(@Nullable @ReadOnly ItemStack var1, @Nullable @ReadOnly ItemStack var2);

   void setInventoryViewTitle(InventoryViewBuilder<?> var1, String var2);

   void updateTrades(Player var1);

   ItemStackMetaTag getItemStackMetaTag(@Nullable @ReadOnly ItemStack var1);

   boolean matches(ItemStackMetaTag var1, ItemStackMetaTag var2, boolean var3);

   @Nullable
   ItemStackComponentsData getItemStackComponentsData(@ReadOnly ItemStack var1);

   ItemStack deserializeItemStack(int var1, NamespacedKey var2, int var3, @Nullable ItemStackComponentsData var4);

   <T extends Keyed> Registry<T> getRegistry(Class<T> var1);

   default void setCopperGolemWeatherState(Golem golem, String weatherState) {
   }

   default void setCopperGolemNextWeatheringTick(Golem golem, int tick) {
   }

   default void setMannequinHideDescription(LivingEntity mannequin, boolean hideDescription) {
   }

   default void setMannequinDescription(LivingEntity mannequin, @Nullable String description) {
   }

   default void setMannequinMainHand(LivingEntity mannequin, MainHand mainHand) {
   }

   default void setMannequinPose(LivingEntity mannequin, Pose pose) {
   }

   default void setMannequinProfile(LivingEntity mannequin, @Nullable PlayerProfile profile) {
   }

   default void setZombieNautilusVariant(LivingEntity zombieNautilus, NamespacedKey variant) {
   }

   default NamespacedKey cycleZombieNautilusVariant(NamespacedKey variant, boolean backwards) {
      return variant;
   }
}
