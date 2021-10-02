package not.hub.headlessbot.fsm;

public class DtLeaf extends DtNode {

    public final Runnable runnable;

    public DtLeaf(String name, Runnable runnable) {
        super(name);
        this.runnable = runnable;
    }

    @Override
    public Boolean isLeaf() {
        return true;
    }

}
