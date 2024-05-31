package us.timinc.mc.cobblemon.shinysparkles.blocks.blockentities

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import java.util.*


class ShinySparkleBlockEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(BlockEntities.SHINY_SPARKLE_BE, pos, state) {
    var player: UUID? = null
    var pokemon: PokemonProperties? = null
    var life: Int = 0

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putString("player", player.toString())
        nbt.put("pokemon", pokemon?.saveToNBT() as NbtCompound)

        super.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        player = UUID.fromString(nbt.getString("player"))
        pokemon = PokemonProperties().loadFromNBT(nbt.get("pokemon") as NbtCompound)
    }
}