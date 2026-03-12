package com.nisovin.shopkeepers.util.data.persistence;

import com.nisovin.shopkeepers.util.java.FileUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface DataStoreBase extends DataStore {
   default void load(File file) throws IOException, InvalidDataFormatException {
      Validate.notNull(file, (String)"file is null");
      this.load(file.toPath());
   }

   default void load(Path path) throws IOException, InvalidDataFormatException {
      Validate.notNull(path, (String)"path is null");
      this.load((Reader)Files.newBufferedReader(path, StandardCharsets.UTF_8));
   }

   default void load(Reader reader) throws IOException, InvalidDataFormatException {
      String content = FileUtils.read(reader);
      this.loadFromString(content);
   }

   default void save(File file) throws IOException {
      Validate.notNull(file, (String)"file is null");
      this.save(file.toPath());
   }

   default void save(Path path) throws IOException {
      Validate.notNull(path, (String)"path is null");
      FileUtils.createParentDirectories(path);
      this.save((Writer)Files.newBufferedWriter(path, StandardCharsets.UTF_8));
   }

   default void save(Writer writer) throws IOException {
      Validate.notNull(writer, (String)"writer is null");
      String data = this.saveToString();

      try {
         writer.write(data);
      } finally {
         writer.close();
      }

   }
}
