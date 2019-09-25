package neu.edu.csye6225.assignment2.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonResult {
    private Integer state;
    private String msg;
    private Object data;
    public CommonResult(){
        this.state=200;
        this.msg="success";
    }
    public CommonResult(Integer state,String msg){
        this.state=state;
        this.msg=msg;
    }
    public CommonResult(Integer state,String msg,Object data){
        this.state=state;
        this.msg=msg;
        this.data=data;
    }
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static CommonResult buildError(Integer code, String msg) {
        return new CommonResult(code, null, msg);
    }

}
