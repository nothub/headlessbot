package lol.hub.headlessbot.behavior.nodes.leafs;

import baritone.api.pathing.goals.GoalNear;
import lol.hub.headlessbot.Baritone;
import lol.hub.headlessbot.Log;
import lol.hub.headlessbot.MC;
import lol.hub.headlessbot.race_conditions.Cooldowns;

import java.util.Comparator;

import static lol.hub.headlessbot.behavior.State.*;

/**
 * Will try to pathfind to the closest player.
 * If arrived at the player, will return SUCCESS.
 * If no player can be found, will return FAILURE.
 * Will return RUNNING while busy.
 */
public class BaritoneGotoClosestPlayerNode extends McNode {
    public BaritoneGotoClosestPlayerNode() {
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

            var goal = new GoalNear(target.getBlockPos(), 6);
            if (goal.isInGoal(mc.player.getBlockPos())) {
                Cooldowns.baritone.expire();
                return SUCCESS;
            }

            if (Cooldowns.baritone.isActive()) return RUNNING;
            if (Baritone.isBusy()) return RUNNING;

            Log.info("pathfinding to %s %s",
                target.getName().getString(),
                goal.toString());
            Baritone.get().getCustomGoalProcess().setGoalAndPath(goal);
            Cooldowns.baritone.reset();
            return RUNNING;
        });
    }
}
