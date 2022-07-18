import axios from 'axios'

export default class ApiService {
    static instance = axios.create({
        baseURL: 'http://localhost:6969/',
        timeout: 5000
    });

    static authenticatedPost(endpoint, body, token) {
        return this.instance.post(endpoint, body,
            {headers: {'Authorization': token}})
    }

    static post(endpoint, body) {
        return this.instance.post('public/' + endpoint, body)
    }

    static authenticatedGet(endpoint, token) {
        return this.instance.get(endpoint, {
            headers: {
                'Authorization': token
            }
        })
    }

    static get(endpoint) {
        return this.instance.get('public/' + endpoint)
    }
}