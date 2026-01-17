import axios from "axios";

const token = localStorage.getItem("jwtToken");

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type':'application/json',
        ...(token &&{'Authorization': `Bearer ${token}`})
    },
    withCredentials: true
});

export default apiClient;