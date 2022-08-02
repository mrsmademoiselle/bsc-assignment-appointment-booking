export class Adresse {
    constructor(json) {
        if (Adresse.isValidAdresse(json)) {
            this.hausnummer = json.hausnummer;
            this.plz = json.plz;
            this.ort = json.ort;
            this.strasse = json.strasse;

        } else {
            console.log("is not a valid Adresse!" + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }

    static isValidAdresse(jsonObject) {
        return this.hasField(jsonObject, "hausnummer") &&
            this.hasField(jsonObject, "plz") &&
            this.hasField(jsonObject, "ort") &&
            this.hasField(jsonObject, "strasse");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

    formatToReadableString() {
        return this.strasse + " " + this.hausnummer + ", " + this.plz + " " + this.ort;
    }

}