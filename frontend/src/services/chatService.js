export async function sendMessageToAI(message) {
  try {
    const response = await fetch("http://localhost:9094/api/chat", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ message })
    });

    return await response.json();
  } catch (error) {
    console.error("Error sending message to AI:", error);
    throw error;
  }
}
