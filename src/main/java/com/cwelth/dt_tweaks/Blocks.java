package com.cwelth.dt_tweaks;

import com.cwelth.dt_tweaks.blocks.BlockDriedWheat;
import com.cwelth.dt_tweaks.blocks.BlockPetrifiedLava;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;

/**
 * Created by zth on 05/10/15.
 */
public class Blocks {

    public static BlockDriedWheat DriedWheat;
    public static BlockPetrifiedLava PetrifieddLava;
    public static BlockPetrifiedLava PetrifiedLava;


    public static final void init()
    {
        GameRegistry.registerBlock(DriedWheat = new BlockDriedWheat("driedwheat", Material.cloth), "driedwheat");
        GameRegistry.registerBlock(PetrifieddLava = new BlockPetrifiedLava("petrifiedd_lava", Material.cloth), "petrifiedd_lava");
        GameRegistry.registerBlock(PetrifiedLava = new BlockPetrifiedLava("petrified_lava", Material.cloth), "petrified_lava");
    }
}
