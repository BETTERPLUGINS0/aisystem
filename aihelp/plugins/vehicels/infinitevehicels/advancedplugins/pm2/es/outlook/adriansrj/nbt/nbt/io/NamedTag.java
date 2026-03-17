package es.outlook.adriansrj.nbt.nbt.io;

import es.outlook.adriansrj.nbt.nbt.tag.Tag;

public class NamedTag {
   private String name;
   private Tag<?> tag;

   public NamedTag(String var1, Tag<?> var2) {
      this.name = var1;
      this.tag = var2;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setTag(Tag<?> var1) {
      this.tag = var1;
   }

   public String getName() {
      return this.name;
   }

   public Tag<?> getTag() {
      return this.tag;
   }
}
