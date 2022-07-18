import myApi from "@/services/myApi";
import {Appointment} from "@/entity/Appointment";

export class TerminService {

    /**
     * Gibt ein Objekt zurück, das für jeden Tag eine Liste der verfügbaren Uhrzeiten beinhält.
     * @returns {Promise<VerfuegbareUhrzeiten>}
     */
    static async getAlleVerfuegbarenUhrzeiten(date) {
        if (date !== null) {
            let verfuegbareUhrzeiten = [];

            verfuegbareUhrzeiten = ((await (myApi.get("public/termin/uhrzeiten/get/" + date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate()))).data);
            return verfuegbareUhrzeiten;
        }
    }

    /**
     * Gibt eine Liste von Datümern zurück, die komplett nicht auswählbar sind.
     */
    static async getKomplettBelegteDatuemer() {
        let komplettBelegteDatuemer = [];
        komplettBelegteDatuemer = ((await (myApi.get("public/termin/komplett-belegt/all/"))).data);
        return komplettBelegteDatuemer;
    }

    static async getAllAppointments() {
        let allAppointments = [];
        let data = (await myApi.get("public/termin/get/all")).data;
        data.forEach(appointment => {
            let appointment1 = new Appointment(appointment);
            allAppointments.push(appointment1);
        });
        return allAppointments;
    }

    static async getAppointmentForCancellationToken(token) {
        let appointment;
        let data = (await myApi.get("public/termin/cancel/" + token)).data;
        appointment = new Appointment(data);
        return appointment;
    }

    static async cancelAppointment(id) {
        try {
            await myApi.post("public/termin/cancel/" + id);
        } catch (e) {
            console.log("Could not cancel appointment", e)
        }
    }

    static async legeTerminAn(termin) {
        try {
            let status = (await myApi.post("public/termin/post", termin)).status;
            if (status === 200) {
                alert("Termin erfolgreich angelegt!\n" + JSON.stringify(termin))

            }
        } catch (e) {
            alert("something went wrong: " + e);
        }
    }
}