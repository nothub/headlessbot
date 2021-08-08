package not.hub.headlessbot.modules;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import not.hub.headlessbot.Cooldowns;
import not.hub.headlessbot.Log;
import not.hub.headlessbot.util.StackSlot;

import static not.hub.headlessbot.util.PlayerStuff.quickMoveSlot;

public class AutoArmorModule extends Module {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (mc.world == null) return;
        if (mc.player == null) return;
        if (Cooldowns.INVENTORY.isValid()) return;
        if (mc.player.inventory.armorItemInSlot(1).getItem() == Items.AIR && replaceArmor(EntityEquipmentSlot.LEGS))
            return;
        if (mc.player.inventory.armorItemInSlot(2).getItem() == Items.AIR && replaceArmor(EntityEquipmentSlot.CHEST))
            return;
        if (mc.player.inventory.armorItemInSlot(0).getItem() == Items.AIR && replaceArmor(EntityEquipmentSlot.FEET))
            return;
        if (mc.player.inventory.armorItemInSlot(3).getItem() == Items.AIR) replaceArmor(EntityEquipmentSlot.HEAD);
    }

    private boolean replaceArmor(EntityEquipmentSlot type) {
        StackSlot replacement = findArmor(type);
        if (replacement == null) return false;
        Log.info(getClass(), "Equipping " + replacement.stack.getItem().getRegistryName() + " as " + type.getName());
        quickMoveSlot(replacement.slot);
        return true;
    }

    public StackSlot findArmor(EntityEquipmentSlot type) {
        for (int i = 0; i < 36; i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (!(stack.getItem() instanceof ItemArmor)) continue;
            if (((ItemArmor) stack.getItem()).armorType == type) return StackSlot.of(i, stack);
        }
        return null;
    }

}
