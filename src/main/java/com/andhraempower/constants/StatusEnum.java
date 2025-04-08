package com.andhraempower.constants;

public enum StatusEnum {
    NEW("New"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    WIP("Work in Progress"),
    WFD("Waiting For Sponsor"),
    COMPLETED("Completed"),
    OPEN("New"), // Open is temporary to support existing data
    DRAFT("Draft"),
    HOLD("Hold");

    private final String statusDescription;

    private StatusEnum(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }
}
