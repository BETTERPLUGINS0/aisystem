package emanondev.itemtag.activity;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Factory {
   private final String id;

   public Factory(String id) {
      if (!Pattern.compile("[a-z][_a-z0-9]*").matcher(id).matches()) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
      }
   }

   @NotNull
   public final String getId() {
      return this.id;
   }

   public class Element {
      private final String info;

      public Element(@Nullable String param2) {
         this.info = info;
      }

      @Nullable
      public final String getInfo() {
         return this.info;
      }

      @NotNull
      public final String getId() {
         return Factory.this.getId();
      }
   }
}
