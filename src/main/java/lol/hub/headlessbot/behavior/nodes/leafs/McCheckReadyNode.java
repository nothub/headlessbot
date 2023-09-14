package lol.hub.headlessbot.behavior.nodes.leafs;

import static lol.hub.headlessbot.behavior.State.FAILURE;
import static lol.hub.headlessbot.behavior.State.SUCCESS;

public final class McCheckReadyNode extends McNode {
    public McCheckReadyNode() {
        super(mc -> {
            if (mc.player == null) return FAILURE;
            if (mc.world == null) return FAILURE;
            return SUCCESS;
        });
    }
}
