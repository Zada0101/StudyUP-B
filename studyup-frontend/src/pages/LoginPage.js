// src/pages/LoginPage.js
import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosInstance";             // ✅ FIXED
import AuthContext from "../context/AuthContext";   // ✅ FIXED

const LoginPage = () => {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "" });
  const [status, setStatus] = useState("");

  const handleChange = (e) => {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus("Logging in...");

    try {
      const res = await api.post("/api/auth/login", {
        username: form.username,
        password: form.password,
      });

      // 📌 backend must return { token: "JWT" }
      if (!res.data || !res.data.token) {
        setStatus("Login failed: Invalid server response.");
        return;
      }

      // 📌 Make sure AuthContext expects (token, username)
      login(res.data.token, form.username);

      setStatus("Login successful!");
      setTimeout(() => navigate("/chat"), 300);

    } catch (err) {
      console.error("LOGIN ERROR:", err);

      if (err.response && err.response.data) {
        setStatus("Error: " + (err.response.data.message || err.response.data.error));
      } else {
        setStatus("Network error — backend unreachable.");
      }
    }
  };

  return (
    <div className="card">
      <h2>Login</h2>

      <form className="form" onSubmit={handleSubmit}>
        <label>
          Username
          <input
            name="username"
            value={form.username}
            onChange={handleChange}
            placeholder="test"
            required
          />
        </label>

        <label>
          Password
          <input
            name="password"
            type="password"
            value={form.password}
            onChange={handleChange}
            placeholder="123456"
            required
          />
        </label>

        <button className="btn-primary" type="submit">
          Login
        </button>
      </form>

      <p className="status">{status}</p>
    </div>
  );
};

export default LoginPage;
