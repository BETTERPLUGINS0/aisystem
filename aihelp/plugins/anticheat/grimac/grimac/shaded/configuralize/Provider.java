package ac.grim.grimac.shaded.configuralize;

import ac.grim.grimac.shaded.maps.weak.Dynamic;
import ac.grim.grimac.shaded.snakeyaml.parser.ParserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Provider {
   private final DynamicConfig config;
   private final Source source;
   private Dynamic defaults;
   private Dynamic values;

   private static Dynamic load(DynamicConfig config, Source source, String raw) throws ParseException {
      if (raw == null) {
         throw new IllegalArgumentException("Can't load null config");
      } else {
         String extension = source.getFile().getName().substring(source.getFile().getName().lastIndexOf(".") + 1);

         Map parsed;
         try {
            if (extension.equalsIgnoreCase("yml")) {
               parsed = (Map)config.getYamlParser().loadAs(raw, Map.class);
            } else {
               if (!extension.equalsIgnoreCase("json")) {
                  throw new IllegalArgumentException("Config source extension " + extension + " is not supported");
               }

               parsed = (Map)config.getJsonParser().parse(raw);
            }
         } catch (ParserException | ac.grim.grimac.shaded.json.simple.parser.ParseException var6) {
            throw new ParseException(source, var6);
         }

         return Dynamic.from(parsed);
      }
   }

   public Provider(DynamicConfig config, Source source) {
      this.config = config;
      this.source = source;
   }

   public void load() throws IOException, ParseException {
      this.defaults = this.loadResource();
      this.values = this.loadValues();
   }

   public Dynamic loadValues() throws ParseException, IOException {
      return load(this.config, this.source, new String(Files.readAllBytes(this.source.getFile().toPath())));
   }

   public Dynamic loadResource() throws ParseException, IOException {
      InputStream stream = this.source.getResource().openStream();

      Dynamic var4;
      try {
         Objects.requireNonNull(stream, "Unknown resource " + this.source.getResourcePath(this.config.getLanguage()));
         InputStreamReader reader = new InputStreamReader(stream);

         try {
            BufferedReader buffer = new BufferedReader(reader);

            try {
               var4 = load(this.config, this.source, (String)buffer.lines().collect(Collectors.joining("\n")));
            } catch (Throwable var9) {
               try {
                  buffer.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            buffer.close();
         } catch (Throwable var10) {
            try {
               reader.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         reader.close();
      } catch (Throwable var11) {
         if (stream != null) {
            try {
               stream.close();
            } catch (Throwable var6) {
               var11.addSuppressed(var6);
            }
         }

         throw var11;
      }

      if (stream != null) {
         stream.close();
      }

      return var4;
   }

   public void saveDefaults() throws IOException {
      this.saveDefaults(false);
   }

   public void saveDefaults(boolean overwrite) throws IOException {
      if (!this.source.getFile().exists() || overwrite) {
         if (!this.source.getFile().getParentFile().exists() && !this.source.getFile().getParentFile().mkdirs()) {
            throw new IOException("Failed to create directory " + this.source.getFile().getParentFile().getAbsolutePath());
         } else {
            String resource = this.source.getResourcePath(this.config.getLanguage());
            InputStream stream = this.source.getResource().openStream();

            try {
               Objects.requireNonNull(stream, "Unknown resource " + this.source.getResourcePath(this.config.getLanguage()));
               Files.copy(stream, this.source.getFile().toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (Throwable var7) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (stream != null) {
               stream.close();
            }

         }
      }
   }

   public DynamicConfig getConfig() {
      return this.config;
   }

   public Source getSource() {
      return this.source;
   }

   public Dynamic getDefaults() {
      return this.defaults;
   }

   public Dynamic getValues() {
      return this.values;
   }
}
