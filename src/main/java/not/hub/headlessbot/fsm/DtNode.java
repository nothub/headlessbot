package not.hub.headlessbot.fsm;

public abstract class DtNode {

    public final String name;

    public DtNode(String name) {
        this.name = name;
    }

    public abstract Boolean isLeaf();

}
