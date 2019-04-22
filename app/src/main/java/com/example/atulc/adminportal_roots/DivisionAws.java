package com.example.atulc.adminportal_roots;

import java.util.List;

public class DivisionAws {
    private String division1Name;
    private String division1Sections;
    private String division1Years;
    private List<DivisionAws> divisionSub;

    public String getDivision1Sections() {
        return division1Sections;
    }

    public void setDivision1Sections(String division1Sections) {
        this.division1Sections = division1Sections;
    }

    public String getDivision1Years() {
        return division1Years;
    }

    public void setDivision1Years(String division1Years) {
        this.division1Years = division1Years;
    }

    public String getDivision1Name() {
        return division1Name;
    }

    public void setDivision1Name(String division1Name) {
        this.division1Name = division1Name;
    }

    public List<DivisionAws> getDivisionSub() {
        return divisionSub;
    }

    public void setDivisionSub(List<DivisionAws> divisionSub) {
        this.divisionSub = divisionSub;
    }
}