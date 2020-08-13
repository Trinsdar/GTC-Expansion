package gtc_expansion.util;

import ic2.core.block.base.util.texture.TextureCopyStorage.QuadList;
import net.minecraft.util.math.Vec3i;

public class GTCXHelperPipe {
    public enum GTPipeModel {
        SMALL("_small", new int[] { 6, 10 }),
        MED("", new int[] { 4, 12 }),
        LARGE("_large", new int[] { 1, 15 });

        String suffix;
        int[] sizes;

        GTPipeModel(String suffix, int[] sizes) {
            this.suffix = suffix;
            this.sizes = sizes;
        }

        public String getPrefix() {
            return suffix.replace("_", "");
        }

        public String getSuffix() {
            return this.suffix.toLowerCase();
        }

        public int[] getSizes() {
            return this.sizes;
        }
    }

    public enum GTFluidPipeAmount {
        S800(800),
        S1600(1600),
        S2400(2400),
        S3200(3200),
        S4800(4800),
        S7200(7200),
        S9600(9600),
        S14400(14400),
        S19200(19200);

        int transfer;

        GTFluidPipeAmount(int transfer){
            this.transfer = transfer;
        }

        public int getTransfer() {
            return transfer;
        }
    }

    public static class GTCXQuadWrapper{
        QuadList quadList;
        Vec3i vec;
        public GTCXQuadWrapper(QuadList list, Vec3i vec){
            this.quadList = list;
            this.vec = vec;
        }

        public QuadList getQuadList(){
            return quadList;
        }

        public Vec3i getVec() {
            return vec;
        }
    }
}
