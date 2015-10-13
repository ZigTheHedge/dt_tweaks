package com.cwelth.dt_tweaks.eventHandliers;

import com.cwelth.dt_tweaks.modMain;
import com.cwelth.dt_tweaks.structures.ruinsParser;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zth on 07/10/15.
 */
public class GCWorldGenExtender {

    private ruinsParser rGen = new ruinsParser("wheatfarm.tml");

    @SubscribeEvent
    public void onGCWgen(GCCoreEventPopulate.Post event)
    {
        if(event.worldObj.provider.dimensionId == modMain.instance.moonDimID)
            generateSurface(event.worldObj, event.rand, event.chunkX, event.chunkZ);
    }

    public void generateSurface(World worldObj, Random rand, int chunkX, int chunkZ)
    {
        if(!modMain.instance.isWGEnabled)return;
        int X = chunkX + rand.nextInt(16);
        int Z = chunkZ + rand.nextInt(16);
        int Y = 127;


        while(worldObj.isAirBlock(X, Y, Z) && Y > 2)
            Y--;
        if(rand.nextInt(modMain.instance.genChance) == 0) {
            ArrayList<String> structures = new ArrayList<String>();

            File folder = new File(modMain.instance.tplPath);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()){
                    structures.add(listOfFiles[i].getName());
                    //FMLLog.info("Added: %s", listOfFiles[i].getName());
                }
            }

            int index = worldObj.rand.nextInt(structures.size());
            rGen.setResourceFile(structures.get(index));
            rGen.generate(worldObj, rand, X, Y, Z);
        }
    }

}
