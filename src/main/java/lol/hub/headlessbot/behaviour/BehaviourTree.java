package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.Node;

public class BehaviourTree {
    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */
    // TODO: tick based executor

    private final Node root;

    public BehaviourTree(Node node) {
        this.root = node;
    }

    public State run() {
        return this.root.run();
    }

    public Node root() {
        return root;
    }
}
