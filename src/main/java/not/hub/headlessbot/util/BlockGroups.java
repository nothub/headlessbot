package not.hub.headlessbot.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public enum BlockGroups {

    MINEABLE_HAND(
        Blocks.CRAFTING_TABLE,
        Blocks.DIRT,
        Blocks.GRASS,
        Blocks.HAY_BLOCK,
        Blocks.JUKEBOX,
        Blocks.LOG,
        Blocks.LOG2,
        Blocks.MYCELIUM,
        Blocks.NETHER_WART_BLOCK,
        Blocks.NOTEBLOCK,
        Blocks.PISTON,
        Blocks.PLANKS,
        Blocks.SLIME_BLOCK,
        Blocks.SOUL_SAND,
        Blocks.SPONGE,
        Blocks.STICKY_PISTON,
        Blocks.WOOL
    ),

    PATHING_BLOCKS(
        Blocks.CRAFTING_TABLE,
        Blocks.DIRT,
        Blocks.GRASS,
        Blocks.HAY_BLOCK,
        Blocks.JUKEBOX,
        Blocks.LOG,
        Blocks.LOG2,
        Blocks.MYCELIUM,
        Blocks.NETHER_WART_BLOCK,
        Blocks.NOTEBLOCK,
        Blocks.PISTON,
        Blocks.PLANKS,
        Blocks.SLIME_BLOCK,
        Blocks.SOUL_SAND,
        Blocks.SPONGE,
        Blocks.STICKY_PISTON,
        Blocks.WOOL,
        Blocks.STONE,
        Blocks.COBBLESTONE,
        Blocks.END_STONE,
        Blocks.GLOWSTONE,
        Blocks.GOLD_BLOCK,
        Blocks.IRON_BLOCK,
        Blocks.LAPIS_BLOCK,
        Blocks.DIAMOND_BLOCK,
        Blocks.EMERALD_BLOCK,
        Blocks.NETHERRACK,
        Blocks.OBSIDIAN,
        Blocks.SANDSTONE,
        Blocks.RED_SANDSTONE,
        Blocks.GLASS,
        Blocks.STAINED_GLASS
    ),

    INVENTORIES(
        Blocks.WHITE_SHULKER_BOX,
        Blocks.ORANGE_SHULKER_BOX,
        Blocks.MAGENTA_SHULKER_BOX,
        Blocks.LIGHT_BLUE_SHULKER_BOX,
        Blocks.YELLOW_SHULKER_BOX,
        Blocks.LIME_SHULKER_BOX,
        Blocks.PINK_SHULKER_BOX,
        Blocks.GRAY_SHULKER_BOX,
        Blocks.SILVER_SHULKER_BOX,
        Blocks.CYAN_SHULKER_BOX,
        Blocks.PURPLE_SHULKER_BOX,
        Blocks.BLUE_SHULKER_BOX,
        Blocks.BROWN_SHULKER_BOX,
        Blocks.GREEN_SHULKER_BOX,
        Blocks.RED_SHULKER_BOX,
        Blocks.BLACK_SHULKER_BOX
    ),

    ;

    public final Set<ResourceLocation> names;
    public final Set<Item> items;
    public final Set<Block> blocks;

    BlockGroups(Item... items) {
        this.names = Collections.unmodifiableSet(Arrays.stream(items).map(IForgeRegistryEntry.Impl::getRegistryName).collect(Collectors.toSet()));
        this.items = Collections.unmodifiableSet(names.stream().map(Item.REGISTRY::getObject).collect(Collectors.toSet()));
        this.blocks = Collections.unmodifiableSet(names.stream().map(Block.REGISTRY::getObject).collect(Collectors.toSet()));
    }

    BlockGroups(Block... blocks) {
        this.names = Collections.unmodifiableSet(Arrays.stream(blocks).map(IForgeRegistryEntry.Impl::getRegistryName).collect(Collectors.toSet()));
        this.items = Collections.unmodifiableSet(names.stream().map(Item.REGISTRY::getObject).collect(Collectors.toSet()));
        this.blocks = Collections.unmodifiableSet(names.stream().map(Block.REGISTRY::getObject).collect(Collectors.toSet()));
    }

}
