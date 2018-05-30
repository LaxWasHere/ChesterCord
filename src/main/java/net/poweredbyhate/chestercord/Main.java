package net.poweredbyhate.chestercord;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Invalid amount of arguments provided!");
            return;
        }

        DiscordApi api = new DiscordApiBuilder().setToken(args[0]).login().join();
        api.addListener(new Chester(api));
    }


}
