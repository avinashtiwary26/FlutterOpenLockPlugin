package com.example.flutterpluginupdate.api.response.key_status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KeyStatusResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

//    private Success success;
//
//    public Success getSuccess() {
//        return success;
//    }
//
//    public void setSuccess(Success success) {
//        this.success = success;
//    }

}