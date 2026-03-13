package com.volmit.iris.util.slimjar.resolver.reader.resolution;

import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.util.Serialization;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PreResolutionDataReader {
   PreResolutionDataReader DEFAULT = (inputStream) -> {
      try {
         DataInputStream in = new DataInputStream(inputStream);

         LinkedHashMap var9;
         try {
            List<Entry<String, ResolutionResult>> list = Serialization.readList(in, (din) -> {
               return Map.entry(din.readUTF(), ResolutionResult.read(din));
            });
            Map<String, ResolutionResult> result = new LinkedHashMap(list.size());
            Iterator var4 = list.iterator();

            while(true) {
               if (!var4.hasNext()) {
                  var9 = result;
                  break;
               }

               Entry<String, ResolutionResult> entry = (Entry)var4.next();
               result.put((String)entry.getKey(), (ResolutionResult)entry.getValue());
            }
         } catch (Throwable var7) {
            try {
               in.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }

            throw var7;
         }

         in.close();
         return var9;
      } catch (IOException var8) {
         throw new ResolutionException("Failed to read dependency file", var8);
      }
   };

   @NotNull
   Map<String, ResolutionResult> read(@NotNull InputStream var1) throws ResolutionException;
}
