package fr.xephi.authme.libs.org.mariadb.jdbc.util.constants;

public final class ConnectionState {
   public static final int STATE_NETWORK_TIMEOUT = 1;
   public static final int STATE_DATABASE = 2;
   public static final int STATE_READ_ONLY = 4;
   public static final int STATE_AUTOCOMMIT = 8;
   public static final int STATE_TRANSACTION_ISOLATION = 16;
}
