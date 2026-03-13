package com.volmit.iris.util.inventorygui;

import com.volmit.iris.util.data.MaterialBlock;
import org.bukkit.Material;

public class UIStaticDecorator implements WindowDecorator {
   private final Element element;

   public UIStaticDecorator(Element element) {
      this.element = (Element)(var1 == null ? (new UIElement("bg")).setMaterial(new MaterialBlock(Material.AIR)) : var1);
   }

   public Element onDecorateBackground(Window window, int position, int row) {
      return this.element;
   }
}
