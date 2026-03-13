package com.volmit.iris.util.matter;

import com.volmit.iris.util.math.M;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import lombok.Generated;

public class MatterHeader {
   private String author = "";
   private long createdAt = M.ms();
   private int version = 1;

   public void write(DataOutputStream out) {
      var1.writeUTF(this.author);
      var1.writeLong(this.createdAt);
      var1.writeShort(this.version);
   }

   public void read(DataInputStream din) {
      this.setAuthor(var1.readUTF());
      this.setCreatedAt(var1.readLong());
      this.setVersion(var1.readShort());
   }

   @Generated
   public String getAuthor() {
      return this.author;
   }

   @Generated
   public long getCreatedAt() {
      return this.createdAt;
   }

   @Generated
   public int getVersion() {
      return this.version;
   }

   @Generated
   public void setAuthor(final String author) {
      this.author = var1;
   }

   @Generated
   public void setCreatedAt(final long createdAt) {
      this.createdAt = var1;
   }

   @Generated
   public void setVersion(final int version) {
      this.version = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterHeader)) {
         return false;
      } else {
         MatterHeader var2 = (MatterHeader)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getCreatedAt() != var2.getCreatedAt()) {
            return false;
         } else if (this.getVersion() != var2.getVersion()) {
            return false;
         } else {
            String var3 = this.getAuthor();
            String var4 = var2.getAuthor();
            if (var3 == null) {
               if (var4 == null) {
                  return true;
               }
            } else if (var3.equals(var4)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof MatterHeader;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = this.getCreatedAt();
      int var6 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var6 = var6 * 59 + this.getVersion();
      String var5 = this.getAuthor();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = this.getAuthor();
      return "MatterHeader(author=" + var10000 + ", createdAt=" + this.getCreatedAt() + ", version=" + this.getVersion() + ")";
   }
}
