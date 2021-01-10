package com.majruszs_difficulty.events.on_attack;

import com.majruszs_difficulty.GameState.Mode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.Difficulty;

/** Base class representing event on which enemies will receive some stackable effects after being attacked. */
public abstract class OnAttackStackableEffectBase extends OnAttackEffectBase {
	protected final boolean isAmplifierStackable;
	protected final boolean isDurationStackable;
	protected final int maximumAmplifier;
	protected final int maximumDurationInTicks;

	public OnAttackStackableEffectBase( Class< ? extends LivingEntity > entityCausingEffect, Mode minimumMode, boolean shouldBeMultipliedByCRD,
		Effect[] effects, boolean isAmplifierStackable, boolean isDurationStackable, int maximumAmplifier, int maximumDurationInTicks
	) {
		super( entityCausingEffect, minimumMode, shouldBeMultipliedByCRD, effects );

		this.isAmplifierStackable = isAmplifierStackable;
		this.isDurationStackable = isDurationStackable;
		this.maximumAmplifier = maximumAmplifier;
		this.maximumDurationInTicks = maximumDurationInTicks;
	}

	public OnAttackStackableEffectBase( Class< ? extends LivingEntity > entityCausingEffect, Mode minimumMode, boolean shouldBeMultipliedByCRD,
		Effect effect, boolean isAmplifierStackable, boolean isDurationStackable, int maximumAmplifier, int maximumDurationInTicks
	) {
		this( entityCausingEffect, minimumMode, shouldBeMultipliedByCRD, new Effect[]{ effect }, isAmplifierStackable, isDurationStackable,
			maximumAmplifier, maximumDurationInTicks
		);
	}

	/**
	 Applying effect on entity directly. (if possible, because enemy may be immune for example)

	 @param target     Entity who will get effect.
	 @param effect     Effect type to apply.
	 @param difficulty Current world difficulty.
	 */
	@Override
	protected void applyEffect( LivingEntity target, Effect effect, Difficulty difficulty ) {
		EffectInstance previousEffectInstance = target.getActivePotionEffect( effect );

		if( previousEffectInstance == null ) {
			super.applyEffect( target, effect, difficulty );
			return;
		}

		int durationInTicks = Math.min( getDurationInTicks( difficulty ) + previousEffectInstance.getDuration(), this.maximumDurationInTicks );
		int amplifier = Math.min( getAmplifier( difficulty ) + previousEffectInstance.getAmplifier(), this.maximumAmplifier );

		EffectInstance effectInstance = new EffectInstance( effect, durationInTicks, amplifier );
		if( target.isPotionApplicable( effectInstance ) )
			target.addPotionEffect( effectInstance );
	}
}