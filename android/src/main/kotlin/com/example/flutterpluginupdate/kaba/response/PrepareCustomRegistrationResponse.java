package com.example.flutterpluginupdate.kaba.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrepareCustomRegistrationResponse {

    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("token")
    @Expose
    private String token;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}