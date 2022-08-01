export class Abwesenheit {
    constructor(json) {
        if (Abwesenheit.isValidAbwesenheit(json)) {
            this.startDatum = json.startDatum;
            this.endDatum = json.endDatum;

        } else {
            console.log("is not a valid Abwesenheit!" + JSON.stringify(json));
            let e = {};
            throw e;
        }
    }

    static isValidAbwesenheit(jsonObject) {
        return this.hasField(jsonObject, "startDatum") &&
            this.hasField(jsonObject, "endDatum");
    }

    static hasField(object, field) {
        return Object.prototype.hasOwnProperty.call(object, field);
    }

}