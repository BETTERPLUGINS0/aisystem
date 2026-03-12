package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.text.Text;

public interface ViewContext {
   String getName();

   Object getObject();

   default String getLogPrefix() {
      return this.getName() + ": ";
   }

   boolean isValid();

   Text getNoLongerValidMessage();
}
