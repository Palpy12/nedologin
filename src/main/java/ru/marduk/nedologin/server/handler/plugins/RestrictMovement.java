package ru.marduk.nedologin.server.handler.plugins;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.marduk.nedologin.server.handler.HandlerPlugin;
import ru.marduk.nedologin.server.handler.Login;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class RestrictMovement implements HandlerPlugin {
    private ScheduledExecutorService executor;
    private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    @Override
    public void enable(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void preLogin(ServerPlayerEntity player, Login login) {
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() ->
            ServerTickEvents.END_SERVER_TICK.register(listener -> {
                player.setPos(login.posX, login.posY, login.posZ);
                player.teleport(login.posX, login.posY, login.posZ, false);
            }), 0, 100, TimeUnit.MILLISECONDS);
        Optional.ofNullable(futures.put(login.name, future)).ifPresent(f -> f.cancel(true));
    }

    @Override
    public void postLogin(ServerPlayerEntity player, Login login) {
        Optional.ofNullable(futures.remove(login.name))
                .ifPresent(f -> f.cancel(true));
    }

    @Override
    public void preLogout(ServerPlayerEntity player) {
        Optional.ofNullable(futures.remove(player.getGameProfile().name().toLowerCase()))
                .ifPresent(f -> f.cancel(true));
    }

    @Override
    public void disable() {
        this.futures.values().forEach(f -> f.cancel(true));
    }
}
