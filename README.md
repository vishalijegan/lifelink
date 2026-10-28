# LifeLink – Blood Donor Finder

A beginner-friendly Java 21 + JavaFX desktop mini project. Donor and emergency request records are stored in local CSV files; no database, internet, or external API is used.

## Features
- Modern dashboard with a donation-focused hero section, community statistics and donor encouragement cards
- Quick actions to register as a donor or find a donor
- Donor registration with validation, edit, delete and consent for contact sharing
- Search available donors by blood group and city
- Donor directory with live text search
- Emergency blood requests with required date and urgency
- CSV files created automatically under `data/`
- Clean modern UI with a dark navy sidebar, red donation call-to-action, soft neutral backgrounds and rounded cards
- Donation awareness tips and clear emergency-use disclaimer

> **Important:** This is an educational directory only. It does not medically verify blood compatibility, donor eligibility, or availability. In a real emergency, contact a hospital or local emergency services directly. Only use fictional/demo data unless you have appropriate consent and safeguards.

## Folder structure
```text
LifeLink/
├── README.md
├── run.bat
├── compile.bat
├── .vscode/
│   └── settings.json
└── src/
    └── main/
        └── java/
            └── lifelink/
                ├── LifeLink.java
                ├── Donor.java
                ├── BloodRequest.java
                └── FileStorage.java
```
When first run, `data/donors.csv` and `data/requests.csv` are created in the current working directory.

## Requirements (Windows)
- JDK 21 (64-bit)
- JavaFX SDK 21 (Windows x64)
- VS Code with Extension Pack for Java, or IntelliJ IDEA

Download a JavaFX SDK 21 build from the official Gluon JavaFX downloads page. Extract it somewhere stable, for example:
`C:\javafx-sdk-21`

The required library path is:
`C:\javafx-sdk-21\lib`

## Configure JavaFX path
Open `compile.bat` and `run.bat`. If your JavaFX SDK is not in `C:\javafx-sdk-21`, update `FX_LIB` to your actual `lib` folder.

Check JDK installation in Command Prompt:
```bat
java -version
javac -version
```
Both should report version 21.

## Compile and run (Windows CMD)
Open Command Prompt in the `LifeLink` project folder.

Compile:
```bat
compile.bat
```
Run:
```bat
run.bat
```
Or run directly after compiling:
```bat
java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls -cp out lifelink.LifeLink
```

## IntelliJ IDEA
1. Open the `LifeLink` folder as a project.
2. Set Project SDK to JDK 21.
3. Open **Run > Edit Configurations** and add a Java Application configuration:
   - Main class: `lifelink.LifeLink`
   - Working directory: the `LifeLink` project folder
   - VM options: `--module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls`
4. Before running, compile the Java files with the command above or configure IntelliJ's JavaFX library to point to the SDK `lib` folder.

## VS Code
1. Install **Extension Pack for Java**.
2. Open the project folder.
3. Update the JavaFX SDK path in `.vscode/settings.json` if necessary.
4. Use the included `compile.bat` and `run.bat` for the most predictable setup.

## Classes explained
- `LifeLink.java`: JavaFX application, pages, form validation, navigation, tables and actions.
- `Donor.java`: object model for one donor record.
- `BloodRequest.java`: object model for one emergency request.
- `FileStorage.java`: creates CSV files and reads/writes records with Java file handling.

## Project abstract
LifeLink is a desktop-based blood donor directory designed to help users find registered donors by blood group and city. It allows donor registration, maintains a directory, and records emergency blood requests. JavaFX provides the graphical interface, while CSV files provide persistent local storage without a database.

## Objectives
1. Build a user-friendly desktop interface using JavaFX.
2. Register and manage donor details.
3. Search available donors using blood group and city.
4. Record and manage emergency requests.
5. Practice object-oriented programming and file handling in Java.

## Modules
1. Home dashboard
2. Donor registration
3. Find blood donors
4. Emergency blood requests
5. Donor directory

## Software requirements
- Windows 10/11
- Java Development Kit 21
- JavaFX SDK 21
- VS Code or IntelliJ IDEA

## Advantages
- No database server required
- Simple CSV storage that can be inspected with a spreadsheet editor
- Persistent local data
- Clear GUI and modular classes

## Limitations
- Local files are not encrypted and are accessible to anyone with access to the project folder.
- No multi-user or network synchronization.
- Contact consent is a basic demo control, not a complete privacy or access-control system.
- Does not medically verify blood compatibility, donor eligibility, or real-time availability.
- CSV is suitable for a small educational project, not a production service.

## Future enhancements
- Add authentication and role-based access
- Encrypt or securely manage personal information
- Add export/import and backup features
- Add unit tests and stronger CSV validation
- Add consent withdrawal and contact visibility controls
- Add multilingual UI and accessibility improvements

## Common errors
**`java` or `javac` is not recognized**  
Install JDK 21 and add its `bin` directory to PATH, then open a new CMD window.

**JavaFX runtime components are missing**  
Check that `FX_LIB` points to the JavaFX SDK's `lib` directory and that `--module-path` is passed to Java.

**`module not found: javafx.controls`**  
Use the JavaFX SDK for Windows and verify the folder contains JavaFX `.jar` files.

**Application opens but CSV files seem missing**  
The `data` folder is created relative to the process working directory. Run from the project folder.

**CSV row skipped**  
Inspect the row in the CSV file. Invalid or incomplete records are skipped and a message is written to the console.

## Viva questions and answers
1. **What is JavaFX?** A Java framework for building desktop GUI applications.
2. **Why use CSV?** It is a simple text-based format for storing structured records without a database.
3. **What is OOP?** Object-oriented programming organizes code around objects containing data and behavior.
4. **What is encapsulation?** Keeping fields private and exposing controlled methods to access data.
5. **What does `FileStorage` do?** It reads and writes donor and request records to CSV files.
6. **What is a `TableView`?** A JavaFX control for displaying rows and columns of data.
7. **Why use `ObservableList`?** It is a JavaFX collection that can notify UI controls when data changes.
8. **What is input validation?** Checking user input before saving it.
9. **What is a `UUID` used for?** Creating a unique identifier for each record.
10. **How is data persistent?** The app writes records to CSV files, which remain after the app closes.
11. **What is an event handler?** Code that runs in response to an action such as a button click.
12. **Does the app verify medical compatibility?** No. It is an educational directory only.
