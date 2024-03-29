package jukebot.commands

import jukebot.audio.AudioHandler
import jukebot.utils.Command
import jukebot.utils.CommandProperties
import jukebot.utils.Context
import java.text.DecimalFormat

@CommandProperties(description = "Displays player statistics", aliases = ["ps"], developerOnly = true)
class PlayerStats : Command(ExecutionType.REQUIRE_MUTUAL) {

    private val dpFormatter = DecimalFormat("0.00")

    override fun execute(context: Context) {
        val player = context.getAudioPlayer()

        if (!player.isPlaying) {
            context.embed("Not Playing", "Nothing is currently playing.")
            return
        }

        val packetsLost = player.trackPacketLost.toDouble() / player.trackPacketsSent * 100
        val packetLossPc = dpFormatter.format(packetsLost)

        val trackProgress = player.player.playingTrack.position.toDouble() / 60000
        val totalPackets = player.trackPacketsSent + player.trackPacketLost
        val expectedPackets = AudioHandler.EXPECTED_PACKET_COUNT_PER_MIN * trackProgress
        val framesDeficit = Math.abs(expectedPackets - totalPackets).toInt()

        context.channel.sendMessage(
                "Dropped packets: ${player.trackPacketLost} ($packetLossPc%)\n" +
                        "Deficit packets: $framesDeficit\n" +
                        "Sent packets: ${player.trackPacketsSent}"
        ).queue()
    }

}
