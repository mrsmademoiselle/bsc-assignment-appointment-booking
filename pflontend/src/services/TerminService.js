import myApi from "@/services/myApi";
import {Appointment} from "@/entity/Appointment";

export class TerminService {
    static async getAlleBelegtenTermine() {
        // TODO
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
}