const API_URL = "http://localhost:9091/api/chat";

export const sendMessageToAI = async (message) => {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ message })
    });

    if (!response.ok) {
      throw new Error("Failed to connect to AI backend");
    }

    return await response.json();
  } catch (error) {
    console.error("Chat Service Error:", error);
    throw error;
  }
};
