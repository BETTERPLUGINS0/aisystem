package emanondev.itemedit.utility;

import java.util.Collection;
import org.bukkit.Keyed;
import org.bukkit.Tag;

public class TagContainer<T extends Keyed> {
   private final Tag<T> tag;

   public Collection<T> getValues() {
      return this.tag.getValues();
   }

   public TagContainer(Tag<T> tag) {
      this.tag = tag;
   }

   public Tag<T> getTag() {
      return this.tag;
   }
}
