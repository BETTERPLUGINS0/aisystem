package advancedplugins.pm2.cv.api.item;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.util.reflection.EnumReflection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeConfiguration implements ConfigurationSectionWritable {
   @NotNull
   private final String result;
   @NotNull
   private final RecipeConfiguration.Shape shape;

   public static RecipeConfiguration load(ConfigurationSection var0) {
      String var1 = var0.getString("result");
      if (StringUtils.isBlank(var1)) {
         throw new InvalidConfigurationException("invalid result");
      } else {
         RecipeConfiguration.Shaped var2 = null;
         RecipeConfiguration.Shapeless var3 = null;
         if (var0.contains("shaped")) {
            var2 = RecipeConfiguration.Shaped.parse(var0, "shaped");
         } else if (var0.contains("shapeless")) {
            var3 = RecipeConfiguration.Shapeless.parse(var0, "shapeless");
         }

         if (var2 != null) {
            return new RecipeConfiguration(var1, var2);
         } else if (var3 != null) {
            return new RecipeConfiguration(var1, var3);
         } else {
            throw new InvalidConfigurationException("a valid shape must be set");
         }
      }
   }

   public RecipeConfiguration(@NotNull String var1, @NotNull RecipeConfiguration.Shape var2) {
      this.result = var1;
      this.shape = var2;
   }

   @Nullable
   public Recipe getRecipe() {
      NamespacedKey var1 = new NamespacedKey(InfiniteVehicles.getPlugin(), UUID.randomUUID().toString());
      ItemConfiguration var2 = (ItemConfiguration)Registries.getRegistry(ItemConfiguration.class).get(this.result);
      if (var2 == null) {
         return null;
      } else if (this.shape instanceof RecipeConfiguration.Shaped) {
         RecipeConfiguration.Shaped var8 = (RecipeConfiguration.Shaped)this.shape;
         ShapedRecipe var9 = new ShapedRecipe(var1, var2.getItemStack());
         var9.shape(new String[]{"012", "345", "678"});

         for(int var10 = 0; var10 < var8.value.length; ++var10) {
            String var11 = var8.value[var10];
            RecipeChoice var7 = this.createChoice(var11);
            if (var7 != null) {
               var9.setIngredient(String.valueOf(var10).charAt(0), var7);
            }
         }

         return var9;
      } else if (this.shape instanceof RecipeConfiguration.Shapeless) {
         ShapelessRecipe var3 = new ShapelessRecipe(var1, var2.getItemStack());
         Iterator var4 = ((RecipeConfiguration.Shapeless)this.shape).value.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            RecipeChoice var6 = this.createChoice(var5);
            if (var6 != null) {
               var3.addIngredient(var6);
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   private RecipeChoice createChoice(String var1) {
      if (StringUtils.isBlank(var1)) {
         return null;
      } else {
         ItemConfiguration var2 = (ItemConfiguration)Registries.getRegistry(ItemConfiguration.class).get(var1);
         if (var2 != null) {
            ItemStack var4 = var2.getItemStack();
            var4.setAmount(1);
            return new ExactChoice(var4);
         } else {
            Material var3 = (Material)EnumReflection.getEnumConstant(Material.class, var1.trim().toUpperCase());
            return var3 != null ? new MaterialChoice(var3) : null;
         }
      }
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("result", this.result);
      this.shape.write(var1, this.shape instanceof RecipeConfiguration.Shaped ? "shaped" : "shapeless");
   }

   public static class Shaped extends RecipeConfiguration.Shape {
      @NotNull
      private final String[] value = new String[9];

      public static RecipeConfiguration.Shaped parse(@NotNull ConfigurationSection var0, @NotNull String var1) {
         List var2 = var0.getStringList(var1);
         String[] var3 = new String[9];
         boolean var4 = true;

         for(int var5 = 0; var5 < var2.size() && var5 < 3; ++var5) {
            String var6 = (String)var2.get(var5);
            if (!StringUtils.isBlank(var6)) {
               int var7 = 0;

               while(true) {
                  int var8 = var6.indexOf(91);
                  int var9 = var6.indexOf(93);
                  if (var8 == -1 || var9 == -1 || var8 > var9) {
                     break;
                  }

                  String var10 = var6.substring(var8 + 1, var9).trim().replace("[", "").replace("]", "");
                  if (StringUtils.isNotBlank(var10)) {
                     var4 = false;
                     var3[var5 * 3 + var7] = var10;
                  }

                  var6 = var6.length() > var9 + 1 ? var6.substring(var9 + 1) : "";
                  if (var7 + 1 >= 3) {
                     break;
                  }

                  ++var7;
               }
            }
         }

         if (var4) {
            throw new InvalidConfigurationException("at least one ingredient must be set");
         } else {
            return new RecipeConfiguration.Shaped(var3);
         }
      }

      public Shaped(@NotNull String[] var1) {
         if (var1.length != this.value.length) {
            throw new InvalidConfigurationException("invalid value");
         } else {
            System.arraycopy(var1, 0, this.value, 0, this.value.length);
         }
      }

      public void write(@NotNull ConfigurationSection var1, @NotNull String var2) {
         ArrayList var3 = new ArrayList();

         for(int var4 = 0; var4 < 3; ++var4) {
            StringBuilder var5 = new StringBuilder();

            for(int var6 = 0; var6 < 3; ++var6) {
               var5.append('[');
               String var7 = this.value[var4 * 3 + var6];
               if (StringUtils.isNotBlank(var7)) {
                  var5.append(var7);
               }

               var5.append(']');
            }

            var3.add(var5.toString());
         }

         var1.set(var2, var3);
      }
   }

   public static class Shapeless extends RecipeConfiguration.Shape {
      @NotNull
      private final Set<String> value = new LinkedHashSet();

      public static RecipeConfiguration.Shapeless parse(@NotNull ConfigurationSection var0, @NotNull String var1) {
         ArrayList var2 = new ArrayList(var0.getStringList(var1));
         if (var2.size() == 0) {
            throw new InvalidConfigurationException("at least one ingredient must be set");
         } else {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               String var4 = (String)var2.get(var3);
               if (StringUtils.isNotBlank(var4)) {
                  var2.set(var3, var4.trim());
               }
            }

            return new RecipeConfiguration.Shapeless(var2);
         }
      }

      public Shapeless(@NotNull Collection<String> var1) {
         if (var1.size() == 0) {
            throw new InvalidConfigurationException("invalid value");
         } else {
            this.value.addAll(var1);
         }
      }

      public void write(@NotNull ConfigurationSection var1, @NotNull String var2) {
         var1.set(var2, new ArrayList(this.value));
      }
   }

   public abstract static class Shape {
      public abstract void write(@NotNull ConfigurationSection var1, @NotNull String var2);
   }
}
