package ru.marduk.nedologin.server.handler.plugins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.handler.HandlerPlugin;
import ru.marduk.nedologin.server.handler.Login;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;

import static ru.marduk.nedologin.server.handler.LastPositionDataAttachment.LAST_POSITION_ATTACHMENT;

public final class ProtectCoord implements HandlerPlugin {
    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        // NO-OP
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        try {
            Vec3d position = player.getAttachedOrThrow(LAST_POSITION_ATTACHMENT);
            player.setPos(position.getX(), position.getY(), position.getZ());
        } catch (NullPointerException e) {
            Nedologin.logger.error("Fail to get player position to teleport.", e);
        }
    }

    @Override
    public void preLogout(ServerPlayerEntity player, Login login) {
        try {
            if (PlayerLoginHandler.instance().hasPlayerLoggedIn(player.getName().getString())) {
                final Vec3d pos = player.getEntityPos();
                player.setAttached(LAST_POSITION_ATTACHMENT, pos);
            }
            player.setPos(0, 1111, 0);
        } catch (Exception ex) {
            Nedologin.logger.error("Fail to set player position to spawn point when logging out.", ex);
        }
    }

    @Override
    public void disable() {
        // NO-OP
    }
}
