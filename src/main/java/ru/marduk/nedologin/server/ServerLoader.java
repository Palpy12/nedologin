package ru.marduk.nedologin.server;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.handler.PlayerLoginHandler;
import ru.marduk.nedologin.server.storage.NLStorage;
import ru.marduk.nedologin.NLConfig;

import java.io.IOException;

public final class ServerLoader {

    public static void serverSetup() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            NLStorage.initialize(NLConfig.SERVER.storageProvider.get());

            PlayerLoginHandler.initLoginHandler(NLConfig.SERVER.plugins.get().stream().map(ResourceLocation::parse));
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            PlayerLoginHandler.instance().stop();

            Nedologin.logger.info("Saving all entries");
            if (NLStorage.instance() != null) {
                try {
                    NLStorage.instance().storageProvider.save();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
