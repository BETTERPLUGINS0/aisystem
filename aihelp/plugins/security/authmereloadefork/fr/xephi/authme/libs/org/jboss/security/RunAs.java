package fr.xephi.authme.libs.org.jboss.security;

import java.security.Principal;

public interface RunAs extends Principal {
   <T> T getIdentity();

   <T> T getProof();
}
