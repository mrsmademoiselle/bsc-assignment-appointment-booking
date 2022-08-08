import {Beratungsstelle} from "@/entity/Beratungsstelle";

export class Termin {
    constructor(json) {
        if (Termin.hatKorrektesFormat(json)) {
            this.id = json.id;
            this.vorname = json.vorname;
            this.nachname = json.nachname;
            this.email = json.email;
            this.geschlecht = json.geschlecht;
            this.telefon = json.telefon;
            this.bemerkung = json.bemerkung;
            this.beratungsstelle = new Beratungsstelle(json.beratungsstelle);
            this.bereitsMitglied = json.bereitsMitglied;
            this.beratungsgrund = json.beratungsgrund;
            this.ausgewaehlterTermin = json.ausgewaehlterTermin;
            this.uhrzeit = json.uhrzeit;
        } else {
            console.log("Das Format ist inkorrekt für Termin " + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }


    static hatKorrektesFormat(jsonObject) {
        return this.hasField(jsonObject, "vorname") && this.hasField(jsonObject, "id") &&
            this.hasField(jsonObject, "nachname") &&
            this.hasField(jsonObject, "geschlecht") &&
            this.hasField(jsonObject, "email") &&
            this.hasField(jsonObject, "telefon") &&
            this.hasField(jsonObject, "bemerkung") &&
            this.hasField(jsonObject, "bereitsMitglied") &&
            this.hasField(jsonObject, "beratungsgrund") &&
            this.hasField(jsonObject, "ausgewaehlterTermin") &&
            this.hasField(jsonObject, "uhrzeit")
    }

    /*
    *
    * */

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

    formatDateToGermanLocale() {
        try {
            let date = new Date(this.ausgewaehlterTermin);
            return date.toLocaleString("de-DE").split(",")[0];
        } catch (e) {
            console.log(e);
            return "Datum konnte nicht geparst werden.";
        }
    }
}