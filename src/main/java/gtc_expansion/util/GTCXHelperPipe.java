package gtc_expansion.util;

import ic2.core.block.base.util.texture.TextureCopyStorage.QuadList;
import net.minecraft.util.math.Vec3i;

public class GTCXHelperPipe {
    public enum GTPipeModel {
        SMALL("small_", new int[] { 5, 11 }),
        MED("", new int[] { 4, 12 }),
        LARGE("large_", new int[] { 2, 14 }),
        HUGE("huge_", new int[]{0, 16}),
        QUAD("quad_", new int[]{0, 16});

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
        S100(100),
        s200(200),
        S300(300),
        s400(400),
        S600(600),
        s1200(1200),
        S2400(2400),
        S3600(3600),
        S7200(7200);

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
