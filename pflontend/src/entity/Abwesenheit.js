export class Abwesenheit {
    constructor(json) {
        if (Abwesenheit.hatKorrektesFormat(json)) {
            this.startDatum = json.startDatum;
            this.endDatum = json.endDatum;
            this.id = json.id;

        } else {
            console.log("Falsches Format für Abwesenheit: " + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }

    static hatKorrektesFormat(jsonObject) {
        return this.hasField(jsonObject, "startDatum") &&
            this.hasField(jsonObject, "endDatum") && this.hasField(jsonObject, "id")
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

}