package com.majruszs_difficulty.generation.structures;

/** Flying Phantom structure in The End. */
public class FlyingPhantomStructure {

} /* extends NoFeatureBaseStructure {
	private static final List< MobSpawnInfo.Spawners > STRUCTURE_MONSTERS = ImmutableList.of(
		new MobSpawnInfo.Spawners( SkyKeeperEntity.type, 40, 1, 1 ), new MobSpawnInfo.Spawners( EntityType.PHANTOM, 10, 1, 1 ) );

	public FlyingPhantomStructure() {
		super( "FlyingPhantom", "Flying Phantom", 1717171717, 47, 67, Instances.FLYING_PHANTOM_FEATURE );
	}

	// /** Generation stage for where to generate the structure. /
	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.RAW_GENERATION;
	}

	// /** Factory for generating new structures. /
	public Structure.IStartFactory< NoFeatureConfig > getStartFactory() {
		return FlyingPhantomStructure.Start::new;
	}

	// /** Enemies spawning naturally near the structure. /
	@Override
	public List< MobSpawnInfo.Spawners > getDefaultSpawnList() {
		return STRUCTURE_MONSTERS;
	}

	public static class Start extends StructureStart< NoFeatureConfig > {
		public Start( Structure< NoFeatureConfig > structure, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int reference, long seed
		) {
			super( structure, chunkX, chunkZ, mutableBoundingBox, reference, seed );
		}

		@Override
		public void func_230364_a_( DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManager,
			int chunkX, int chunkZ, Biome biome, NoFeatureConfig config
		) {
			Rotation rotation = Rotation.values()[ this.rand.nextInt( Rotation.values().length ) ];

			int x = ( chunkX << 4 ) + 7, z = ( chunkZ << 4 ) + 7; // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
			int y = Math.min( 80, chunkGenerator.getHeight( x, z, Heightmap.Types.WORLD_SURFACE_WG ) ) + 60 + MajruszLibrary.RANDOM.nextInt( 60 );

			BlockPos blockpos = new BlockPos( x, y, z );
			FlyingPhantomPiece.start( templateManager, blockpos, rotation, this.components, this.rand );

			this.recalculateStructureSize();
		}
	}
}*/