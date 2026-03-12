package fr.xephi.authme.libs.org.postgresql.hostchooser;

public enum HostStatus {
   ConnectFail,
   ConnectOK,
   Primary,
   Secondary;

   // $FF: synthetic method
   private static HostStatus[] $values() {
      return new HostStatus[]{ConnectFail, ConnectOK, Primary, Secondary};
   }
}
