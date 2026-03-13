package org.avarion.yaml.v2;

import org.avarion.yaml.YamlWrapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlWrapperImpl implements YamlWrapper {
   private final Yaml yaml;

   public YamlWrapperImpl() {
      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      options.setPrettyFlow(true);
      this.yaml = new Yaml(new ToStringRepresenter(), options);
   }

   public String dump(Object data) {
      return this.yaml.dump(data);
   }

   public Object load(String content) {
      return this.yaml.load(content);
   }
}
