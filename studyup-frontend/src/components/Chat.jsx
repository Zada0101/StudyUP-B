import { useState, useEffect, useRef } from "react";
import axios from "../api/axiosInstance"; // ← you will create this in Step C

export default function Chat() {
    const [messages, setMessages] = useState([]); // {sender: "user"|"ai", text: "..."}
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);

    const bottomRef = useRef(null);

    // Auto-scroll to bottom whenever messages update
    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages]);

    const sendMessage = async (e) => {
        e.preventDefault();

        if (!input.trim()) return;

        const userMessage = input.trim();

        // Add user message to chat
        setMessages((prev) => [
            ...prev,
            { sender: "user", text: userMessage }
        ]);

        setInput("");
        setLoading(true);

        try {
            const response = await axios.post("/api/chat", {
                message: userMessage
            });

            const aiReply = response.data.reply;

            setMessages((prev) => [
                ...prev,
                { sender: "ai", text: aiReply }
            ]);

        } catch (err) {
            setMessages((prev) => [
                ...prev,
                {
                    sender: "ai",
                    text: "❌ Error contacting the AI service."
                }
            ]);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="chat-container" style={styles.container}>
            <div className="chat-window" style={styles.chatWindow}>
                {messages.map((msg, idx) => (
                    <div
                        key={idx}
                        style={{
                            ...styles.message,
                            alignSelf:
                                msg.sender === "user" ? "flex-end" : "flex-start",
                            background:
                                msg.sender === "user" ? "#cce5ff" : "#e2e2e2",
                        }}
                    >
                        {msg.text}
                    </div>
                ))}

                {/* Auto-scroll marker */}
                <div ref={bottomRef} />
            </div>

            <form onSubmit={sendMessage} style={styles.form}>
                <input
                    type="text"
                    placeholder="Type your message..."
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    style={styles.input}
                />

                <button type="submit" disabled={loading} style={styles.button}>
                    {loading ? "Sending..." : "Send"}
                </button>
            </form>
        </div>
    );
}

//
// 💅 Simple Inline Styles (replace with Tailwind/your CSS later)
//
const styles = {
    container: {
        width: "100%",
        maxWidth: "800px",
        margin: "0 auto",
        display: "flex",
        flexDirection: "column",
        height: "90vh",
    },
    chatWindow: {
        flex: 1,
        overflowY: "auto",
        border: "1px solid #ccc",
        borderRadius: "8px",
        padding: "10px",
        display: "flex",
        flexDirection: "column",
        gap: "10px",
    },
    message: {
        padding: "10px 14px",
        borderRadius: "14px",
        maxWidth: "70%",
        fontSize: "15px",
    },
    form: {
        display: "flex",
        marginTop: "10px",
    },
    input: {
        flex: 1,
        padding: "10px",
        borderRadius: "6px",
        border: "1px solid #ccc",
        marginRight: "10px",
    },
    button: {
        padding: "10px 18px",
        borderRadius: "6px",
        background: "#007bff",
        color: "white",
        border: "none",
        cursor: "pointer",
    },
};
