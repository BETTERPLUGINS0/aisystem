package com.bergerkiller.bukkit.tc.offline.sign;

import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.tc.Util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

class OfflineSignStoreUpgradeV1ToV2 {
   public static DataInputStream upgrade(DataInputStream stream) throws IOException {
      ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
      Util.writeVariableLengthInt(outByteStream, 2);

      while(stream.available() > 0) {
         byte[] encodedData = Util.readByteArray(stream);
         ByteArrayInputStream m_b_stream = new ByteArrayInputStream(encodedData);

         OfflineBlock signBlock;
         String[] signLines;
         byte[] metadataContents;
         try {
            InflaterInputStream m_d_stream = new InflaterInputStream(m_b_stream);

            try {
               DataInputStream m_stream = new DataInputStream(m_d_stream);

               try {
                  signBlock = OfflineBlock.readFrom(m_stream);
                  signLines = new String[4];
                  int n = 0;

                  while(true) {
                     if (n >= 4) {
                        ByteArrayOutputStream data = new ByteArrayOutputStream();

                        int b;
                        while((b = m_stream.read()) != -1) {
                           data.write(b);
                        }

                        metadataContents = data.toByteArray();
                        break;
                     }

                     signLines[n] = m_stream.readUTF();
                     ++n;
                  }
               } catch (Throwable var14) {
                  try {
                     m_stream.close();
                  } catch (Throwable var13) {
                     var14.addSuppressed(var13);
                  }

                  throw var14;
               }

               m_stream.close();
            } catch (Throwable var15) {
               try {
                  m_d_stream.close();
               } catch (Throwable var12) {
                  var15.addSuppressed(var12);
               }

               throw var15;
            }

            m_d_stream.close();
         } catch (Throwable var16) {
            try {
               m_b_stream.close();
            } catch (Throwable var11) {
               var16.addSuppressed(var11);
            }

            throw var16;
         }

         m_b_stream.close();
         byte[] upgradedData = encodeMetadata(signBlock, signLines, metadataContents);
         Util.writeByteArray(outByteStream, upgradedData);
      }

      return new DataInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
   }

   private static byte[] encodeMetadata(OfflineBlock signBlock, String[] signLines, byte[] metadata) throws IOException {
      ByteArrayOutputStream b_stream = new ByteArrayOutputStream();

      byte[] var16;
      try {
         DeflaterOutputStream d_stream = new DeflaterOutputStream(b_stream);

         try {
            DataOutputStream stream = new DataOutputStream(d_stream);

            try {
               OfflineBlock.writeTo(stream, signBlock);
               stream.writeBoolean(true);
               String[] var6 = signLines;
               int var7 = signLines.length;
               int var8 = 0;

               while(true) {
                  if (var8 >= var7) {
                     stream.write(metadata);
                     break;
                  }

                  String line = var6[var8];
                  stream.writeUTF(line);
                  ++var8;
               }
            } catch (Throwable var13) {
               try {
                  stream.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }

               throw var13;
            }

            stream.close();
         } catch (Throwable var14) {
            try {
               d_stream.close();
            } catch (Throwable var11) {
               var14.addSuppressed(var11);
            }

            throw var14;
         }

         d_stream.close();
         var16 = b_stream.toByteArray();
      } catch (Throwable var15) {
         try {
            b_stream.close();
         } catch (Throwable var10) {
            var15.addSuppressed(var10);
         }

         throw var15;
      }

      b_stream.close();
      return var16;
   }
}
