package com.cwelth.dt_tweaks.event_handlers;

import com.cwelth.dt_tweaks.modMain;
import com.cwelth.dt_tweaks.structures.ruinsParser;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by zth on 06/10/15.
 */
public class commandHandler implements ICommand {
    @Override
    public String getCommandName() {
        return "sset";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "sset <set|get|reload|tp>";
    }

    @Override
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length != 1)
            sender.addChatMessage(new ChatComponentText("Usage: sset <set|get|reload|tp>"));
        else
        {
            if(args[0].equals("set"))
            {
                modMain m = modMain.instance;
                m.X = sender.getPlayerCoordinates().posX;
                m.Y = sender.getPlayerCoordinates().posY;
                m.Z = sender.getPlayerCoordinates().posZ;
                m.dimID = ((EntityPlayer)sender).dimension;
                m.saveConfig();
                if(!sender.getEntityWorld().isRemote)
                    MinecraftServer.getServer().worldServerForDimension(m.dimID).setSpawnLocation(m.X, m.Y, m.Z);
                sender.addChatMessage(new ChatComponentText("Spawn location saved!"));
            }
            if(args[0].equals("get"))
            {
                modMain m = modMain.instance;
                String msg = "Spawn location: "+m.X+", "+m.Y+", "+m.Z+" @"+m.dimID;
                sender.addChatMessage(new ChatComponentText(msg));
            }
            if(args[0].equals("reload"))
            {
                modMain m = modMain.instance;
                m.loadConfig();
                sender.addChatMessage(new ChatComponentText("Config reloaded."));
            }
            if(args[0].equals("tp"))
            {
                modMain m = modMain.instance;
                sender.addChatMessage(new ChatComponentText("Teleporting to the spawn point!"));
                MinecraftServer mc = MinecraftServer.getServer();
                mc.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) sender, m.dimID, new spawnTeleporter(mc.worldServerForDimension(m.dimID)));
            }
            if(args[0].equals("gen"))
            {
                modMain m = modMain.instance;
                ruinsParser prs = new ruinsParser("test.tml");
                prs.generate(sender.getEntityWorld(), sender.getEntityWorld().rand, sender.getPlayerCoordinates().posX, sender.getPlayerCoordinates().posY, sender.getPlayerCoordinates().posZ);
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
