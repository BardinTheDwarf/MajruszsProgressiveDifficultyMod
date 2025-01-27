package com.majruszs_difficulty.features;

import com.majruszs_difficulty.GameState;
import com.majruszs_difficulty.Instances;
import com.majruszs_difficulty.entities.*;
import com.mlib.config.AvailabilityConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Disabling natural spawns for entities that have not met certain conditions. */
@Mod.EventBusSubscriber
public class SpawnDisabler {
	@SubscribeEvent
	public static void disableSpawns( LivingSpawnEvent.CheckSpawn event ) {
		if( shouldEntitySpawnBeDisabled( event.getEntityLiving() ) )
			event.setResult( Event.Result.DENY );
	}

	/** Checks whether given entity should not spawn. */
	protected static boolean shouldEntitySpawnBeDisabled( Entity entity ) {
		EntitiesConfig config = Instances.ENTITIES_CONFIG;
		boolean isGiant = entity instanceof GiantEntity;
		boolean isIllusioner = entity instanceof Illusioner;
		boolean isPillagerWolf = entity instanceof PillagerWolfEntity;
		boolean isEliteSkeleton = entity instanceof EliteSkeletonEntity;
		boolean isSkyKeeper = entity instanceof SkyKeeperEntity;
		boolean isCreeperling = entity instanceof CreeperlingEntity;
		boolean isParasite = entity instanceof ParasiteEntity;

		if( isGiant )
			return shouldBeDisabled( GameState.State.EXPERT, config.giant.availability );
		else if( isIllusioner )
			return shouldBeDisabled( GameState.State.EXPERT, config.illusioner.availability ) || isVillageNearby( entity );
		else if( isPillagerWolf )
			return shouldBeDisabled( GameState.State.EXPERT, config.pillagerWolf.availability );
		else if( isEliteSkeleton )
			return shouldBeDisabled( GameState.State.EXPERT, config.eliteSkeleton.availability );
		else if( isSkyKeeper )
			return shouldBeDisabled( GameState.State.EXPERT, config.skyKeeper.availability );
		else if( isCreeperling )
			return shouldBeDisabled( GameState.State.NORMAL, config.creeperling.availability );
		else if( isParasite )
			return shouldBeDisabled( GameState.State.MASTER, config.parasite.availability );
		else
			return false;
	}

	/** Checks most common variant for disabling. */
	private static boolean shouldBeDisabled( GameState.State minimumState, AvailabilityConfig config ) {
		return !GameState.atLeast( minimumState ) || config.isDisabled();
	}

	/**
	 Checks whether there is the village nearby.
	 It is required to disable spawns near village for Illusioner because
	 otherwise Illusioner may spawn underground while there is raid active and
	 players will have a problem to finish it.
	 */
	private static boolean isVillageNearby( Entity entity ) {
		if( !( entity.level instanceof ServerLevel ) )
			return false;

		ServerLevel world = ( ServerLevel )entity.level;
		return world.findNearestMapFeature( StructureFeature.VILLAGE, entity.blockPosition(), 10000, false ) != null;
	}
}
