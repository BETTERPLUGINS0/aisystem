package emanondev.itemtag.activity.arguments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringListArgument extends Argument {
   private final List<String> values;
   private final String separator;

   public StringListArgument(Collection<String> values) {
      this(values, ";");
   }

   public StringListArgument(@NotNull Collection<String> values, @NotNull String separator) {
      this.values = new ArrayList();
      this.values.addAll(values);
      this.separator = separator;
   }

   public StringListArgument(@Nullable String info) {
      this(info, ";");
   }

   public StringListArgument(@Nullable String info, @NotNull String separator) {
      this.values = new ArrayList();
      if (info != null) {
         String[] var3 = info.split(separator);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String value = var3[var5];
            if (!value.isEmpty()) {
               this.values.add(value);
            }
         }
      }

      this.separator = separator;
   }

   public boolean contains(String value) {
      return this.values.contains(value);
   }

   public boolean isEmpty() {
      return this.values.isEmpty();
   }

   public List<String> getValues() {
      return Collections.unmodifiableList(this.values);
   }

   public String toString() {
      StringBuilder b = new StringBuilder();

      String arg;
      for(Iterator var2 = this.values.iterator(); var2.hasNext(); b.append(arg).append(this.separator)) {
         arg = (String)var2.next();
         if (b.length() != 0) {
            b.append(this.separator);
         }
      }

      return b.toString();
   }
}
