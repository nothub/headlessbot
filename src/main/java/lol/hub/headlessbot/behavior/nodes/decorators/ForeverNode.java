package lol.hub.headlessbot.behavior.nodes.decorators;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.RUNNING;

/**
 * WHILE TRUE
 */
public final class ForeverNode extends DecoratorNode {
    public ForeverNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        child().tick();
        return RUNNING;
    }
}
