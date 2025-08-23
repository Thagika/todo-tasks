import { render, screen, fireEvent } from '@testing-library/react';
import AddTask from '../components/AddTask';

describe('AddTask Component', () => {
  const mockOnTaskAdded = jest.fn();

  beforeEach(() => {
    render(<AddTask onTaskAdded={mockOnTaskAdded} />);
  });

  it('renders input fields and button', () => {
    expect(screen.getByPlaceholderText('Title')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Description')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /add/i })).toBeInTheDocument();
  });

  it('allows user to type in input fields', () => {
    fireEvent.change(screen.getByPlaceholderText('Title'), { target: { value: 'Test Task' } });
    fireEvent.change(screen.getByPlaceholderText('Description'), { target: { value: 'Task description' } });

    expect(screen.getByPlaceholderText('Title').value).toBe('Test Task');
    expect(screen.getByPlaceholderText('Description').value).toBe('Task description');
  });

  it('calls onTaskAdded when form is submitted', () => {
    fireEvent.change(screen.getByPlaceholderText('Title'), { target: { value: 'Test Task' } });
    fireEvent.change(screen.getByPlaceholderText('Description'), { target: { value: 'Task description' } });

    fireEvent.click(screen.getByRole('button', { name: /add/i }));

    expect(mockOnTaskAdded).toHaveBeenCalledTimes(1);
  });

  it('displays error message on failed task addition', async () => {
    // Mock the fetch to fail
    global.fetch = jest.fn(() =>
      Promise.reject(new Error('Failed to add task'))
    );

    fireEvent.change(screen.getByPlaceholderText('Title'), { target: { value: 'Test Task' } });
    fireEvent.change(screen.getByPlaceholderText('Description'), { target: { value: 'Task description' } });
    fireEvent.click(screen.getByRole('button', { name: /add/i }));

    expect(await screen.findByText('Failed to add task')).toBeInTheDocument();
  });
});
