import apiService from "@/services/ApiService";
import {Appointment} from "@/entity/Appointment";

export class TerminService {

    /**
     * Gibt ein Objekt zurück, das für jeden Tag eine Liste der verfügbaren Uhrzeiten beinhält.
     * @returns {Promise<VerfuegbareUhrzeiten>}
     */
    static async getAlleVerfuegbarenUhrzeiten(date) {
        if (date !== null) {
            let verfuegbareUhrzeiten = [];

            verfuegbareUhrzeiten = ((await (apiService.get("termin/uhrzeiten/get/" + date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate()))).data);
            return verfuegbareUhrzeiten;
        }
    }

    /**
     * Gibt eine Liste von Datümern zurück, die komplett nicht auswählbar sind.
     */
    static async getKomplettBelegteDatuemer() {
        let komplettBelegteDatuemer = [];
        komplettBelegteDatuemer = ((await (apiService.get("termin/komplett-belegt/all/"))).data);
        return komplettBelegteDatuemer;
    }

    static async getAppointmentForCancellationToken(token) {
        let appointment;
        console.log("getAppointmentForCancellationToken")
        let data = (await apiService.get("termin/cancel/" + token)).data;
        appointment = new Appointment(data);
        console.log('got appointment ' + JSON.stringify(appointment))
        return appointment;
    }

    static async cancelAppointment(id) {
        try {
            await apiService.post("termin/cancel/" + id);
        } catch (e) {
            console.log("Could not cancel appointment", e)
        }
    }

    static async legeTerminAn(termin) {
        try {
            (await apiService.post("termin/post", termin));
        } catch (e) {
            alert("something went wrong: " + e);
        }
    }
}