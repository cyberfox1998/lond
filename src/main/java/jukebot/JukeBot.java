package jukebot;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import jukebot.audioutilities.GuildMusicManager;
import jukebot.utils.Bot;
import jukebot.utils.Log4JConfig;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.managers.AudioManager;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.sqlite.SQLiteJDBCLoader;

import java.util.HashMap;

import static jukebot.utils.Bot.LOG;

public class JukeBot {

    public static final long startTime = System.currentTimeMillis();

    private static final DatabaseHandler db = new DatabaseHandler();

    private static final HashMap<Long, GuildMusicManager> musicManagers = new HashMap<>();

    private static Shard[] shards;

    public static void main(String[] args) throws Exception {
        ConfigurationFactory.setConfigurationFactory(new Log4JConfig());

        LOG.info(".:: JukeBot " + Bot.VERSION + " ::.\n" +
                ":: JDA: " + JDAInfo.VERSION + "\n" +
                ":: Lavaplayer: " + PlayerLibrary.VERSION + "\n" +
                ":: SQLite: " + SQLiteJDBCLoader.getVersion() + "\n" +
                ":: JVM running in " + System.getProperty("sun.arch.data.model") + "-bit mode");

        Bot.Configure();

        final int MaxShards = Integer.parseInt(db.getPropertyFromConfig("maxshards"));

        shards = new Shard[MaxShards];

        for (int i = 0; i < MaxShards; i++) {
            try {
                shards[i] = new Shard(i, MaxShards);
            } catch(Exception ignored) {
                LOG.error("[" + (i + 1) + "/" + MaxShards + "] failed to login");
            }
            Thread.sleep(5500);
        }

    }

    public static Shard[] getShards() {
        return shards;
    }

    public static HashMap<Long, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }

    public static GuildMusicManager getGuildMusicManager(final AudioManager manager) {

        GuildMusicManager musicManager = musicManagers.computeIfAbsent(manager.getGuild().getIdLong(), k -> new GuildMusicManager());

        if (manager.getSendingHandler() == null)
            manager.setSendingHandler(musicManager.handler);

        return musicManager;

    }

}
