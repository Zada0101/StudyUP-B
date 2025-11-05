// src/components/Chat.js
import { useState, useEffect, useRef } from "react";
import axios from "axios";

export default function Chat() {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const messagesEndRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = async () => {
    if (!input.trim()) return;

    // Add user message
    setMessages(prev => [...prev, { sender: "user", text: input }]);

    try {
      const token = localStorage.getItem("token"); // JWT token
      const res = await axios.post(
        "/api/chat",
        { message: input },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      // Add AI reply
      setMessages(prev => [...prev, { sender: "ai", text: res.data.reply }]);
    } catch (err) {
      setMessages(prev => [...prev, { sender: "ai", text: "Error contacting AI" }]);
    }

    setInput("");
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") sendMessage();
  };

  return (
    <div style={{ maxWidth: "600px", margin: "20px auto", fontFamily: "Arial" }}>
      <h2>AI Chat</h2>
      <div style={{
        height: "400px",
        overflowY: "scroll",
        border: "1px solid #ccc",
        padding: "10px",
        marginBottom: "10px",
        borderRadius: "5px",
        backgroundColor: "#f9f9f9"
      }}>
        {messages.map((msg, i) => (
          <div key={i} style={{ margin: "5px 0" }}>
            <b>{msg.sender}:</b> {msg.text}
          </div>
        ))}
        <div ref={messagesEndRef}></div>
      </div>
      <input
        type="text"
        value={input}
        onChange={e => setInput(e.target.value)}
        onKeyPress={handleKeyPress}
        placeholder="Type your message..."
        style={{ width: "80%", padding: "8px" }}
      />
      <button onClick={sendMessage} style={{ padding: "8px 12px", marginLeft: "5px" }}>Send</button>
    </div>
  );
}
