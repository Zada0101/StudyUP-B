import axios from 'axios' // HTTP client


const client = axios.create({ baseURL: '/api' }) // Proxy handles backend host


client.interceptors.request.use((config) => { // Before each request
const token = localStorage.getItem('token') // Read token
if (token) config.headers['Authorization'] = `Bearer ${token}` // Attach header
return config
})


export default client