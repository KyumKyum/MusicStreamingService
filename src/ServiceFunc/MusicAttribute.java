package ServiceFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MusicAttribute {
    IDX(0, "IDX", "idx"),
    TITLE(1, "Title", "none"),
    ARTIST(2, "Artist", "none"),
    PRODUCER(3, "Prod", "none"),
    PLAYTIME(4, "Playtime", "none"),
    GENRE(5, "Genre", "none"),
    RELEASED(6, "Released", "none"),
    TOTAL_PLAYED_COUNT(7, "T_played", "rank"),
    URL(8, "URL", "play");

    private final int index;
    private final String attribute;
    private final String infoType;

    MusicAttribute(int index, String attribute, String infoType) {
        this.index = index;
        this.attribute = attribute;
        this.infoType = infoType;
    }

    public static List<MusicAttribute> getGeneralTypes() {
        return Arrays.stream(values())
                .filter(musicAttribute -> musicAttribute.infoType.equals("none") || musicAttribute.infoType.equals("idx"))
                .collect(Collectors.toList());
    }

    public static List<MusicAttribute> getInputTypes() {
        return Arrays.stream(values())
                .filter(musicAttribute -> !musicAttribute.infoType.equals("idx") && !musicAttribute.infoType.equals("rank"))
                .collect(Collectors.toList());
    }

    public static List<MusicAttribute> getAllTypes(){
        return Arrays.asList(values());
    }

    public String getInfoType() {
        return infoType;
    }

    public int getIndex() {
        return index;
    }

    public String getAttribute() {
        return attribute;
    }
}