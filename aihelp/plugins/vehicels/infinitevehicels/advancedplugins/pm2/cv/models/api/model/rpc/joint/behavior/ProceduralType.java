package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

public enum ProceduralType {
   ANIMATION,
   TRANSFORM;

   private static ProceduralType[] $values() {
      return new ProceduralType[]{ANIMATION, TRANSFORM};
   }

   // $FF: synthetic method
   private static ProceduralType[] $values$() {
      return new ProceduralType[]{ANIMATION, TRANSFORM};
   }
}
