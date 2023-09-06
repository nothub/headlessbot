package lol.hub.headlessbot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;

public class Mod implements ModInitializer, ClientModInitializer {

    public static long lastKeepAlive;
    public static long ticksOnline;

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            ticksOnline++;

            if (client.player.isDead()) {
                if (ticksOnline % 20 == 0) {
                    Log.info("requesting respawn");
                    client.player.requestRespawn();
                }
                return;
            }

            for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
                if (player.getUuid().equals(client.player.getUuid())) continue;
                Log.info("seeing player: %s (%s)", player.getGameProfile().getName(), player.getUuid().toString());
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            var netHandler = client.getNetworkHandler();
            if (netHandler == null) return;

            var serverInfo = netHandler.getServerInfo();
            if (serverInfo == null) return;

            Log.info("client connected to %s (%s)", serverInfo.name, serverInfo.address);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            Log.warn("client disconnected");
            client.close();
            //System.exit(0);
        });
    }
}
