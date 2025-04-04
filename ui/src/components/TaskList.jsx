import { useEffect, useState } from 'react';

const TaskList = () => {
  const [tasks, setTasks] = useState([]);

  const fetchTasks = async () => {
    const res = await fetch('http://localhost:8080/api/tasks');
    const data = await res.json();
    setTasks(data.filter(task => !task.completed).slice(0, 5));
  };

  const markAsDone = async (id) => {
    await fetch(`http://localhost:8080/api/tasks/${id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ completed: true }),
    });
    fetchTasks();
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  return (
    <ul>
      {tasks.map(task => (
        <li key={task.id}>
          <strong>{task.title}</strong>: {task.description}
          <button onClick={() => markAsDone(task.id)}>Done</button>
        </li>
      ))}
    </ul>
  );
};

export default TaskList;
