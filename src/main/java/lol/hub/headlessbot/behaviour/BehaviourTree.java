package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.Node;

public class BehaviourTree implements Tickable {
    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */

    private final Node root;

    public BehaviourTree(Node node) {
        this.root = node;
    }

    public Node root() {
        return root;
    }

    public State tick() {
        return root.tick();
    }
}
