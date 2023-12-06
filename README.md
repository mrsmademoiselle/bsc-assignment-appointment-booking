# Appointment booking tool

This repository is an assignment from my universitys course of summer 2022.
The UI is designed in german due to the course being german.

It's explicitly cut towards tax consultants use cases.

## Project setup

### 1. Install dependencies

Frontend:

npm (or alternatively yarn) has to be installed: https://www.npmjs.com/package/download

```
cd pflontend
npm install
```

Backend:

Maven has to be installed: https://maven.apache.org/download.cgi?Preferred=ftp://ftp.osuosl.org/pub/apache/

```
cd packend
mvn clean install
```

### 2. Mail Settings

You need to configure your own mail settings in order to send them to your clients.

The settings for this can be found in the `application.properties` file.

Please note that imap/pop is allowed for your mail account. Enable this in your providers settings if not already enabled.

The following lines need to be adjusted:

```properties
# Settings of the mail sender
spring.mail.username=<mail address of sender>
spring.mail.password=<password to mail account above>
# Automatic CC to this mail address, if applicable. Can be left empty if not needed.
spring.mail.cc=<second mail address>
```

### 3. Run

Backend-Server: Run `PackendApplication.java`

Frontend-Server:
```npm run serve```

The application can now be opened at http://localhost:8080.

```
Username: admin
Passwort: admin
```

For easier testing, random data is created upon application start (appointments, employees,...).