package fr.xephi.authme.data.auth;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.security.crypts.HashedPassword;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Location;

public class PlayerAuth {
   public static final String DB_EMAIL_DEFAULT = "your@email.com";
   public static final long DB_LAST_LOGIN_DEFAULT = 0L;
   public static final String DB_LAST_IP_DEFAULT = "127.0.0.1";
   private String nickname;
   private String realName;
   private HashedPassword password;
   private String totpKey;
   private String email;
   private String lastIp;
   private int groupId;
   private Long lastLogin;
   private String registrationIp;
   private long registrationDate;
   private double x;
   private double y;
   private double z;
   private String world;
   private float yaw;
   private float pitch;
   private UUID uuid;

   private PlayerAuth() {
   }

   public void setNickname(String nickname) {
      this.nickname = nickname.toLowerCase(Locale.ROOT);
   }

   public String getNickname() {
      return this.nickname;
   }

   public String getRealName() {
      return this.realName;
   }

   public void setRealName(String realName) {
      this.realName = realName;
   }

   public int getGroupId() {
      return this.groupId;
   }

   public void setQuitLocation(Location location) {
      this.x = (double)location.getBlockX();
      this.y = (double)location.getBlockY();
      this.z = (double)location.getBlockZ();
      this.world = location.getWorld().getName();
   }

   public double getQuitLocX() {
      return this.x;
   }

   public void setQuitLocX(double d) {
      this.x = d;
   }

   public double getQuitLocY() {
      return this.y;
   }

   public void setQuitLocY(double d) {
      this.y = d;
   }

   public double getQuitLocZ() {
      return this.z;
   }

   public void setQuitLocZ(double d) {
      this.z = d;
   }

   public String getWorld() {
      return this.world;
   }

   public void setWorld(String world) {
      this.world = world;
   }

   public float getYaw() {
      return this.yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public String getLastIp() {
      return this.lastIp;
   }

   public void setLastIp(String lastIp) {
      this.lastIp = lastIp;
   }

   public Long getLastLogin() {
      return this.lastLogin;
   }

   public void setLastLogin(long lastLogin) {
      this.lastLogin = lastLogin;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public HashedPassword getPassword() {
      return this.password;
   }

   public void setPassword(HashedPassword password) {
      this.password = password;
   }

   public String getRegistrationIp() {
      return this.registrationIp;
   }

   public long getRegistrationDate() {
      return this.registrationDate;
   }

   public void setRegistrationDate(long registrationDate) {
      this.registrationDate = registrationDate;
   }

   public String getTotpKey() {
      return this.totpKey;
   }

   public void setTotpKey(String totpKey) {
      this.totpKey = totpKey;
   }

   public UUID getUuid() {
      return this.uuid;
   }

   public void setUuid(UUID uuid) {
      this.uuid = uuid;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof PlayerAuth)) {
         return false;
      } else {
         PlayerAuth other = (PlayerAuth)obj;
         return Objects.equals(other.lastIp, this.lastIp) && Objects.equals(other.nickname, this.nickname);
      }
   }

   public int hashCode() {
      int hashCode = 7;
      int hashCode = 71 * hashCode + (this.nickname != null ? this.nickname.hashCode() : 0);
      hashCode = 71 * hashCode + (this.lastIp != null ? this.lastIp.hashCode() : 0);
      return hashCode;
   }

   public String toString() {
      return "Player : " + this.nickname + " | " + this.realName + " ! IP : " + this.lastIp + " ! LastLogin : " + this.lastLogin + " ! LastPosition : " + this.x + "," + this.y + "," + this.z + "," + this.world + " ! Email : " + this.email + " ! Password : {" + this.password.getHash() + ", " + this.password.getSalt() + "} ! UUID : " + this.uuid;
   }

   public static PlayerAuth.Builder builder() {
      return new PlayerAuth.Builder();
   }

   // $FF: synthetic method
   PlayerAuth(Object x0) {
      this();
   }

   public static final class Builder {
      private String name;
      private String realName;
      private HashedPassword password;
      private String totpKey;
      private String lastIp;
      private String email;
      private int groupId = -1;
      private Long lastLogin;
      private String registrationIp;
      private Long registrationDate;
      private double x;
      private double y;
      private double z;
      private String world;
      private float yaw;
      private float pitch;
      private UUID uuid;

      public PlayerAuth build() {
         PlayerAuth auth = new PlayerAuth();
         auth.nickname = ((String)Preconditions.checkNotNull(this.name)).toLowerCase(Locale.ROOT);
         auth.realName = (String)Optional.ofNullable(this.realName).orElse("Player");
         auth.password = (HashedPassword)Optional.ofNullable(this.password).orElse(new HashedPassword(""));
         auth.totpKey = this.totpKey;
         auth.email = "your@email.com".equals(this.email) ? null : this.email;
         auth.lastIp = this.lastIp;
         auth.groupId = this.groupId;
         auth.lastLogin = isEqualTo(this.lastLogin, 0L) ? null : this.lastLogin;
         auth.registrationIp = this.registrationIp;
         auth.registrationDate = this.registrationDate == null ? System.currentTimeMillis() : this.registrationDate;
         auth.x = this.x;
         auth.y = this.y;
         auth.z = this.z;
         auth.world = (String)Optional.ofNullable(this.world).orElse("world");
         auth.yaw = this.yaw;
         auth.pitch = this.pitch;
         auth.uuid = this.uuid;
         return auth;
      }

      private static boolean isEqualTo(Long value, long defaultValue) {
         return value != null && defaultValue == value;
      }

      public PlayerAuth.Builder name(String name) {
         this.name = name;
         return this;
      }

      public PlayerAuth.Builder realName(String realName) {
         this.realName = realName;
         return this;
      }

      public PlayerAuth.Builder password(HashedPassword password) {
         this.password = password;
         return this;
      }

      public PlayerAuth.Builder password(String hash, String salt) {
         return this.password(new HashedPassword(hash, salt));
      }

      public PlayerAuth.Builder totpKey(String totpKey) {
         this.totpKey = totpKey;
         return this;
      }

      public PlayerAuth.Builder lastIp(String lastIp) {
         this.lastIp = lastIp;
         return this;
      }

      public PlayerAuth.Builder location(Location location) {
         this.x = location.getX();
         this.y = location.getY();
         this.z = location.getZ();
         this.world = location.getWorld().getName();
         this.yaw = location.getYaw();
         this.pitch = location.getPitch();
         return this;
      }

      public PlayerAuth.Builder locX(double x) {
         this.x = x;
         return this;
      }

      public PlayerAuth.Builder locY(double y) {
         this.y = y;
         return this;
      }

      public PlayerAuth.Builder locZ(double z) {
         this.z = z;
         return this;
      }

      public PlayerAuth.Builder locWorld(String world) {
         this.world = world;
         return this;
      }

      public PlayerAuth.Builder locYaw(float yaw) {
         this.yaw = yaw;
         return this;
      }

      public PlayerAuth.Builder locPitch(float pitch) {
         this.pitch = pitch;
         return this;
      }

      public PlayerAuth.Builder lastLogin(Long lastLogin) {
         this.lastLogin = lastLogin;
         return this;
      }

      public PlayerAuth.Builder groupId(int groupId) {
         this.groupId = groupId;
         return this;
      }

      public PlayerAuth.Builder email(String email) {
         this.email = email;
         return this;
      }

      public PlayerAuth.Builder registrationIp(String ip) {
         this.registrationIp = ip;
         return this;
      }

      public PlayerAuth.Builder registrationDate(long date) {
         this.registrationDate = date;
         return this;
      }

      public PlayerAuth.Builder uuid(UUID uuid) {
         this.uuid = uuid;
         return this;
      }
   }
}
