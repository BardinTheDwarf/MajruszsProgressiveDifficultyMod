package com.majruszs_difficulty.features.when_damaged;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;

/** Making attack with tools inflict bleeding on enemies. */
public class SharpItemBleedingOnAttack extends WhenDamagedApplyBleedingBase {
	private static final String CONFIG_NAME = "SharpItemBleeding";
	private static final String CONFIG_COMMENT = "All sharp items inflict bleeding. (tools, weapons, shears etc.)";

	public SharpItemBleedingOnAttack() {
		super( CONFIG_NAME, CONFIG_COMMENT, 0.25, 24.0 );
	}

	/** Checking if all conditions were met. */
	@Override
	public boolean shouldBeExecuted( @Nullable LivingEntity attacker, LivingEntity target, DamageSource damageSource ) {
		if( attacker != null ) {
			ItemStack heldItemStack = attacker.getMainHandItem();
			Item heldItem = heldItemStack.getItem();
			boolean attackerHasSharpItem = heldItem instanceof TieredItem || heldItem instanceof TridentItem || heldItem instanceof SwordItem || heldItem instanceof ShearsItem;

			return attackerHasSharpItem && super.shouldBeExecuted( attacker, target, damageSource );
		}

		return false;
	}
}