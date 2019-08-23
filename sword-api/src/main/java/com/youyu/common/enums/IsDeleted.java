package com.youyu.common.enums;

import com.youyu.common.api.BaseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否被删除
 *
 * @author WangSongJun
 * @date 2019-03-12
 */
@Getter
@AllArgsConstructor
public enum IsDeleted implements BaseCodeEnum {
    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private int code;
    private String desc;
}
