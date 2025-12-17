package ru.marduk.nedologin.server;

import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import ru.marduk.nedologin.NLConstants;
import ru.marduk.nedologin.network.MessageRequestLogin;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;

import java.util.logging.Logger;
import java.util.stream.Stream;

public final class ServerLoader {

    public static void serverSetup() {
        try {
            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                NLConstants.setServer(server);
                Stream<Identifier> plugins = Stream.of(
                        Identifier.of("nedologin", "protect_coord"),
                        //Identifier.of("nedologin", "restrict_game_type"),
                        Identifier.of("nedologin", "timeout")

                        //Эта херня всё ломает
                        //Identifier.of("nedologin", "restrict_movement")
                );
                PlayerLoginHandler.initLoginHandler(plugins);
                ServerPlayConnectionEvents.JOIN.register((handler, sender, minecraftServer) ->
                    ServerPlayNetworking.send(handler.player, new MessageRequestLogin())
                );
            });
        } catch (MinecraftClientHttpException e) {
            Logger.getGlobal().info(e.toString());
        }

        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
            PlayerLoginHandler.instance().stop()
        );
    }
}
