package com.nisovin.shopkeepers.util.data.persistence;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public interface DataStore extends DataContainer {
   void load(File var1) throws IOException, InvalidDataFormatException;

   void load(Path var1) throws IOException, InvalidDataFormatException;

   void load(Reader var1) throws IOException, InvalidDataFormatException;

   void loadFromString(String var1) throws InvalidDataFormatException;

   void save(File var1) throws IOException;

   void save(Path var1) throws IOException;

   void save(Writer var1) throws IOException;

   String saveToString();
}
