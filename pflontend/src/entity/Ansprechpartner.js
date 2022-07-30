export class Ansprechpartner {
    constructor(json) {
        if (Ansprechpartner.isValidAnsprechpartner(json)) {
            this.username = json.username;
            this.vorname = json.vorname;
            this.nachname = json.nachname;

        } else {
            console.log("is not a valid ansprechpartner!" + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }

    static isValidAnsprechpartner(jsonObject) {
        return this.hasField(jsonObject, "vorname") &&
            this.hasField(jsonObject, "nachname") &&
            this.hasField(jsonObject, "username");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

}