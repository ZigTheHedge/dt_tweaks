package com.cwelth.dt_tweaks.eventHandliers;

import com.cwelth.dt_tweaks.modMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * Created by zth on 06/10/15.
 */
public class spawnTeleporter extends Teleporter {
    private final WorldServer worldServerInstance;

    public spawnTeleporter(WorldServer par1WorldServer)
    {
        super(par1WorldServer);
        worldServerInstance = par1WorldServer;
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        // Do almost nothing - we don't actually want to create a portal!
        if (par1Entity.dimension == modMain.instance.dimID) {
            par1Entity.setLocationAndAngles(modMain.instance.X, modMain.instance.Y, modMain.instance.Z,
                    par1Entity.rotationYaw, par1Entity.rotationPitch);
        } else {
            EntityPlayerMP player = (EntityPlayerMP)par1Entity;
            WorldServer worldServer = player.mcServer.worldServerForDimension(par1Entity.dimension);
            ChunkCoordinates spawnpoint = worldServer.getSpawnPoint();
            spawnpoint.posY = worldServer.getTopSolidOrLiquidBlock(spawnpoint.posX, spawnpoint.posZ);
            par1Entity.setLocationAndAngles(spawnpoint.posX, spawnpoint.posY, spawnpoint.posZ, par1Entity.rotationYaw, par1Entity.rotationPitch);

        }
    }
}
