import React, { useState, useEffect, useRef } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const Chat = () => {
  const { token } = useAuth(); // JWT token from context
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const messagesEndRef = useRef(null);

  // Scroll to the bottom whenever messages change
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Send message to backend
  const sendMessage = async () => {
    if (!input.trim()) return;

    // Add user message to chat
    const userMessage = { role: "user", content: input };
    setMessages((prev) => [...prev, userMessage]);
    const prompt = input;
    setInput("");

    try {
      const response = await axios.post(
        "http://localhost:8080/api/chat",
        { prompt },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      const aiReply = { role: "ai", content: response.data.reply };
      setMessages((prev) => [...prev, aiReply]);
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") sendMessage();
  };

  return (
    <div style={{ maxWidth: "600px", margin: "auto", padding: "1rem" }}>
      <div
        style={{
          border: "1px solid #ccc",
          borderRadius: "8px",
          padding: "1rem",
          minHeight: "400px",
          overflowY: "auto",
        }}
      >
        {messages.map((msg, idx) => (
          <div
            key={idx}
            style={{
              margin: "0.5rem 0",
              textAlign: msg.role === "user" ? "right" : "left",
            }}
          >
            <span
              style={{
                padding: "0.5rem 1rem",
                borderRadius: "12px",
                backgroundColor: msg.role === "user" ? "#DCF8C6" : "#EEE",
              }}
            >
              {msg.content}
            </span>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      <div style={{ display: "flex", marginTop: "1rem" }}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type your message..."
          style={{ flex: 1, padding: "0.5rem", borderRadius: "8px", border: "1px solid #ccc" }}
        />
        <button
          onClick={sendMessage}
          style={{ marginLeft: "0.5rem", padding: "0.5rem 1rem", borderRadius: "8px" }}
        >
          Send
        </button>
      </div>
    </div>
  );
};

export default Chat;
