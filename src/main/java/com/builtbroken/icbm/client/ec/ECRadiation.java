package com.builtbroken.icbm.client.ec;

import com.builtbroken.icbm.ICBM;
import com.builtbroken.icbm.content.blast.potion.ExRadiation;
import com.builtbroken.mc.api.explosive.ITexturedExplosiveHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/26/2016.
 */
@Deprecated //TODO replace with JSON as ICONS are not used in 1.8+
public class ECRadiation extends ExRadiation implements ITexturedExplosiveHandler
{
    IIcon icon;

    @Override
    public IIcon getBottomLeftCornerIcon(ItemStack stack)
    {
        return icon;
    }

    @Override
    public void registerExplosiveHandlerIcons(IIconRegister reg, boolean blocks)
    {
        if (!blocks)
        {
            icon = reg.registerIcon(ICBM.PREFIX + "ex.icon.radiation");
        }
    }
}
