import axios from "axios";

// Create an Axios instance with the backend URL
const api = axios.create({
  baseURL: "http://localhost:9095", // ✅ Backend port
});

// Automatically attach JWT token from localStorage to all requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token"); // JWT stored after login
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Optional: handle responses globally (for example, logout on 401)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or unauthorized
      localStorage.removeItem("token");
      window.location.href = "/login"; // redirect to login
    }
    return Promise.reject(error);
  }
);

export default api;
