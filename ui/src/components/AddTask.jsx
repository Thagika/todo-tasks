import { useState } from 'react';
import { motion } from 'framer-motion';

const apiUrl = import.meta.env.VITE_TODO_API;

const AddTask = ({ onTaskAdded }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState(null); // To handle errors
  const [loading, setLoading] = useState(false); // To handle loading state

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); // Set loading state

    try {
      const response = await fetch(`${apiUrl}/api/tasks`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, description }),
      });

      if (!response.ok) {
        throw new Error('Failed to add task');
      }

      setTitle('');
      setDescription('');
      setError(null); // Clear any previous errors

      // Dispatch a custom event when a task is added
      window.dispatchEvent(new Event('taskAdded'));
    } catch (error) {
      setError(error.message); // Set error message
    } finally {
      setLoading(false); // Reset loading state
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mb-6 p-4">
      <h2 className="font-bold text-lg mb-2">Add a Task</h2>
      <input
        type="text"
        placeholder="Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
        className="w-full p-2 border rounded 2xl mb-2"
      />
      <textarea
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        className="w-full p-2 border rounded-2xl mb-2 h-24"
      />
      <div className="flex justify-end">
        <motion.button
          type="submit"
          disabled={loading}
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 w-1/3"
          whileHover={{ scale: 1.05 }} // Scale up on hover
          whileTap={{ scale: 0.95 }} // Scale down on click
        >
          {loading ? 'Adding...' : 'Add'}
        </motion.button>
      </div>
      {error && <p className="text-red-500 mt-2">{error}</p>}
    </form>
  );
};

export default AddTask;
