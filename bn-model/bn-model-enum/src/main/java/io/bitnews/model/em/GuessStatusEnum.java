package io.bitnews.model.em;

/**
 * Created by ywd on 2019/11/27.
 */
public enum GuessStatusEnum {
    //0-开盘, 1-封盘, 2-结算
    OPEN("开盘", 0),CLOSE("封盘", 1),SETTLEMENT("结算", 2),FAIL("流猜", 3);

    private int index;
    private String name;

    private GuessStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (GuessStatusEnum se : GuessStatusEnum.values()) {
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
