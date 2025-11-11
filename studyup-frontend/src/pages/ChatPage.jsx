import React, { useEffect, useState, useRef } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default function ChatPage() {
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);
  const [isConnected, setIsConnected] = useState(false);
  const stompClientRef = useRef(null);

  // ✅ Load backend URL dynamically from .env
  const API_BASE_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8097";
  const WS_URL = `${API_BASE_URL.replace(/^http/, "ws")}/ws`;

  // ✅ Connect WebSocket on page load
  useEffect(() => {
    console.log("🔌 Connecting to WebSocket:", WS_URL);
    const socket = new SockJS(`${API_BASE_URL}/ws`);
    const stompClient = Stomp.over(socket);

    stompClient.connect(
      {},
      (frame) => {
        console.log("✅ Connected:", frame);
        setIsConnected(true);
        stompClient.subscribe("/topic/messages", (msg) => {
          const received = JSON.parse(msg.body);
          setMessages((prev) => [...prev, received]);
        });
      },
      (error) => {
        console.error("❌ WebSocket connection error:", error);
        setIsConnected(false);
      }
    );

    stompClientRef.current = stompClient;
    return () => {
      if (stompClient.connected) stompClient.disconnect();
      console.log("🔌 Disconnected WebSocket");
    };
  }, [API_BASE_URL]);

  // ✅ Handle send message
  const sendMessage = () => {
    if (!stompClientRef.current || !stompClientRef.current.connected) {
      console.warn("⚠️ Not connected to WebSocket server");
      return;
    }

    const payload = {
      content: message,
      sender: localStorage.getItem("username") || "User",
    };

    stompClientRef.current.send("/app/chat", {}, JSON.stringify(payload));
    setMessages((prev) => [...prev, payload]);
    setMessage("");
  };

  return (
    <div style={{ padding: "2rem", textAlign: "center" }}>
      <h2>💬 StudyUp Chat</h2>
      <p style={{ color: isConnected ? "green" : "red" }}>
        {isConnected ? "🟢 Connected" : "🔴 Disconnected"}
      </p>

      <div
        style={{
          border: "1px solid #ccc",
          height: "300px",
          width: "80%",
          margin: "1rem auto",
          overflowY: "scroll",
          padding: "1rem",
        }}
      >
        {messages.map((msg, idx) => (
          <p key={idx}>
            <strong>{msg.sender}:</strong> {msg.content}
          </p>
        ))}
      </div>

      <div>
        <input
          type="text"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type a message..."
          style={{ width: "60%", marginRight: "10px" }}
        />
        <button onClick={sendMessage}>Send</button>
      </div>
    </div>
  );
}
