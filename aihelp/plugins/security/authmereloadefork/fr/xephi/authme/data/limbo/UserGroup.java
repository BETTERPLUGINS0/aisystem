package fr.xephi.authme.data.limbo;

import java.util.Map;
import java.util.Objects;

public class UserGroup {
   private String groupName;
   private Map<String, String> contextMap;

   public UserGroup(String groupName) {
      this.groupName = groupName;
   }

   public UserGroup(String groupName, Map<String, String> contextMap) {
      this.groupName = groupName;
      this.contextMap = contextMap;
   }

   public String getGroupName() {
      return this.groupName;
   }

   public Map<String, String> getContextMap() {
      return this.contextMap;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         UserGroup userGroup = (UserGroup)o;
         return Objects.equals(this.groupName, userGroup.groupName) && Objects.equals(this.contextMap, userGroup.contextMap);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.groupName, this.contextMap});
   }
}
