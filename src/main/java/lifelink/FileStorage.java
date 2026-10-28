package lifelink;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class FileStorage {
    private final Path dataDir = Paths.get("data");
    private final Path donorsFile = dataDir.resolve("donors.csv");
    private final Path requestsFile = dataDir.resolve("requests.csv");

    public FileStorage() throws IOException {
        Files.createDirectories(dataDir);
        if (Files.notExists(donorsFile)) Files.writeString(donorsFile,
            "id,name,age,gender,bloodGroup,mobile,city,lastDonationDate,available,contactConsent\n", StandardCharsets.UTF_8);
        if (Files.notExists(requestsFile)) Files.writeString(requestsFile,
            "id,requesterName,bloodGroup,hospital,city,contact,requiredDate,urgency\n", StandardCharsets.UTF_8);
    }
    private String esc(String s) { return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\"\""); }
    private String csv(String s) { return "\"" + esc(s) + "\""; }

    // Small CSV reader supporting quoted commas and escaped quotes.
    private List<String> parse(String line) {
        List<String> out = new ArrayList<>(); StringBuilder b = new StringBuilder(); boolean quoted = false;
        for (int i=0;i<line.length();i++) {
            char c=line.charAt(i);
            if (c=='"') {
                if (quoted && i+1<line.length() && line.charAt(i+1)=='"') { b.append('"'); i++; }
                else quoted=!quoted;
            } else if (c==',' && !quoted) { out.add(b.toString()); b.setLength(0); }
            else b.append(c);
        }
        out.add(b.toString()); return out;
    }
    private String row(String... fields) {
        StringJoiner j = new StringJoiner(",");
        for (String f: fields) j.add(csv(f));
        return j.toString();
    }
    public List<Donor> loadDonors() throws IOException {
        List<Donor> list = new ArrayList<>(); List<String> lines=Files.readAllLines(donorsFile, StandardCharsets.UTF_8);
        for (int i=1;i<lines.size();i++) {
            if(lines.get(i).isBlank()) continue;
            try {
                List<String> v=parse(lines.get(i));
                list.add(new Donor(v.get(0),v.get(1),Integer.parseInt(v.get(2)),v.get(3),v.get(4),
                    v.get(5),v.get(6),v.get(7).isBlank()?null:LocalDate.parse(v.get(7)),
                    Boolean.parseBoolean(v.get(8)),Boolean.parseBoolean(v.get(9))));
            } catch (RuntimeException ex) { System.err.println("Skipping invalid donor CSV row " + (i+1)); }
        }
        return list;
    }
    public List<BloodRequest> loadRequests() throws IOException {
        List<BloodRequest> list=new ArrayList<>(); List<String> lines=Files.readAllLines(requestsFile,StandardCharsets.UTF_8);
        for(int i=1;i<lines.size();i++){
            if(lines.get(i).isBlank()) continue;
            try {
                List<String> v=parse(lines.get(i));
                list.add(new BloodRequest(v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),v.get(5),
                    LocalDate.parse(v.get(6)),v.get(7)));
            } catch(RuntimeException ex){ System.err.println("Skipping invalid request CSV row " + (i+1)); }
        }
        return list;
    }
    public void saveDonors(List<Donor> donors) throws IOException {
        List<String> lines=new ArrayList<>(); lines.add("id,name,age,gender,bloodGroup,mobile,city,lastDonationDate,available,contactConsent");
        for(Donor d:donors) lines.add(row(d.getId(),d.getName(),String.valueOf(d.getAge()),d.getGender(),d.getBloodGroup(),
            d.getMobile(),d.getCity(),d.getLastDonationDate()==null?"":d.getLastDonationDate().toString(),
            String.valueOf(d.isAvailable()),String.valueOf(d.isContactConsent())));
        Files.write(donorsFile,lines,StandardCharsets.UTF_8);
    }
    public void saveRequests(List<BloodRequest> requests) throws IOException {
        List<String> lines=new ArrayList<>(); lines.add("id,requesterName,bloodGroup,hospital,city,contact,requiredDate,urgency");
        for(BloodRequest r:requests) lines.add(row(r.getId(),r.getRequesterName(),r.getBloodGroup(),r.getHospital(),
            r.getCity(),r.getContact(),r.getRequiredDate().toString(),r.getUrgency()));
        Files.write(requestsFile,lines,StandardCharsets.UTF_8);
    }
}
