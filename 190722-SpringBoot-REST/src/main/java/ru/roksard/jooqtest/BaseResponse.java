package ru.roksard.jooqtest;

public class BaseResponse {

	public static final String SUCCESS_STATUS = "success";
	public static final String ERROR_STATUS = "error";
	public static final int CODE_SUCCESS = 100;
	public static final int CODE_ERROR = 400;
	
    private final String status;
    private final Integer code;
    
    public BaseResponse(String status, Integer code) {
        this.status = status;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }
}
