package com.nisovin.shopkeepers.api.ui;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import java.util.List;

public interface DefaultUITypes {
   List<? extends UIType> getAllUITypes();

   UIType getEditorUIType();

   UIType getEquipmentEditorUIType();

   UIType getTradingUIType();

   UIType getHiringUIType();

   static DefaultUITypes getInstance() {
      return ShopkeepersPlugin.getInstance().getDefaultUITypes();
   }

   static UIType EDITOR() {
      return getInstance().getEditorUIType();
   }

   static UIType EQUIPMENT_EDITOR() {
      return getInstance().getTradingUIType();
   }

   static UIType TRADING() {
      return getInstance().getTradingUIType();
   }

   static UIType HIRING() {
      return getInstance().getHiringUIType();
   }
}
