# pflontend

## Project setup

### 1. Dependencies installieren

Frontend:

```
npm install
```

Backend:

```
mvn clean install
```

### 2. E-Mail-Einstellungen

Die Einstellungen für den E-Mail-Versand befinden sich in der application.properties-Datei.

Bitte darauf achten, dass imap/pop für den E-Mail-Account erlaubt ist! Ggf. beim Anbieter überprüfen.

Folgende Zeilen müssen angepasst werden:

```properties
# Für die Einstellungen des E-Mail-Senders
spring.mail.username=<Email-Adresse des E-Mail-Senders>
spring.mail.password=<Passwort zu o.g. E-Mail-Account>
# Automatisches CC an diese E-Mail-Adresse
spring.mail.cc=<E-Mail-Adresse>
```

### 3. Ausführen

Backend-Server: PackendApplication.java ausführen

Frontend-Server:
```npm run serve```

Über http://localhost:8080 kann nun die Anwendung aufgerufen werden. Für Testzwecke werden Zugangsdaten für die
Login-Maske mitgeliefert:

```
Username: admin
Passwort: admin
```

Für ein leichteres Testing werden außerdem beim Start der Anwendung in PackendApplication.java Dummydaten erstellt (
Termine, Beratungsstellen, Mitarbeiter). 