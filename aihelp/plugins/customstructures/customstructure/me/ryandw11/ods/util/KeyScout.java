package me.ryandw11.ods.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeyScout {
   private List<KeyScoutChild> children = new ArrayList();
   private KeyScoutChild end;

   public void addChild(KeyScoutChild child) {
      this.children.add(child);
   }

   public List<KeyScoutChild> getChildren() {
      return this.children;
   }

   public KeyScoutChild getChildByName(String name) {
      Iterator var2 = this.children.iterator();

      KeyScoutChild child;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         child = (KeyScoutChild)var2.next();
      } while(!child.getName().equals(name));

      return child;
   }

   public KeyScoutChild getEnd() {
      return this.end;
   }

   public void setEnd(KeyScoutChild end) {
      this.end = end;
   }

   public void removeAmount(int size) {
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         KeyScoutChild child = (KeyScoutChild)var2.next();
         child.removeSize(size);
      }

   }

   public void addAmount(int size) {
      Iterator var2 = this.children.iterator();

      while(var2.hasNext()) {
         KeyScoutChild child = (KeyScoutChild)var2.next();
         child.addSize(size);
      }

   }
}
