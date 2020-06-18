package csu.demo.model.enums;

public enum BookStatusEnum {
    NORMAL(0),
    DELETE(1),
    RECOMMAND(2);

    private int value;

    BookStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
