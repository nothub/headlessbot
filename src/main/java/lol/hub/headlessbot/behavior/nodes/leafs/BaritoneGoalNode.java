package lol.hub.headlessbot.behavior.nodes.leafs;

import baritone.api.pathing.goals.Goal;
import lol.hub.headlessbot.Baritone;
import lol.hub.headlessbot.Log;
import lol.hub.headlessbot.race_conditions.Cooldowns;

import static lol.hub.headlessbot.behavior.State.RUNNING;
import static lol.hub.headlessbot.behavior.State.SUCCESS;

/**
 * Will try to pathfind close to the goal.
 * If arrived at the goal, will return SUCCESS.
 * Will return RUNNING while busy.
 */
public class BaritoneGoalNode extends McNode {

    public BaritoneGoalNode(Goal goal) {
        super(mc -> {
            if (goal.isInGoal(mc.player.getBlockPos())) {
                Cooldowns.baritone.expire();
                return SUCCESS;
            }

            if (Cooldowns.baritone.isActive()) return RUNNING;
            if (Baritone.isBusy()) return RUNNING;

            Log.info("pathfinding to: %s", goal.toString());
            Baritone.get().getCustomGoalProcess().setGoalAndPath(goal);
            Cooldowns.baritone.reset();
            return RUNNING;
        });
    }
}
