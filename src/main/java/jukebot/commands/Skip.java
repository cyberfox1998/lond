package jukebot.commands;

import jukebot.JukeBot;
import jukebot.audioutilities.GuildMusicManager;
import jukebot.utils.Bot;
import jukebot.utils.Command;
import jukebot.utils.Permissions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Skip implements Command {

    final Permissions permissions = new Permissions();

    public void execute(GuildMessageReceivedEvent e, String query) {

        final GuildMusicManager musicManager = JukeBot.getGuildMusicManager(e.getGuild().getAudioManager());

        if (!musicManager.isPlaying()) {
            e.getChannel().sendMessage(new EmbedBuilder()
                    .setColor(Bot.EmbedColour)
                    .setTitle("No playback activity")
                    .setDescription("There's nothing playing.")
                    .build()
            ).queue();
            return;
        }

        if (!permissions.checkVoiceChannel(e.getMember())) {
            e.getChannel().sendMessage(new EmbedBuilder()
                    .setColor(Bot.EmbedColour)
                    .setTitle("No Mutual VoiceChannel")
                    .setDescription("Join my VoiceChannel to use this command.")
                    .build()
            ).queue();
            return;
        }

        final boolean skipAdded = musicManager.handler.voteSkip(e.getAuthor().getIdLong());

        final int Votes = musicManager.handler.getVotes();
        final long NeededVotes = Math.round(((double) e.getMember().getVoiceState().getChannel().getMembers().stream().filter(u -> !u.getUser().isBot()).count()) / 2);

        if (NeededVotes - Votes <= 0)
            musicManager.handler.playNext(null);
        else
            e.getChannel().sendMessage(new EmbedBuilder()
                    .setColor(Bot.EmbedColour)
                    .setTitle(skipAdded ? "Vote Acknowledged" : "Already Voted")
                    .setDescription((NeededVotes - Votes) + " votes needed to skip.")
                    .build()
            ).queue();
    }
}