package lol.hub.headlessbot.behavior.nodes.composites;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import java.util.Objects;

import static lol.hub.headlessbot.behavior.State.*;

/**
 * AND
 * Execute children in order, one per tick,
 * until a child returns FAILURE.
 * Returns RUNNING until the end is reached,
 * then returns SUCCESS.
 */
public final class SequenceOneNode extends CompositeNode {
    private int index = 0;

    public SequenceOneNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        if (index >= children().size()) {
            index = 0;
            return SUCCESS;
        }
        if (children().get(index++).tick() == FAILURE) {
            index = 0;
            return FAILURE;
        }
        return RUNNING;
    }
}
