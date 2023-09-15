package lol.hub.headlessbot;

import baritone.api.pathing.goals.GoalNear;
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
import net.minecraft.util.math.BlockPos;

public class Mod implements ModInitializer, ClientModInitializer {

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
                    // Find a player nearby and path to the player.
                    new BaritoneGotoClosestPlayerNode(),
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

}
