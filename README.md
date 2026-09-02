# Appointment Creator

JavaFX desktop app for managing customers and appointments against a local MySQL database.

Author: David O'Camb  
Version: 1.0

## Features

- Login verification, with French UI when the system locale is French
- Login attempts appended to `AppointmentCreator/src/files/login_report.txt`
- View appointments (all / week / month)
- Create, read, update, and delete customers and appointments
- Timed alerts for appointments starting in the next 15 minutes
- Reports from the home screen
- MVC and DAO structure

## Repository layout

```
appointment-creator/
  README.md
  .gitignore
  AppointmentCreator/
    src/                 application source
    javadoc/             generated API documentation
    schema.sql           MySQL schema and demo data
```

Open [`AppointmentCreator/javadoc/index.html`](AppointmentCreator/javadoc/index.html) in a browser to browse the API docs.

## Requirements

- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [JDK 20](https://www.oracle.com/java/technologies/javase/20-0-1-relnotes.html)
- [JavaFX SDK 20](https://openjfx.io/) (not bundled with the JDK)
- [MySQL Server](https://dev.mysql.com/downloads/mysql/) and [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
- [MySQL Connector/J 8.0.33](https://dev.mysql.com/downloads/connector/j/)

## Database setup

1. Start MySQL locally.
2. Run `AppointmentCreator/schema.sql` in MySQL Workbench (or `mysql -u root -p < AppointmentCreator/schema.sql`).
3. That script creates the `client_schedule` database, tables, demo rows, and the app user.

Default connection used by the app:

| Setting  | Value            |
|----------|------------------|
| Host     | `localhost`      |
| Database | `client_schedule`|
| User     | `sqlUser`        |
| Password | `Passw0rd!`      |

Demo login in the app: **test** / **test** (also **admin** / **admin**).

If creating `sqlUser` fails, run that last section of the script as a MySQL admin, or change `src/utilities/DatabaseConnection.java` to match your local user.

## Run in IntelliJ

1. Clone this repository and open the **appointment-creator** folder in IntelliJ (`File` → `Open`).
2. Set the project SDK to JDK 20.
3. Mark `AppointmentCreator/src` as a **Sources Root** (right-click the folder → `Mark Directory as` → `Sources Root`).
4. Add libraries:
   - JavaFX SDK `lib` folder
   - `mysql-connector-j-8.0.33.jar`
5. Edit the run configuration for `main.Main` and set **VM options** (point `--module-path` at your JavaFX `lib` folder):

   ```
   --module-path /path/to/javafx-sdk-20.0.1/lib --add-modules javafx.controls,javafx.fxml
   ```

6. Confirm MySQL is running and `schema.sql` has been applied.
7. Run `main.Main`.

## Using the app

- **Reports:** on the home screen, choose a report option on the bottom left, pick the related filters, then click **Generate**.
- **Appointment filters:** use **All**, **Week**, or **Month** at the top right of the appointment table.

## Tools

- JavaFX
- JDBC
- [Scene Builder](https://www.jetbrains.com/help/idea/opening-fxml-files-in-javafx-scene-builder.html) (optional, for editing FXML)
