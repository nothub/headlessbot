package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.FAILURE;
import static lol.hub.headlessbot.behaviour.State.RUNNING;

// WHILE TRUE
public class ForeverNode extends DecoratorNode {
    private final boolean exitOnFail;

    public ForeverNode(Node child, boolean exitOnFail) {
        super(child);
        this.exitOnFail = exitOnFail;
    }

    @Override
    public State tick() {
        if (child().tick() == FAILURE && exitOnFail) {
            return FAILURE;
        }
        return RUNNING;
    }
}
