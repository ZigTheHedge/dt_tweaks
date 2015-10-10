package com.cwelth.dt_tweaks;

import com.cwelth.dt_tweaks.blocks.BlockDriedWheat;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;

/**
 * Created by zth on 05/10/15.
 */
public class Blocks {

    public static BlockDriedWheat DriedWheat;


    public static final void init()
    {
        GameRegistry.registerBlock(DriedWheat = new BlockDriedWheat("driedwheat", Material.cloth), "driedwheat");
    }
}
