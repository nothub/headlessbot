package lol.hub.headlessbot.behaviour.nodes.decorators;

import lol.hub.headlessbot.behaviour.State;
import lol.hub.headlessbot.behaviour.nodes.Node;

public class InverterNode extends DecoratorNode {
    public InverterNode(Node parent, Node child) {
        super(parent, child);
    }

    @Override
    public State run() {
        var state = child.run();
        switch (state) {
            case SUCCESS -> {
                return State.FAILURE;
            }
            case FAILURE -> {
                return State.SUCCESS;
            }
        }
        throw new IllegalStateException(String.format("child node state must be SUCCESS or FAILURE but was %s", state.name()));
    }
}
