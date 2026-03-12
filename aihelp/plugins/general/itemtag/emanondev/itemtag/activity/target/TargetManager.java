package emanondev.itemtag.activity.target;

import emanondev.itemtag.activity.Manager;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetManager extends Manager<TargetType, TargetType.Target> {
   public static final String OWNER_TARGET = "owner";
   public static final String TARGET_TARGET = "target";
   public static final String ENTITY_TARGET = "entity";
   public static final String BLOCK_TARGET = "block";
   public static final String EVENT_TARGET = "event";
   public static final String PROJECTILE_TARGET = "projectile";
   public static final String PLAYER_TARGET = "player";
   public static final String LOCATION_TARGET = "location";

   public TargetManager() {
      super("Target");
   }

   @Nullable
   public static String extractTarget(@NotNull String line) throws IllegalArgumentException {
      if (!line.contains("@")) {
         return null;
      } else {
         int start = line.indexOf("@");
         int parenthesis = 0;
         int end = -1;

         label39:
         for(int i = start + 1; i < line.length(); ++i) {
            switch(line.charAt(i)) {
            case ' ':
               if (parenthesis == 0) {
                  if (i == start + 1) {
                     throw new IllegalArgumentException();
                  }

                  end = i;
                  break label39;
               }
               break;
            case '(':
               ++parenthesis;
               break;
            case ')':
               --parenthesis;
               if (parenthesis < 0) {
                  throw new IllegalArgumentException();
               }

               if (parenthesis == 0) {
                  end = i;
                  break label39;
               }
            }
         }

         if (end == -1) {
            throw new IllegalArgumentException();
         } else {
            return line.substring(start, end + 1);
         }
      }
   }

   public void load() {
      this.register(new LocationTargetType());
      this.register(new OwnerTargetType());
   }

   @Nullable
   public TargetType getType(@NotNull String id) {
      return id.startsWith("@") ? (TargetType)super.getType(id.substring(1)) : (TargetType)super.getType(id);
   }

   @NotNull
   public TargetType.Target read(@NotNull String target, @NotNull HashMap<String, TargetType.Target> baseTargets) throws IllegalArgumentException {
      if (target.startsWith("@")) {
         target = target.substring(1);
      }

      while(target.endsWith(" ")) {
         target = target.substring(0, target.length() - 1);
      }

      if (!target.contains("(")) {
         if (target.contains(" ")) {
            throw new IllegalArgumentException();
         } else {
            TargetType type = this.getType(target);
            return type != null ? type.read((String)null) : (TargetType.Target)baseTargets.get(target.toLowerCase(Locale.ENGLISH));
         }
      } else {
         int parenthesis = 1;
         int finalIndex = -1;
         int startIndex = target.indexOf("(");

         for(int i = startIndex + 1; i < target.length(); ++i) {
            if (target.charAt(i) == '(') {
               ++parenthesis;
            }

            if (target.charAt(i) == ')') {
               --parenthesis;
            }

            if (parenthesis == 0) {
               finalIndex = i;
               break;
            }
         }

         if (finalIndex == -1) {
            throw new IllegalArgumentException();
         } else {
            String name = target.substring(0, startIndex);
            String info = target.substring(startIndex + 1, finalIndex);
            target = target.substring(startIndex + 1, finalIndex);
            TargetType type = this.getType(name);
            return type != null ? type.read(info) : (TargetType.Target)baseTargets.get(target.toLowerCase(Locale.ENGLISH));
         }
      }
   }
}
