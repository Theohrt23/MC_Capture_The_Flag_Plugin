package entity;

import org.bukkit.World;

public class Spawn {

    private int XBlueSpawn;

    private int YBlueSpawn;

    private int ZBlueSpawn;

    private int XRedSpawn;

    private int YRedSpawn;

    private int ZRedSpawn;

    private World world;

    public Spawn() {}

    public Spawn(int XBlueSpawn, int YBlueSpawn, int ZBlueSpawn, int XRedSpawn, int YRedSpawn, int ZRedSpawn, World world) {
        this.XBlueSpawn = XBlueSpawn;
        this.YBlueSpawn = YBlueSpawn;
        this.ZBlueSpawn = ZBlueSpawn;
        this.XRedSpawn = XRedSpawn;
        this.YRedSpawn = YRedSpawn;
        this.ZRedSpawn = ZRedSpawn;
        this.world = world;
    }

    public int getXBlueSpawn() {
        return XBlueSpawn;
    }

    public void setXBlueSpawn(int XBlueSpawn) {
        this.XBlueSpawn = XBlueSpawn;
    }

    public int getYBlueSpawn() {
        return YBlueSpawn;
    }

    public void setYBlueSpawn(int YBlueSpawn) {
        this.YBlueSpawn = YBlueSpawn;
    }

    public int getZBlueSpawn() {
        return ZBlueSpawn;
    }

    public void setZBlueSpawn(int ZBlueSpawn) {
        this.ZBlueSpawn = ZBlueSpawn;
    }

    public int getXRedSpawn() {
        return XRedSpawn;
    }

    public void setXRedSpawn(int XRedSpawn) {
        this.XRedSpawn = XRedSpawn;
    }

    public int getYRedSpawn() {
        return YRedSpawn;
    }

    public void setYRedSpawn(int YRedSpawn) {
        this.YRedSpawn = YRedSpawn;
    }

    public int getZRedSpawn() {
        return ZRedSpawn;
    }

    public void setZRedSpawn(int ZRedSpawn) {
        this.ZRedSpawn = ZRedSpawn;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
