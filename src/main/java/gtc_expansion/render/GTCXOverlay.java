package gtc_expansion.render;

public class GTCXOverlay {
    public int x, z;
    public double y;

    public GTCXOverlay(int x, double y, int z) {
        this.x = x;
        this.y = y + 0.01;
        this.z = z;
    }
}
