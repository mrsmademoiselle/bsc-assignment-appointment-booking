export class Mitarbeiter {
    constructor(json) {
        if (Mitarbeiter.hatKorrektesFormat(json)) {
            this.username = json.username;
            this.vorname = json.vorname;
            this.nachname = json.nachname;

        } else {
            console.log("Falsches Format für Mitarbeiter:" + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }

    static hatKorrektesFormat(jsonObject) {
        return this.hasField(jsonObject, "vorname") &&
            this.hasField(jsonObject, "nachname") &&
            this.hasField(jsonObject, "username");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

}