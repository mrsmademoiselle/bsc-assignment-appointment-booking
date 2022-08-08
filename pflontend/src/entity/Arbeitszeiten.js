export class Arbeitszeiten {
    constructor(json) {
        if (Arbeitszeiten.hatKorrektesFormat(json)) {
            this.montag = json.montag;
            this.dienstag = json.dienstag;
            this.mittwoch = json.mittwoch;
            this.donnerstag = json.donnerstag;
            this.freitag = json.freitag;
            this.mitarbeiterId = json.mitarbeiterId;

        } else {
            console.log("Falsches Format für Arbeitszeiten:" + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }

    static hatKorrektesFormat(jsonObject) {
        return this.hasField(jsonObject, "montag") &&
            this.hasField(jsonObject, "dienstag") &&
            this.hasField(jsonObject, "donnerstag") &&
            this.hasField(jsonObject, "freitag") &&
            this.hasField(jsonObject, "mittwoch") &&
            this.hasField(jsonObject, "mitarbeiterId");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

}