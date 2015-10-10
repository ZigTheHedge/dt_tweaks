package com.cwelth.dt_tweaks.blocks;

import com.cwelth.dt_tweaks.modMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;

/**
 * Created by zth on 05/10/15.
 */
public class BlockDriedWheat extends Block {

    public BlockDriedWheat(String unlocalizedName, Material material) {
        super(material);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(modMain.MODID + ":" + unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);

        this.setHardness(0.2F);
        this.setResistance(2.0F);

        this.setStepSound(soundTypeGrass);
    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if (world.rand.nextInt(5) != 0) {
            return ret;
        }
        ItemStack item=ForgeHooks.getGrassSeed(world);
        if (item != null) {
            ret.add(item);
        }
        return ret;
    }


}
