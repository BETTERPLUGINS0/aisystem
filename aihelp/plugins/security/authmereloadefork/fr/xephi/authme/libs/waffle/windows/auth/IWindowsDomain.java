package fr.xephi.authme.libs.waffle.windows.auth;

public interface IWindowsDomain {
   String getFqn();

   String getTrustDirectionString();

   String getTrustTypeString();
}
