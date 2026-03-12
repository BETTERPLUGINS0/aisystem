package me.SuperRonanCraft.BetterRTP.references.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.jetbrains.annotations.Nullable;

public class LogUploader {
   private static final String UPLOAD_URL = "https://logs.ronanplugins.com/documents";
   public static final String KEY_URL = "https://logs.ronanplugins.com/";

   @Nullable
   public static String post(List<String> requestBody) {
      try {
         URL url = new URL("https://logs.ronanplugins.com/documents");
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "text/plain");
         connection.setDoOutput(true);
         OutputStream outputStream = connection.getOutputStream();

         String line;
         try {
            Iterator var4 = requestBody.iterator();

            while(var4.hasNext()) {
               line = (String)var4.next();
               byte[] input = (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
               outputStream.write(input, 0, input.length);
            }
         } catch (Throwable var10) {
            if (outputStream != null) {
               try {
                  outputStream.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (outputStream != null) {
            outputStream.close();
         }

         StringBuilder response = new StringBuilder();
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

         try {
            while((line = reader.readLine()) != null) {
               response.append(line);
            }
         } catch (Throwable var9) {
            try {
               reader.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         reader.close();
         return response.toString();
      } catch (IOException var11) {
         return null;
      }
   }

   @Nullable
   public static String post(File file) {
      try {
         URL url = new URL("https://logs.ronanplugins.com/documents");
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "text/yaml");
         connection.setDoOutput(true);
         OutputStream outputStream = connection.getOutputStream();

         String line;
         try {
            Scanner scan = new Scanner(file);

            while(true) {
               if (!scan.hasNextLine()) {
                  scan.close();
                  break;
               }

               line = scan.nextLine();
               byte[] input = (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
               outputStream.write(input, 0, input.length);
            }
         } catch (Throwable var10) {
            if (outputStream != null) {
               try {
                  outputStream.close();
               } catch (Throwable var7) {
                  var10.addSuppressed(var7);
               }
            }

            throw var10;
         }

         if (outputStream != null) {
            outputStream.close();
         }

         StringBuilder response = new StringBuilder();
         BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

         try {
            while((line = reader.readLine()) != null) {
               response.append(line);
            }
         } catch (Throwable var9) {
            try {
               reader.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         reader.close();
         return response.toString();
      } catch (IOException var11) {
         var11.printStackTrace();
         return null;
      }
   }
}
