package lol.hub.headlessbot.behavior.nodes.composites;

import lol.hub.headlessbot.behavior.State;
import lol.hub.headlessbot.behavior.nodes.Node;

import static lol.hub.headlessbot.behavior.State.*;

/**
 * AND
 * Execute children in order, all in one tick,
 * until a child returns RUNNING or FAILURE.
 */
public class SequenceAllNode extends CompositeNode {
    public SequenceAllNode(Node... children) {
        super(children);
    }

    @Override
    public synchronized State tick() {
        for (Node child : children()) {
            switch (child.tick()) {
                case RUNNING -> { return RUNNING; }
                case FAILURE -> { return FAILURE; }
            }
        }
        return SUCCESS;
    }
}
