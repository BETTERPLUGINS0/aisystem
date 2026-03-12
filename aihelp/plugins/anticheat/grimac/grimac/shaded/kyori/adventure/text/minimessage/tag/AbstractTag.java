package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag;

import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.examination.Examinable;

abstract class AbstractTag implements Tag, Examinable {
   public final String toString() {
      return Internals.toString(this);
   }
}
