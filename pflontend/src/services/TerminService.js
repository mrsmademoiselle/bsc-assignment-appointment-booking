import myApi from "@/services/myApi";
import {Appointment} from "@/entity/Appointment";

export class TerminService {

    static async getAllAppointments() {
        let allAppointments = [];
        let data = (await myApi.get("example/get/all")).data;
        console.log(data)
        data.forEach(appointment => {
            allAppointments.push(new Appointment(appointment));
        });
        console.log(allAppointments.length)
        return allAppointments;
    }

    static async getAppointmentForCancellationToken(token) {
        let appointment;
        let data = (await myApi.get("example/cancel/" + token)).data;
        appointment = new Appointment(data);
        return appointment;
    }

    static async cancelAppointment(id) {
        try {
            await myApi.post("example/cancel/" + id);
        } catch (e) {
            console.log("Could not cancel appointment", e)
        }
    }
}