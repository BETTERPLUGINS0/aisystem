package fr.xephi.authme.libs.ch.jalu.configme.resource;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class YamlFileResource implements PropertyResource {
   private final Path path;
   private final YamlFileResourceOptions options;
   private final String indentationSpace;
   private Yaml yamlObject;

   public YamlFileResource(Path path) {
      this(path, YamlFileResourceOptions.builder().build());
   }

   public YamlFileResource(Path path, YamlFileResourceOptions options) {
      this.path = path;
      this.options = options;
      this.indentationSpace = options.getIndentation();
   }

   /** @deprecated */
   @Deprecated
   public YamlFileResource(File file) {
      this(file.toPath());
   }

   public PropertyReader createReader() {
      return new YamlFileReader(this.path, this.options.getCharset(), this.options.splitDotPaths());
   }

   public void exportProperties(ConfigurationData configurationData) {
      try {
         OutputStream os = Files.newOutputStream(this.path);
         Throwable var3 = null;

         try {
            OutputStreamWriter writer = new OutputStreamWriter(os, this.options.getCharset());
            Throwable var5 = null;

            try {
               PropertyPathTraverser pathTraverser = new PropertyPathTraverser(configurationData);
               Iterator var7 = configurationData.getProperties().iterator();

               while(var7.hasNext()) {
                  Property<?> property = (Property)var7.next();
                  Object exportValue = this.getExportValue(property, configurationData);
                  this.exportValue(writer, pathTraverser, Arrays.asList(property.getPath().split("\\.")), exportValue);
               }

               writer.append("\n");
               writer.flush();
            } catch (Throwable var46) {
               var5 = var46;
               throw var46;
            } finally {
               if (writer != null) {
                  if (var5 != null) {
                     try {
                        writer.close();
                     } catch (Throwable var45) {
                        var5.addSuppressed(var45);
                     }
                  } else {
                     writer.close();
                  }
               }

            }
         } catch (Throwable var48) {
            var3 = var48;
            throw var48;
         } finally {
            if (os != null) {
               if (var3 != null) {
                  try {
                     os.close();
                  } catch (Throwable var44) {
                     var3.addSuppressed(var44);
                  }
               } else {
                  os.close();
               }
            }

         }
      } catch (IOException var50) {
         throw new ConfigMeException("Could not save config to '" + this.path + "'", var50);
      } finally {
         this.onWriteComplete();
      }
   }

   protected final Path getPath() {
      return this.path;
   }

   /** @deprecated */
   @Deprecated
   protected final File getFile() {
      return this.path.toFile();
   }

   protected void exportValue(Writer writer, PropertyPathTraverser pathTraverser, List<String> pathElements, Object value) throws IOException {
      if (value != null) {
         if (value instanceof Map && !((Map)value).isEmpty()) {
            Map<String, ?> mapValue = (Map)value;
            Iterator var10 = mapValue.entrySet().iterator();

            while(var10.hasNext()) {
               Entry<String, ?> entry = (Entry)var10.next();
               List<String> pathElementsForEntry = this.combinePathElementsAndMapEntryKey(pathElements, (String)entry.getKey());
               this.exportValue(writer, pathTraverser, pathElementsForEntry, entry.getValue());
            }
         } else {
            List<PropertyPathTraverser.PathElement> newPathElements = pathTraverser.getPathElements(pathElements);
            boolean isRootProperty = newPathElements.size() == 1 && "".equals(((PropertyPathTraverser.PathElement)newPathElements.get(0)).getName());
            Iterator var7 = newPathElements.iterator();

            while(var7.hasNext()) {
               PropertyPathTraverser.PathElement pathElement = (PropertyPathTraverser.PathElement)var7.next();
               this.writeIndentingBetweenLines(writer, pathElement);
               this.writeComments(writer, pathElement.getIndentationLevel(), pathElement);
               writer.append(this.getNewLineIfNotFirstElement(pathElement));
               if (!isRootProperty) {
                  writer.append(this.indent(pathElement.getIndentationLevel())).append(this.escapePathElementIfNeeded(pathElement.getName())).append(":");
               }
            }

            if (!isRootProperty) {
               writer.append(" ");
            }

            writer.append(this.toYamlIndented(value, ((PropertyPathTraverser.PathElement)newPathElements.get(newPathElements.size() - 1)).getIndentationLevel()));
         }

      }
   }

   protected void writeComments(Writer writer, int indentation, PropertyPathTraverser.PathElement pathElement) throws IOException {
      if (!pathElement.getComments().isEmpty()) {
         String lineStart = pathElement.isFirstElement() ? "" : "\n";
         String commentStart = this.indent(indentation) + "# ";
         Iterator var6 = pathElement.getComments().iterator();

         while(var6.hasNext()) {
            String comment = (String)var6.next();
            writer.append(lineStart);
            lineStart = "\n";
            if (!"\n".equals(comment)) {
               writer.append(commentStart).append(comment);
            }
         }

      }
   }

   protected List<String> combinePathElementsAndMapEntryKey(List<String> parentPathElements, String mapEntryKey) {
      Stream<String> parentPathElems = parentPathElements.size() == 1 && "".equals(parentPathElements.get(0)) ? Stream.empty() : parentPathElements.stream();
      Stream<String> pathElemsFromEntryKey = this.options.splitDotPaths() ? Arrays.stream(mapEntryKey.split("\\.")) : Stream.of(mapEntryKey);
      return (List)Stream.concat(parentPathElems, pathElemsFromEntryKey).collect(Collectors.toList());
   }

   private void writeIndentingBetweenLines(Writer writer, PropertyPathTraverser.PathElement pathElement) throws IOException {
      int numberOfEmptyLines = this.options.getNumberOfEmptyLinesBefore(pathElement);

      for(int i = 0; i < numberOfEmptyLines; ++i) {
         writer.append("\n");
      }

   }

   private String getNewLineIfNotFirstElement(PropertyPathTraverser.PathElement pathElement) {
      return pathElement.isFirstElement() && pathElement.getComments().isEmpty() ? "" : "\n";
   }

   protected String toYamlIndented(@Nullable Object value, int indent) {
      String representation = this.toYaml(value);
      String[] lines = representation.split("\\n");
      return String.join("\n" + this.indent(indent), lines);
   }

   protected String toYaml(@Nullable Object value) {
      if (value instanceof String) {
         return this.getYamlObject().dump(value);
      } else if (value instanceof Collection) {
         List<?> list = collectionToList((Collection)value);
         return list.isEmpty() ? "[]" : "\n" + this.getYamlObject().dump(list);
      } else if (value instanceof Object[]) {
         Object[] array = (Object[])((Object[])value);
         return array.length == 0 ? "[]" : "\n" + this.getYamlObject().dump(array);
      } else {
         return this.getYamlObject().dump(value);
      }
   }

   protected String indent(int level) {
      switch(level) {
      case 0:
         return "";
      case 1:
         return this.indentationSpace;
      case 2:
         return this.indentationSpace + this.indentationSpace;
      case 3:
         return this.indentationSpace + this.indentationSpace + this.indentationSpace;
      case 4:
         return this.indentationSpace + this.indentationSpace + this.indentationSpace + this.indentationSpace;
      case 5:
         return this.indentationSpace + this.indentationSpace + this.indentationSpace + this.indentationSpace + this.indentationSpace;
      default:
         StringBuilder result = new StringBuilder(level * this.indentationSpace.length());

         for(int i = 0; i < level; ++i) {
            result.append(this.indentationSpace);
         }

         return result.toString();
      }
   }

   protected String escapePathElementIfNeeded(String path) {
      return this.getYamlObject().dump(path).trim();
   }

   protected void onWriteComplete() {
      this.yamlObject = null;
   }

   protected Yaml getYamlObject() {
      if (this.yamlObject == null) {
         this.yamlObject = this.createNewYaml();
      }

      return this.yamlObject;
   }

   protected Yaml createNewYaml() {
      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(FlowStyle.BLOCK);
      options.setAllowUnicode(true);
      return new Yaml(options);
   }

   protected final YamlFileResourceOptions getOptions() {
      return this.options;
   }

   private <T> Object getExportValue(Property<T> property, ConfigurationData configurationData) {
      return property.toExportValue(configurationData.getValue(property));
   }

   private static List<?> collectionToList(Collection<?> collection) {
      return (List)(collection instanceof List ? (List)collection : new ArrayList(collection));
   }
}
