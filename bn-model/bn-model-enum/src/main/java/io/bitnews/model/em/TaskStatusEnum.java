package io.bitnews.model.em;

/**
 * Created by ywd on 2019/12/17.
 */
public enum TaskStatusEnum {

    //0-开盘, 1-封盘, 2-结算
    NO("未完成", 0),YES("待领取", 1),REWARD("已领取", 2);

    private int index;
    private String name;

    TaskStatusEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (TaskStatusEnum se : TaskStatusEnum.values()) {
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
