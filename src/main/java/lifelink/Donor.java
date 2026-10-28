package lifelink;

import java.time.LocalDate;

public class Donor {
    private String id, name, gender, bloodGroup, mobile, city;
    private int age;
    private LocalDate lastDonationDate;
    private boolean available, contactConsent;

    public Donor(String id, String name, int age, String gender, String bloodGroup,
                 String mobile, String city, LocalDate lastDonationDate,
                 boolean available, boolean contactConsent) {
        this.id = id; this.name = name; this.age = age; this.gender = gender;
        this.bloodGroup = bloodGroup; this.mobile = mobile; this.city = city;
        this.lastDonationDate = lastDonationDate; this.available = available;
        this.contactConsent = contactConsent;
    }
    public String getId(){ return id; }
    public String getName(){ return name; }
    public int getAge(){ return age; }
    public String getGender(){ return gender; }
    public String getBloodGroup(){ return bloodGroup; }
    public String getMobile(){ return mobile; }
    public String getCity(){ return city; }
    public LocalDate getLastDonationDate(){ return lastDonationDate; }
    public boolean isAvailable(){ return available; }
    public boolean isContactConsent(){ return contactConsent; }
    public String getAvailabilityText(){ return available ? "Available" : "Unavailable"; }
    public void updateFrom(Donor d) {
        name=d.name; age=d.age; gender=d.gender; bloodGroup=d.bloodGroup; mobile=d.mobile;
        city=d.city; lastDonationDate=d.lastDonationDate; available=d.available;
        contactConsent=d.contactConsent;
    }
}
