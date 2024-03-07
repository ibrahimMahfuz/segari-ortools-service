package id.segari.ortools.error;

import id.segari.ortools.exception.BaseException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class SegariRoutingErrors {
    public static BaseException emptyOrder() {
        return BaseException.builder()
                .message("orders tidak boleh kosong")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException indexOneNotSp() {
        return BaseException.builder()
                .message("index 1 must be sp type")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException indexZeroNotDummy() {
        return BaseException.builder()
                .message("index 1 must be dummy type")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException cannotUseAddDistanceBetweenOrderDimension() {
        return BaseException.builder()
                .message("cannot use addDistanceBetweenOrderDimension and addDistanceBetweenNodeDimension at the same time")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException cannotUseAddDistanceWithSpDimension() {
        return BaseException.builder()
                .message("cannot use addDistanceWithSpDimension and addDistanceBetweenNodeDimension at the same time")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException cannotUseAddDistanceBetweenNodeDimension() {
        return BaseException.builder()
                .message("cannot use addDistanceBetweenNodeDimension and addDistanceWithSpDimension or addDistanceBetweenOrderDimension at the same time")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException typeNotSupported() {
        return BaseException.builder()
                .message("SegariRoutingType not supported yet for this process")
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static BaseException invalidRoutingParameter(String parameterNames) {
        return BaseException.builder()
                .message("Invalid Routing Parameter: " + parameterNames)
                .errorCode("TODO")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }
}
