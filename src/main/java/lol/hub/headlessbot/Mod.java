package lol.hub.headlessbot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;

public class Mod implements ModInitializer, ClientModInitializer {

    public static long lastKeepAlive;

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            // onTick()
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            System.err.println("client connected");
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            System.err.println("client disconnected");
        });
    }
}
