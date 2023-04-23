package entity;

import org.bukkit.World;
import util.TeamColors;

public class Flag {

    private String name;

    private TeamColors colors;

    private World world;

    private int X;

    private int Y;

    private int Z;

    public Flag(String name, TeamColors colors, World world, int x, int y, int z) {
        this.name = name;
        this.colors = colors;
        this.world = world;
        X = x;
        Y = y;
        Z = z;
    }

    public Flag(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamColors getColors() {
        return colors;
    }

    public void setColors(TeamColors colors) {
        this.colors = colors;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getZ() {
        return Z;
    }

    public void setZ(int z) {
        Z = z;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}

