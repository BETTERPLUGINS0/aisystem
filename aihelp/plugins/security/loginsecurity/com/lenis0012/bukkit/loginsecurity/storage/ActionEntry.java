package com.lenis0012.bukkit.loginsecurity.storage;

import java.sql.Timestamp;

public class ActionEntry {
   private int id;
   private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
   private String uniqueUserId;
   private String type;
   private String service;
   private String provider;

   public int getId() {
      return this.id;
   }

   public Timestamp getTimestamp() {
      return this.timestamp;
   }

   public String getUniqueUserId() {
      return this.uniqueUserId;
   }

   public String getType() {
      return this.type;
   }

   public String getService() {
      return this.service;
   }

   public String getProvider() {
      return this.provider;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setTimestamp(Timestamp timestamp) {
      this.timestamp = timestamp;
   }

   public void setUniqueUserId(String uniqueUserId) {
      this.uniqueUserId = uniqueUserId;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setService(String service) {
      this.service = service;
   }

   public void setProvider(String provider) {
      this.provider = provider;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ActionEntry)) {
         return false;
      } else {
         ActionEntry other = (ActionEntry)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getId() != other.getId()) {
            return false;
         } else {
            label73: {
               Object this$timestamp = this.getTimestamp();
               Object other$timestamp = other.getTimestamp();
               if (this$timestamp == null) {
                  if (other$timestamp == null) {
                     break label73;
                  }
               } else if (this$timestamp.equals(other$timestamp)) {
                  break label73;
               }

               return false;
            }

            Object this$uniqueUserId = this.getUniqueUserId();
            Object other$uniqueUserId = other.getUniqueUserId();
            if (this$uniqueUserId == null) {
               if (other$uniqueUserId != null) {
                  return false;
               }
            } else if (!this$uniqueUserId.equals(other$uniqueUserId)) {
               return false;
            }

            label59: {
               Object this$type = this.getType();
               Object other$type = other.getType();
               if (this$type == null) {
                  if (other$type == null) {
                     break label59;
                  }
               } else if (this$type.equals(other$type)) {
                  break label59;
               }

               return false;
            }

            Object this$service = this.getService();
            Object other$service = other.getService();
            if (this$service == null) {
               if (other$service != null) {
                  return false;
               }
            } else if (!this$service.equals(other$service)) {
               return false;
            }

            Object this$provider = this.getProvider();
            Object other$provider = other.getProvider();
            if (this$provider == null) {
               if (other$provider != null) {
                  return false;
               }
            } else if (!this$provider.equals(other$provider)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ActionEntry;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = result * 59 + this.getId();
      Object $timestamp = this.getTimestamp();
      result = result * 59 + ($timestamp == null ? 43 : $timestamp.hashCode());
      Object $uniqueUserId = this.getUniqueUserId();
      result = result * 59 + ($uniqueUserId == null ? 43 : $uniqueUserId.hashCode());
      Object $type = this.getType();
      result = result * 59 + ($type == null ? 43 : $type.hashCode());
      Object $service = this.getService();
      result = result * 59 + ($service == null ? 43 : $service.hashCode());
      Object $provider = this.getProvider();
      result = result * 59 + ($provider == null ? 43 : $provider.hashCode());
      return result;
   }

   public String toString() {
      return "ActionEntry(id=" + this.getId() + ", timestamp=" + this.getTimestamp() + ", uniqueUserId=" + this.getUniqueUserId() + ", type=" + this.getType() + ", service=" + this.getService() + ", provider=" + this.getProvider() + ")";
   }
}
