package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.data.packetentity.JumpableEntity;
import ac.grim.grimac.utils.enums.BoatEntityStatus;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class VehicleData {
   public boolean boatUnderwater = false;
   public double lastYd;
   public double midTickY;
   public float landFriction;
   public BoatEntityStatus status;
   public BoatEntityStatus oldStatus;
   public double waterLevel;
   public float deltaRotation;
   public float nextVehicleHorizontal = 0.0F;
   public float nextVehicleForward = 0.0F;
   public float vehicleHorizontal = 0.0F;
   public float vehicleForward = 0.0F;
   public boolean lastDummy = false;
   public boolean wasVehicleSwitch = false;
   public float playerPitch = 0.0F;
   public float playerYaw = 0.0F;
   public final Deque<Pair<Integer, JumpableEntity>> pendingJumps = new ArrayDeque();
   public final ConcurrentLinkedQueue<Pair<Integer, Vector3d>> vehicleTeleports = new ConcurrentLinkedQueue();
}
