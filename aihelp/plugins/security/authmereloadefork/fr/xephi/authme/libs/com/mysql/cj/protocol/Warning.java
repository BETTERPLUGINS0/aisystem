package fr.xephi.authme.libs.com.mysql.cj.protocol;

public interface Warning {
   int getLevel();

   long getCode();

   String getMessage();
}
