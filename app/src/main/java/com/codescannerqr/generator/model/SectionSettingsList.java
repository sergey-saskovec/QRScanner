package com.codescannerqr.generator.model;

public class SectionSettingsList {

    private int name;
    private final int logo;
    private boolean switchBools;
    private int image;
    private final boolean visibleImage;
    private boolean visibleSwitch;

    public SectionSettingsList(int name, int logo, boolean switchBools, boolean visibleImage,
                               boolean visibleSwitch) {
        this.name = name;
        this.logo = logo;
        this.switchBools = switchBools;
        this.visibleImage = visibleImage;
        this.visibleSwitch = visibleSwitch;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getLogo() {
        return logo;
    }

    public boolean isSwitchBools() {
        return switchBools;
    }

    public void setSwitchBools(boolean switchBools){
        this.switchBools = switchBools;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isVisibleImage() {
        return visibleImage;
    }

    public boolean isVisibleSwitch() {
        return visibleSwitch;
    }

}
