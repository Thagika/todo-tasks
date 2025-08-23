import { render, screen, fireEvent } from '@testing-library/react';
import TaskList from '../components/TaskList';

describe('TaskList Component', () => {
  const tasks = [
    { id: 1, title: 'Task 1', description: 'Description 1', isCompleted: 0 },
    { id: 2, title: 'Task 2', description: 'Description 2', isCompleted: 0 },
  ];

  beforeEach(() => {
    render(<TaskList />);
  });

  it('renders tasks correctly', () => {
    // Mock fetch to return tasks
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(tasks),
      })
    );

    expect(screen.getByText('Task 1')).toBeInTheDocument();
    expect(screen.getByText('Task 2')).toBeInTheDocument();
  });

  it('calls toggleDone when Done button is clicked', async () => {
    // Mock fetch to return tasks
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(tasks),
      })
    );

    // Mock toggleDone
    const toggleDone = jest.fn();
    render(<TaskList toggleDone={toggleDone} />);

    fireEvent.click(screen.getByRole('button', { name: /done/i }));

    expect(toggleDone).toHaveBeenCalledTimes(1);
  });

  it('removes task from the list when Done button is clicked', async () => {
    // Mock fetch to return tasks
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(tasks),
      })
    );

    fireEvent.click(screen.getByRole('button', { name: /done/i }));

    // Update the expected behavior here based on your toggleDone implementation
    expect(screen.queryByText('Task 1')).not.toBeInTheDocument();
  });
});
