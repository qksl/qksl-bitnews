package io.bitnews.model.em;

/**
 * Created by ywd on 2019/11/27.
 */
public enum BetStatusEnum {
    //0-开盘, 1-封盘, 2-结算
    OK("赢", 1),NO("输", 2);

    private int index;
    private String name;

    private BetStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (BetStatusEnum se : BetStatusEnum.values()) {
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
