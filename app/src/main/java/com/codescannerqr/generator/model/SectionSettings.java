package com.codescannerqr.generator.model;

import java.util.List;

public class SectionSettings {

    private final String sectionName;
    private List<SectionSettingsList> sectionList;

    public SectionSettings(String sectionName, List<SectionSettingsList> sectionList) {
        this.sectionName = sectionName;
        this.sectionList = sectionList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public List<SectionSettingsList> getSectionList() {
        return sectionList;
    }

}
