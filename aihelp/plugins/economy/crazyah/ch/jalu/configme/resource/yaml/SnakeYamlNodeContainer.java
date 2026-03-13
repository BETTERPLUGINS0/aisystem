package ch.jalu.configme.resource.yaml;

import java.util.List;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.nodes.Node;

public interface SnakeYamlNodeContainer {
   @NotNull
   SnakeYamlNodeContainer getOrCreateChildContainer(String var1, Supplier<List<String>> var2);

   @NotNull
   Node getRootValueNode();

   void putNode(@NotNull String var1, @NotNull Node var2);

   @NotNull
   Node convertToNode(@NotNull SnakeYamlNodeBuilder var1);
}
