package com.builtbroken.icbm.content.crafting.station;

import com.builtbroken.jlib.data.vector.IPos3D;
import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.multiblock.IMultiTileHost;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import com.builtbroken.mc.prefab.tile.TileModuleMachine;
import com.builtbroken.mc.framework.multiblock.MultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

/**
 * Prefab for all the workstation to use for making implementation easier
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/5/2015.
 */
public abstract class TileAbstractWorkstation<I extends IInventory> extends TileModuleMachine<I> implements IMultiTileHost
{
    /** Trigger to prevent breaking blocks while the multi block is being removed */
    private boolean _destroyingStructure = false;
    /** Trigger to prevent break blocks while the multi block is rotating */
    protected boolean rotating = false;

    /** Connected side of the block, if set reset mutli block structure to avoid ghost blocks & invalid renders. */
    public ForgeDirection connectedBlockSide = ForgeDirection.UP;

    public TileAbstractWorkstation(String name, Material material)
    {
        super(name, material);
    }

    @Override
    public void onRemove(Block block, int par6)
    {
        super.onRemove(block, par6);
        breakDownStructure(true, true);
    }

    @Override
    public void onMultiTileAdded(IMultiTile tileMulti)
    {
        if (tileMulti instanceof TileEntity)
        {
            Pos pos = new Pos((TileEntity) tileMulti);
            if (!getLayoutOfMultiBlock().containsKey(pos))
            {
                tileMulti.setHost(null);
            }
        }
        else
        {
            tileMulti.setHost(null);
        }
    }

    @Override
    public boolean onMultiTileBroken(IMultiTile tileMulti, Object source, boolean harvest)
    {
        if (!_destroyingStructure && !rotating && tileMulti instanceof TileEntity)
        {
            Pos pos = new Pos((TileEntity) tileMulti).sub(new Pos((TileEntity)this));
            if (getLayoutOfMultiBlock().containsKey(pos))
            {
                breakDownStructure(harvest, true);
                return true;
            }
        }
        return false;
    }

    protected void breakDownStructure(boolean drop, boolean destroy)
    {
        if (!_destroyingStructure)
        {
            _destroyingStructure = true;
            MultiBlockHelper.destroyMultiBlockStructure(this, drop, true, destroy);
            _destroyingStructure = false;
        }
    }

    @Override
    public void onTileInvalidate(IMultiTile tileMulti)
    {

    }

    @Override
    public boolean onMultiTileActivated(IMultiTile tile, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        return onPlayerActivated(player, side, new Pos(xHit, yHit, zHit));
    }

    @Override
    public void onMultiTileClicked(IMultiTile tile, EntityPlayer player)
    {

    }

    @Override
    public HashMap<IPos3D, String> getLayoutOfMultiBlock()
    {
        return getLayoutOfMultiBlock(getDirection());
    }

    /**
     * Version of {@link TileAbstractWorkstation#getLayoutOfMultiBlock()} that uses the rotation to
     * get the layout
     *
     * @param rotation - direction the machine is facing, not the connected side
     * @return map of layout, never null
     */
    public abstract HashMap<IPos3D, String> getLayoutOfMultiBlock(ForgeDirection rotation);


    @Override
    public ForgeDirection getDirection()
    {
        return facing;
    }

    public void setDirection(ForgeDirection newDir)
    {
        this.facing = newDir;
    }

    /**
     * Checks to see if the rotation is blocked.
     *
     * @param newRotation - rotation to check
     * @return true if any block in the multi block map is not air.
     */
    public boolean isRotationBlocked(ForgeDirection newRotation)
    {
        for (IPos3D p : getLayoutOfMultiBlock(newRotation).keySet())
        {
            Pos pos = this.toPos().add(p);
            Block block = oldWorld().getBlock((int) pos.x(), (int) pos.y(), (int) pos.z());
            if (!block.isAir(oldWorld(), (int) pos.x(), (int) pos.y(), (int) pos.z()))
            {
                TileEntity tile = pos.getTileEntity(oldWorld());
                if (tile instanceof IMultiTile && (((IMultiTile) tile).getHost() == this || ((IMultiTile) tile).getHost() == null))
                {
                    continue;
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
    }

    /**
     * Called to eject crafting items
     */
    public void ejectCraftingItems()
    {
        for (int i = 0; i <= getSizeInventory(); i++)
        {
            if (getStackInSlot(i) != null)
            {
                InventoryUtility.dropItemStack(toLocation(), getStackInSlot(i));
                setInventorySlotContents(i, null);
            }
        }
    }
}
