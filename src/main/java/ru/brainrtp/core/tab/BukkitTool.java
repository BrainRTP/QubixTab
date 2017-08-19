package ru.brainrtp.core.tab;

import org.bukkit.Bukkit;

public class BukkitTool {

    public static enum ClassType{
        NMS,
        CB;
    }

    public static String getNMSPackageName() {
        return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public static String getOBCPackageName(){
        return "org.bukkit.craftbukkit" + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public static Class getClass(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            System.out.print("Failed to find a valid class for: " + name);
            return null;
        }
    }

    public static Class getNMSClass(String name){
        return getClass(getNMSPackageName() + "." + name);
    }

    public static Class getCBClass(String name){
        return getClass(getOBCPackageName() + "." + name);
    }

}
