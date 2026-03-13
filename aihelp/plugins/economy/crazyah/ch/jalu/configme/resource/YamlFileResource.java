package ch.jalu.configme.resource;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.exception.ConfigMeException;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.resource.yaml.SnakeYamlNodeBuilder;
import ch.jalu.configme.resource.yaml.SnakeYamlNodeBuilderImpl;
import ch.jalu.configme.resource.yaml.SnakeYamlNodeContainer;
import ch.jalu.configme.resource.yaml.SnakeYamlNodeContainerImpl;
import ch.jalu.configme.utils.StreamUtils;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.nodes.Node;

public class YamlFileResource implements PropertyResource {
   private final Path path;
   @NotNull
   private final YamlFileResourceOptions options;
   @Nullable
   private Yaml yamlObject;

   public YamlFileResource(@NotNull Path path) {
      this(path, YamlFileResourceOptions.builder().build());
   }

   public YamlFileResource(@NotNull Path path, @NotNull YamlFileResourceOptions options) {
      this.path = path;
      this.options = options;
   }

   /** @deprecated */
   @Deprecated
   public YamlFileResource(@NotNull File file) {
      this(file.toPath());
   }

   @NotNull
   public PropertyReader createReader() {
      return new YamlFileReader(this.path, this.options.getCharset(), this.options.splitDotPaths());
   }

   public void exportProperties(@NotNull ConfigurationData configurationData) {
      SnakeYamlNodeContainer root = this.createNodeContainerForRoot(configurationData.getCommentsForSection(""));
      PropertyPathTraverser pathTraverser = new PropertyPathTraverser();
      SnakeYamlNodeBuilder nodeBuilder = this.createNodeBuilder();
      List<Property<?>> properties = configurationData.getProperties();
      Iterator var6 = properties.iterator();

      while(var6.hasNext()) {
         Property<?> property = (Property)var6.next();
         Object exportValue = this.getExportValue(property, configurationData);
         if (exportValue != null) {
            String path = property.getPath();
            List<PropertyPathTraverser.PathElement> pathElements = pathTraverser.getPathElements(path);
            this.createAndAddYamlNode(exportValue, path, pathElements, root, configurationData, nodeBuilder);
         }
      }

      Node rootNode;
      if (properties.size() == 1 && "".equals(((Property)properties.get(0)).getPath())) {
         rootNode = root.getRootValueNode();
      } else {
         rootNode = root.convertToNode(nodeBuilder);
      }

      try {
         OutputStream os = Files.newOutputStream(this.path);
         Throwable var56 = null;

         try {
            OutputStreamWriter writer = new OutputStreamWriter(os, this.options.getCharset());
            Throwable var58 = null;

            try {
               this.getYamlObject().serialize(rootNode, writer);
            } catch (Throwable var48) {
               var58 = var48;
               throw var48;
            } finally {
               if (writer != null) {
                  if (var58 != null) {
                     try {
                        writer.close();
                     } catch (Throwable var47) {
                        var58.addSuppressed(var47);
                     }
                  } else {
                     writer.close();
                  }
               }

            }
         } catch (Throwable var50) {
            var56 = var50;
            throw var50;
         } finally {
            if (os != null) {
               if (var56 != null) {
                  try {
                     os.close();
                  } catch (Throwable var46) {
                     var56.addSuppressed(var46);
                  }
               } else {
                  os.close();
               }
            }

         }
      } catch (IOException var52) {
         throw new ConfigMeException("Could not save config to '" + this.path + "'", var52);
      } finally {
         this.onWriteComplete();
      }

   }

   protected void createAndAddYamlNode(@NotNull Object exportValue, @NotNull String path, @NotNull List<PropertyPathTraverser.PathElement> pathElements, @NotNull SnakeYamlNodeContainer rootContainer, @NotNull ConfigurationData configurationData, @NotNull SnakeYamlNodeBuilder nodeBuilder) {
      SnakeYamlNodeContainer container = rootContainer;
      Iterator var8 = pathElements.iterator();

      while(var8.hasNext()) {
         PropertyPathTraverser.PathElement pathElement = (PropertyPathTraverser.PathElement)var8.next();
         if (pathElement.isEndOfPath()) {
            int emptyLines = this.options.getNumberOfEmptyLinesBefore(pathElement);
            container.putNode(pathElement.getName(), nodeBuilder.createYamlNode(exportValue, path, configurationData, emptyLines));
         } else {
            container = container.getOrCreateChildContainer(pathElement.getName(), () -> {
               return this.getCommentsForPathElement(configurationData, pathElement);
            });
         }
      }

   }

   @NotNull
   protected List<String> getCommentsForPathElement(@NotNull ConfigurationData configurationData, @NotNull PropertyPathTraverser.PathElement pathElement) {
      return (List)Stream.concat(StreamUtils.repeat("\n", this.options.getNumberOfEmptyLinesBefore(pathElement)), configurationData.getCommentsForSection(pathElement.getFullPath()).stream()).collect(Collectors.toList());
   }

   @NotNull
   protected final Path getPath() {
      return this.path;
   }

   /** @deprecated */
   @Deprecated
   @NotNull
   protected final File getFile() {
      return this.path.toFile();
   }

   protected void onWriteComplete() {
      this.yamlObject = null;
   }

   @NotNull
   protected Yaml getYamlObject() {
      if (this.yamlObject == null) {
         this.yamlObject = this.createNewYaml();
      }

      return this.yamlObject;
   }

   @NotNull
   protected Yaml createNewYaml() {
      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(FlowStyle.BLOCK);
      options.setAllowUnicode(true);
      options.setProcessComments(true);
      options.setIndent(this.options.getIndentationSize());
      return new Yaml(options);
   }

   @NotNull
   protected final YamlFileResourceOptions getOptions() {
      return this.options;
   }

   @NotNull
   protected SnakeYamlNodeBuilder createNodeBuilder() {
      return new SnakeYamlNodeBuilderImpl();
   }

   @NotNull
   protected SnakeYamlNodeContainer createNodeContainerForRoot(@NotNull List<String> rootComments) {
      return new SnakeYamlNodeContainerImpl(rootComments);
   }

   @Nullable
   private <T> Object getExportValue(@NotNull Property<T> property, @NotNull ConfigurationData configurationData) {
      return property.toExportValue(configurationData.getValue(property));
   }
}
