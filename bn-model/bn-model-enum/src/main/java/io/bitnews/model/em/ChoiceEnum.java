package io.bitnews.model.em;

/**
 * Created by ywd on 2019/11/27.
 */
public enum ChoiceEnum {
    //0-开盘, 1-封盘, 2-结算
    OK("支持", 0),NO("反对", 1);

    private int index;
    private String name;

    private ChoiceEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (ChoiceEnum se : ChoiceEnum.values()) {
            if (se.getIndex() == index) {
                return se.getName();
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
