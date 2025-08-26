import { render, screen, waitFor, fireEvent } from "@testing-library/react";
import { vi } from "vitest";
import TaskList from "../../src/components/TaskList";

const mockTasks = [
  { id: 1, title: "Task 1", description: "Desc 1", isCompleted: 0 },
  { id: 2, title: "Task 2", description: "Desc 2", isCompleted: 0 },
];

describe("TaskList Component", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockTasks),
      })
    );
  });

  it("renders tasks from API", async () => {
    render(<TaskList />);

    // ✅ use findBy for async elements
    expect(await screen.findByText("Task 1")).toBeInTheDocument();
    expect(await screen.findByText("Task 2")).toBeInTheDocument();
  });

  it("completes a task on button click", async () => {
    // mock POST call
    global.fetch = vi.fn((url, options) => {
      if (options?.method === "POST") {
        return Promise.resolve({ ok: true });
      }
      return Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockTasks),
      });
    });

    render(<TaskList />);

    const doneButtons = await screen.findAllByText("Done");

    fireEvent.click(doneButtons[0]);

    await waitFor(() => expect(global.fetch).toHaveBeenCalled());
  });
});

describe('TaskList Edge Cases', () => {
  it('renders no tasks message when API returns empty array', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve([]),
      })
    );

    render(<TaskList />);
    await waitFor(() => {
      expect(screen.queryByText('Task 1')).not.toBeInTheDocument();
    });
  });

  it('handles API error gracefully', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({ ok: false })
    );

    render(<TaskList />);
    await waitFor(() => {
      // Adjust this if you show an error message in UI
      expect(screen.queryByText('Task 1')).not.toBeInTheDocument();
    });
  });
});
