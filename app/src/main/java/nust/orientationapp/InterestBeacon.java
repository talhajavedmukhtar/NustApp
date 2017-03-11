package nust.orientationapp;

/**
 * Created by Talha on 8/28/16.
 */
public class InterestBeacon {
    private String creatorId;
    private String interestName;
    private String interestType;
    private String description;
    private String interestId;

    public InterestBeacon(){}

    public InterestBeacon(String creatorId, String interestName, String interestType, String description) {
        this.creatorId = creatorId;
        this.interestName = interestName;
        this.interestType = interestType;
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }
}
