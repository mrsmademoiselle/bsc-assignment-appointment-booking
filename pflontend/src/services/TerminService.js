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
        let data = (await apiService.get("termin/cancel/" + token)).data;
        appointment = new Appointment(data);
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
            let status = (await apiService.post("termin/post", termin)).status;
            if (status === 200) {
                alert("Termin erfolgreich angelegt!\n" + JSON.stringify(termin))

            }
        } catch (e) {
            alert("something went wrong: " + e);
        }
    }
}