import axios from "axios";

/**
 * Sends a message to the backend ChatController
 * and returns the AI's reply.
 */
export async function sendMessageToAI(message) {
  try {
    const response = await axios.post("/api/chat", message, {
      headers: { "Content-Type": "application/json" }
    });
    return response.data; // expects ChatResponse { reply: "..." }
  } catch (error) {
    console.error("Error sending message:", error);
    throw error;
  }
}
