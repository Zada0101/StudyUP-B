import React, { useState } from "react";
import { sendMessageToAI } from "../services/chatService";

export default function Chat() {
  const [userInput, setUserInput] = useState("");
  const [messages, setMessages] = useState([]);

  const handleSend = async () => {
    if (!userInput.trim()) return;

    const userMsg = { sender: "You", text: userInput };
    setMessages([...messages, userMsg]);

    try {
      const aiResponse = await sendMessageToAI(userInput);
      setMessages((prev) => [
        ...prev,
        userMsg,
        { sender: "AI", text: aiResponse.reply || "(no reply)" }
      ]);
    } catch {
      setMessages((prev) => [
        ...prev,
        { sender: "System", text: "⚠️ Failed to contact server" }
      ]);
    }

    setUserInput("");
  };

  return (
    <div style={{ padding: "2rem", maxWidth: "600px", margin: "auto" }}>
      <h2>💬 StudyUp AI Chat</h2>

      <div
        style={{
          border: "1px solid #ccc",
          borderRadius: "8px",
          padding: "1rem",
          height: "300px",
          overflowY: "auto",
          marginBottom: "1rem"
        }}
      >
        {messages.map((msg, index) => (
          <div key={index}>
            <strong>{msg.sender}: </strong> {msg.text}
          </div>
        ))}
      </div>

      <input
        type="text"
        value={userInput}
        onChange={(e) => setUserInput(e.target.value)}
        placeholder="Type your message..."
        style={{ width: "80%", padding: "0.5rem" }}
      />
      <button onClick={handleSend} style={{ padding: "0.5rem 1rem", marginLeft: "0.5rem" }}>
        Send
      </button>
    </div>
  );
}
