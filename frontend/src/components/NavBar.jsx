import React from "react";
import { Link, useNavigate } from "react-router-dom";

export default function NavBar() {
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav style={styles.nav}>
      <div style={styles.brand}>StudyUp AI</div>
      <div style={styles.controls}>
        <Link to="/dashboard" style={styles.link}>Dashboard</Link>
        <Link to="/chat" style={styles.link}>Chat</Link>
        <button onClick={logout} style={styles.btn}>Logout</button>
      </div>
    </nav>
  );
}

const styles = {
  nav: {
    display: "flex", justifyContent: "space-between", alignItems: "center",
    padding: "12px 20px", background: "#0f172a", color: "white", boxShadow: "0 2px 6px rgba(2,6,23,0.2)"
  },
  brand: { fontWeight: 700, fontSize: 18 },
  controls: { display: "flex", gap: 12, alignItems: "center" },
  link: { color: "white", textDecoration: "none", padding: "6px 10px", borderRadius: 6 },
  btn: { background: "#ef4444", color: "white", border: "none", padding: "8px 12px", borderRadius: 6, cursor: "pointer" }
};
