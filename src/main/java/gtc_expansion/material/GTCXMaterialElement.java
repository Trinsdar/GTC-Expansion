package gtc_expansion.material;

import gtclassic.api.material.GTMaterialElement;
import gtclassic.api.material.GTMaterialGen;
import net.minecraft.item.ItemStack;

public class GTCXMaterialElement {
    public static final GTMaterialElement antimony = addElement(51, "dustAntimony", GTMaterialGen.getDust(GTCXMaterial.Antimony, 1));
    public static final GTMaterialElement lead = addElement(82, "dustLead", GTMaterialGen.getDust(GTCXMaterial.Lead, 1));
    public static final GTMaterialElement magnesium = addElement(12, "dustMagnesium", GTMaterialGen.getDust(GTCXMaterial.Magnesium, 1));
    public static final GTMaterialElement manganese = addElement(25, "dustManganese", GTMaterialGen.getDust(GTCXMaterial.Manganese, 1));
    public static final GTMaterialElement nickel = addElement(28, "dustNickel", GTMaterialGen.getDust(GTCXMaterial.Nickel, 1));
    public static final GTMaterialElement osmium = addElement(76, "dustOsmium", GTMaterialGen.getDust(GTCXMaterial.Osmium, 1));
    public static final GTMaterialElement phosphorus = addElement(15, "dustPhosphorus", GTMaterialGen.getDust(GTCXMaterial.Phosphorus, 1));
    public static final GTMaterialElement sulfur = addElement(16, "dustSulfur", GTMaterialGen.getDust(GTCXMaterial.Sulfur, 1));
    public static final GTMaterialElement zinc = addElement(30, "dustZinc", GTMaterialGen.getDust(GTCXMaterial.Zinc, 1));

    public static void init(){}

    public static GTMaterialElement addElement(int number, String input, ItemStack output) { ;
        return GTMaterialElement.addElement(number, input, output);
    }

    public static GTMaterialElement addElement(int number, ItemStack output) {
        return GTMaterialElement.addElement(number, output);
    }
}
