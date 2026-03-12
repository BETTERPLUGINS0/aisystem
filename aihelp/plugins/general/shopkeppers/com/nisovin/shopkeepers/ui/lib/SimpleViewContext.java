package com.nisovin.shopkeepers.ui.lib;

import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.java.Validate;

public class SimpleViewContext implements ViewContext {
   private final String name;

   public SimpleViewContext(String name) {
      Validate.notEmpty(name, "name is empty");
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public Object getObject() {
      return this;
   }

   public boolean isValid() {
      return true;
   }

   public Text getNoLongerValidMessage() {
      return Text.EMPTY;
   }
}
