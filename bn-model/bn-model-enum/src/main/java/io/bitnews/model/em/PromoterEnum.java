package io.bitnews.model.em;

/**
 * Created by ywd on 2019/11/27.
 */
public enum PromoterEnum {
    //0-开盘, 1-封盘, 2-结算
    OK("", 0),NO("不能", 1);

    private int index;
    private String name;

    private PromoterEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (PromoterEnum se : PromoterEnum.values()) {
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
