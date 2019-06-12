package gtc_expansion.material;

import gtclassic.material.GTMaterial;
import gtclassic.material.GTMaterialFlag;

public class GTMaterial2 {
    static GTMaterialFlag particle = new GTMaterialFlag("_particle", 15, false);
    static GTMaterialFlag fluid = GTMaterialFlag.FLUID;
    static GTMaterialFlag gas = GTMaterialFlag.GAS;
    static GTMaterialFlag smalldust = new GTMaterialFlag("_dustsmall", 1, false);
    static GTMaterialFlag dust = GTMaterialFlag.DUST;
    static GTMaterialFlag gem = GTMaterialFlag.GEM;
    static GTMaterialFlag ingot = GTMaterialFlag.INGOT;
    static GTMaterialFlag nugget = new GTMaterialFlag("_nugget", 5, false);
    static GTMaterialFlag plate = new GTMaterialFlag("_plate", 6, false);
    static GTMaterialFlag smallplate = new GTMaterialFlag("_smallplate", 7, false);
    static GTMaterialFlag stick = new GTMaterialFlag("_stick", 8, false);
    static GTMaterialFlag magnetic = new GTMaterialFlag("_magneticstick", 8, true);
    static GTMaterialFlag wire = new GTMaterialFlag("_finewire", 12, false);
    static GTMaterialFlag gear = new GTMaterialFlag("_gear", 10, false);
    static GTMaterialFlag foil = new GTMaterialFlag("_foil", 11, false);
    static GTMaterialFlag block = GTMaterialFlag.BLOCK;
    static GTMaterialFlag casing = new GTMaterialFlag("_casing", 50, false);
    static GTMaterialFlag coil = new GTMaterialFlag("_coil", 52, false);
    static GTMaterialFlag[] dustAll = { smalldust, dust };
    static GTMaterialFlag[] gemAll = { smalldust, dust, gem, block };
    static GTMaterialFlag[] metalFull = { smalldust, dust, nugget, ingot, plate, stick, gear, block, casing };
    static GTMaterialFlag[] metalBase = { smalldust, dust, nugget, ingot, plate, stick };
    static GTMaterialFlag[] metalIc2 = { smalldust, nugget, plate, stick, gear, casing };
    static GTMaterialFlag[] metalMC = { smalldust, plate, stick, gear, casing };
    static GTMaterialFlag[] slurryBase = { smalldust, dust, fluid };

    public static GTMaterial2
    Bronze = new GTMaterial2(new GTMaterial("Bronze", 230, 83, 34, metalIc2),6.0F, 192, 2),
    RefinedIron = new GTMaterial2(new GTMaterial("RefinedIron", 220, 235, 235, stick, plate, gear, casing),6.0F, 384, 2);


    private GTMaterial material;
    private float speed;
    private int durability;
    private int level;
    public GTMaterial2(GTMaterial material, float speed, int durability, int level){
        this.material = material;
        this.speed = speed;
        this.durability = durability;
        this.level = level;
    }

    public GTMaterial getMaterial() {
        return material;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDurability() {
        return durability;
    }

    public int getLevel() {
        return level;
    }
}
