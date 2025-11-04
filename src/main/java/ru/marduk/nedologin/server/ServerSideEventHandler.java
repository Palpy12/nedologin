package ru.marduk.nedologin.server;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;

public class ServerSideEventHandler {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            server.execute(() -> {
                PlayerLoginHandler.instance().playerJoin(handler.player);
            });
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            server.execute(() -> {
                PlayerLoginHandler.instance().playerLeave(handler.player);
            });
        });
    }
}
