package ru.marduk.nedologin.server.handler.plugins;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import ru.marduk.nedologin.server.handler.HandlerPlugin;
import ru.marduk.nedologin.server.handler.Login;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class Timeout implements HandlerPlugin {
    private ScheduledExecutorService executor;
    private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    @Override
    public void enable(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        ScheduledFuture<?> future = executor.schedule(() ->
            player.networkHandler.disconnect(Text.literal("Login timeout")), 1, TimeUnit.SECONDS);

        Optional.ofNullable(futures.put(login.name, future))
                .ifPresent(f -> f.cancel(true));
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        Optional.ofNullable(futures.remove(login.name)).ifPresent(f -> f.cancel(true));
    }

    @Override
    public void preLogout(ServerPlayerEntity player, Login login) {
        Optional.ofNullable(futures.remove(player.getGameProfile().name().toLowerCase()))
                .ifPresent(f -> f.cancel(true));
    }

    @Override
    public void disable() {
        futures.values().forEach(f -> f.cancel(true));
    }
}
