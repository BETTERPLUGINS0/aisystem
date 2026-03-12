package com.nisovin.shopkeepers.util.yaml;

import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.configuration.file.YamlConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.representer.Representer;

public final class YamlUtils {
   private static final ThreadLocal<Yaml> YAML_COMPACT = ThreadLocal.withInitial(() -> {
      DumperOptions yamlDumperOptions = new DumperOptions();
      yamlDumperOptions.setDefaultFlowStyle(FlowStyle.FLOW);
      yamlDumperOptions.setDefaultScalarStyle(ScalarStyle.PLAIN);
      yamlDumperOptions.setSplitLines(false);
      yamlDumperOptions.setWidth(Integer.MAX_VALUE);
      Representer yamlRepresenter = new CompactYamlRepresenter();
      yamlRepresenter.setDefaultFlowStyle(FlowStyle.FLOW);
      yamlRepresenter.setDefaultScalarStyle(ScalarStyle.PLAIN);
      LoaderOptions yamlLoaderOptions = new LoaderOptions();
      yamlLoaderOptions.setMaxAliasesForCollections(Integer.MAX_VALUE);
      yamlLoaderOptions.setCodePointLimit(Integer.MAX_VALUE);
      yamlLoaderOptions.setNestingDepthLimit(100);
      BaseConstructor yamlConstructor = new YamlConstructor(yamlLoaderOptions);
      return new Yaml(yamlConstructor, yamlRepresenter, yamlDumperOptions);
   });
   private static final String YAML_NEWLINE = "\n";

   public static String toCompactYaml(@Nullable Object object) {
      String yamlString = toYaml((Yaml)YAML_COMPACT.get(), object);
      yamlString = StringUtils.stripTrailingNewlines(yamlString);
      return yamlString;
   }

   private static String toYaml(Yaml yaml, @Nullable Object object) {
      assert yaml != null;

      if (object == null) {
         return "";
      } else {
         String yamlString = yaml.dump(object);

         assert yamlString != null;

         return yamlString;
      }
   }

   @Nullable
   public static <T> T fromYaml(String yamlString) {
      Validate.notNull(yamlString, (String)"yamlString is null");
      Yaml yaml = (Yaml)YAML_COMPACT.get();
      Object object = yaml.load(yamlString);
      return object;
   }

   public static String yamlNewline() {
      return "\n";
   }

   private YamlUtils() {
   }
}
