package github.nighter.smartspawner.commands.list.gui.list.enums;

import lombok.Generated;

public enum FilterOption {
   ALL("filter.all"),
   ACTIVE("filter.active"),
   INACTIVE("filter.inactive");

   private final String langPath;

   private FilterOption(String param3) {
      this.langPath = langPath;
   }

   public FilterOption getNextOption() {
      FilterOption var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = ACTIVE;
         break;
      case 1:
         var10000 = INACTIVE;
         break;
      case 2:
         var10000 = ALL;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public String getColorPath() {
      return this.langPath + ".color";
   }

   public String getName() {
      return this.name().toLowerCase();
   }

   @Generated
   public String getLangPath() {
      return this.langPath;
   }

   // $FF: synthetic method
   private static FilterOption[] $values() {
      return new FilterOption[]{ALL, ACTIVE, INACTIVE};
   }
}
