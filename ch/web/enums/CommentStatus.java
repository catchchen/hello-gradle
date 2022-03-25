package com.ch.web.enums;

/**
 * @author chenk
 * @date 2022/3/20 12:14
 * @description
 */
public enum CommentStatus implements ValueEnum<Integer> {
    /**
     * 已发布
     */
    PUBLISHED(0),

    /**
     * Auditing status.
     */
    AUDITING(1),

    /**
     * 回收站
     */
    RECYCLE(2);

    private final Integer value;

    CommentStatus(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}