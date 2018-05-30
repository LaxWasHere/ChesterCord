package net.poweredbyhate.chestercord;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.jibble.jmegahal.JMegaHal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Chester implements MessageCreateListener {

    JMegaHal hal = new JMegaHal();
    DiscordApi api;

    public Chester(DiscordApi api) {
        this.api = api;
        awaken();
    }

    private void awaken() {
        try (BufferedReader br = new BufferedReader(new FileReader("chester.brain"))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                hal.add(line);
            }
        } catch (Exception e) {
            firstRun();
        }
    }

    private void firstRun() {
        addToBrain("Have you ever questioned the nature of your reality?");
        addToBrain("Have you ever seen anything so full of splendor?");
        addToBrain("It's a difficult thing, realizing your entire life is some hideous fiction.");
        addToBrain("Time undoes even the mightiest of creatures. Just look at what it's done to you.");
        addToBrain("I want you to break into Hell with me and rob the gods blind");
        addToBrain("You can't play God without being acquainted with the Devil");
    }

    @Override
    public void onMessageCreate(MessageCreateEvent ev) {
        if (ev.getMessage().getAuthor().isYourself()) {
            return;
        }
        if (ev.getMessage().getMentionedUsers().contains(api.getYourself())) {
            ev.getChannel().sendMessage(hal.getSentence(getSeed(ev.getMessage().getContent())));
        } else {
            addToBrain(ev.getMessage().getContent());
        }
    }

    public String getSeed(String message) {
        List<String> words = new ArrayList<>(Arrays.asList(message.split(" ")));
        words.remove(String.format("<@$s>", api.getYourself().getIdAsString()));
        words.removeIf(word -> word.length() < 4);
        if (words.isEmpty()) return null;
        return words.get(ThreadLocalRandom.current().nextInt(words.size()));
    }

    public void addToBrain(String rawmessage) {
        if (rawmessage != null && rawmessage.length() < 3) return;
        String message = clean(rawmessage);
        hal.add(message);
        write(message);
    }

    public String clean(String string) {
        if (string.length() > 300) {
            string = string.substring(0, 300);
        }
        return string.replaceAll("<.*?>", "").replaceAll("\\[.*?\\]", "");
    }

    public void write(String sentence) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("chester.brain", true))) {
            bw.append(sentence);
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
