import React, { useEffect, useState, useContext } from "react";
import {
  getSessions,
  getSessionMessages,
  createSession,
  sendMessageToSession,
} from "../api";
import { AuthContext } from "../context/AuthContext";
import "./Chat.css";

export default function SessionsPage() {
  const { logout } = useContext(AuthContext);

  const [sessions, setSessions] = useState([]);
  const [messages, setMessages] = useState([]);
  const [currentId, setCurrentId] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    loadSessions();
  }, []);

  async function loadSessions() {
    const data = await getSessions();
    setSessions(data);
    if (data.length > 0) selectSession(data[0].id);
  }

  async function selectSession(id) {
    setCurrentId(id);
    const msgs = await getSessionMessages(id);
    setMessages(msgs);
  }

  async function handleCreate() {
    const title = prompt("Session Title", "New Chat");
    if (!title) return;

    const session = await createSession(title);
    setSessions([session, ...sessions]);
    selectSession(session.id);
  }

  async function handleSend() {
    if (!message.trim()) return;

    const userMsg = {
      id: Date.now(),
      role: "user",
      content: message,
      createdAt: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMsg]);

    const res = await sendMessageToSession(currentId, message);
    const aiMsg = {
      id: Date.now() + 1,
      role: "ai",
      content: res.reply,
      createdAt: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, aiMsg]);
    setMessage("");
  }

  return (
    <div className="app">
      <div className="sidebar">
        <h2>Chat Sessions</h2>
        <button onClick={handleCreate}>New Chat</button>

        <div className="sessions-list">
          {sessions.map((s) => (
            <div
              key={s.id}
              className={s.id === currentId ? "session active" : "session"}
              onClick={() => selectSession(s.id)}
            >
              {s.title}
            </div>
          ))}
        </div>

        <button className="logout" onClick={logout}>Logout</button>
      </div>

      <div className="chat-panel">
        <div className="messages">
          {messages.map((m) => (
            <div key={m.id} className={`msg ${m.role}`}>
              {m.content}
            </div>
          ))}
        </div>

        <div className="input-row">
          <input
            placeholder="Type a message..."
            value={message}
            onChange={(e) => setMessage(e.target.value)}
          />
          <button onClick={handleSend}>Send</button>
        </div>
      </div>
    </div>
  );
}
