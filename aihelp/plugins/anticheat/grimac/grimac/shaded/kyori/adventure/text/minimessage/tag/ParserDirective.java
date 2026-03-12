package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ParserDirective extends Tag {
   Tag RESET = new ParserDirective() {
      public String toString() {
         return "ParserDirective.RESET";
      }
   };
}
