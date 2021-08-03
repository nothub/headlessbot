package not.hub.headlessbot.modules;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public abstract class Module {

    static final Minecraft mc = Minecraft.getMinecraft();

    public final String name;
    public final Type type;

    private boolean active = false;

    public Module(Type type) {
        this.name = getClass().getSimpleName().toLowerCase().replaceAll("module", "");
        this.type = type;
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

    public enum Type {
        ALWAYS_ACTIVE,
        SITUATIONAL
    }

}
