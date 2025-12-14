package ru.marduk.nedologin.server.handler.plugins;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.marduk.nedologin.server.handler.HandlerPlugin;
import ru.marduk.nedologin.server.handler.Login;

public final class ProtectCoord implements HandlerPlugin {
    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        player.setPos(0, 1000, 0);
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        player.setPos(login.posX, login.posY, login.posZ);
    }

    @Override
    public void preLogout(ServerPlayerEntity player, Login login) {
        player.setPos(login.posX, login.posY, login.posZ);
        //try {
        //    if (PlayerLoginHandler.instance().hasPlayerLoggedIn(player.getGameProfile().getName())) {
        //        final Position pos = new Position(player.getX(), player.getY(), player.getZ());
        //        LastPosData.setLastPos(player, pos);
        //    }
        //    BlockPos spawnPoint = player.getWorldSpawnPos()
        //    player.setPos(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        //} catch (Exception ex) {
        //    Nedologin.logger.error("Fail to set player position to spawn point when logging out.", ex);
        //}
    }

    @Override
    public void disable() {
        // NO-OP
    }
}
