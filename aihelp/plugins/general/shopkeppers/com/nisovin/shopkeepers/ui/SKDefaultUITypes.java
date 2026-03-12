package com.nisovin.shopkeepers.ui;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.ui.DefaultUITypes;
import com.nisovin.shopkeepers.ui.confirmations.ConfirmationUIType;
import com.nisovin.shopkeepers.ui.editor.EditorUIType;
import com.nisovin.shopkeepers.ui.equipmentEditor.EquipmentEditorUIType;
import com.nisovin.shopkeepers.ui.hiring.HiringUIType;
import com.nisovin.shopkeepers.ui.lib.AbstractUIType;
import com.nisovin.shopkeepers.ui.trading.TradingUIType;
import com.nisovin.shopkeepers.ui.villager.editor.VillagerEditorUIType;
import com.nisovin.shopkeepers.ui.villager.equipmentEditor.VillagerEquipmentEditorUIType;
import java.util.ArrayList;
import java.util.List;

public final class SKDefaultUITypes implements DefaultUITypes {
   private final EditorUIType editorUIType;
   private final EquipmentEditorUIType equipmentEditorUIType;
   private final TradingUIType tradingUIType;
   private final HiringUIType hiringUIType;
   private final VillagerEditorUIType villagerEditorUIType;
   private final VillagerEquipmentEditorUIType villagerEquipmentEditorUIType;
   private final ConfirmationUIType confirmationUIType;

   public SKDefaultUITypes() {
      this.editorUIType = EditorUIType.INSTANCE;
      this.equipmentEditorUIType = EquipmentEditorUIType.INSTANCE;
      this.tradingUIType = TradingUIType.INSTANCE;
      this.hiringUIType = HiringUIType.INSTANCE;
      this.villagerEditorUIType = VillagerEditorUIType.INSTANCE;
      this.villagerEquipmentEditorUIType = VillagerEquipmentEditorUIType.INSTANCE;
      this.confirmationUIType = ConfirmationUIType.INSTANCE;
   }

   public List<? extends AbstractUIType> getAllUITypes() {
      List<AbstractUIType> defaults = new ArrayList();
      defaults.add(this.editorUIType);
      defaults.add(this.tradingUIType);
      defaults.add(this.hiringUIType);
      defaults.add(this.villagerEditorUIType);
      return defaults;
   }

   public EditorUIType getEditorUIType() {
      return this.editorUIType;
   }

   public EquipmentEditorUIType getEquipmentEditorUIType() {
      return this.equipmentEditorUIType;
   }

   public TradingUIType getTradingUIType() {
      return this.tradingUIType;
   }

   public HiringUIType getHiringUIType() {
      return this.hiringUIType;
   }

   public VillagerEditorUIType getVillagerEditorUIType() {
      return this.villagerEditorUIType;
   }

   public VillagerEquipmentEditorUIType getVillagerEquipmentEditorUIType() {
      return this.villagerEquipmentEditorUIType;
   }

   public ConfirmationUIType getConfirmationUIType() {
      return this.confirmationUIType;
   }

   public static SKDefaultUITypes getInstance() {
      return SKShopkeepersPlugin.getInstance().getDefaultUITypes();
   }

   public static EditorUIType EDITOR() {
      return getInstance().getEditorUIType();
   }

   public static EquipmentEditorUIType EQUIPMENT_EDITOR() {
      return getInstance().getEquipmentEditorUIType();
   }

   public static TradingUIType TRADING() {
      return getInstance().getTradingUIType();
   }

   public static HiringUIType HIRING() {
      return getInstance().getHiringUIType();
   }

   public static VillagerEditorUIType VILLAGER_EDITOR() {
      return getInstance().getVillagerEditorUIType();
   }

   public static VillagerEquipmentEditorUIType VILLAGER_EQUIPMENT_EDITOR() {
      return getInstance().getVillagerEquipmentEditorUIType();
   }

   public static ConfirmationUIType CONFIRMATION() {
      return getInstance().getConfirmationUIType();
   }
}
