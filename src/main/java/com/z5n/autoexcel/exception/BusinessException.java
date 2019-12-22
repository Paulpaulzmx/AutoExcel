package com.z5n.autoexcel.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @program: autoexcel
 * @ClassName: BusinessException
 * @Description: BusinessException  业务异常类
 * @Author: chen qi
 * @Date: 2019/12/22 19:26
 * @Version: 1.0
 **/
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected String errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getResultCode());
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BusinessException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getResultCode(), cause);
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}