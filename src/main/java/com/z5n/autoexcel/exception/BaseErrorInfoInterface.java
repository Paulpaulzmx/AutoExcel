package com.z5n.autoexcel.exception;

/**
 * @program: autoexcel
 * @Interface: BaseErrorInfoInterface
 * @Description: BaseErrorInfoInterface
 * @Author: chen qi
 * @Date: 2019/12/22 19:22
 * @Version: 1.0
 **/
public interface BaseErrorInfoInterface {

    /**
     *  错误码
     * @return
     */
    String getResultCode();

    /**
     * 错误信息
     * @return
     */
    String getResultMsg();
}
