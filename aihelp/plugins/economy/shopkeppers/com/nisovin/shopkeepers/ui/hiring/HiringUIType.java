package com.nisovin.shopkeepers.ui.hiring;

import com.nisovin.shopkeepers.ui.lib.AbstractUIType;

public final class HiringUIType extends AbstractUIType {
   public static final HiringUIType INSTANCE = new HiringUIType();

   private HiringUIType() {
      super("hiring", "shopkeeper.hire");
   }
}
