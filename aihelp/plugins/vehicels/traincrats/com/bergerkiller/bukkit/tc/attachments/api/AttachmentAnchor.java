package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.math.Quaternion;
import com.bergerkiller.bukkit.common.math.Vector3;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.controller.components.AttachmentControllerMember;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.util.Vector;

public abstract class AttachmentAnchor {
   private static final Map<String, AttachmentAnchor> registry = new LinkedHashMap();
   private static final List<AttachmentAnchor> values = new ArrayList();
   public static AttachmentAnchor DEFAULT = register(new AttachmentAnchor("default") {
      public void apply(Attachment attachment, Matrix4x4 transform) {
      }
   });
   public static AttachmentAnchor SEAT_PARENT = register(new AttachmentAnchor("seat parent") {
      public boolean supports(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType) {
         return attachmentType == CartAttachmentSeat.TYPE;
      }

      public void apply(Attachment attachment, Matrix4x4 transform) {
         if (attachment.getParent() != null) {
            attachment.getParent().applyPassengerSeatTransform(transform);
         }

      }
   });
   public static AttachmentAnchor NO_ROTATION = register(new AttachmentAnchor("no rotation") {
      public void apply(Attachment attachment, Matrix4x4 transform) {
         Vector3 absolutePosition = transform.toVector3();
         transform.setIdentity();
         transform.translate(absolutePosition);
      }
   });
   public static AttachmentAnchor ALIGN_UP = register(new AttachmentAnchor("align up") {
      public void apply(Attachment attachment, Matrix4x4 transform) {
         Vector3 absolutePosition = transform.toVector3();
         Quaternion rotation = transform.getRotation();
         Vector forward = rotation.forwardVector();
         forward.setY(0.0D);
         transform.setIdentity();
         transform.translate(absolutePosition);
         if (forward.lengthSquared() > 1.0E-9D) {
            transform.rotate(Quaternion.fromLookDirection(forward));
         }

      }
   });
   public static AttachmentAnchor ALIGN_UP_PITCH = register(new AttachmentAnchor("align up [P]") {
      public void apply(Attachment attachment, Matrix4x4 transform) {
         Vector3 absolutePosition = transform.toVector3();
         Quaternion rotation = transform.getRotation();
         Vector right = rotation.rightVector();
         Vector forward = new Vector(-right.getZ(), 0.0D, right.getX());
         transform.setIdentity();
         transform.translate(absolutePosition);
         if (forward.lengthSquared() > 1.0E-9D) {
            transform.rotate(Quaternion.fromLookDirection(forward));
         }

      }
   });
   public static AttachmentAnchor SEAT_EYES = register(new AttachmentAnchor("eyes") {
      public boolean supports(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType) {
         return attachmentType == CartAttachmentSeat.TYPE;
      }

      public boolean appliedLate() {
         return true;
      }

      public void apply(Attachment attachment, Matrix4x4 transform) {
         if (attachment instanceof CartAttachmentSeat) {
            ((CartAttachmentSeat)attachment).transformToEyes(transform);
         }

      }
   });
   public static AttachmentAnchor FRONT_WHEEL = register(new AttachmentAnchor("front wheel") {
      public boolean supports(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType) {
         return managerType.isAssignableFrom(AttachmentControllerMember.class);
      }

      public void apply(Attachment attachment, Matrix4x4 transform) {
         if (attachment.getManager() instanceof AttachmentControllerMember) {
            AttachmentControllerMember controller = (AttachmentControllerMember)attachment.getManager();
            controller.getMember().getWheels().front().getAbsoluteTransform(transform);
         }

      }
   });
   public static AttachmentAnchor BACK_WHEEL = register(new AttachmentAnchor("back wheel") {
      public boolean supports(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType) {
         return managerType.isAssignableFrom(AttachmentControllerMember.class);
      }

      public void apply(Attachment attachment, Matrix4x4 transform) {
         if (attachment.getManager() instanceof AttachmentControllerMember) {
            AttachmentControllerMember controller = (AttachmentControllerMember)attachment.getManager();
            controller.getMember().getWheels().back().getAbsoluteTransform(transform);
         }

      }
   });
   public static AttachmentAnchor CART = register(new AttachmentAnchor("cart") {
      public boolean supports(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType) {
         return managerType.isAssignableFrom(AttachmentControllerMember.class);
      }

      public void apply(Attachment attachment, Matrix4x4 transform) {
         if (attachment.getManager() instanceof AttachmentControllerMember) {
            AttachmentControllerMember controller = (AttachmentControllerMember)attachment.getManager();
            transform.set(controller.getLiveTransform());
         }

      }
   });
   private final String _name;

   public AttachmentAnchor(String name) {
      this._name = name;
   }

   public final String getName() {
      return this._name;
   }

   public boolean supports(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType) {
      return true;
   }

   public boolean appliedLate() {
      return false;
   }

   public abstract void apply(Attachment var1, Matrix4x4 var2);

   public static <T extends AttachmentAnchor> T register(T anchor) {
      registry.put(anchor.getName(), anchor);
      registry.put(anchor.getName().toLowerCase(Locale.ENGLISH), anchor);
      registry.put(anchor.getName().toUpperCase(Locale.ENGLISH), anchor);
      values.add(anchor);
      return anchor;
   }

   public static void unregister(AttachmentAnchor anchor) {
      registry.remove(anchor.getName(), anchor);
      registry.remove(anchor.getName().toLowerCase(Locale.ENGLISH), anchor);
      registry.remove(anchor.getName().toUpperCase(Locale.ENGLISH), anchor);
      values.remove(anchor);
   }

   public static Collection<AttachmentAnchor> values() {
      return values;
   }

   public static AttachmentAnchor find(Class<? extends AttachmentManager> managerType, AttachmentType attachmentType, String name) {
      AttachmentAnchor anchor = (AttachmentAnchor)registry.get(name);
      return anchor != null && anchor.supports(managerType, attachmentType) ? anchor : new AttachmentAnchor(name) {
         public void apply(Attachment attachment, Matrix4x4 transform) {
         }
      };
   }

   public String toString() {
      return this.getName();
   }
}
