package fr.xephi.authme.datasource.columnshandler;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.settings.properties.DatabaseSettings;

public final class AuthMeColumns {
   public static final PlayerAuthColumn<String> NAME;
   public static final PlayerAuthColumn<String> NICK_NAME;
   public static final PlayerAuthColumn<String> PASSWORD;
   public static final PlayerAuthColumn<String> SALT;
   public static final PlayerAuthColumn<String> EMAIL;
   public static final PlayerAuthColumn<String> LAST_IP;
   public static final PlayerAuthColumn<Integer> GROUP_ID;
   public static final PlayerAuthColumn<Long> LAST_LOGIN;
   public static final PlayerAuthColumn<String> REGISTRATION_IP;
   public static final PlayerAuthColumn<Long> REGISTRATION_DATE;
   public static final PlayerAuthColumn<String> UUID;
   public static final PlayerAuthColumn<Double> LOCATION_X;
   public static final PlayerAuthColumn<Double> LOCATION_Y;
   public static final PlayerAuthColumn<Double> LOCATION_Z;
   public static final PlayerAuthColumn<String> LOCATION_WORLD;
   public static final PlayerAuthColumn<Float> LOCATION_YAW;
   public static final PlayerAuthColumn<Float> LOCATION_PITCH;
   public static final DataSourceColumn<Integer> IS_LOGGED;
   public static final DataSourceColumn<Integer> HAS_SESSION;

   private AuthMeColumns() {
   }

   static {
      NAME = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_NAME, PlayerAuth::getNickname);
      NICK_NAME = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_REALNAME, PlayerAuth::getRealName);
      PASSWORD = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_PASSWORD, (auth) -> {
         return auth.getPassword().getHash();
      });
      SALT = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_SALT, (auth) -> {
         return auth.getPassword().getSalt();
      }, AuthMeColumnsFactory.ColumnOptions.OPTIONAL);
      EMAIL = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_EMAIL, PlayerAuth::getEmail, AuthMeColumnsFactory.ColumnOptions.DEFAULT_FOR_NULL);
      LAST_IP = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_LAST_IP, PlayerAuth::getLastIp);
      GROUP_ID = AuthMeColumnsFactory.createInteger(DatabaseSettings.MYSQL_COL_GROUP, PlayerAuth::getGroupId, AuthMeColumnsFactory.ColumnOptions.OPTIONAL);
      LAST_LOGIN = AuthMeColumnsFactory.createLong(DatabaseSettings.MYSQL_COL_LASTLOGIN, PlayerAuth::getLastLogin);
      REGISTRATION_IP = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_REGISTER_IP, PlayerAuth::getRegistrationIp);
      REGISTRATION_DATE = AuthMeColumnsFactory.createLong(DatabaseSettings.MYSQL_COL_REGISTER_DATE, PlayerAuth::getRegistrationDate);
      UUID = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_PLAYER_UUID, (auth) -> {
         return auth.getUuid() == null ? null : auth.getUuid().toString();
      }, AuthMeColumnsFactory.ColumnOptions.OPTIONAL);
      LOCATION_X = AuthMeColumnsFactory.createDouble(DatabaseSettings.MYSQL_COL_LASTLOC_X, PlayerAuth::getQuitLocX);
      LOCATION_Y = AuthMeColumnsFactory.createDouble(DatabaseSettings.MYSQL_COL_LASTLOC_Y, PlayerAuth::getQuitLocY);
      LOCATION_Z = AuthMeColumnsFactory.createDouble(DatabaseSettings.MYSQL_COL_LASTLOC_Z, PlayerAuth::getQuitLocZ);
      LOCATION_WORLD = AuthMeColumnsFactory.createString(DatabaseSettings.MYSQL_COL_LASTLOC_WORLD, PlayerAuth::getWorld);
      LOCATION_YAW = AuthMeColumnsFactory.createFloat(DatabaseSettings.MYSQL_COL_LASTLOC_YAW, PlayerAuth::getYaw);
      LOCATION_PITCH = AuthMeColumnsFactory.createFloat(DatabaseSettings.MYSQL_COL_LASTLOC_PITCH, PlayerAuth::getPitch);
      IS_LOGGED = AuthMeColumnsFactory.createInteger(DatabaseSettings.MYSQL_COL_ISLOGGED);
      HAS_SESSION = AuthMeColumnsFactory.createInteger(DatabaseSettings.MYSQL_COL_HASSESSION);
   }
}
