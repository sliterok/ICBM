package icbm.explosion.potion;

import icbm.core.Settings;
import calclavia.lib.prefab.potion.CustomPotion;

public abstract class ICBMPotion extends CustomPotion
{
	public ICBMPotion(int id, boolean isBadEffect, int color, String name)
	{
		super(Settings.CONFIGURATION.get("Potion", name, id).getInt(id), isBadEffect, color, name);
	}
}