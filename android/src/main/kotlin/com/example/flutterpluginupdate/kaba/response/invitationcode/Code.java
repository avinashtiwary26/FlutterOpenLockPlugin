package com.example.flutterpluginupdate.kaba.response.invitationcode;

import com.example.flutterpluginupdate.kaba.response.PrepareCustomRegistrationResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Code {

    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("prepareCustomRegistrationResponse")
    @Expose
    private PrepareCustomRegistrationResponse prepareCustomRegistrationResponse;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public PrepareCustomRegistrationResponse getPrepareCustomRegistrationResponse() {
        return prepareCustomRegistrationResponse;
    }

    public void setPrepareCustomRegistrationResponse(PrepareCustomRegistrationResponse prepareCustomRegistrationResponse) {
        this.prepareCustomRegistrationResponse = prepareCustomRegistrationResponse;
    }

}