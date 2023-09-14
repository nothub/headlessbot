package lol.hub.headlessbot.behaviour.nodes.leafs;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.Goal;

import static lol.hub.headlessbot.behaviour.State.RUNNING;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;

public class BaritoneGoalNode extends McNode {
    public BaritoneGoalNode(Goal goal) {
        super(mc -> {
            if (goal.isInGoal(
                mc.player.getBlockX(),
                mc.player.getBlockY(),
                mc.player.getBlockZ()
            )) return SUCCESS;

            var baritone = BaritoneAPI.getProvider().getPrimaryBaritone();
            if (baritone.getPathingBehavior().isPathing()) return RUNNING;

            baritone.getCustomGoalProcess().setGoalAndPath(goal);
            return RUNNING;
        });
    }
}