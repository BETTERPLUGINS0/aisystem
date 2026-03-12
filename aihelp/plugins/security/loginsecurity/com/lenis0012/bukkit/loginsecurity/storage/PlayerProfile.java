package com.lenis0012.bukkit.loginsecurity.storage;

import com.lenis0012.bukkit.loginsecurity.util.UserIdMode;
import java.sql.Date;
import java.sql.Timestamp;

public class PlayerProfile {
   private int id;
   private String uniqueUserId;
   private UserIdMode uniqueIdMode;
   private String lastName;
   private String ipAddress;
   private String password;
   private int hashingAlgorithm;
   private Integer inventoryId;
   private Integer loginLocationId;
   private Timestamp lastLogin;
   private Date registrationDate;
   private long version;

   public PlayerProfile() {
      this.uniqueIdMode = UserIdMode.UNKNOWN;
      this.lastLogin = new Timestamp(System.currentTimeMillis());
      this.registrationDate = new Date(System.currentTimeMillis());
   }

   public int getId() {
      return this.id;
   }

   public String getUniqueUserId() {
      return this.uniqueUserId;
   }

   public UserIdMode getUniqueIdMode() {
      return this.uniqueIdMode;
   }

   public String getLastName() {
      return this.lastName;
   }

   public String getIpAddress() {
      return this.ipAddress;
   }

   public String getPassword() {
      return this.password;
   }

   public int getHashingAlgorithm() {
      return this.hashingAlgorithm;
   }

   public Integer getInventoryId() {
      return this.inventoryId;
   }

   public Integer getLoginLocationId() {
      return this.loginLocationId;
   }

   public Timestamp getLastLogin() {
      return this.lastLogin;
   }

   public Date getRegistrationDate() {
      return this.registrationDate;
   }

   public long getVersion() {
      return this.version;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setUniqueUserId(String uniqueUserId) {
      this.uniqueUserId = uniqueUserId;
   }

   public void setUniqueIdMode(UserIdMode uniqueIdMode) {
      this.uniqueIdMode = uniqueIdMode;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setHashingAlgorithm(int hashingAlgorithm) {
      this.hashingAlgorithm = hashingAlgorithm;
   }

   public void setInventoryId(Integer inventoryId) {
      this.inventoryId = inventoryId;
   }

   public void setLoginLocationId(Integer loginLocationId) {
      this.loginLocationId = loginLocationId;
   }

   public void setLastLogin(Timestamp lastLogin) {
      this.lastLogin = lastLogin;
   }

   public void setRegistrationDate(Date registrationDate) {
      this.registrationDate = registrationDate;
   }

   public void setVersion(long version) {
      this.version = version;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof PlayerProfile)) {
         return false;
      } else {
         PlayerProfile other = (PlayerProfile)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (this.getId() != other.getId()) {
            return false;
         } else if (this.getHashingAlgorithm() != other.getHashingAlgorithm()) {
            return false;
         } else if (this.getVersion() != other.getVersion()) {
            return false;
         } else {
            Object this$inventoryId = this.getInventoryId();
            Object other$inventoryId = other.getInventoryId();
            if (this$inventoryId == null) {
               if (other$inventoryId != null) {
                  return false;
               }
            } else if (!this$inventoryId.equals(other$inventoryId)) {
               return false;
            }

            label119: {
               Object this$loginLocationId = this.getLoginLocationId();
               Object other$loginLocationId = other.getLoginLocationId();
               if (this$loginLocationId == null) {
                  if (other$loginLocationId == null) {
                     break label119;
                  }
               } else if (this$loginLocationId.equals(other$loginLocationId)) {
                  break label119;
               }

               return false;
            }

            label112: {
               Object this$uniqueUserId = this.getUniqueUserId();
               Object other$uniqueUserId = other.getUniqueUserId();
               if (this$uniqueUserId == null) {
                  if (other$uniqueUserId == null) {
                     break label112;
                  }
               } else if (this$uniqueUserId.equals(other$uniqueUserId)) {
                  break label112;
               }

               return false;
            }

            Object this$uniqueIdMode = this.getUniqueIdMode();
            Object other$uniqueIdMode = other.getUniqueIdMode();
            if (this$uniqueIdMode == null) {
               if (other$uniqueIdMode != null) {
                  return false;
               }
            } else if (!this$uniqueIdMode.equals(other$uniqueIdMode)) {
               return false;
            }

            Object this$lastName = this.getLastName();
            Object other$lastName = other.getLastName();
            if (this$lastName == null) {
               if (other$lastName != null) {
                  return false;
               }
            } else if (!this$lastName.equals(other$lastName)) {
               return false;
            }

            label91: {
               Object this$ipAddress = this.getIpAddress();
               Object other$ipAddress = other.getIpAddress();
               if (this$ipAddress == null) {
                  if (other$ipAddress == null) {
                     break label91;
                  }
               } else if (this$ipAddress.equals(other$ipAddress)) {
                  break label91;
               }

               return false;
            }

            label84: {
               Object this$password = this.getPassword();
               Object other$password = other.getPassword();
               if (this$password == null) {
                  if (other$password == null) {
                     break label84;
                  }
               } else if (this$password.equals(other$password)) {
                  break label84;
               }

               return false;
            }

            Object this$lastLogin = this.getLastLogin();
            Object other$lastLogin = other.getLastLogin();
            if (this$lastLogin == null) {
               if (other$lastLogin != null) {
                  return false;
               }
            } else if (!this$lastLogin.equals(other$lastLogin)) {
               return false;
            }

            Object this$registrationDate = this.getRegistrationDate();
            Object other$registrationDate = other.getRegistrationDate();
            if (this$registrationDate == null) {
               if (other$registrationDate != null) {
                  return false;
               }
            } else if (!this$registrationDate.equals(other$registrationDate)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof PlayerProfile;
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      int result = result * 59 + this.getId();
      result = result * 59 + this.getHashingAlgorithm();
      long $version = this.getVersion();
      result = result * 59 + (int)($version >>> 32 ^ $version);
      Object $inventoryId = this.getInventoryId();
      result = result * 59 + ($inventoryId == null ? 43 : $inventoryId.hashCode());
      Object $loginLocationId = this.getLoginLocationId();
      result = result * 59 + ($loginLocationId == null ? 43 : $loginLocationId.hashCode());
      Object $uniqueUserId = this.getUniqueUserId();
      result = result * 59 + ($uniqueUserId == null ? 43 : $uniqueUserId.hashCode());
      Object $uniqueIdMode = this.getUniqueIdMode();
      result = result * 59 + ($uniqueIdMode == null ? 43 : $uniqueIdMode.hashCode());
      Object $lastName = this.getLastName();
      result = result * 59 + ($lastName == null ? 43 : $lastName.hashCode());
      Object $ipAddress = this.getIpAddress();
      result = result * 59 + ($ipAddress == null ? 43 : $ipAddress.hashCode());
      Object $password = this.getPassword();
      result = result * 59 + ($password == null ? 43 : $password.hashCode());
      Object $lastLogin = this.getLastLogin();
      result = result * 59 + ($lastLogin == null ? 43 : $lastLogin.hashCode());
      Object $registrationDate = this.getRegistrationDate();
      result = result * 59 + ($registrationDate == null ? 43 : $registrationDate.hashCode());
      return result;
   }

   public String toString() {
      return "PlayerProfile(id=" + this.getId() + ", uniqueUserId=" + this.getUniqueUserId() + ", uniqueIdMode=" + this.getUniqueIdMode() + ", lastName=" + this.getLastName() + ", ipAddress=" + this.getIpAddress() + ", password=" + this.getPassword() + ", hashingAlgorithm=" + this.getHashingAlgorithm() + ", inventoryId=" + this.getInventoryId() + ", loginLocationId=" + this.getLoginLocationId() + ", lastLogin=" + this.getLastLogin() + ", registrationDate=" + this.getRegistrationDate() + ", version=" + this.getVersion() + ")";
   }
}
