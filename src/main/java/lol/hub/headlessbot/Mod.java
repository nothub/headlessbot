package lol.hub.headlessbot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class Mod implements ModInitializer, ClientModInitializer {

    public static long lastKeepAlive;

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // respawn
            if (client.player.isDead()) client.player.requestRespawn();
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            var netHandler = client.getNetworkHandler();
            if (netHandler == null) return;

            var serverInfo = netHandler.getServerInfo();
            if (serverInfo == null) return;

            System.err.printf("client connected to %s (%s)\n", serverInfo.name, serverInfo.address);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            System.err.println("client disconnected");
            client.close();
            //System.exit(0);
        });
    }
}
