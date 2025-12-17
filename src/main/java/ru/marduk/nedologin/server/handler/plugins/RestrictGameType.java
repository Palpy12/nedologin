package ru.marduk.nedologin.server.handler.plugins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import ru.marduk.nedologin.server.handler.HandlerPlugin;
import ru.marduk.nedologin.server.handler.Login;

public final class RestrictGameType implements HandlerPlugin {
    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        player.changeGameMode(GameMode.SPECTATOR);
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        player.changeGameMode(GameMode.byId(login.gamemode));
    }

    @Override
    public void preLogout(ServerPlayerEntity player) {
        //player.changeGameMode(GameMode.byId(login.gamemode));
    }

    @Override
    public void disable() {
        // NO-OP
    }
}
