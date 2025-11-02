package ru.marduk.nedologin.client;

import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import ru.marduk.nedologin.NLConstants;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NLConstants.MODID, value = Dist.CLIENT)
public final class EventHandler {
    @SubscribeEvent
    public static void onGuiOpen(ScreenEvent.Opening event) {
        if (!(event.getScreen() instanceof SetPasswordScreen) && !PasswordHolder.instance().initialized()) {
            Screen prev = event.getScreen();
            event.setNewScreen(new SetPasswordScreen(prev));
        }
    }
}