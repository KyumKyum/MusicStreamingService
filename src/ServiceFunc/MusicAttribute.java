package ServiceFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 이거두 User처럼 바꿔서 사용하면 될 것 같아요 ^^
public enum MusicAttribute {
    TITLE(0, "Title", "none"),
    ARTIST(1, "Artist", "none"),
    PRODUCER(2, "Prod", "none"),
    PLAYTIME(3, "Playtime", "none"),
    GENRE(4, "Genre", "none"),
    RELEASED(5, "Released", "none"),
    TOTAL_PLAYED_COUNT(6, "T_played", "rank"),
    URL(7, "URL", "play");

    private final int index;
    private final String attribute;
    private final String infoType;

    MusicAttribute(int index, String attribute, String infoType) {
        this.index = index;
        this.attribute = attribute;
        this.infoType = infoType;
    }

    public static List<MusicAttribute> getNoneTypes() {
        return Arrays.stream(values())
            .filter(musicAttribute -> musicAttribute.infoType.equals("none"))
            .collect(Collectors.toList());
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
