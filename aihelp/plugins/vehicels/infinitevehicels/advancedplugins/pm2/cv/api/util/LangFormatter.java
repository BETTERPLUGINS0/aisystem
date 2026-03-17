package advancedplugins.pm2.cv.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class LangFormatter {
   private static final char ARGUMENT_CHAR = '%';
   @NotNull
   protected final String context;

   public static LangFormatter single(@NotNull String var0) {
      return new LangFormatter.Single(var0);
   }

   public static LangFormatter multi(@NotNull String var0) {
      return new LangFormatter.Multi(var0);
   }

   public LangFormatter(@NotNull String var1) {
      this.context = var1;
   }

   public abstract LangFormatter arg(char var1, @NotNull String var2);

   @NotNull
   public abstract String format();

   public String toString() {
      return this.format();
   }

   public static class Single extends LangFormatter {
      @NotNull
      private final Map<Character, String> arguments = new HashMap();

      public Single(@NotNull String var1) {
         super(var1);
      }

      public LangFormatter arg(char var1, @NotNull String var2) {
         if (var1 == '%') {
            throw new IllegalArgumentException("unsupported character");
         } else {
            this.arguments.put(Character.toLowerCase(var1), var2);
            return this;
         }
      }

      @NotNull
      public String format() {
         StringBuilder var1 = new StringBuilder();
         char[] var2 = this.context.toCharArray();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] == '%' && var3 + 1 < var2.length) {
               char var4 = Character.toLowerCase(var2[var3 + 1]);
               String var5 = (String)this.arguments.get(var4);
               if (var5 != null) {
                  var1.append(var5);
               }

               ++var3;
            } else {
               var1.append(var2[var3]);
            }
         }

         return var1.toString();
      }
   }

   public static class Multi extends LangFormatter {
      @NotNull
      private final Map<Character, List<String>> arguments = new HashMap();

      public Multi(@NotNull String var1) {
         super(var1);
      }

      public LangFormatter arg(char var1, @NotNull String var2) {
         if (var1 == '%') {
            throw new IllegalArgumentException("unsupported character");
         } else {
            ((List)this.arguments.computeIfAbsent(Character.toLowerCase(var1), (var0) -> {
               return new ArrayList();
            })).add(var2);
            return this;
         }
      }

      @NotNull
      public String format() {
         StringBuilder var1 = new StringBuilder();
         char[] var2 = this.context.toCharArray();
         HashMap var3 = new HashMap(this.arguments);

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] == '%' && var4 + 1 < var2.length) {
               char var5 = Character.toLowerCase(var2[var4 + 1]);
               List var6 = (List)var3.get(var5);
               if (var6 != null && var6.size() > 0) {
                  var1.append((String)var6.remove(0));
               }

               ++var4;
            } else {
               var1.append(var2[var4]);
            }
         }

         return var1.toString();
      }
   }
}
