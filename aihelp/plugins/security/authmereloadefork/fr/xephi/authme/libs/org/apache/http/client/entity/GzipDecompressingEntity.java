package fr.xephi.authme.libs.org.apache.http.client.entity;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;

public class GzipDecompressingEntity extends DecompressingEntity {
   public GzipDecompressingEntity(HttpEntity entity) {
      super(entity, GZIPInputStreamFactory.getInstance());
   }
}
