package lol.hub.headlessbot.behavior.nodes.composites;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import java.util.Objects;

import static lol.hub.headlessbot.behavior.State.*;

/**
 * OR
 * Execute children in order, one per tick,
 * until a child returns SUCCESS.
 * Returns RUNNING until the end is reached,
 * then returns FAILURE.
 */
public final class FallbackOneNode extends CompositeNode {
    private int index = 0;

    public FallbackOneNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        if (index >= children().size()) {
            index = 0;
            return FAILURE;
        }
        if (children().get(index++).tick() == SUCCESS) {
            index = 0;
            return SUCCESS;
        }
        return RUNNING;
    }
}
