import React, { useState } from "react";

function Chat() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);

  const handleSend = () => {
    if (!input.trim()) return;
    setMessages([...messages, { sender: "user", text: input }]);
    setInput(""); // Clear input
  };

  return (
    <div style={styles.container}>
      <h2>💬 AI Tutor Chat</h2>

      <div style={styles.chatBox}>
        {messages.map((msg, index) => (
          <div key={index} style={msg.sender === "user" ? styles.userMsg : styles.aiMsg}>
            {msg.text}
          </div>
        ))}
      </div>

      <div style={styles.inputArea}>
        <input
          style={styles.input}
          type="text"
          placeholder="Type your question..."
          value={input}
          onChange={(e) => setInput(e.target.value)}
        />
        <button style={styles.button} onClick={handleSend}>Send</button>
      </div>
    </div>
  );
}

const styles = {
  container: { width: "500px", margin: "auto", textAlign: "center", marginTop: "50px" },
  chatBox: { border: "1px solid gray", height: "300px", overflowY: "scroll", padding: "10px", marginBottom: "10px" },
  inputArea: { display: "flex" },
  input: { flex: 1, padding: "10px", fontSize: "16px" },
  button: { padding: "10px 20px", marginLeft: "10px" },
  userMsg: { textAlign: "right", color: "blue" },
  aiMsg: { textAlign: "left", color: "green" }
};

export default Chat;
