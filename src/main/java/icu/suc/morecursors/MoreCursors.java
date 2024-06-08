package icu.suc.morecursors;

import icu.suc.morecursors.management.CursorManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;

public class MoreCursors implements ClientModInitializer {

    private CursorManager cursorManager;

    private static MoreCursors instance;

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted);
    }

    private void onClientStarted(MinecraftClient client) {
        instance = this;
        cursorManager = new CursorManager(client);
    }

    public CursorManager getCursorManager() {
        return cursorManager;
    }

    public static MoreCursors getInstance() {
        return instance;
    }
}
