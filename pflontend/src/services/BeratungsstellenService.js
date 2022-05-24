import myApi from "@/services/myApi";
import {Beratungsstelle} from "@/entity/Beratungsstelle";

export class BeratungsstellenService {
    static async getAlleAnreden() {
        // TODO
    }

    static async getAlleBeratungsstellen() {
        let alleBeratungsstellen = [];
        let data = (await myApi.get("public/beratungsstellen/get/all")).data;
        data.forEach(beratungsstelle => {
            console.log("beratungsstelle", data);
            alleBeratungsstellen.push(new Beratungsstelle(beratungsstelle));
        });
        return alleBeratungsstellen;
    }

    static async getAlleTermingruende() {
        let data = (await myApi.get("public/termingrund/get/all")).data;
        return data;
    }
}