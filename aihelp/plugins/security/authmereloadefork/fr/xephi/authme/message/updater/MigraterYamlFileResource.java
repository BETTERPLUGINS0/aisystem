package fr.xephi.authme.message.updater;

import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.ch.jalu.configme.resource.YamlFileResource;
import java.io.File;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;

public class MigraterYamlFileResource extends YamlFileResource {
   private Yaml singleQuoteYaml;

   public MigraterYamlFileResource(File file) {
      super(file);
   }

   public PropertyReader createReader() {
      return MessageMigraterPropertyReader.loadFromFile(this.getFile());
   }

   protected Yaml createNewYaml() {
      if (this.singleQuoteYaml == null) {
         DumperOptions options = new DumperOptions();
         options.setDefaultFlowStyle(FlowStyle.BLOCK);
         options.setAllowUnicode(true);
         options.setDefaultScalarStyle(ScalarStyle.SINGLE_QUOTED);
         options.setSplitLines(false);
         this.singleQuoteYaml = new Yaml(options);
      }

      return this.singleQuoteYaml;
   }

   protected String escapePathElementIfNeeded(String path) {
      return path;
   }
}
