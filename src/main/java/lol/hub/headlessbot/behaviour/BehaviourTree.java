package lol.hub.headlessbot.behaviour;

import lol.hub.headlessbot.behaviour.nodes.Node;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BehaviourTree {
    /* https://www.gamedeveloper.com/programming/behavior-trees-for-ai-how-they-work */
    // TODO: tick based executor

    private final Node root;
    private Node current;

    public BehaviourTree(Node node) {
        this.root = node;
        this.current = node;
    }

    public State start() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
        return this.root.tick();
    }

    public Node root() {
        return root;
    }

    private void tick() {

    }
}
