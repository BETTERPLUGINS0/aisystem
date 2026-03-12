package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;

abstract class AbstractTag implements Tag, Examinable {
   public final String toString() {
      return Internals.toString(this);
   }
}
