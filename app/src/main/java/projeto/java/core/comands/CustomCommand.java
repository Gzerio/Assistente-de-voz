package projeto.java.core.comands;

import java.util.Arrays;
import java.util.List;

public class CustomCommand {
    private String name;       
    private String phrases;     
    private String targetUrl;   
    public CustomCommand() {
    }

    public CustomCommand(String name, String phrases, String targetUrl) {
        this.name = name;
        this.phrases = phrases;
        this.targetUrl = targetUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhrases() {
        return phrases;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public List<String> getPhraseList() {
        if (phrases == null || phrases.isBlank()) return List.of();
        return Arrays.stream(phrases.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    @Override
    public String toString() {
        return name + " → " + targetUrl;
    }
}
