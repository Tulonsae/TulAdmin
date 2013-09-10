package org.tulonsae.mc.plugins;

import org.bukkit.command.CommandSender;
import org.bukkit.World;

/**
 * Generates a set of chunks within a Bukkit Scheduler repeating task.
 *
 * @author Tulonsae
 */
public class GenChunksTask implements Runnable {

    private CommandSender sender = null;
    private World world = null;

    private GenMap genMap;
    private int xStart;
    private int zStart;
    private int xEnd;
    private int zEnd;
    private int numChunks;

    private int xNext;
    private int zNext;

    /**
     * Creates this object.
     */
    public GenChunksTask(GenMap genMap, CommandSender sender, World world, int xStart, int zStart, int xEnd, int zEnd, int numChunks) {
        this.genMap = genMap;
        this.sender = sender;
        this.world = world;
        this.xStart = xStart;
        this.zStart = zStart;
        this.xEnd = xEnd;
        this.zEnd = zEnd;
        this.numChunks = numChunks;

        xNext = xStart;
        zNext = zStart;
    }

    /**
     * Runs the task of generating chunks.
     */
    public void run() {
        int cnt = 0;

        // step through each x value while cycling through z values
        while (xNext <= xEnd) {
            while (zNext <= zEnd) {
                sender.sendMessage("Chunk " + xNext + "," + zNext);
                world.loadChunk(xNext, zNext);
                world.unloadChunk(xNext, zNext);
                cnt++;
                if (cnt >= numChunks) {
                    break;
                }
                zNext++;
            }
            xNext++;
            if ((cnt >= numChunks) || (xNext > xEnd)) {
                break;
            }
            if (zNext > zEnd) {
                zNext = zStart;
            }
        }

        Util.sendMessage(sender, "Saving world...");
        world.save();
        Util.sendMessage(sender, "      ...done.");

        // check if we're completely done
        if ((xNext >= xEnd) && (zNext >= zEnd)) {
            genMap.cancelTask();
        }
    }

}
