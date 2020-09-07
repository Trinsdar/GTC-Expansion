package gtc_expansion.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3d;

public abstract class Transformation {
    public Matrix4 mat;

    public Transformation(Matrix4 mat) {
        this.mat = mat;
    }

    @SideOnly(Side.CLIENT)
    public void glApply() {
        mat.glApply();
    }

    public abstract void apply(Vector3d v);

}
