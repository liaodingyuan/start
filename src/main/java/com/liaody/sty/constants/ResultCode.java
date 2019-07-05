package com.liaody.sty.constants;

/**
 * 类描述:
 *
 * @author 80003254
 * @date 2019-02-27 15:11
 */
public enum ResultCode {
    /*
     * ------------------------------------
     *
     * 通用返回码，范围：[0, 99]
     *
     * ------------------------------------
     */

    /**
     * 成功
     */
    SUCCESS(1, "请求已被成功处理") {
        @Override
        public ResultMessage toResultMessage(String message, Object data) {
            return ResultMessage.success(message, data);
        }
    },

    /**
     * 请求参数错误，或者缺少某些参数
     */
    BAD_USRE_INFO(9, "用户信息错误"),
    /**
     * 请求参数错误，或者缺少某些参数
     */
    BAD_REQUEST_PARAMS(10, "请求参数有误"),
    /*
     * ------------------------------------
     *
     * 供应商服务接口相关返回码，范围：[500, 599]
     *
     * ------------------------------------
     */
    ERR_SERVICE_NOT_FOUND(501, "未找到供应商服务");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ResultMessage withMessage(String msg) {
        return toResultMessage(msg, null);
    }

    public ResultMessage withData(Object data) {
        return toResultMessage(message, data);
    }

    public ResultMessage toResultMessage(String message, Object data) {
        ResultMessage msg = ResultMessage.fail(message, data);
        msg.setResult(code);
        return msg;
    }

    @Override
    public String toString() {
        return String.format("[code=%d, message=%s]", code, message);
    }
}