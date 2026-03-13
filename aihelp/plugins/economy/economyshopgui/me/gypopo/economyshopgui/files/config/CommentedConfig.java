package me.gypopo.economyshopgui.files.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.util.exceptions.ConfigLoadException;
import me.gypopo.economyshopgui.util.exceptions.ConfigSaveException;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.yaml.snakeyaml.Yaml;

public class CommentedConfig extends Config {
   private final Set<String> specialKeys = new HashSet(Arrays.asList("display-lore-layout", "visual-transaction-limits", "disable-back", "click-mappings"));
   private final Map<String, String> comments;
   private boolean reload = false;

   public CommentedConfig(EconomyShopGUI plugin, File file, InputStream def) throws ConfigLoadException {
      super(file);
      Object comments = new HashMap();

      try {
         BufferedReader input = new BufferedReader(new InputStreamReader(def, StandardCharsets.UTF_8));
         comments = this.getComments((List)input.lines().collect(Collectors.toList()));
         input.close();
      } catch (FileNotFoundException var6) {
      } catch (IOException var7) {
         throw new ConfigLoadException("A unknown IOE Exception occurred while loading " + file.getName() + "\n" + this.stackTraceToString(var7));
      }

      this.comments = (Map)comments;
      this.setDef(plugin.loadConfiguration(new BufferedReader(new InputStreamReader(plugin.getResource(file.getName()), StandardCharsets.UTF_8)), file.getName()));
   }

   private Set<String> getKeys() throws InvalidConfigurationException {
      Set<String> results = new LinkedHashSet();
      Set<String> old = this.getKeys(false);
      old.removeAll(results);
      Iterator var3 = this.defaults.getKeys(false).iterator();

      while(true) {
         while(var3.hasNext()) {
            String def = (String)var3.next();
            results.add(def);
            Object value = this.get(def);
            if (value instanceof MemorySection) {
               Iterator var8 = ((Configuration)(this.specialKeys.contains(def) ? this.getDef() : this)).getConfigurationSection(def).getKeys(true).iterator();

               while(var8.hasNext()) {
                  String deepKey = (String)var8.next();
                  results.add(def + "." + deepKey);
               }
            } else if (value == null) {
               ConfigurationSection section = this.getDef().getConfigurationSection(def);
               if (section != null) {
                  section.getKeys(true).forEach((deepKeyx) -> {
                     results.add(def + "." + deepKeyx);
                  });
               }

               this.reload = true;
            }
         }

         results.addAll(old);
         return results;
      }
   }

   public void save() throws ConfigSaveException {
      try {
         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(this.getFilePath()), StandardCharsets.UTF_8));
         Yaml yaml = new Yaml();
         Iterator var3 = this.getKeys().iterator();

         while(true) {
            while(var3.hasNext()) {
               String key = (String)var3.next();
               String[] keys = key.split("\\.");
               String actualKey = keys[keys.length - 1];
               String comment = (String)this.comments.get(key);
               StringBuilder prefixBuilder = new StringBuilder();
               int indents = keys.length - 1;
               this.appendPrefixSpaces(prefixBuilder, indents);
               String prefixSpaces = prefixBuilder.toString();
               if (comment != null) {
                  writer.write(comment);
               }

               Object obj = this.get(key, this.getDef().get(key));
               if (obj instanceof ConfigurationSerializable) {
                  writer.write(prefixSpaces + actualKey + ": " + yaml.dump(((ConfigurationSerializable)obj).serialize()));
               } else if (!(obj instanceof String) && !(obj instanceof Character)) {
                  if (obj instanceof List) {
                     writer.write(this.getListAsString((List)obj, actualKey, prefixSpaces));
                  } else if (obj instanceof MemorySection) {
                     writer.write(prefixSpaces + actualKey + ":\n");
                  } else {
                     writer.write(prefixSpaces + actualKey + ": " + yaml.dump(obj));
                  }
               } else if (obj instanceof String && ((String)obj).contains("\n")) {
                  writer.write(prefixSpaces + actualKey + ": \"" + ((String)obj).replace("\n", "\\n") + "\"\n");
               } else {
                  String raw = yaml.dump(obj);
                  if (!raw.startsWith("|")) {
                     writer.write(prefixSpaces + actualKey + ": " + raw);
                  } else {
                     String[] lines = raw.substring(0, raw.length() - 1).split("\n");
                     writer.write(prefixSpaces + actualKey + ": " + lines[0] + "\n");

                     for(int i = 1; i < lines.length; ++i) {
                        writer.write(prefixSpaces + "  " + lines[i] + "\n");
                     }
                  }
               }
            }

            String danglingComments = (String)this.comments.get((Object)null);
            if (danglingComments != null) {
               writer.write(danglingComments);
            }

            writer.close();
            if (this.reload) {
               this.reload = false;
               this.reload();
            }

            return;
         }
      } catch (IOException | InvalidConfigurationException | ConfigLoadException var15) {
         throw new ConfigSaveException(Lang.COULD_NOT_SAVE_CONFIG.get().replace("%fileName%", this.getFilePath().getFileName().toString()) + "\n" + this.stackTraceToString(var15));
      }
   }

   private String stackTraceToString(Exception e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      return sw.toString();
   }

   private Map<String, String> getComments(List<String> lines) {
      Map<String, String> comments = new HashMap();
      StringBuilder builder = new StringBuilder();
      StringBuilder keyBuilder = new StringBuilder();
      int lastLineIndentCount = 0;
      Iterator var6 = lines.iterator();

      while(true) {
         while(true) {
            String line;
            do {
               if (!var6.hasNext()) {
                  if (builder.length() > 0) {
                     comments.put((Object)null, builder.toString());
                  }

                  return comments;
               }

               line = (String)var6.next();
            } while(line != null && line.trim().startsWith("-"));

            if (line != null && !line.trim().equals("") && !line.trim().startsWith("#")) {
               lastLineIndentCount = this.setFullKey(keyBuilder, line, lastLineIndentCount);
               if (keyBuilder.length() > 0) {
                  comments.put(keyBuilder.toString(), builder.toString());
                  builder.setLength(0);
               }
            } else {
               builder.append(line).append("\n");
            }
         }
      }
   }

   private String getListAsString(List list, String actualKey, String prefixSpaces) {
      StringBuilder builder = (new StringBuilder(prefixSpaces)).append(actualKey).append(":");
      if (list.isEmpty()) {
         builder.append(" []\n");
         return builder.toString();
      } else {
         builder.append("\n");

         for(int i = 0; i < list.size(); ++i) {
            Object o = list.get(i);
            if (!(o instanceof String) && !(o instanceof Character)) {
               if (o instanceof List) {
                  builder.append(prefixSpaces).append("- ").append((new Yaml()).dump(o));
               } else {
                  builder.append(prefixSpaces).append("- ").append(o);
               }
            } else {
               String s = o.toString();
               if (s.contains("\"") || s.contains("'")) {
                  s = s.replace("'", "''");
               }

               builder.append(prefixSpaces).append("- '").append(s).append("'");
            }

            if (i != list.size()) {
               builder.append("\n");
            }
         }

         return builder.toString();
      }
   }

   private int setFullKey(StringBuilder keyBuilder, String configLine, int lastLineIndentCount) {
      int currentIndents = this.countIndents(configLine);
      String key = configLine.trim().split(":")[0];
      if (keyBuilder.length() == 0) {
         keyBuilder.append(key);
      } else if (currentIndents == lastLineIndentCount) {
         this.removeLastKey(keyBuilder);
         if (keyBuilder.length() > 0) {
            keyBuilder.append(".");
         }

         keyBuilder.append(key);
      } else if (currentIndents > lastLineIndentCount) {
         keyBuilder.append(".").append(key);
      } else {
         int difference = lastLineIndentCount - currentIndents;

         for(int i = 0; i < difference + 1; ++i) {
            this.removeLastKey(keyBuilder);
         }

         if (keyBuilder.length() > 0) {
            keyBuilder.append(".");
         }

         keyBuilder.append(key);
      }

      return currentIndents;
   }

   private int countIndents(String s) {
      int spaces = 0;
      char[] var3 = s.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char c = var3[var5];
         if (c != ' ') {
            break;
         }

         ++spaces;
      }

      return spaces / 2;
   }

   private void removeLastKey(StringBuilder keyBuilder) {
      String temp = keyBuilder.toString();
      String[] keys = temp.split("\\.");
      if (keys.length == 1) {
         keyBuilder.setLength(0);
      } else {
         temp = temp.substring(0, temp.length() - keys[keys.length - 1].length() - 1);
         keyBuilder.setLength(temp.length());
      }
   }

   private String getPrefixSpaces(int indents) {
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < indents; ++i) {
         builder.append("  ");
      }

      return builder.toString();
   }

   private void appendPrefixSpaces(StringBuilder builder, int indents) {
      builder.append(this.getPrefixSpaces(indents));
   }

   private final class ConfigurationDefaults {
      public Configuration defaults;

      public ConfigurationDefaults(Configuration param2) {
         this.defaults = defaults;
      }
   }
}
