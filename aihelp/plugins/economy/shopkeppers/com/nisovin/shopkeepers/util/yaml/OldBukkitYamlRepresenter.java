package com.nisovin.shopkeepers.util.yaml;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlRepresenter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.SafeRepresenter.RepresentMap;

class OldBukkitYamlRepresenter extends YamlRepresenter {
   OldBukkitYamlRepresenter() {
      super(new DumperOptions());
      this.multiRepresenters.put(ConfigurationSection.class, new OldBukkitYamlRepresenter.RepresentConfigurationSection(this));
   }

   private class RepresentConfigurationSection extends RepresentMap {
      private RepresentConfigurationSection(final OldBukkitYamlRepresenter param1) {
         super(var1);
      }

      public Node representData(@Nullable Object data) {
         assert data instanceof ConfigurationSection;

         ConfigurationSection configSection = (ConfigurationSection)Unsafe.castNonNull(data);
         return (Node)Unsafe.assertNonNull(super.representData(configSection.getValues(false)));
      }
   }
}
