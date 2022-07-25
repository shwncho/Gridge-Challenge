package com.server.insta.config.response;

import com.server.insta.config.exception.BusinessExceptionStatus;
import com.server.insta.config.response.result.CommonResult;
import com.server.insta.config.response.result.MultipleResult;
import com.server.insta.config.response.result.SingleResult;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Getter
    public enum CommonResponse {
        SUCCESS( "SUCCESS","응답 성공"),
        FAIL("FAIL","응답 실패");

        String code;
        String message;

        CommonResponse(String code, String message){
            this.code=code;
            this.message=message;
        }
    }

    // 단일건 결과 리턴
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> singleResult = new SingleResult<>();
        singleResult.setData(data);
        setSuccessResult(singleResult);
        return singleResult;
    }

    // 복수건 결과 처리 메서드
    public <T> MultipleResult<T> getMultipleResult(List<T> data) {
        MultipleResult<T> result = new MultipleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 성공 결과만 처리
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    // 실패 결과만 처리
    public CommonResult getFailResult(BusinessExceptionStatus status) {
        CommonResult result = new CommonResult();
        setFailResult(result,status);
        return result;
    }

    public CommonResult getFailResult(String code, String message){
        CommonResult result = new CommonResult();
        setFailResult(result,code,message);
        return result;
    }

    // API 요청 성공 시 응답 모델을 성공 데이터로 세팅
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(CommonResult result, BusinessExceptionStatus status) {
        result.setSuccess(false);
        result.setCode(status.getCode());
        result.setMessage(status.getMessage());
    }

    private void setFailResult(CommonResult result, String code, String message) {
        result.setSuccess(false);
        result.setCode(code);
        result.setMessage(message);
    }
}
