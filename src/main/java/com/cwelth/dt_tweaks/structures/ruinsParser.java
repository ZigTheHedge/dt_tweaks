package com.cwelth.dt_tweaks.structures;

import com.cwelth.dt_tweaks.modMain;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by ZtH on 07.10.2015.
 */
public class ruinsParser extends WorldGenerator {

    private String resourceFile;

    public ruinsParser(String resourceFile)
    {
        setResourceFile(resourceFile);
    }

    public void setResourceFile(String resFile)
    {
        this.resourceFile = resFile;
    }

    @Override
    public boolean generate(World worldObj, Random rand, int X, int Y, int Z) {
        FMLLog.info("DT_TWEAKS: Generating %s structure at %d, %d, %d", this.resourceFile, X, Y, Z);
        File f = new File(modMain.instance.tplPath, this.resourceFile);
        //FMLLog.info("DT_TWEAKS: looking for %s...", f.getAbsolutePath());

        HashMap<Integer, HashMap<String,String>> rules = new HashMap<Integer, HashMap<String, String>>();
        ArrayList<ArrayList<String>> layers = new ArrayList<ArrayList<String>>();
        ArrayList<String> singleLayer = null;
        int x = X;
        int y = Y + 1;
        int z = Z;
        try {
            FileInputStream fstream = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String line = null;
            String[] dimensions = new String[]{};
            boolean areInLayer = false;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("rule")) {
                    String[] cmp = line.substring(line.indexOf('=') + 1).split(",");
                    //System.out.println(line.substring(4,line.indexOf('=')));
                    HashMap<String, String> obj = new HashMap<String, String>();
                    obj.put("priority", cmp[0]);
                    obj.put("chance", cmp[1]);
                    obj.put("blocks", line.substring(line.indexOf('=') + 1).substring(cmp[0].length() + cmp[1].length() + 2));
                    rules.put(Integer.valueOf(line.substring(4, line.indexOf('='))), obj);
                }
                if (line.startsWith("layer")) {
                    areInLayer = true;
                    singleLayer = new ArrayList<String>();
                    continue;
                }
                if (line.startsWith("endlayer")) {
                    areInLayer = false;
                    layers.add(singleLayer);
                    singleLayer = null;
                    continue;
                }
                if (areInLayer) {
                    singleLayer.add(line);
                }
                if (line.startsWith("dimensions"))
                {
                    dimensions = line.split("=")[1].split(",");
                }
            }
            br.close();

            for(Integer condition : new Integer[]{0, 1, -1, 2, -2, 3, -3, 7}) {
                y = Y;
                x = X;
                z = Z;
                for (ArrayList<String> sl : layers) {
                    if(worldObj.checkChunksExist(x, y, z, x+Integer.valueOf(dimensions[1]), y+Integer.valueOf(dimensions[0]), z-Integer.valueOf(dimensions[2]))) {
                        spawnLayerWithCondition(worldObj, x, y, z, condition, sl, rules);
                        y++;
                    }
                }
            }


        } catch (FileNotFoundException e) {
            FMLLog.warning("DT_TWEAKS: %s hasn't been found!", f.getAbsolutePath());
            return false;
        } catch (IOException e) {
            FMLLog.warning("DT_TWEAKS: Cannot read %s!", f.getAbsolutePath());
            return false;
        }


        return true;
    }

    public void spawnLayerWithCondition(World worldObj, int X, int Y, int Z, int Condition, ArrayList<String> layer, HashMap<Integer, HashMap<String,String>> rules) {
        //FMLLog.info("Building a layer with %d condition...", Condition);
        int x = X;
        int y = Y;
        int z = Z;
        try {
            for (String line : layer) {
                String[] blocks = line.split(",");
                for (String block : blocks) {
                    if (Integer.valueOf(block) != 0) {

                        //Check for condition
                        Integer reqCondition = Integer.valueOf(rules.get(Integer.valueOf(block)).get("priority"));
                        if (Condition != reqCondition) {
                            z--;
                            continue;
                        }

                        boolean blResult = false;
                        switch (reqCondition) {
                            case 0:
                            case 7:
                                blResult = true;
                                break;
                            case 1:
                                if (!worldObj.isAirBlock(x, y - 1, z)) blResult = true;
                                break;
                            case -1:
                                if (worldObj.isAirBlock(x, y - 1, z)) blResult = true;
                                break;
                            case 2: {
                                boolean ajBlFound = false;
                                if (!worldObj.isAirBlock(x + 1, y, z)) ajBlFound = true;
                                if (!worldObj.isAirBlock(x - 1, y, z)) ajBlFound = true;
                                if (!worldObj.isAirBlock(x, y, z + 1)) ajBlFound = true;
                                if (!worldObj.isAirBlock(x, y, z - 1)) ajBlFound = true;
                                blResult = ajBlFound;
                                break;
                            }
                            case -2: {
                                boolean ajBlNotFound = true;
                                if (!worldObj.isAirBlock(x + 1, y, z)) ajBlNotFound = false;
                                if (!worldObj.isAirBlock(x - 1, y, z)) ajBlNotFound = false;
                                if (!worldObj.isAirBlock(x, y, z + 1)) ajBlNotFound = false;
                                if (!worldObj.isAirBlock(x, y, z - 1)) ajBlNotFound = false;
                                blResult = ajBlNotFound;
                                break;
                            }
                            case 3:
                                if (!worldObj.isAirBlock(x, y + 1, z)) blResult = true;
                                break;
                            case -3:
                                if (worldObj.isAirBlock(x, y + 1, z)) blResult = true;
                                break;

                        }
                        if (!blResult) {
                            z--;
                            continue;
                        }

                        //Check chance
                        int chanceCheck = worldObj.rand.nextInt(100 - Integer.valueOf(rules.get(Integer.valueOf(block)).get("chance")) + 1);
                        if (chanceCheck != 0) {
                            z--;
                            continue;
                        }

                        //All passed. Choosing block from the list.
                        String blPossibilities[] = rules.get(Integer.valueOf(block)).get("blocks").split(",");
                        //FMLLog.info("Possibilities: %s", rules.get(Integer.valueOf(block)).get("blocks"));
                        int blChosenOne = worldObj.rand.nextInt(blPossibilities.length);
                        String[] finalBlock = blPossibilities[blChosenOne].split("-");
                        String finalMeta = (finalBlock.length > 1) ? finalBlock[1] : "0";
                        if (finalBlock[0].equalsIgnoreCase("preserveBlock")) {
                            z--;
                            continue;
                        }
                        if (finalBlock[0].equalsIgnoreCase("air")) {
                            //FMLLog.info("setting block to Air at %d, %d, %d", x, y, z);
                            worldObj.setBlockToAir(x, y, z);
                            z--;
                            continue;
                        }

                        //FMLLog.info("setting block %s:%s at %d, %d, %d", finalBlock[0], finalMeta, x, y, z);
                        worldObj.setBlock(x, y, z, Block.getBlockFromName(finalBlock[0]), Integer.valueOf(finalMeta), 2);
                    } else {
                        if (Condition == 0) {
                            //FMLLog.info("setting block to Air at %d, %d, %d", x, y, z);
                            worldObj.setBlockToAir(x, y, z);
                        }
                    }
                    z--;
                }
                x++;
                z = Z;
            }
        } catch (RuntimeException e) {
            FMLLog.warning("Already decorating! caught... And ignored.");
        }
        //FMLLog.info("Done");
    }
}
