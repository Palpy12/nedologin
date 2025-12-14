package ru.marduk.nedologin.server.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import ru.marduk.nedologin.NLConstants;
import ru.marduk.nedologin.Nedologin;
import ru.marduk.nedologin.server.NLRegistries;
import ru.marduk.nedologin.server.storage.NLStorage;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

public final class PlayerLoginHandler {
    private static PlayerLoginHandler INSTANCE;

    private final Set<Login> loginList = ConcurrentHashMap.newKeySet();

    private final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2, new ThreadFactoryBuilder()
            .setNameFormat("Nedologin-Worker-%d")
            .build());

    private final Map<Identifier, HandlerPlugin> plugins = new ConcurrentHashMap<>();

    private PlayerLoginHandler(Stream<Identifier> plugins) {
        // Load plugins
        plugins.forEach(this::loadPlugin);
    }

    public void loadPlugin(Identifier rl) {
        if (this.plugins.containsKey(rl)) return;
        Nedologin.logger.info("Loading plugin {}", rl.toString());
        HandlerPlugin plugin = NLRegistries.PLUGINS.get(rl).orElseThrow(() ->
                new IllegalArgumentException("No such plugin found: " + rl)).get();

        // Should not be possible though
        Optional.ofNullable(this.plugins.put(rl, plugin)).ifPresent(HandlerPlugin::disable);
        plugin.enable(executor);
    }

    public static void initLoginHandler(Stream<Identifier> pluginList) {
        if (INSTANCE != null) throw new IllegalStateException();
        INSTANCE = new PlayerLoginHandler(pluginList);
    }

    // Singleton
    public static PlayerLoginHandler instance() {
        if (INSTANCE == null) throw new IllegalStateException();
        return INSTANCE;
    }

    public void login(String id, String pwd) {
        id = id.toLowerCase();
        MinecraftServer server = NLConstants.minecraftServer;
        Login login = getLoginByName(id);
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(id);

        // Though player shouldn't be null if login is not null
        if (login == null || player == null) {
            return;
        }

        loginList.remove(login);

        String encoded_pwd = SHA256.getSHA256(pwd);
        if (!NLStorage.instance().storageProvider.registered(id)) {
            NLStorage.instance().storageProvider.register(id, encoded_pwd);
            Nedologin.logger.info("Player {} has successfully registered.", id);
            postLogin(player, login);
        } else if (NLStorage.instance().storageProvider.checkPassword(id, encoded_pwd)) {
            Nedologin.logger.info("Player {} has successfully logged in.", id);
            postLogin(player, login);
        } else {
            Nedologin.logger.warn("Player {} tried to login with a wrong password.", id);
            player.networkHandler.disconnect(Text.literal("Wrong Password."));
        }
    }

    public void stop() {
        Nedologin.logger.info("Shutting down player login handler");
        Nedologin.logger.info("Disabling all plugins");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                Nedologin.logger.error("Timed out waiting player login handler to terminate.");
            }
        } catch (InterruptedException ignore) {
            Nedologin.logger.error("Interrupted when waiting player login handler to terminate.");
        }
    }

    public void postLogin(final ServerPlayerEntity player, final Login login) {
        plugins.values().forEach(p -> p.postLogin(player, login));
    }

    public void playerJoin(final ServerPlayerEntity player) {
        Login login = new Login(player);
        loginList.add(login);
        plugins.values().forEach(p -> p.preLogin(player, login));
    }

    public void playerLeave(ServerPlayerEntity player) {
        plugins.values().forEach(p -> p.preLogout(player, getLoginByName(player.getStringifiedName())));
        loginList.removeIf(l -> l.name.equals(player.getGameProfile().name()));
    }

    @Nullable
    public Login getLoginByName(String name) {
        return loginList.stream().filter(l -> l.name.equals(name)).findAny().orElse(null);
    }
}
