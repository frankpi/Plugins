package com.tapjoy;


public interface TapjoyVideoNotifier {
    void videoComplete();

    void videoError(int arg1);

    void videoStart();
}

