package ru.marduk.nedologin.server;

import net.minecraft.util.Identifier;
import ru.marduk.nedologin.server.handler.HandlerPlugin;
import ru.marduk.nedologin.server.storage.StorageProvider;
import ru.marduk.nedologin.server.storage.StorageProviderFile;
import ru.marduk.nedologin.server.storage.StorageProviderMariaDB;
import ru.marduk.nedologin.server.storage.StorageProviderSQLite;
import ru.marduk.nedologin.utils.ServerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class NLRegistries<S> {
    private final Map<Identifier, Supplier<? extends S>> plugins = new HashMap<>();

    public synchronized void register(Identifier rl, Supplier<? extends S> plugin) {
        if (plugins.containsKey(rl)) {
            throw new IllegalArgumentException("Resource location " + rl.toString() + " already exists.");
        }
        plugins.put(rl, plugin);
    }

    public Optional<Supplier<? extends S>> get(Identifier rl) {
        return Optional.ofNullable(plugins.get(rl));
    }

    private NLRegistries() {
    }

    public static final NLRegistries<HandlerPlugin> PLUGINS = new NLRegistries<>();
    public static final NLRegistries<StorageProvider> STORAGE_PROVIDERS = new NLRegistries<>();

    static {
        // Default storage providers
        STORAGE_PROVIDERS.register(Identifier.of("nedologin", "file"),
                () -> mustCall(() -> new StorageProviderFile(ServerUtil.getServerRoot().resolve("nl_entries.db"))));
        STORAGE_PROVIDERS.register(Identifier.of("nedologin", "sqlite"),
                () -> mustCall((Callable<StorageProvider>) StorageProviderSQLite::new));
        STORAGE_PROVIDERS.register(Identifier.of("nedologin", "mariadb"),
                () -> mustCall((Callable<StorageProvider>) StorageProviderMariaDB::new));
    }

    private static <S> S mustCall(Callable<S> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
