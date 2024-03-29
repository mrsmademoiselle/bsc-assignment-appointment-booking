import {Mitarbeiter} from "@/entity/Mitarbeiter";
import {Adresse} from "@/entity/Adresse";

export class Beratungsstelle {
    constructor(json) {
        if (Beratungsstelle.hatKorrektesFormat(json)) {
            this.id = json.id;
            this.ansprechpartner = new Mitarbeiter(json.ansprechpartner);
            this.adresse = new Adresse(json.adresse)
            this.mitarbeiterListe = json.mitarbeiterListe;
        } else {
            let e = {}
            console.log("Falsches Format für Beratungsstelle: " + JSON.stringify(json));

            throw e;
        }
    }

    static hatKorrektesFormat(jsonObject) {

        return this.hasField(jsonObject, "id") &&
            this.hasField(jsonObject, "ansprechpartner") &&
            this.hasField(jsonObject, "mitarbeiterListe") &&
            this.hasField(jsonObject, "adresse");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

    formatToReadableString() {
        return this.ansprechpartner.vorname + " " + this.ansprechpartner.nachname + ", " + this.adresse.strasse + " " + this.adresse.hausnummer + ", " + this.adresse.plz + " " + this.adresse.ort;
    }
}