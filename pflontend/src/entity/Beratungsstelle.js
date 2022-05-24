export class Beratungsstelle {
    constructor(json) {
        if (Beratungsstelle.isValidBeratungsstelle(json)) {
            this.id = json.id;
            this.ansprechpartner = json.ansprechpartner;
            this.hausnummer = json.hausnummer;
            this.ort = json.ort;
            this.plz = json.plz;
            this.strasse = json.strasse;
        } else {
            let e = {}
            throw e;
        }
    }

    static isValidBeratungsstelle(jsonObject) {

        return this.hasField(jsonObject, "id") &&
            this.hasField(jsonObject, "ansprechpartner") &&
            this.hasField(jsonObject, "hausnummer") &&
            this.hasField(jsonObject, "ort") &&
            this.hasField(jsonObject, "plz") &&
            this.hasField(jsonObject, "strasse");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

    formatToReadableString() {
        return this.ansprechpartner + ", " + this.strasse + " " + this.hausnummer + ", " + this.plz + " " + this.ort;
    }
}