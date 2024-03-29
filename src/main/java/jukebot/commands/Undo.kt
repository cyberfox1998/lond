package jukebot.commands

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import jukebot.utils.Command
import jukebot.utils.CommandProperties
import jukebot.utils.Context

@CommandProperties(aliases = ["z"], description = "Removes the last song queued by you", category = CommandProperties.category.MEDIA)
class Undo : Command(ExecutionType.REQUIRE_MUTUAL) {

    override fun execute(context: Context) {
        val handler = context.getAudioPlayer()

        if (handler.queue.isEmpty()) {
            return context.embed("Nothing to Remove", "The queue is empty!")
        }

        val queue = handler.queue
        val i = queue.descendingIterator()

        while (i.hasNext()) {
            val t = i.next() as AudioTrack
            val requester = t.userData as Long

            if (requester == context.author.idLong) {
                i.remove()
                return context.embed("Track Removed", "**" + t.info.title + "** removed from the queue.")
            }
        }

        context.embed("No Tracks Found", "No tracks queued by you were found.")
    }
}
