package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

import static lol.hub.headlessbot.behaviour.State.RUNNING;

// WHILE TRUE
public class ForeverNode extends DecoratorNode {
    public ForeverNode(Node child) {
        super(child);
    }

    @Override
    public State tick() {
        child().tick();
        return RUNNING;
    }
}
