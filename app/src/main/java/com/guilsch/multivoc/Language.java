package com.guilsch.multivoc;

public class Language {

    private String name;
    private String isoName;
    private int flagAddress;

    public Language(String name, String isoName, int flagAddress) {
        this.name = name;
        this.isoName = isoName;
        this.flagAddress = flagAddress;
    }

    public String getName() {
        return name;
    }

    public String getIsoName() {
        return isoName;
    }

    public int getFlagAddress() {
        return flagAddress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public void setFlagAddress(int flagAddress) {
        this.flagAddress = flagAddress;
    }
}
