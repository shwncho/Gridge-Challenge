package com.server.insta.config.response;

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
        SUCCESS( "응답 성공"),
        FAIL("응답 실패");

        String message;

        CommonResponse(String message) {
            this.message = message;
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
    public CommonResult getFailResult(String message) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
        setFailResult(result, message);
        return result;
    }

    // API 요청 성공 시 응답 모델을 성공 데이터로 세팅
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setMessage(CommonResponse.SUCCESS.getMessage());
    }

    // API 요청 실패 시 응답 모델을 실패 데이터로 세팅
    private void setFailResult(CommonResult result, String message) {
        result.setSuccess(false);
        result.setMessage(message);
    }
}
