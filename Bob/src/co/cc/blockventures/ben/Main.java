package co.cc.blockventures.ben;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	 private long pollInterval = 150L;
	    private long lastTimestamp = 0L;
	    private long lastDifference = 0L;
    @Override
    public void onEnable(){
       
    }
 
    @Override
    public void onDisable() {
       System.out.println("Test");
    }
    private void printSpecs(CommandSender cs) {
        cs.sendMessage("§8==============================");
        cs.sendMessage("§bServer Specs");

        cs.sendMessage("§7Operating System§f: §a" + ManagementFactory.getOperatingSystemMXBean().getName() + " version " + ManagementFactory.getOperatingSystemMXBean().getVersion());
        cs.sendMessage("§7Architecture§f: §a" + ManagementFactory.getOperatingSystemMXBean().getArch());

        cs.sendMessage("§8==============================");
        cs.sendMessage("§bCPU Specs");
        double rawCPUUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
        cs.sendMessage("§7CPU Usage§f: " + renderBar(rawCPUUsage, (Runtime.getRuntime().availableProcessors())));
        cs.sendMessage("§7Available cores§f: §a" + Runtime.getRuntime().availableProcessors());

        cs.sendMessage("§8==============================");
        cs.sendMessage("§bMemory Specs");
        double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;
        double memMax = Runtime.getRuntime().maxMemory() / 1048576;

        cs.sendMessage("§7JVM Memory§f: " + renderBar(memUsed, memMax));
        cs.sendMessage("§7JVM Heap Memory (MB)§f: §a" + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1048576);
        cs.sendMessage("§7JVM Free Memory (MB)§f: §a" + Runtime.getRuntime().freeMemory() / 1048576);
        cs.sendMessage("§7JVM Maximum Memory (MB)§f: §a" + ((Runtime.getRuntime().maxMemory() == Long.MAX_VALUE) ? "No defined limit" : Runtime.getRuntime().maxMemory() / 1048576));
        cs.sendMessage("§7JVM Used Memory (MB)§f: §a" + Runtime.getRuntime().totalMemory() / 1048576);

        cs.sendMessage("§8==============================");
        cs.sendMessage("§bBukkit Specs");

        World[] loadedWorlds = new World[Bukkit.getWorlds().size()];
        loadedWorlds = Bukkit.getWorlds().toArray(loadedWorlds);

        cs.sendMessage("§7Loaded Worlds§f:");

        for (World world : loadedWorlds) {
            Chunk[] chunks = world.getLoadedChunks();

            Entity[] entities = new Entity[world.getEntities().size()];
            entities = world.getEntities().toArray(entities);

            cs.sendMessage("§a- " + world.getName() + " §7[§fChunks: §6" + chunks.length + "§f, Entities: §6" + entities.length + "§7]");
        }

        String ticks = "§7TPS§f: ";

        if (lastDifference == 0L) {
            cs.sendMessage(ticks + "§cNo TPS poll yet");
        } else {
            cs.sendMessage(ticks + renderTPSBar(calculateTPS(), 20));
        }
    }
    
    private String renderBar(double value, double max) {
        double usedLevel = 20 * (value / max);
        int usedRounded = (int) Math.round(usedLevel);
        String bar = "§8[";
        
        for (int i = 0; i < 20; i++) {
            if ((i + 1) <= usedRounded) {
                bar += "§b#";
            } else {
                bar += "§7_";
            }
        }
        
        double percent = (usedLevel / 20);
        NumberFormat format = NumberFormat.getPercentInstance();
        
        return (bar + "§8] (§f" + format.format(percent) + "§8)");
    }
    
    private String renderTPSBar(double value, double max) {
        double usedLevel = 20 * (value / max);
        int usedRounded = (int) Math.round(usedLevel);
        String bar = "§8[";
        
        for (int i = 0; i < 20; i++) {
            if ((i + 1) <= usedRounded) {
                bar += "§b#";
            } else {
                bar += "§7_";
            }
        }
        
        double percent = (usedLevel / 20);
        if (percent > 1) {
            percent = 1;
        }
        
        return (bar + "§8] (§f" + percent * 20 + " TPS§8)");
    }
    
    private double calculateTPS() {
        if (lastDifference == 0L)
            lastDifference = 1L;
        
        double tps = pollInterval / lastDifference;
        return tps;
    }
}