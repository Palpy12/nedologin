package ru.marduk.nedologin.server.handler.plugins;

import net.minecraft.server.network.ServerPlayerEntity;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.handler.Login;
import ru.marduk.nedologin.server.storage.NLStorage;
import ru.marduk.nedologin.server.handler.HandlerPlugin;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class AutoSave implements HandlerPlugin {
    private ScheduledFuture<?> future;

    @Override
    public void enable(ScheduledExecutorService executor) {
        this.future = executor.scheduleAtFixedRate(() -> {
            Nedologin.logger.info("Auto saving entries");
            long start = System.currentTimeMillis();
            try {
                NLStorage.instance().storageProvider.save();
            } catch (IOException e) {
                Nedologin.logger.error("Failed saving nedologin entries", e);
            }
            Nedologin.logger.info("Done! Took " + (System.currentTimeMillis() - start) + "ms.");
        }, 0, 5, TimeUnit.MINUTES);
    }

    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        // NO-OP
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        // NO-OP
    }

    @Override
    public void preLogout(ServerPlayerEntity player, Login login) {
        // NO-OP
    }

    @Override
    public void disable() {
        future.cancel(true);
    }
}
