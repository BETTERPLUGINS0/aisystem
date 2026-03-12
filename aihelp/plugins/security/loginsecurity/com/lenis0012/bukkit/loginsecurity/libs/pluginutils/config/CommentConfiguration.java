package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class CommentConfiguration extends YamlConfiguration {
   private List<String> mainHeader = Lists.newArrayList();
   private final Map<String, List<String>> headers = Maps.newConcurrentMap();
   private final File file;
   private boolean loadHeaders;

   public CommentConfiguration(File file) {
      this.file = file;
      file.getParentFile().mkdirs();
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

   }

   public void mainHeader(String... header) {
      this.mainHeader = Arrays.asList(header);
   }

   public List<String> mainHeader() {
      return this.mainHeader;
   }

   public void header(String key, String... header) {
      this.headers.put(key, Arrays.asList(header));
   }

   public List<String> header(String key) {
      return (List)this.headers.get(key);
   }

   public <T> T get(String key, Class<T> type) {
      return type.cast(this.get(key));
   }

   public void reload() {
      this.reload(this.headers.isEmpty() && this.mainHeader.isEmpty());
   }

   public void reload(boolean loadHeaders) {
      this.loadHeaders = loadHeaders;

      try {
         this.load(this.file);
      } catch (Exception var3) {
         Bukkit.getLogger().log(Level.WARNING, "failed to reload file", var3);
      }

   }

   public void loadFromString(String contents) throws InvalidConfigurationException {
      StringBuilder memoryData = new StringBuilder();
      int indentLength = this.options().indent();
      String pathSeparator = Character.toString(this.options().pathSeparator());
      int currentIndents = 0;
      String key = "";
      List<String> headers = Lists.newArrayList();
      String[] var8 = contents.split("\n");
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         String line = var8[var10];
         if (!line.isEmpty()) {
            int indent = this.getSuccessiveCharCount(line, ' ');
            String subline = indent > 0 ? line.substring(indent) : line;
            if (subline.startsWith("#")) {
               if (this.loadHeaders) {
                  String txt;
                  if (subline.startsWith("#>")) {
                     txt = subline.startsWith("#> ") ? subline.substring(3) : subline.substring(2);
                     this.mainHeader.add(txt);
                  } else {
                     txt = subline.startsWith("# ") ? subline.substring(2) : subline.substring(1);
                     headers.add(txt);
                  }
               }
            } else {
               int indents = indent / indentLength;
               if (indents <= currentIndents) {
                  String[] array = key.split(Pattern.quote(pathSeparator));
                  int backspace = currentIndents - indents + 1;
                  key = this.join(array, this.options().pathSeparator(), 0, array.length - backspace);
               }

               String separator = key.length() > 0 ? pathSeparator : "";
               String lineKey = line.contains(":") ? line.split(Pattern.quote(":"))[0] : line;
               key = key + separator + lineKey.substring(indent);
               currentIndents = indents;
               memoryData.append(line).append('\n');
               if (!headers.isEmpty()) {
                  this.headers.put(key, headers);
                  headers = Lists.newArrayList();
               }
            }
         }
      }

      super.loadFromString(memoryData.toString());
   }

   public void save() {
      if (this.headers.isEmpty() && this.mainHeader.isEmpty()) {
         try {
            super.save(this.file);
         } catch (IOException var18) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to save file", var18);
         }

      } else {
         int indentLength = this.options().indent();
         String pathSeparator = Character.toString(this.options().pathSeparator());
         String content = this.saveToString();
         StringBuilder fileData = new StringBuilder(this.buildHeader());
         int currentIndents = 0;
         String key = "";
         Iterator var7 = this.mainHeader.iterator();

         while(var7.hasNext()) {
            String h = (String)var7.next();
            fileData.append("#> ").append(h).append('\n');
         }

         String[] var22 = content.split("\n");
         int var24 = var22.length;

         for(int var9 = 0; var9 < var24; ++var9) {
            String line = var22[var9];
            if (!line.isEmpty()) {
               int indent = this.getSuccessiveCharCount(line, ' ');
               int indents = indent / indentLength;
               String indentText = indent > 0 ? line.substring(0, indent) : "";
               if (indents <= currentIndents) {
                  String[] array = key.split(Pattern.quote(pathSeparator));
                  int backspace = currentIndents - indents + 1;
                  key = this.join(array, this.options().pathSeparator(), 0, array.length - backspace);
               }

               String separator = key.length() > 0 ? pathSeparator : "";
               String lineKey = line.contains(":") ? line.split(Pattern.quote(":"))[0] : line;
               key = key + separator + lineKey.substring(indent);
               currentIndents = indents;
               List<String> header = (List)this.headers.get(key);
               String headerText = header != null ? this.addHeaderTags(header, indentText) : "";
               fileData.append(headerText).append(line).append('\n');
            }
         }

         try {
            OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(this.file.toPath()), StandardCharsets.UTF_8);

            try {
               writer.write(fileData.toString());
               writer.flush();
            } catch (Throwable var20) {
               try {
                  writer.close();
               } catch (Throwable var19) {
                  var20.addSuppressed(var19);
               }

               throw var20;
            }

            writer.close();
         } catch (IOException var21) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to save file", var21);
         }

      }
   }

   private String addHeaderTags(List<String> header, String indent) {
      StringBuilder builder = new StringBuilder();
      Iterator var4 = header.iterator();

      while(var4.hasNext()) {
         String line = (String)var4.next();
         builder.append(indent).append("# ").append(line).append('\n');
      }

      return builder.toString();
   }

   private String join(String[] array, char joinChar, int start, int length) {
      String[] copy = new String[length - start];
      System.arraycopy(array, start, copy, 0, length - start);
      return Joiner.on(joinChar).join(copy);
   }

   private int getSuccessiveCharCount(String text, char key) {
      int count = 0;

      for(int i = 0; i < text.length() && text.charAt(i) == key; ++i) {
         ++count;
      }

      return count;
   }
}
