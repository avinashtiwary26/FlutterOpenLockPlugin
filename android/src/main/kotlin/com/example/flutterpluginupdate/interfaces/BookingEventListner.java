package com.example.flutterpluginupdate.interfaces;

import com.example.flutterpluginupdate.api.response.session.SessionResponse;

public interface BookingEventListner {
    void getBooking(boolean isBookingFound, SessionResponse sessionResponse);
}
