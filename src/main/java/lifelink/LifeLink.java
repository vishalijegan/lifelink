package lifelink;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LifeLink extends Application {
    private FileStorage storage;
    private final ObservableList<Donor> donors=FXCollections.observableArrayList();
    private final ObservableList<BloodRequest> requests=FXCollections.observableArrayList();
    private BorderPane root;
    private VBox sidebar;
    private Label donorCount, requestCount;
    private static final String RED="#C6283D", DARK="#182B3A", PINK="#FDECEF", BG="#F5F7FA", MUTED="#64748B", GREEN="#16856B";
    private static final String[] GROUPS={"A+","A-","B+","B-","AB+","AB-","O+","O-"};

    @Override public void start(Stage stage) {
        try { storage=new FileStorage(); donors.setAll(storage.loadDonors()); requests.setAll(storage.loadRequests()); }
        catch(IOException e){ alert(Alert.AlertType.ERROR,"File error","Could not open CSV data files:\n"+e.getMessage()); return; }
        root=new BorderPane(); root.setStyle("-fx-background-color:"+BG+";");
        sidebar=new VBox(10); sidebar.setPadding(new Insets(28,16,20,16)); sidebar.setPrefWidth(225);
        sidebar.setStyle("-fx-background-color:"+DARK+";");
        Label logo=new Label("♥  LifeLink"); logo.setStyle("-fx-text-fill:white;-fx-font-size:27px;-fx-font-weight:bold;");
        Label sub=new Label("GIVE BLOOD. GIVE LIFE."); sub.setStyle("-fx-text-fill:#F9B8C4;-fx-font-size:10px;-fx-font-weight:bold;-fx-letter-spacing:1px;");
        sidebar.getChildren().addAll(logo,sub,new Separator());
        nav("⌂   Dashboard",()->showDashboard()); nav("＋   Become a Donor",()->showDonorForm(null));
        nav("⌕   Find Blood Donors",()->showFindDonors()); nav("♥   Blood Requests",()->showRequests());
        nav("▤   Donor Directory",()->showDirectory());
        Label note=new Label("ONE DONATION CAN\nSAVE UP TO THREE LIVES\n\nLifeLink • Community first"); note.setWrapText(true);
        note.setStyle("-fx-text-fill:#E9BFC7;-fx-font-size:11px;"); VBox.setVgrow(note,Priority.ALWAYS); sidebar.getChildren().add(note);
        root.setLeft(sidebar); showDashboard();
        Scene scene=new Scene(root,1120,720);
        stage.setTitle("LifeLink – Blood Donor Finder"); stage.setMinWidth(900); stage.setMinHeight(620);
        stage.setScene(scene); stage.show();
    }
    private void nav(String text,Runnable action){
        Button b=new Button(text); b.setMaxWidth(Double.MAX_VALUE); b.setAlignment(Pos.CENTER_LEFT);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:#F7E9EC;-fx-font-size:13px;-fx-padding:13 10;-fx-background-radius:8;-fx-cursor:hand;");
        b.setOnAction(e->action.run()); sidebar.getChildren().add(b);
    }
    private VBox page(String title,String subtitle){
        VBox box=new VBox(18); box.setPadding(new Insets(30,34,30,34)); Label h=new Label(title);
        h.setStyle("-fx-font-size:29px;-fx-font-weight:bold;-fx-text-fill:"+DARK+";");
        Label s=new Label(subtitle); s.setStyle("-fx-font-size:13px;-fx-text-fill:"+MUTED+";");
        box.getChildren().addAll(h,s); root.setCenter(box); return box;
    }
    private Label metric(String title, String value){
        Label l=new Label(title+"\n\n"+value); l.setMinWidth(175); l.setMaxWidth(Double.MAX_VALUE); l.setPadding(new Insets(20));
        l.setStyle("-fx-background-color:white;-fx-background-radius:14;-fx-border-color:#E6EAF0;-fx-border-radius:14;-fx-font-size:15px;-fx-font-weight:bold;-fx-text-fill:"+DARK+";");
        return l;
    }
    private void showDashboard(){
        VBox p=page("Every drop counts.","Welcome to LifeLink — helping people connect with willing blood donors in their community.");
        VBox hero=new VBox(12); hero.setPadding(new Insets(26));
        hero.setStyle("-fx-background-color:linear-gradient(to right,#C6283D,#8F1730);-fx-background-radius:18;");
        Label heroTitle=new Label("Be someone's reason to live."); heroTitle.setStyle("-fx-font-size:25px;-fx-font-weight:bold;-fx-text-fill:white;");
        Label heroText=new Label("Register as a donor today. When someone needs blood, your willingness to help can make all the difference."); heroText.setWrapText(true); heroText.setStyle("-fx-font-size:14px;-fx-text-fill:#FFF0F2;");
        Button pledge=primary("♥  I want to donate",()->showDonorForm(null));
        Button find=secondary("Find a donor",()->showFindDonors());
        hero.getChildren().addAll(heroTitle,heroText,new HBox(10,pledge,find));
        long available=donors.stream().filter(Donor::isAvailable).count();
        HBox cards=new HBox(12); cards.setAlignment(Pos.CENTER_LEFT);
        cards.getChildren().addAll(metric("Registered donors",String.valueOf(donors.size())),metric("Available now",String.valueOf(available)),metric("Community requests",String.valueOf(requests.size())),metric("Lives matter", "Every donation"));
        Label tipTitle=new Label("Small act. Life-changing impact."); tipTitle.setStyle("-fx-font-size:19px;-fx-font-weight:bold;-fx-text-fill:"+DARK+";");
        HBox tips=new HBox(12); tips.getChildren().addAll(tipCard("01", "Check eligibility", "Adults who meet local health criteria may be able to donate. A medical professional will confirm eligibility."),tipCard("02", "Keep your details updated", "Update your city and availability so people can find accurate donor information."),tipCard("03", "Share the cause", "Encourage friends and family to register and help build a stronger donor community."));
        Label safety=new Label("Emergency? Contact a hospital or local emergency service directly. LifeLink is a community directory and does not verify medical eligibility or guarantee donor availability.");
        safety.setWrapText(true); safety.setPadding(new Insets(14)); safety.setStyle("-fx-background-color:#FFF4DB;-fx-background-radius:10;-fx-text-fill:#79551B;-fx-font-size:12px;");
        p.getChildren().addAll(hero,cards,tipTitle,tips,safety);
    }
    private VBox tipCard(String number,String title,String body){
        VBox card=new VBox(8); card.setPadding(new Insets(16)); card.setPrefWidth(250); card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle("-fx-background-color:white;-fx-background-radius:13;-fx-border-color:#E6EAF0;-fx-border-radius:13;");
        Label n=new Label(number); n.setStyle("-fx-text-fill:"+RED+";-fx-font-size:12px;-fx-font-weight:bold;");
        Label h=new Label(title); h.setStyle("-fx-text-fill:"+DARK+";-fx-font-size:14px;-fx-font-weight:bold;");
        Label b=new Label(body); b.setWrapText(true); b.setStyle("-fx-text-fill:"+MUTED+";-fx-font-size:12px;"); card.getChildren().addAll(n,h,b); HBox.setHgrow(card,Priority.ALWAYS); return card;
    }
    private Button primary(String text,Runnable action){
        Button b=new Button(text); b.setStyle("-fx-background-color:"+RED+";-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:11 17;-fx-background-radius:8;-fx-cursor:hand;");
        b.setOnAction(e->action.run()); return b;
    }
    private TextField field(String prompt){TextField t=new TextField();t.setPromptText(prompt);return t;}
    private ComboBox<String> combo(String... items){ComboBox<String> c=new ComboBox<>(FXCollections.observableArrayList(items));c.setMaxWidth(Double.MAX_VALUE);c.setPromptText("Select");return c;}
    private void showDonorForm(Donor existing){
        VBox p=page(existing==null?"Donor Registration":"Edit Donor","Fields marked by the form are required. Contact details are shown only with donor consent.");
        GridPane g=new GridPane();g.setHgap(14);g.setVgap(12);g.setPadding(new Insets(10));
        TextField name=field("Full name"),age=field("Age"),mobile=field("10-digit mobile number"),city=field("City");
        ComboBox<String> gender=combo("Female","Male","Non-binary","Prefer not to say"),blood=combo(GROUPS),availability=combo("Available","Unavailable");
        DatePicker last=new DatePicker();last.setPromptText("Last donation date (optional)");
        CheckBox consent=new CheckBox("I consent to my mobile number being shown to people searching for donors.");
        add(g,0,"Full Name",name);add(g,1,"Age",age);add(g,2,"Gender",gender);add(g,3,"Blood Group",blood);
        add(g,4,"Mobile Number",mobile);add(g,5,"City",city);add(g,6,"Last Donation Date",last);add(g,7,"Availability",availability);
        g.add(consent,1,8,2,1);
        if(existing!=null){name.setText(existing.getName());age.setText(""+existing.getAge());gender.setValue(existing.getGender());blood.setValue(existing.getBloodGroup());mobile.setText(existing.getMobile());city.setText(existing.getCity());last.setValue(existing.getLastDonationDate());availability.setValue(existing.getAvailabilityText());consent.setSelected(existing.isContactConsent());}
        Button save=primary(existing==null?"Save Donor":"Update Donor",()->{
            String error=null; int a=0;
            try{a=Integer.parseInt(age.getText().trim());}catch(Exception ex){error="Age must be a number.";}
            if(error==null&&(name.getText().isBlank()||city.getText().isBlank()||mobile.getText().isBlank()||gender.getValue()==null||blood.getValue()==null||availability.getValue()==null))error="Please complete all required fields.";
            if(error==null&&(a<18||a>100))error="Donors must be aged 18–100 for this demo directory.";
            if(error==null&&!mobile.getText().trim().matches("[0-9+() -]{7,18}"))error="Enter a valid contact number.";
            if(error!=null){alert(Alert.AlertType.WARNING,"Check form",error);return;}
            Donor d=new Donor(existing==null?UUID.randomUUID().toString():existing.getId(),name.getText().trim(),a,gender.getValue(),blood.getValue(),
                mobile.getText().trim(),city.getText().trim(),last.getValue(),"Available".equals(availability.getValue()),consent.isSelected());
            if(existing==null)donors.add(d);else existing.updateFrom(d);
            persistDonors(); showDirectory();
        });
        Button cancel=secondary("Cancel",()->showDirectory()); HBox buttons=new HBox(10,save,cancel);
        p.getChildren().addAll(g,buttons);
    }
    private void add(GridPane g,int row,String label,Control c){g.add(new Label(label),0,row);c.setMaxWidth(Double.MAX_VALUE);g.add(c,1,row);}
    private Button secondary(String text,Runnable action){Button b=new Button(text);b.setStyle("-fx-background-color:white;-fx-text-fill:"+DARK+";-fx-font-weight:bold;-fx-padding:11 16;-fx-background-radius:8;-fx-border-color:#DDE3EA;-fx-border-radius:8;-fx-cursor:hand;");b.setOnAction(e->action.run());return b;}
    private void showFindDonors(){
        VBox p=page("Find Blood Donors","Search by blood group and city. Contact details are displayed only when the donor has consented.");
        ComboBox<String> blood=combo(GROUPS);TextField city=field("City (optional)");Button search=primary("Search",()->{});
        TableView<Donor> table=donorTable(true);
        Runnable apply=()->{String bg=blood.getValue(),ct=city.getText().trim().toLowerCase(Locale.ROOT);
            List<Donor> found=donors.stream().filter(d->d.isAvailable()).filter(d->bg==null||d.getBloodGroup().equals(bg))
                .filter(d->ct.isEmpty()||d.getCity().toLowerCase(Locale.ROOT).contains(ct)).collect(Collectors.toList());
            table.getItems().setAll(found);
            if(found.isEmpty())alert(Alert.AlertType.INFORMATION,"No donors found","No available donors match those filters. Try another city or blood group.");
        };
        search.setOnAction(e->apply.run());city.setOnAction(e->apply.run());
        HBox filters=new HBox(10,blood,city,search);filters.setAlignment(Pos.CENTER_LEFT);p.getChildren().addAll(filters,table);
        VBox.setVgrow(table,Priority.ALWAYS);
    }
    private TableView<Donor> donorTable(boolean hideUnconsented){
        TableView<Donor> t=new TableView<>();t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);t.setStyle("-fx-background-color:white;-fx-background-radius:10;-fx-border-color:#E6EAF0;-fx-border-radius:10;");
        t.getColumns().add(col("Name",d->d.getName()));t.getColumns().add(col("Blood Group",d->d.getBloodGroup()));
        t.getColumns().add(col("City",d->d.getCity()));t.getColumns().add(col("Availability",Donor::getAvailabilityText));
        t.getColumns().add(col("Contact",d->(!hideUnconsented||d.isContactConsent())?d.getMobile():"Not shared"));
        return t;
    }
    private <T> TableColumn<T,String> col(String title,java.util.function.Function<T,String> f){
        TableColumn<T,String> c=new TableColumn<>(title);c.setCellValueFactory(v->new SimpleStringProperty(Objects.toString(f.apply(v.getValue()),"—")));return c;
    }
    private void showDirectory(){
        VBox p=page("Donor Directory","Browse, search, edit or remove registered donor records.");
        TextField query=field("Search by name, city, blood group or mobile");TableView<Donor> table=donorTable(false);
        Runnable filter=()->{String q=query.getText().toLowerCase(Locale.ROOT);table.getItems().setAll(donors.filtered(d->
            d.getName().toLowerCase(Locale.ROOT).contains(q)||d.getCity().toLowerCase(Locale.ROOT).contains(q)||
            d.getBloodGroup().toLowerCase(Locale.ROOT).contains(q)||d.getMobile().contains(q)));};
        query.textProperty().addListener((o,a,b)->filter.run());filter.run();
        Button edit=primary("Edit Selected",()->{Donor d=table.getSelectionModel().getSelectedItem();if(d==null)alert(Alert.AlertType.WARNING,"Select donor","Choose a donor first.");else showDonorForm(d);});
        Button delete=secondary("Delete Selected",()->{Donor d=table.getSelectionModel().getSelectedItem();if(d==null){alert(Alert.AlertType.WARNING,"Select donor","Choose a donor first.");return;}
            if(confirm("Delete donor","Delete "+d.getName()+" from the directory?")){donors.remove(d);persistDonors();showDirectory();}});
        Button add=primary("Add Donor",()->showDonorForm(null));p.getChildren().addAll(query,table,new HBox(10,add,edit,delete));VBox.setVgrow(table,Priority.ALWAYS);
    }
    private void showRequests(){
        VBox p=page("Emergency Blood Requests","Review saved requests. Requests are stored locally in requests.csv.");
        TableView<BloodRequest> t=new TableView<>();t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);t.setStyle("-fx-background-color:white;-fx-background-radius:10;-fx-border-color:#E6EAF0;-fx-border-radius:10;");
        t.getColumns().add(col("Requester",BloodRequest::getRequesterName));t.getColumns().add(col("Blood Group",BloodRequest::getBloodGroup));
        t.getColumns().add(col("Hospital",BloodRequest::getHospital));t.getColumns().add(col("City",BloodRequest::getCity));
        t.getColumns().add(col("Contact",BloodRequest::getContact));t.getColumns().add(col("Required Date",r->r.getRequiredDate().toString()));
        t.getColumns().add(col("Urgency",BloodRequest::getUrgency));t.getItems().setAll(requests);
        Button add=primary("New Request",()->showRequestForm());
        Button delete=secondary("Delete Selected",()->{BloodRequest r=t.getSelectionModel().getSelectedItem();if(r==null){alert(Alert.AlertType.WARNING,"Select request","Choose a request first.");return;}
            if(confirm("Delete request","Delete this blood request?")){requests.remove(r);persistRequests();showRequests();}});
        p.getChildren().addAll(new HBox(10,add,delete),t);VBox.setVgrow(t,Priority.ALWAYS);
    }
    private void showRequestForm(){
        VBox p=page("Emergency Blood Request","Submit a request. In a real emergency, contact local emergency services and hospitals directly.");
        GridPane g=new GridPane();g.setHgap(14);g.setVgap(12);TextField name=field("Requester name"),hospital=field("Hospital name"),city=field("City"),contact=field("Contact number");
        ComboBox<String> blood=combo(GROUPS),urgency=combo("Critical","High","Normal");DatePicker date=new DatePicker(LocalDate.now());
        add(g,0,"Requester Name",name);add(g,1,"Required Blood Group",blood);add(g,2,"Hospital Name",hospital);add(g,3,"City",city);
        add(g,4,"Contact Number",contact);add(g,5,"Required Date",date);add(g,6,"Urgency",urgency);
        Button save=primary("Submit Request",()->{
            if(name.getText().isBlank()||hospital.getText().isBlank()||city.getText().isBlank()||contact.getText().isBlank()||blood.getValue()==null||urgency.getValue()==null||date.getValue()==null){
                alert(Alert.AlertType.WARNING,"Check form","Please complete every field.");return;}
            if(!contact.getText().trim().matches("[0-9+() -]{7,18}")){alert(Alert.AlertType.WARNING,"Check contact","Enter a valid contact number.");return;}
            if(date.getValue().isBefore(LocalDate.now())){alert(Alert.AlertType.WARNING,"Check date","Required date cannot be in the past.");return;}
            requests.add(new BloodRequest(UUID.randomUUID().toString(),name.getText().trim(),blood.getValue(),hospital.getText().trim(),city.getText().trim(),contact.getText().trim(),date.getValue(),urgency.getValue()));
            persistRequests();showRequests();
        });
        p.getChildren().addAll(g,save);
    }
    private boolean confirm(String title,String message){Alert a=new Alert(Alert.AlertType.CONFIRMATION,message,ButtonType.YES,ButtonType.NO);a.setTitle(title);a.setHeaderText(null);return a.showAndWait().orElse(ButtonType.NO)==ButtonType.YES;}
    private void alert(Alert.AlertType type,String title,String message){Alert a=new Alert(type,message);a.setTitle(title);a.setHeaderText(null);a.showAndWait();}
    private void persistDonors(){try{storage.saveDonors(new ArrayList<>(donors));}catch(IOException e){alert(Alert.AlertType.ERROR,"Save failed",e.getMessage());}refreshCounts();}
    private void persistRequests(){try{storage.saveRequests(new ArrayList<>(requests));}catch(IOException e){alert(Alert.AlertType.ERROR,"Save failed",e.getMessage());}refreshCounts();}
    private void refreshCounts(){if(donorCount!=null)donorCount.setText("Registered Donors\n"+donors.size());if(requestCount!=null)requestCount.setText("Blood Requests\n"+requests.size());}
    @Override public void stop(){if(storage!=null){persistQuietly();}}
    private void persistQuietly(){try{storage.saveDonors(new ArrayList<>(donors));storage.saveRequests(new ArrayList<>(requests));}catch(IOException e){System.err.println("Could not save data: "+e.getMessage());}}
    public static void main(String[] args){launch(args);}
}
