package fr.xephi.authme.libs.org.apache.http.client.entity;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;

public class DeflateDecompressingEntity extends DecompressingEntity {
   public DeflateDecompressingEntity(HttpEntity entity) {
      super(entity, DeflateInputStreamFactory.getInstance());
   }
}
