package advancedplugins.pm2.cv.api.vehicle.configuration.model.compound;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.Identifiable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RigConfiguration implements ConfigurationSectionWritable {
   private final Map<UUID, RigConfiguration.Element<?>> elementMap = new HashMap();

   public static RigConfiguration.Builder builder() {
      return new RigConfiguration.Builder();
   }

   @Nullable
   public static RigConfiguration load(@NotNull ConfigurationSection var0, @NotNull CompoundModelConfiguration var1) {
      Set var2 = ConfigurationUtil.getConfigurationSections(var0, false);
      HashMap var3 = new HashMap();
      Iterator var4 = var2.iterator();

      ConfigurationSection var5;
      RigConfiguration.Element var6;
      while(var4.hasNext()) {
         var5 = (ConfigurationSection)var4.next();
         var6 = RigConfiguration.Element.of(var5, var1);
         if (var6 != null && !var3.containsKey(var6.identifier)) {
            var3.put(var6.identifier, var6);
         }
      }

      var4 = var2.iterator();

      while(var4.hasNext()) {
         var5 = (ConfigurationSection)var4.next();
         var6 = (RigConfiguration.Element)var3.get(Identifiable.loadIdentifier(var5));
         if (var6 != null) {
            try {
               UUID var7 = UUID.fromString(var5.getString("parent", ""));
               RigConfiguration.Element var8 = (RigConfiguration.Element)var3.get(var7);
               if (var8 instanceof RigConfiguration.BoneElement) {
                  var6.parent = (RigConfiguration.BoneElement)var8;
               }
            } catch (IllegalArgumentException var13) {
            }
         }
      }

      var4 = var2.iterator();

      while(true) {
         RigConfiguration.BoneElement var14;
         do {
            if (!var4.hasNext()) {
               return var3.size() > 0 ? new RigConfiguration(var3.values()) : null;
            }

            var5 = (ConfigurationSection)var4.next();
            var6 = (RigConfiguration.Element)var3.get(Identifiable.loadIdentifier(var5));
            var14 = var6 instanceof RigConfiguration.BoneElement ? (RigConfiguration.BoneElement)var6 : null;
         } while(var14 == null);

         Iterator var15 = var5.getStringList("children").iterator();

         while(var15.hasNext()) {
            String var9 = (String)var15.next();

            try {
               UUID var10 = UUID.fromString(var9);
               RigConfiguration.Element var11 = (RigConfiguration.Element)var3.get(var10);
               if (var11 != null) {
                  var14.children.add(var11);
               }
            } catch (IllegalArgumentException var12) {
            }
         }
      }
   }

   RigConfiguration(@NotNull Collection<RigConfiguration.Element<?>> var1) {
      if (var1.size() == 0) {
         throw new InvalidConfigurationException("malformed rig");
      } else {
         int var2 = 0;
         Iterator var3 = var1.iterator();

         RigConfiguration.Element var4;
         while(var3.hasNext()) {
            var4 = (RigConfiguration.Element)var3.next();
            if (var4 instanceof RigConfiguration.PartElement && var4.parent == null) {
               throw new InvalidConfigurationException("malformed rig (part has no parent bone: " + String.valueOf(var4.identifier) + ")");
            }

            if (var4 instanceof RigConfiguration.BoneElement && var4.parent == null) {
               ++var2;
            }
         }

         if (var2 == 0) {
            throw new InvalidConfigurationException("malformed rig");
         } else {
            var3 = var1.iterator();

            while(var3.hasNext()) {
               var4 = (RigConfiguration.Element)var3.next();
               this.elementMap.put(var4.identifier, var4);
            }

         }
      }
   }

   public boolean contains(@NotNull UUID var1) {
      return this.elementMap.containsKey(var1);
   }

   public boolean contains(@NotNull PartConfiguration var1) {
      return this.contains(var1.getIdentifier());
   }

   public boolean contains(@NotNull BoneConfiguration var1) {
      return this.contains(var1.getIdentifier());
   }

   @NotNull
   public List<BoneConfiguration> getAncestors(@NotNull BoneConfiguration var1) {
      ArrayList var2 = new ArrayList();
      RigConfiguration.BoneElement var3 = this.getWrapper(var1);

      for(RigConfiguration.BoneElement var4 = var3.parent; var4 != null; var4 = var4.parent) {
         var2.add((BoneConfiguration)var4.value);
      }

      Collections.reverse(var2);
      return var2;
   }

   @NotNull
   public List<BoneConfiguration> getHierarchyUp(@NotNull BoneConfiguration var1) {
      List var2 = this.getAncestors(var1);
      var2.add(var1);
      return var2;
   }

   @NotNull
   public List<BoneConfiguration> getHierarchyDown(@NotNull BoneConfiguration var1) {
      List var2 = this.getChildrenBones(var1, true);
      var2.add(0, var1);
      return var2;
   }

   @NotNull
   public List<BoneConfiguration> getChildrenBones(@NotNull BoneConfiguration var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      RigConfiguration.BoneElement var4 = this.getWrapper(var1);
      Iterator var5 = this.getChildrenBones(var4, var2).iterator();

      while(var5.hasNext()) {
         RigConfiguration.BoneElement var6 = (RigConfiguration.BoneElement)var5.next();
         var3.add((BoneConfiguration)var6.value);
      }

      return var3;
   }

   @NotNull
   public List<PartConfiguration> getChildrenParts(@NotNull BoneConfiguration var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      RigConfiguration.BoneElement var4 = this.getWrapper(var1);
      Iterator var5 = this.getChildrenParts(var4, var2).iterator();

      while(var5.hasNext()) {
         RigConfiguration.PartElement var6 = (RigConfiguration.PartElement)var5.next();
         var3.add((PartConfiguration)var6.value);
      }

      return var3;
   }

   @Nullable
   public BoneConfiguration getParent(@NotNull BoneConfiguration var1) {
      RigConfiguration.BoneElement var2 = this.getWrapper(var1);
      RigConfiguration.BoneElement var3 = var2.parent;
      return var3 != null ? (BoneConfiguration)var3.value : null;
   }

   @Nullable
   public BoneConfiguration getParent(@NotNull PartConfiguration var1) {
      return (BoneConfiguration)this.getWrapper(var1).parent.value;
   }

   public void write(@NotNull ConfigurationSection var1) {
      Iterator var2 = this.elementMap.values().iterator();

      while(var2.hasNext()) {
         RigConfiguration.Element var3 = (RigConfiguration.Element)var2.next();
         var3.write(var1.createSection(var3.identifier.toString()));
      }

   }

   private List<RigConfiguration.BoneElement> getChildrenBones(RigConfiguration.BoneElement var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.children.iterator();

      while(var4.hasNext()) {
         RigConfiguration.Element var5 = (RigConfiguration.Element)var4.next();
         if (var5 instanceof RigConfiguration.BoneElement) {
            var3.add((RigConfiguration.BoneElement)var5);
            if (var2) {
               var3.addAll(this.getChildrenBones((RigConfiguration.BoneElement)var5, true));
            }
         }
      }

      return var3;
   }

   private List<RigConfiguration.PartElement> getChildrenParts(RigConfiguration.BoneElement var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.children.iterator();

      while(var4.hasNext()) {
         RigConfiguration.Element var5 = (RigConfiguration.Element)var4.next();
         if (var5 instanceof RigConfiguration.PartElement) {
            var3.add((RigConfiguration.PartElement)var5);
         } else if (var5 instanceof RigConfiguration.BoneElement && var2) {
            var3.addAll(this.getChildrenParts((RigConfiguration.BoneElement)var5, true));
         }
      }

      return var3;
   }

   @NotNull
   private RigConfiguration.PartElement getWrapper(@NotNull PartConfiguration var1) {
      RigConfiguration.PartElement var2 = (RigConfiguration.PartElement)this.elementMap.get(var1.getIdentifier());
      if (var2 != null) {
         return var2;
      } else {
         throw new IllegalArgumentException("part with identifier '" + String.valueOf(var1.getIdentifier()) + "' was not found in rig");
      }
   }

   @NotNull
   private RigConfiguration.BoneElement getWrapper(@NotNull BoneConfiguration var1) {
      RigConfiguration.BoneElement var2 = (RigConfiguration.BoneElement)this.elementMap.get(var1.getIdentifier());
      if (var2 != null) {
         return var2;
      } else {
         throw new IllegalArgumentException("bone with identifier '" + String.valueOf(var1.getIdentifier()) + "' was not found in rig");
      }
   }

   public Map<UUID, RigConfiguration.Element<?>> getElementMap() {
      return this.elementMap;
   }

   public String toString() {
      return "RigConfiguration(elementMap=" + String.valueOf(this.getElementMap()) + ")";
   }

   public static class Builder {
      private final List<RigConfiguration.Element<?>> wrappers = new ArrayList();

      public RigConfiguration build() {
         return new RigConfiguration(this.wrappers);
      }

      public void root(@NotNull BoneConfiguration var1) {
         this.getWrapper(var1);
      }

      public void bind(@NotNull PartConfiguration var1, @NotNull BoneConfiguration var2) {
         RigConfiguration.PartElement var3 = this.getWrapper(var1);
         RigConfiguration.BoneElement var4 = this.getWrapper(var2);
         if (var3.parent != null) {
            throw new IllegalStateException("provided part is already bound to another bone");
         } else {
            var3.parent = var4;
            var4.children.add(var3);
         }
      }

      public void bind(@NotNull BoneConfiguration var1, @NotNull BoneConfiguration var2) {
         RigConfiguration.BoneElement var3 = this.getWrapper(var1);
         RigConfiguration.BoneElement var4 = this.getWrapper(var2);
         if (var3.parent != null) {
            throw new IllegalStateException("provided bone is already bound to another bone");
         } else {
            var3.parent = var4;
            var4.children.add(var3);
         }
      }

      private RigConfiguration.PartElement getWrapper(PartConfiguration var1) {
         RigConfiguration.Element var2 = this.getWrapperRaw(var1.getIdentifier());
         if (var2 instanceof RigConfiguration.PartElement) {
            return (RigConfiguration.PartElement)var2;
         } else {
            RigConfiguration.PartElement var3 = new RigConfiguration.PartElement(var1);
            this.wrappers.add(var3);
            return var3;
         }
      }

      private RigConfiguration.BoneElement getWrapper(BoneConfiguration var1) {
         RigConfiguration.Element var2 = this.getWrapperRaw(var1.getIdentifier());
         if (var2 instanceof RigConfiguration.BoneElement) {
            return (RigConfiguration.BoneElement)var2;
         } else {
            RigConfiguration.BoneElement var3 = new RigConfiguration.BoneElement(var1);
            this.wrappers.add(var3);
            return var3;
         }
      }

      private RigConfiguration.Element<?> getWrapperRaw(UUID var1) {
         Iterator var2 = this.wrappers.iterator();

         RigConfiguration.Element var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (RigConfiguration.Element)var2.next();
         } while(!Objects.equals(var3.identifier, var1));

         return var3;
      }
   }

   abstract static class Element<E> implements ConfigurationSectionWritable {
      @NotNull
      final UUID identifier;
      @NotNull
      final E value;
      RigConfiguration.BoneElement parent;

      static RigConfiguration.Element<?> of(@NotNull ConfigurationSection var0, @NotNull CompoundModelConfiguration var1) {
         UUID var2 = Identifiable.loadIdentifier(var0);
         if (var2 == null) {
            return null;
         } else {
            PartConfiguration var3 = var1.getPartByIdentifier(var2);
            if (var3 != null) {
               return new RigConfiguration.PartElement(var3);
            } else {
               BoneConfiguration var4 = var1.getBoneByIdentifier(var2);
               return var4 != null ? new RigConfiguration.BoneElement(var4) : null;
            }
         }
      }

      private Element(@NotNull UUID var1, @NotNull E var2) {
         this.identifier = var1;
         this.value = var2;
      }

      public void write(@NotNull ConfigurationSection var1) {
         Identifiable.writeIdentifier(this.identifier, var1);
         if (this.parent != null) {
            var1.set("parent", this.parent.identifier.toString());
         }

      }

      public String toString() {
         String var10000 = String.valueOf(this.identifier);
         return "RigConfiguration.Element(identifier=" + var10000 + ", value=" + String.valueOf(this.value) + ", parent=" + String.valueOf(this.parent) + ")";
      }
   }

   static final class BoneElement extends RigConfiguration.Element<BoneConfiguration> {
      final Set<RigConfiguration.Element<?>> children = new HashSet();

      BoneElement(@NotNull BoneConfiguration var1) {
         super(var1.getIdentifier(), var1);
      }

      public void write(@NotNull ConfigurationSection var1) {
         super.write(var1);
         if (this.children.size() > 0) {
            var1.set("children", this.children.stream().map((var0) -> {
               return var0.identifier.toString();
            }).collect(Collectors.toList()));
         }

      }
   }

   static final class PartElement extends RigConfiguration.Element<PartConfiguration> {
      PartElement(@NotNull PartConfiguration var1) {
         super(var1.getIdentifier(), var1);
      }
   }
}
