package ac.grim.grimac.shaded.maps.weak;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Dynamic extends Weak<Dynamic> {
   String ROOT_KEY = "root";

   static Dynamic from(Object val) {
      if (val == null) {
         return DynamicNothing.INSTANCE;
      } else if (val instanceof Dynamic) {
         return (Dynamic)val;
      } else if (val instanceof Map) {
         return new DynamicMap((Map)val);
      } else if (val instanceof List) {
         return new DynamicList((List)val);
      } else {
         return (Dynamic)(val instanceof Collection ? new DynamicCollection((Collection)val) : new DynamicSomething(val));
      }
   }

   Dynamic get(Object var1);

   Stream<Dynamic> children();

   default Dynamic get(String keyPath, String separator) {
      Dynamic result = this;
      String[] var4 = keyPath.split(Pattern.quote(separator));
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String part = var4[var6];
         result = result.get(part);
      }

      return result;
   }

   default Dynamic dget(String dotSeparatedPath) {
      return this.get(dotSeparatedPath, ".");
   }

   Weak<?> key();

   default Stream<Dynamic> allChildren() {
      return this.allChildrenDepthFirst();
   }

   default Stream<Dynamic> allChildrenDepthFirst() {
      return this.children().flatMap((child) -> {
         return Stream.concat(Stream.of(child), child.allChildrenDepthFirst());
      });
   }

   default Stream<Dynamic> allChildrenBreadthFirst() {
      return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new BreadthChildIterator(this), 16), false);
   }

   default <T> T as(Class<T> type) {
      try {
         return type.cast(this.asObject());
      } catch (ClassCastException var3) {
         throw new ClassCastException(String.format("'root' miscast: %s. Avoid by checking `if (aDynamic.is(%s.class)) ...` or using `aDynamic.maybe().as(%<s.class)`", var3.getMessage(), type.getSimpleName()));
      }
   }
}
