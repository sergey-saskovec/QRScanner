package com.codescannerqr.generator.model;

import java.util.List;

public class Section {

    private final String sectionName;
    private List<SectionList> sectionList;

    public Section(String sectionName, List<SectionList> sectionList) {
        this.sectionName = sectionName;
        this.sectionList = sectionList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public List<SectionList> getSectionList() {
        return sectionList;
    }
}
