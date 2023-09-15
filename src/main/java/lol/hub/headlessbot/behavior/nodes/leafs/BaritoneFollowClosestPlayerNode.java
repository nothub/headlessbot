package lol.hub.headlessbot.behavior.nodes.leafs;

import lol.hub.headlessbot.Baritone;
import lol.hub.headlessbot.Log;
import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.race_conditions.Cooldowns;

import java.util.Comparator;

import static lol.hub.headlessbot.behavior.State.FAILURE;
import static lol.hub.headlessbot.behavior.State.RUNNING;

/**
 * Will try to follow the closest player.
 * If arrived at the player, will return SUCCESS.
 * If no player can be found, will return FAILURE.
 * Will return RUNNING while busy.
 */
public class BaritoneFollowClosestPlayerNode extends McNode {
    public BaritoneFollowClosestPlayerNode() {
        super(mc -> {
            var players =
                mc.world.getPlayers().stream()
                    .filter(p -> !p.getUuid().equals(MC.player().getUuid()))
                    .sorted(Comparator.comparingInt(p -> mc.player
                        .getBlockPos()
                        .getManhattanDistance(p.getBlockPos())))
                    .toList();

            if (players.isEmpty()) return FAILURE;
            var target = players.get(0);

            if (Cooldowns.baritone.isActive()) return RUNNING;
            if (Baritone.isBusy()) return RUNNING;

            Log.info("following %s %s", target.getName().getString());
            Baritone.get().getFollowProcess()
                .follow(entity -> entity.getUuid().equals(target.getUuid()));
            Cooldowns.baritone.reset();
            return RUNNING;
        });
    }
}
