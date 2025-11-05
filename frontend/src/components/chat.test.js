// src/components/Chat.test.js
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import Chat from "./Chat";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";

// Mock localStorage
beforeAll(() => {
  Storage.prototype.getItem = jest.fn(() => "fake-token");
});

describe("Chat Component", () => {
  let mock;

  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  afterEach(() => {
    mock.reset();
  });

  test("renders input field and send button", () => {
    render(<Chat />);
    expect(screen.getByPlaceholderText("Type your message...")).toBeInTheDocument();
    expect(screen.getByText("Send")).toBeInTheDocument();
  });

  test("sends user message and displays AI response", async () => {
    render(<Chat />);

    const userMessage = "Hello AI!";

    // Mock backend response
    mock.onPost("/api/chat").reply(200, { reply: "AI says: Hello AI!" });

    const input = screen.getByPlaceholderText("Type your message...");
    const button = screen.getByText("Send");

    fireEvent.change(input, { target: { value: userMessage } });
    fireEvent.click(button);

    // Check user message appears
    expect(await screen.findByText(/user:/i)).toBeInTheDocument();
    expect(screen.getByText(userMessage)).toBeInTheDocument();

    // Wait for AI response
    await waitFor(() => screen.getByText("AI says: Hello AI!"));
    expect(screen.getByText("AI says: Hello AI!")).toBeInTheDocument();
  });

  test("does not send empty message", () => {
    render(<Chat />);
    const button = screen.getByText("Send");
    fireEvent.click(button);
    expect(screen.queryByText(/user:/i)).not.toBeInTheDocument();
  });
});
