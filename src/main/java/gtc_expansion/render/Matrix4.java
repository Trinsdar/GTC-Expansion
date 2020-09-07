package gtc_expansion.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Matrix4 {
    private static FloatBuffer glBuf = ByteBuffer.allocateDirect(16 * 8).order(ByteOrder.nativeOrder()).asFloatBuffer();

    public double m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

    public Matrix4(double d00, double d01, double d02, double d03, double d10, double d11, double d12, double d13, double d20, double d21, double d22, double d23, double d30, double d31, double d32, double d33) {
        m00 = d00;
        m01 = d01;
        m02 = d02;
        m03 = d03;
        m10 = d10;
        m11 = d11;
        m12 = d12;
        m13 = d13;
        m20 = d20;
        m21 = d21;
        m22 = d22;
        m23 = d23;
        m30 = d30;
        m31 = d31;
        m32 = d32;
        m33 = d33;
    }




    @SideOnly(Side.CLIENT)
    public void glApply() {
        glBuf.put((float) m00).put((float) m10).put((float) m20).put((float) m30).put((float) m01).put((float) m11).put((float) m21).put((float) m31).put((float) m02).put((float) m12).put((float) m22).put((float) m32).put((float) m03).put((float) m13).put((float) m23).put((float) m33);
        glBuf.flip();
        GlStateManager.multMatrix(glBuf);
    }



    @Override
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + Double.doubleToLongBits(m00);
        bits = 31L * bits + Double.doubleToLongBits(m01);
        bits = 31L * bits + Double.doubleToLongBits(m02);
        bits = 31L * bits + Double.doubleToLongBits(m03);
        bits = 31L * bits + Double.doubleToLongBits(m10);
        bits = 31L * bits + Double.doubleToLongBits(m11);
        bits = 31L * bits + Double.doubleToLongBits(m12);
        bits = 31L * bits + Double.doubleToLongBits(m13);
        bits = 31L * bits + Double.doubleToLongBits(m20);
        bits = 31L * bits + Double.doubleToLongBits(m21);
        bits = 31L * bits + Double.doubleToLongBits(m22);
        bits = 31L * bits + Double.doubleToLongBits(m23);
        bits = 31L * bits + Double.doubleToLongBits(m30);
        bits = 31L * bits + Double.doubleToLongBits(m31);
        bits = 31L * bits + Double.doubleToLongBits(m32);
        bits = 31L * bits + Double.doubleToLongBits(m33);
        return (int) (bits ^ (bits >> 32));
    }
}
