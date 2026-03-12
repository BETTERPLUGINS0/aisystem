package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ParserDirective extends Tag {
   Tag RESET = new ParserDirective() {
      public String toString() {
         return "ParserDirective.RESET";
      }
   };
}
