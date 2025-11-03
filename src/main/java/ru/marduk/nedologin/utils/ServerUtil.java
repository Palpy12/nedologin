package ru.marduk.nedologin.utils;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public class ServerUtil {
    private static Path serverRootPath;
    private static MinecraftServer server;

    static {
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            server = s;
            serverRootPath = s.getRunDirectory().getRoot();
        });
    }

    public static Path getServerRoot() {
        return serverRootPath;
    }

    public static MinecraftServer getServer() {
        return server;
    }
}