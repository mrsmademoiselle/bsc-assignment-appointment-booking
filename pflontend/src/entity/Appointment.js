export class Appointment {
    constructor(json) {
        if (Appointment.isValidAppointment(json)) {
            console.log("is valid appointment!");

            this.id = json.id;
            this.vorname = json.vorname;
            this.nachname = json.nachname;
            this.email = json.email;
            this.telefon = json.telefon;
            this.ort = json.ort;
            this.bemerkung = json.bemerkung;
            this.terminerinnerungPerMail = json.terminerinnerungPerMail;
            this.beratungsstelle = json.beratungsstelle;
            this.bereitsMitglied = json.bereitsMitglied;
            this.beratungsgrund = json.beratungsgrund;
            this.ausgewaehlterTermin = json.ausgewaehlterTermin;
            this.uhrzeit = json.uhrzeit;
        }
    }

    static isValidAppointment(jsonObject) {
        return this.hasField(jsonObject, "vorname") &&
            this.hasField(jsonObject, "nachname") &&
            this.hasField(jsonObject, "id") &&
            this.hasField(jsonObject, "email") &&
            this.hasField(jsonObject, "telefon") &&
            this.hasField(jsonObject, "ort") &&
            this.hasField(jsonObject, "bemerkung") &&
            this.hasField(jsonObject, "beratungsstelle") &&
            this.hasField(jsonObject, "bereitsMitglied") &&
            this.hasField(jsonObject, "beratungsgrund") &&
            this.hasField(jsonObject, "ausgewaehlterTermin") &&
            this.hasField(jsonObject, "uhrzeit") &&
            this.hasField(jsonObject, "terminerinnerungPerMail");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

    formatDateToGermanLocale() {
        try {
            let date = new Date(this.ausgewaehlterTermin);
            return date.toLocaleString("de-DE").split(",")[0];
        } catch (e) {
            console.log(e);
            return "Date konnte nicht geparst werden.";
        }
    }
}