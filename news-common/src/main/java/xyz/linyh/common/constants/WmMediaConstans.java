package xyz.linyh.common.constants;

public class WmMediaConstans {
    public static final String USER_ID_KEY = "userId";

    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE=50;

//    文章状态

    //草稿
    public static final int NEWS_STATUS_DRAFT = 0;
    //提交（待审核）
    public static final int NEWS_STATUS_COMMIT=1;
//    审核失败
    public static final int NEWS_STATUS_FAIL=2;
//  人工审核
    public static final int NEWS_STATUS_MANUAL_AUDIT = 3;
//  人工审核通过
    public static final int NEWS_STATUS_MANUAL_AUDIT_PASS=4;
//    审核通过（待发布）
    public static final int NEWS_STATUS_COMMIT_PASS=8;
//    已发布
    public static final int NEWS_STATUS_OK=9;

//    文章图片状态

//    自动
    public static final int NEWS_IMAGE_CONFIG_AUTO=-1;
//    无图
    public static final int NEWS_IMAGE_CONFIG_NO=0;
//    单图
    public static final int NEWS_IMAGE_CONFIG_ONE=1;
//    多图
    public static final int NEWS_IMAGE_CONFIG_MORE=3;
}
