package io.bitnews.model.em;

/**
 * Created by ywd on 2019/11/27.
 */
public enum FearEnum {
    EXTREMEFEAR("极度恐慌", "Extreme Fear"),FEAR("恐慌", "Fear")
    ,NEUTRAL("中性", "Neutral"), GREED("贪婪","Greed");

    private String index;
    private String name;

    private FearEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(String index) {
        for (FearEnum se : FearEnum.values()) {
            if (se.getIndex().equals(index)) {
                return se.getName();
            }
        }
        return null;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
