package not.hub.headlessbot.modules;

import net.minecraftforge.common.MinecraftForge;
import not.hub.headlessbot.MC;

public abstract class Module implements MC {

    public final String name;

    private boolean active = false;

    public Module() {
        this.name = getClass().getSimpleName().toLowerCase().replaceAll("module", "");
    }

    public Module(String name) {
        this.name = name;
    }

    public void activate() {
        if (active) return;
        active = true;
        MinecraftForge.EVENT_BUS.register(this);
        onActivate();
    }

    void onActivate() {
    }

    public void deactivate() {
        if (!active) return;
        active = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        onDeactivate();
    }

    void onDeactivate() {
    }

    public boolean isActive() {
        return active;
    }

}
