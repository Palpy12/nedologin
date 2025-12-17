package ru.marduk.nedologin.server.handler;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class LastPositionDataAttachment {
    public static final AttachmentType<Vec3d> LAST_POSITION_ATTACHMENT = AttachmentRegistry.create(
            Identifier.of("nedologin", "last_position_attachment"),
            builder -> builder
                    .persistent(Vec3d.CODEC)
                    .initializer(() -> new Vec3d(0, 11111, 0)) // The default value of the Attachment, if one has not been set.
                    .copyOnDeath()
    );
}
