import ApiService from "@/services/ApiService";
import {Beratungsstelle} from "@/entity/Beratungsstelle";

export class BeratungsstellenService {
    static async getAlleAnreden() {
        let data = (await ApiService.get("public/anrede/get/all")).data;
        return data;
    }

    static async getAlleBeratungsstellen() {
        let alleBeratungsstellen = [];
        let data = (await ApiService.get("public/beratungsstellen/get/all")).data;
        data.forEach(beratungsstelle => {
            alleBeratungsstellen.push(new Beratungsstelle(beratungsstelle));
        });
        return alleBeratungsstellen;
    }

    static async getAlleTermingruende() {
        let data = (await ApiService.get("public/termingrund/get/all")).data;
        return data;
    }
}