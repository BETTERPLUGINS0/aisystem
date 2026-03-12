package fr.xephi.authme.data.limbo.persistence;

public enum LimboPersistenceType {
   INDIVIDUAL_FILES(IndividualFilesPersistenceHandler.class),
   DISTRIBUTED_FILES(DistributedFilesPersistenceHandler.class),
   DISABLED(NoOpPersistenceHandler.class);

   private final Class<? extends LimboPersistenceHandler> implementationClass;

   private LimboPersistenceType(Class<? extends LimboPersistenceHandler> param3) {
      this.implementationClass = implementationClass;
   }

   public Class<? extends LimboPersistenceHandler> getImplementationClass() {
      return this.implementationClass;
   }

   // $FF: synthetic method
   private static LimboPersistenceType[] $values() {
      return new LimboPersistenceType[]{INDIVIDUAL_FILES, DISTRIBUTED_FILES, DISABLED};
   }
}
