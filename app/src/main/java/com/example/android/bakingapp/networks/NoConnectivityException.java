package com.example.android.bakingapp.networks;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "No connectivity";
    }
}
