import { render, screen, waitFor } from "@testing-library/react";
import { vi } from "vitest";
import App from "../src/App";
import AddTask from "../src/components/AddTask";

const mockTasks = [
  { id: 1, title: "Task 1", description: "Desc 1", isCompleted: 0 },
];

describe("App Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("renders AddTask and TaskList when tasks exist", async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockTasks),
      })
    );

    render(<App />);


    expect(await screen.findByText("Task 1")).toBeInTheDocument();


    expect(screen.getByPlaceholderText("Title")).toBeInTheDocument();
  });

  it("renders AddTask centered when no tasks exist", async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([]),
      })
    );

    render(<App />);


    expect(
      await screen.findByPlaceholderText("Title")
    ).toBeInTheDocument();


    expect(screen.queryByText("Task 1")).not.toBeInTheDocument();
  });
});

describe('App Component Edge Cases', () => {
  it('shows no tasks if fetch fails', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({ ok: false }) // simulate API failure
    );

    render(<App />);
    await waitFor(() =>
      expect(screen.queryByText('Task 1')).not.toBeInTheDocument()
    );
  });
});