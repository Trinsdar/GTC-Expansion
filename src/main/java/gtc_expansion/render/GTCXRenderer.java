package gtc_expansion.render;

import gtc_expansion.GTCExpansion;
import ic2.core.RotationList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;

@SideOnly(Side.CLIENT)
public class GTCXRenderer {

    public static Transformation[] sideRotations = new Transformation[]{//
            new Transformation(new Matrix4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)) {
                @Override
                public void glApply() {

                }

                @Override
                public void apply(Vector3d v) {

                }
            },
            new Transformation(new Matrix4(1, 0, 0, 0, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3d vec) {
                    vec.y = -vec.y;
                    vec.z = -vec.z;
                }

            },
            new Transformation(new Matrix4(1, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3d vec) {
                    double d1 = vec.y;
                    double d2 = vec.z;
                    vec.y = -d2;
                    vec.z = d1;
                }

            },
            new Transformation(new Matrix4(1, 0, 0, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3d vec) {
                    double d1 = vec.y;
                    double d2 = vec.z;
                    vec.y = d2;
                    vec.z = -d1;
                }

            },
            new Transformation(new Matrix4(0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3d vec) {
                    double d0 = vec.x;
                    double d1 = vec.y;
                    vec.x = d1;
                    vec.y = -d0;
                }

            },
            new Transformation(new Matrix4(0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)) {
                @Override
                public void apply(Vector3d vec) {
                    double d0 = vec.x;
                    double d1 = vec.y;
                    vec.x = -d1;
                    vec.y = d0;
                }

            }
    };

    public static void renderOverlay(EntityPlayer aPlayer, BlockPos pos, EnumFacing aSide, float aPartialTicks, RotationList connections)
    {
        int aX = pos.getX();
        int aY = pos.getY();
        int aZ = pos.getZ();

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslated(-(aPlayer.lastTickPosX + (aPlayer.posX - aPlayer.lastTickPosX) * aPartialTicks), -(aPlayer.lastTickPosY + (aPlayer.posY - aPlayer.lastTickPosY) * aPartialTicks), -(aPlayer.lastTickPosZ + (aPlayer.posZ - aPlayer.lastTickPosZ) * aPartialTicks));
        GL11.glTranslated(aX + 0.5, aY + 0.5, aZ + 0.5);
        sideRotations[aSide.getIndex()].glApply();
        GL11.glTranslated(0, -0.5025, 0);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBegin(GL11.GL_LINES);
        double animation = (double) GTCExpansion.instance.counter / 10;
        double tColor = (animation % 42 < 21 ? 0.25 + ((animation % 21)/40.0) : 0.75 - ((animation % 21)/40.0));
        GL11.glColor4d(tColor, tColor, tColor, 1);


        GL11.glVertex3d(0.50, 0, -0.25);
        GL11.glVertex3d(-0.50, 0, -0.25);
        GL11.glVertex3d(0.50, 0, 0.25);
        GL11.glVertex3d(-0.50, 0, 0.25);
        GL11.glVertex3d(0.25, 0, -0.50);
        GL11.glVertex3d(0.25, 0, 0.50);
        GL11.glVertex3d(-0.25, 0, -0.50);
        GL11.glVertex3d(-0.25, 0, 0.50);
        switch (aSide) {
            case DOWN:
                if (connections.contains(EnumFacing.DOWN)) {
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.UP)) {
                    GL11.glVertex3d(-0.50, 0, -0.50);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.50);

                    GL11.glVertex3d(+0.50, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);

                    GL11.glVertex3d(+0.50, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);

                    GL11.glVertex3d(-0.50, 0, +0.50);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.NORTH)) {
                    GL11.glVertex3d(-0.25, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);
                }
                if (connections.contains(EnumFacing.SOUTH)) {
                    GL11.glVertex3d(-0.25, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.WEST)) {
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.EAST)) {
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                break;
            case UP:
                if (connections.contains(EnumFacing.DOWN)) {
                    GL11.glVertex3d(-0.50, 0, -0.50);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.50);

                    GL11.glVertex3d(+0.50, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);

                    GL11.glVertex3d(+0.50, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);

                    GL11.glVertex3d(-0.50, 0, +0.50);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.UP)) {
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.NORTH)) {
                    GL11.glVertex3d(-0.25, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.SOUTH)) {
                    GL11.glVertex3d(-0.25, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);
                }
                if (connections.contains(EnumFacing.WEST)) {
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.EAST)) {
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                break;
            case NORTH:
                if (connections.contains(EnumFacing.DOWN)) {
                    GL11.glVertex3d(-0.25, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.UP)) {
                    GL11.glVertex3d(-0.25, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);
                }
                if (connections.contains(EnumFacing.NORTH)) {
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.SOUTH)) {
                    GL11.glVertex3d(-0.50, 0, -0.50);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.50);

                    GL11.glVertex3d(+0.50, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);

                    GL11.glVertex3d(+0.50, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);

                    GL11.glVertex3d(-0.50, 0, +0.50);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.WEST)) {
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.EAST)) {
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                break;
            case SOUTH:
                if (connections.contains(EnumFacing.DOWN)) {
                    GL11.glVertex3d(-0.25, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);
                }
                if (connections.contains(EnumFacing.UP)) {
                    GL11.glVertex3d(-0.25, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.NORTH)) {
                    GL11.glVertex3d(-0.50, 0, -0.50);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.50);

                    GL11.glVertex3d(+0.50, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);

                    GL11.glVertex3d(+0.50, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);

                    GL11.glVertex3d(-0.50, 0, +0.50);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.SOUTH)) {
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.WEST)) {
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.EAST)) {
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                break;
            case WEST:
                if (connections.contains(EnumFacing.DOWN)) {
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.UP)) {
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.NORTH)) {
                    GL11.glVertex3d(-0.25, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);
                }
                if (connections.contains(EnumFacing.SOUTH)) {
                    GL11.glVertex3d(-0.25, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.WEST)) {
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.EAST)) {
                    GL11.glVertex3d(-0.50, 0, -0.50);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.50);

                    GL11.glVertex3d(+0.50, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);

                    GL11.glVertex3d(+0.50, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);

                    GL11.glVertex3d(-0.50, 0, +0.50);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.50);
                }
                break;
            case EAST:
                if (connections.contains(EnumFacing.DOWN)) {
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.UP)) {
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                if (connections.contains(EnumFacing.NORTH)) {
                    GL11.glVertex3d(-0.25, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);
                }
                if (connections.contains(EnumFacing.SOUTH)) {
                    GL11.glVertex3d(-0.25, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.WEST)) {
                    GL11.glVertex3d(-0.50, 0, -0.50);
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(-0.50, 0, -0.25);
                    GL11.glVertex3d(-0.25, 0, -0.50);

                    GL11.glVertex3d(+0.50, 0, +0.50);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(+0.50, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, +0.50);

                    GL11.glVertex3d(+0.50, 0, -0.50);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                    GL11.glVertex3d(+0.50, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, -0.50);

                    GL11.glVertex3d(-0.50, 0, +0.50);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(-0.50, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.50);
                }
                if (connections.contains(EnumFacing.EAST)) {
                    GL11.glVertex3d(-0.25, 0, -0.25);
                    GL11.glVertex3d(+0.25, 0, +0.25);
                    GL11.glVertex3d(-0.25, 0, +0.25);
                    GL11.glVertex3d(+0.25, 0, -0.25);
                }
                break;
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }





    public static void renderSide(EntityPlayer aPlayer, BlockPos pos, EnumFacing aSide, float aPartialTicks, float d)
    {
        int aX = pos.getX();
        int aY = pos.getY();
        int aZ = pos.getZ();

        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslated(-(aPlayer.lastTickPosX + (aPlayer.posX - aPlayer.lastTickPosX) * aPartialTicks), -(aPlayer.lastTickPosY + (aPlayer.posY - aPlayer.lastTickPosY) * aPartialTicks), -(aPlayer.lastTickPosZ + (aPlayer.posZ - aPlayer.lastTickPosZ) * aPartialTicks));
        GL11.glTranslated(aX + 0.5, aY + 0.5, aZ + 0.5);
        sideRotations[aSide.getIndex()].glApply();
        GL11.glTranslated(0, -0.5025, 0);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBegin(GL11.GL_QUADS);


        double animation = (double) GTCExpansion.instance.counter / 10;
        double tColor = (animation % 42 < 21 ? 0.25 + ((animation % 21)/40.0) : 0.75 - ((animation % 21)/40.0));
        GL11.glColor4d(tColor, tColor, tColor, 0.3);

        GL11.glVertex3d(d, 0, d);
        GL11.glVertex3d(-1*d, 0, d);
        GL11.glVertex3d(-1*d, 0, -1*d);
        GL11.glVertex3d(d, 0, -1*d);

        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawOutline(EntityPlayer player, BlockPos pos, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        double animation = (double) GTCExpansion.instance.counter / 10;
        float color = (float) (animation % 42 < 21 ? 0.25 + ((animation % 21)/40.0) : 0.75 - ((animation % 21)/40.0))/255;

        GlStateManager.color(color, color, color, 0.3F);

        Vec3d pos2 = new Vec3d(player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks, player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks, player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks).scale(-1);
        RenderGlobal.renderFilledBox(Block.FULL_BLOCK_AABB.offset(pos).expand(0.002, 0.002, 0.002).offset(pos2), color, color, color, 1);
        RenderGlobal.drawSelectionBoundingBox(Block.FULL_BLOCK_AABB.offset(pos).expand(0.002, 0.002, 0.002).offset(pos2), color, color, color, 1);

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
