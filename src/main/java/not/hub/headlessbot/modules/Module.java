package not.hub.headlessbot.modules;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.common.MinecraftForge;
import not.hub.headlessbot.util.MC;

public abstract class Module implements MC {

    public final String name;

    private boolean active = false;

    public Module() {
        this.name = getClass().getSimpleName().toLowerCase().replaceAll("module", "");
    }

    public Module(String name) {
        this.name = name;
    }

    static void sendChat(String message) {
        // TODO: sanitize to not get kicked: unicode, max length
        mc.addScheduledTask(() -> mc.player.connection.sendPacket(new CPacketChatMessage(message)));
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

    public boolean hasBlocks() {
        return mc
            .player
            .inventory
            .mainInventory
            .stream()
            .filter(itemStack -> !itemStack.isEmpty())
            .map(ItemStack::getItem)
            .anyMatch(item -> item instanceof ItemBlock);
    }

    public boolean hasPickaxe() {
        return mc
            .player
            .inventory
            .mainInventory
            .stream()
            .filter(itemStack -> !itemStack.isEmpty())
            .map(ItemStack::getItem)
            .anyMatch(item -> item instanceof ItemPickaxe);
    }

}
