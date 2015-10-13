package com.cwelth.dt_tweaks.blocks;

import com.cwelth.dt_tweaks.modMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by ZtH on 11.10.2015.
 */
public class BlockPetrifiedLava extends Block {
    public BlockPetrifiedLava(String unlocalizedName, Material material) {
        super(material);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(modMain.MODID + ":" + unlocalizedName);
        this.setCreativeTab(CreativeTabs.tabBlock);

        this.setHardness(0.2F);
        this.setLightLevel(2);
        this.setResistance(2.0F);

        this.setStepSound(soundTypeStone);
    }


    @Override
    public void onBlockDestroyedByPlayer(World worldObj, int x, int y, int z, int metadata) {
        worldObj.setBlock(x, y, z, Blocks.lava);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        return ret;
    }
}
