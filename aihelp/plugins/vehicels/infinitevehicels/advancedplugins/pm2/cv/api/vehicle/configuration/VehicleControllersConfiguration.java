package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleControllersConfiguration implements ConfigurationSectionWritable {
   public static final VehicleControllersConfiguration EMPTY = new VehicleControllersConfiguration((Map)null);
   @NotNull
   private final Map<String, VehicleControllerProperties> entries = new LinkedHashMap();

   public static VehicleControllersConfiguration load(ConfigurationSection var0) {
      LinkedHashMap var1 = new LinkedHashMap();

      String var4;
      VehicleControllerProperties var5;
      for(Iterator var2 = ConfigurationUtil.getConfigurationSections(var0, false).iterator(); var2.hasNext(); var1.put(var4, var5)) {
         ConfigurationSection var3 = (ConfigurationSection)var2.next();
         var4 = IDeyed.loadId(var3);
         var5 = new VehicleControllerProperties();
         ConfigurationSection var6 = var3.getConfigurationSection("properties");
         if (var6 != null) {
            var5 = VehicleControllerProperties.load(var6);
         }
      }

      return new VehicleControllersConfiguration(var1);
   }

   public VehicleControllersConfiguration(@Nullable Map<String, VehicleControllerProperties> var1) {
      if (var1 != null) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            this.entries.put(IDeyed.idCheck((String)var3.getKey()), (VehicleControllerProperties)var3.getValue());
         }
      }

   }

   @NotNull
   public Map<String, VehicleControllerProperties> getEntries() {
      return Collections.unmodifiableMap(this.entries);
   }

   public void write(@NotNull ConfigurationSection var1) {
      int var2 = 0;
      Iterator var3 = this.entries.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         String var5 = (String)var4.getKey();
         VehicleControllerProperties var6 = (VehicleControllerProperties)var4.getValue();
         int var10001 = var2++;
         ConfigurationSection var7 = var1.createSection("controller-" + var10001);
         IDeyed.writeId(var5, var7);
         var6.write(var7.createSection("properties"));
      }

   }

   public static VehicleControllersConfiguration.VehicleControllersConfigurationBuilder builder() {
      return new VehicleControllersConfiguration.VehicleControllersConfigurationBuilder();
   }

   public static class VehicleControllersConfigurationBuilder {
      private ArrayList<String> entries$key;
      private ArrayList<VehicleControllerProperties> entries$value;

      VehicleControllersConfigurationBuilder() {
      }

      public VehicleControllersConfiguration.VehicleControllersConfigurationBuilder entry(String var1, VehicleControllerProperties var2) {
         if (this.entries$key == null) {
            this.entries$key = new ArrayList();
            this.entries$value = new ArrayList();
         }

         this.entries$key.add(var1);
         this.entries$value.add(var2);
         return this;
      }

      public VehicleControllersConfiguration.VehicleControllersConfigurationBuilder entries(Map<? extends String, ? extends VehicleControllerProperties> var1) {
         if (var1 == null) {
            throw new NullPointerException("entries cannot be null");
         } else {
            if (this.entries$key == null) {
               this.entries$key = new ArrayList();
               this.entries$value = new ArrayList();
            }

            Iterator var2 = var1.entrySet().iterator();

            while(var2.hasNext()) {
               Entry var3 = (Entry)var2.next();
               this.entries$key.add((String)var3.getKey());
               this.entries$value.add((VehicleControllerProperties)var3.getValue());
            }

            return this;
         }
      }

      public VehicleControllersConfiguration.VehicleControllersConfigurationBuilder clearEntries() {
         if (this.entries$key != null) {
            this.entries$key.clear();
            this.entries$value.clear();
         }

         return this;
      }

      public VehicleControllersConfiguration build() {
         Map var1;
         switch(this.entries$key == null ? 0 : this.entries$key.size()) {
         case 0:
            var1 = Collections.emptyMap();
            break;
         case 1:
            var1 = Collections.singletonMap((String)this.entries$key.get(0), (VehicleControllerProperties)this.entries$value.get(0));
            break;
         default:
            LinkedHashMap var3 = new LinkedHashMap(this.entries$key.size() < 1073741824 ? 1 + this.entries$key.size() + (this.entries$key.size() - 3) / 3 : Integer.MAX_VALUE);

            for(int var2 = 0; var2 < this.entries$key.size(); ++var2) {
               var3.put((String)this.entries$key.get(var2), (VehicleControllerProperties)this.entries$value.get(var2));
            }

            var1 = Collections.unmodifiableMap(var3);
         }

         return new VehicleControllersConfiguration(var1);
      }

      public String toString() {
         String var10000 = String.valueOf(this.entries$key);
         return "VehicleControllersConfiguration.VehicleControllersConfigurationBuilder(entries$key=" + var10000 + ", entries$value=" + String.valueOf(this.entries$value) + ")";
      }
   }
}
