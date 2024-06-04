package us.timinc.mc.cobblemon.shinysparkles.store.player

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.storage.player.PlayerData
import com.cobblemon.mod.common.api.storage.player.PlayerDataExtension
import com.google.gson.JsonObject
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

class SparklesData : PlayerDataExtension {
    companion object {
        const val NAME = "sparkles"

        fun getFromPlayer(player: PlayerEntity): SparklesData {
            val playerData = Cobblemon.playerData.get(player)
            return getFromPlayerData(playerData)
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun getFromPlayerData(playerData: PlayerData): SparklesData {
            return playerData.extraData.getOrPut(NAME) { SparklesData() } as SparklesData
        }

        fun modifyForPlayer(player: PlayerEntity, modifier: (SparklesData) -> Unit) {
            val playerData = Cobblemon.playerData.get(player)
            modifier(getFromPlayerData(playerData))
            Cobblemon.playerData.saveSingle(playerData)
        }
    }

    private var _pos: Long = -1L
    var pos: BlockPos?
        get() {
            return if (_pos == -1L) {
                null
            } else {
                BlockPos.fromLong(_pos)
            }
        }
        set(pos) {
            _pos = pos?.asLong() ?: -1
        }

    override fun name(): String {
        return NAME
    }

    override fun deserialize(json: JsonObject): PlayerDataExtension {
        val posSaved = json.getAsJsonPrimitive("pos").asLong
        _pos = posSaved

        return this
    }

    override fun serialize(): JsonObject {
        val json = JsonObject()
        json.addProperty("name", NAME)
        json.addProperty("pos", _pos)

        return json
    }
}