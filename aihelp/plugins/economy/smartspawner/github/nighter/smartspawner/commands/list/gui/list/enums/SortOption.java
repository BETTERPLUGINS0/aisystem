package github.nighter.smartspawner.commands.list.gui.list.enums;

import lombok.Generated;

public enum SortOption {
   DEFAULT("sort.default"),
   STACK_SIZE_DESC("sort.stack_size_desc"),
   STACK_SIZE_ASC("sort.stack_size_asc");

   private final String langPath;

   private SortOption(String param3) {
      this.langPath = langPath;
   }

   public SortOption getNextOption() {
      SortOption var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = STACK_SIZE_DESC;
         break;
      case 1:
         var10000 = STACK_SIZE_ASC;
         break;
      case 2:
         var10000 = DEFAULT;
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
   private static SortOption[] $values() {
      return new SortOption[]{DEFAULT, STACK_SIZE_DESC, STACK_SIZE_ASC};
   }
}
