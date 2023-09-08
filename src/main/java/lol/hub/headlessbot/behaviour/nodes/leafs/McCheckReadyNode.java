package lol.hub.headlessbot.behaviour.nodes.leafs;

import baritone.api.pathing.goals.Goal;

import static lol.hub.headlessbot.behaviour.State.FAILURE;
import static lol.hub.headlessbot.behaviour.State.SUCCESS;

public final class McCheckReadyNode extends McNode {
    public McCheckReadyNode() {
        super(mc -> {
            if (mc.player == null) return FAILURE;
            if (mc.world == null) return FAILURE;
            return SUCCESS;
        });
    }
}
