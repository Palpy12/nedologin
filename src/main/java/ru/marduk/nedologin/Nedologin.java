package ru.marduk.nedologin;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.marduk.nedologin.server.ServerLoader;
import ru.marduk.nedologin.utils.ServerUtil;

public final class Nedologin implements ModInitializer {
    public static Logger logger = LogManager.getLogger(NLConstants.MODID);

    //container.getEventBus().addListener(CommandLoader::commonSetup);
    //container.registerConfig(ModConfig.Type.SERVER, NLConfig.SERVER_SPEC);

    @Override
    public void onInitialize() {
        ServerLoader.serverSetup();
        new ServerUtil();
    }
}
