package com.tspolice.htplive.models;

import com.google.gson.annotations.SerializedName;

public class DistanceMatrix {

    @SerializedName("destination_addresses")
    private String[] destinationAddresses;

    @SerializedName("origin_addresses")
    private String[] originAddresses;

    private Rows[] rows;

    private String status;

    public String[] getDestinationAddresses() {
        return destinationAddresses;
    }

    public String[] getOriginAddresses() {
        return originAddresses;
    }

    public Rows[] getRows() {
        return rows;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ClassPojo [destination_addresses = " + destinationAddresses + ", origin_addresses = " + originAddresses
                + ", rows = " + rows + ", status = " + status +"]";
    }
}
