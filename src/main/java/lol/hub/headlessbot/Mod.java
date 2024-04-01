package lol.hub.headlessbot;

import baritone.api.pathing.goals.GoalNear;
import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.Counter;
import lol.hub.headlessbot.behavior.BehaviorTree;
import lol.hub.headlessbot.behavior.nodes.composites.FallbackAllNode;
import lol.hub.headlessbot.behavior.nodes.composites.SequenceAllNode;
import lol.hub.headlessbot.behavior.nodes.composites.SequenceOneNode;
import lol.hub.headlessbot.behavior.nodes.decorators.MaybeRunNode;
import lol.hub.headlessbot.behavior.nodes.leafs.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerWarningScreen;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Mod implements ModInitializer, ClientModInitializer {

    // metrics examples
    static final Counter requests = Counter.build()
        .name("my_library_requests_total")
        .help("Total requests.")
        .labelNames("path")
        .register();
    public static long lastKeepAlive;
    public static long ticksOnline;
    private BehaviorTree behavior;
    private Chat chat;

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        behavior = defaultBehavior();
        chat = new Chat();

        try {
            var server = webServer();
            Metrics.init(server);
            // TODO: web ui
        } catch (IOException ex) {
            // when the mod did not load properly,
            // there is no need for graceful shutdown 🙈
            throw new IllegalStateException(ex.getMessage());
        }

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AccessibilityOnboardingScreen) {
                Log.info("Closing AccessibilityOnboardingScreen");
                screen.close();
            } else if (screen instanceof MultiplayerWarningScreen) {
                Log.info("Closing MultiplayerWarningScreen");
                screen.close();
            } else if (screen instanceof TitleScreen) {
                Log.info("Client is in TitleScreen");
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.world == null) return;
            ticksOnline++;
            behavior.tick();
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            var netHandler = client.getNetworkHandler();
            if (netHandler == null) return;

            var serverInfo = netHandler.getServerInfo();
            if (serverInfo == null) return;

            Log.info("client connected to %s (%s)",
                serverInfo.name,
                serverInfo.address);

            clientDefaultSettings();
            Baritone.defaultSettings();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            Log.warn("client disconnected");
            try {
                client.close();
            } catch (Exception ex) {
                Log.error(ex.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.error(ex.getMessage());
                }
                System.exit(0);
            }
        });
    }

    private BehaviorTree defaultBehavior() {
        return new BehaviorTree(

            // Execute children in order, all in one tick,
            // until a child returns RUNNING or FAILURE.
            new SequenceAllNode(

                // If the client is not ready yet, McCheckReadyNode
                // returns FAILURE and stop executing the sequence.
                new McCheckReadyNode(),

                // McNode is a utility to access the client context.
                // (ListPlayersNode extends McNode extends LeafNode)
                new ListPlayersNode(),

                // The MaybeRunNode will only tick the child node if
                // the provided function returns true. If the function
                // returns false, it will not tick the child node and
                // it will return SUCCESS.
                new MaybeRunNode(new RespawnNode(), () -> MC.player().isDead()),

                // While the player is dead, exit the sequence here.
                new MaybeRunNode(new FailNode(), () -> MC.player().isDead()),

                //  Execute children in order, all in one tick,
                //  until a child returns RUNNING or SUCCESS.
                new FallbackAllNode(
                    // Find and follow closest player.
                    new BaritoneFollowClosestPlayerNode(),
                    // When no player is nearby, run between waypoints.
                    new SequenceOneNode(
                        new BaritoneGoalNode(new GoalNear(
                            BlockPos.ofFloored(32, 64, 0), 8)),
                        new BaritoneGoalNode(new GoalNear(
                            BlockPos.ofFloored(-32, 64, 0), 8))
                    )
                )
            )
        );
    }

    private HttpServer webServer() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), -1);

        server.setExecutor(new ThreadPoolExecutor(
            1, 3, 5,
            TimeUnit.MINUTES,
            new SynchronousQueue<>()
        ));

        server.createContext("/", exchange -> {
            String response = "Hello, World!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stop(1)));

        return server;
    }

    private void clientDefaultSettings() {
        MC.client().options.getViewDistance().setValue(4);
        MC.client().options.getSimulationDistance().setValue(4);
    }

}
