package ch.jalu.configme.resource.yaml;

import ch.jalu.configme.configurationdata.ConfigurationData;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.nodes.Node;

public interface SnakeYamlNodeBuilder {
   @NotNull
   Node createYamlNode(@NotNull Object var1, @NotNull String var2, @NotNull ConfigurationData var3, int var4);

   @NotNull
   Node createKeyNode(@NotNull String var1);

   @NotNull
   Stream<CommentLine> createCommentLines(@NotNull String var1);

   void transferComments(@NotNull Node var1, @NotNull Node var2);
}
