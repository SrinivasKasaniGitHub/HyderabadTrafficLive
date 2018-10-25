package com.tspolice.htplive.adapters;

import android.location.Address;

public class GeoSearchResult {

    private Address address;

    GeoSearchResult(Address address) {
        this.address = address;
    }

    public String getAddress() {
        String display_address = "";
        display_address += address.getAddressLine(0) + "\n";
        StringBuilder display_addressBuilder = new StringBuilder(display_address);
        for (int i = 1; i < address.getMaxAddressLineIndex(); i++) {
            display_addressBuilder.append(address.getAddressLine(i)).append(", ");
        }
        display_address = display_addressBuilder.toString();
        display_address = display_address.substring(0, display_address.length() - 2);
        return display_address;
    }

    public String toString() {
        String display_address = "";
        if (address.getFeatureName() != null) {
            display_address += address + ", ";
        }
        StringBuilder display_addressBuilder = new StringBuilder(display_address);
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            display_addressBuilder.append(address.getAddressLine(i));
        }
        display_address = display_addressBuilder.toString();
        return display_address;
    }
}
