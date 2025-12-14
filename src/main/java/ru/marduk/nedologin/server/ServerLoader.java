package ru.marduk.nedologin.server;

import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import ru.marduk.nedologin.NLConstants;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;
import ru.marduk.nedologin.server.storage.NLStorage;

import java.util.logging.Logger;
import java.util.stream.Stream;

public final class ServerLoader {

    public static void serverSetup() {
        try {
            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                NLConstants.setServer(server);
                Stream<Identifier> plugins = Stream.of(
                        //Identifier.of("nedologin", "auto_save")/*,
                        //Identifier.of("nedologin", "protect_coord"),
                        //Identifier.of("nedologin", "restrict_game_type"),
                        //Identifier.of("nedologin", "timeout")/*,
                        //Identifier.of("nedologin", "restrict_movement")
                );
                PlayerLoginHandler.initLoginHandler(plugins);
            });
        } catch (MinecraftClientHttpException e) {
            Logger.getGlobal().info(e.toString());
        }

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            PlayerLoginHandler.instance().stop();

            Nedologin.logger.info("Saving all entries");
            if (NLStorage.instance() != null) {
                NLStorage.instance().storageProvider.save();
            }
        });
    }
}
