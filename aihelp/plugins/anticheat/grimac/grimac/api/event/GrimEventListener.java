package ac.grim.grimac.api.event;

@FunctionalInterface
public interface GrimEventListener<T extends GrimEvent> {
   void handle(T var1) throws Exception;
}
