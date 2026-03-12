package fr.xephi.authme.libs.com.maxmind.db.model;

import java.util.Objects;

public abstract class AbstractRecord {
   private final String name;
   private final Integer geoNameId;

   protected AbstractRecord(String name, Integer geoNameId) {
      this.name = name;
      this.geoNameId = geoNameId;
   }

   public String getName() {
      return this.name;
   }

   public Integer getGeoNameId() {
      return this.geoNameId;
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (other instanceof AbstractRecord) {
         AbstractRecord that = (AbstractRecord)other;
         return Objects.equals(this.geoNameId, that.geoNameId);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.geoNameId;
   }

   public String toString() {
      return "AbstractRecord{name='" + this.name + '\'' + ", geoNameId=" + this.geoNameId + '}';
   }
}
