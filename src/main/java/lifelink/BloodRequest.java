package lifelink;

import java.time.LocalDate;

public class BloodRequest {
    private final String id, requesterName, bloodGroup, hospital, city, contact, urgency;
    private final LocalDate requiredDate;

    public BloodRequest(String id, String requesterName, String bloodGroup, String hospital,
                        String city, String contact, LocalDate requiredDate, String urgency) {
        this.id=id; this.requesterName=requesterName; this.bloodGroup=bloodGroup;
        this.hospital=hospital; this.city=city; this.contact=contact;
        this.requiredDate=requiredDate; this.urgency=urgency;
    }
    public String getId(){return id;}
    public String getRequesterName(){return requesterName;}
    public String getBloodGroup(){return bloodGroup;}
    public String getHospital(){return hospital;}
    public String getCity(){return city;}
    public String getContact(){return contact;}
    public LocalDate getRequiredDate(){return requiredDate;}
    public String getUrgency(){return urgency;}
}
