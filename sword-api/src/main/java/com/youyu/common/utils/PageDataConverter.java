//package com.youyu.common.utils;
//
//import com.youyu.common.api.PageData;
//import com.youyu.common.exception.BizException;
//import com.github.pagehelper.Page;
//
//import java.util.List;
//
//
///**
// * @author superwen
// */
//public class PageDataConverter {
//
//    @SuppressWarnings("unchecked")
//    public static PageData converter(Page page) {
//        PageData pageData = new PageData();
//        pageData.setTotalCount(page.getTotal());
//        pageData.setPageNum(page.getPageNum());
//        pageData.setPageSize(page.getPageSize());
//        pageData.setTotalPage(page.getPages());
//        pageData.setRows(page);
//        return pageData;
//    }
//
//    /**
//     * 将 List 转为分页 bean,暂时只支持 page
//     *
//     * @param page page
//     * @return page
//     * @thorw PageDataConverterException PageDataConverterException
//     */
//    public static PageData converter(List page) {
//        if (page instanceof Page) {
//            return converter((Page) page);
//        }
//        throw new PageDataConverterException("转换page失败");
//    }
//
//    public static class PageDataConverterException extends BizException {
//
//        public PageDataConverterException() {
//            this("转换page失败");
//        }
//
//        public PageDataConverterException(String code) {
//            super(code);
//        }
//
//        public PageDataConverterException(String message, String code) {
//            super(message, code);
//        }
//
//        public PageDataConverterException(String message, Throwable cause, String code) {
//            super(code, message, cause);
//        }
//
//        public PageDataConverterException(Throwable cause, String code) {
//            super(code, cause);
//        }
//
//        public PageDataConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
//            super(message, cause, enableSuppression, writableStackTrace, code);
//        }
//    }
//}
