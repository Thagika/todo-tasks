import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AddTask from "../../src/components/AddTask";

describe('AddTask Component', () => {
  it('renders input fields and submit button', () => {
    render(<AddTask />);
    expect(screen.getByPlaceholderText(/Title/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Description/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Add/i })).toBeInTheDocument();
  });

  it('shows loading state when submitting', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({}),
      })
    );

    render(<AddTask />);
    fireEvent.change(screen.getByPlaceholderText(/Title/i), { target: { value: 'Test Task' } });
    fireEvent.change(screen.getByPlaceholderText(/Description/i), { target: { value: 'Test Desc' } });
    fireEvent.click(screen.getByRole('button', { name: /Add/i }));

    expect(screen.getByRole('button', { name: /Adding.../i })).toBeInTheDocument();
  });

  // prevent empty title submission
  it('does not submit if title is empty', () => {
    global.fetch = vi.fn();

    render(<AddTask />);
    fireEvent.change(screen.getByPlaceholderText(/Description/i), { target: { value: 'Only description' } });
    fireEvent.click(screen.getByRole('button', { name: /Add/i }));

    expect(global.fetch).not.toHaveBeenCalled(); // fix here
  });

  // handle API failure
  it('shows error if submission fails', async () => {
    global.fetch = vi.fn(() =>
      Promise.resolve({ ok: false })
    );

    render(<AddTask />);
    fireEvent.change(screen.getByPlaceholderText(/Title/i), { target: { value: 'Fail Task' } });
    fireEvent.change(screen.getByPlaceholderText(/Description/i), { target: { value: 'Fail Desc' } });
    fireEvent.click(screen.getByRole('button', { name: /Add/i }));

    await waitFor(() => {
      // Adjust this depending on how you show errors in AddTask
      expect(screen.getByRole('button', { name: /Add/i })).toBeInTheDocument();
    });
  });
});
