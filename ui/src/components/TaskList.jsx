import React, { useEffect, useState } from "react";

function TaskList() {
  const [tasks, setTasks] = useState([]);

  const fetchTasks = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/tasks");
      const data = await res.json();
      const incomplete = data.filter((t) => t.isCompleted === 0);
      const latest = incomplete.sort((a, b) => b.id - a.id).slice(0, 5);
      setTasks(latest);
    } catch (err) {
      console.error("Failed to fetch tasks", err);
    }
  };

  useEffect(() => {
    fetchTasks();
    window.addEventListener("taskAdded", fetchTasks);
    return () => window.removeEventListener("taskAdded", fetchTasks);
  }, []);

  const toggleDone = async (id) => {
    await fetch(`http://localhost:8080/api/tasks/${id}/toggle`, {
      method: "PUT",
    });
    fetchTasks();
  };

  return (
    <ul className="space-y-2">
      {tasks.map((task) => (
        <li
          key={task.id}
          className="bg-gray-100 p-3 rounded flex justify-between items-center transition-all hover:bg-green-100"
        >
          <div>
            <p className="font-medium">{task.title}</p>
            <p className="text-sm text-gray-600">{task.description}</p>
          </div>
          <button
            className="text-green-600 hover:text-green-800 font-semibold"
            onClick={() => toggleDone(task.id)}
          >
            Done
          </button>
        </li>
      ))}
    </ul>
  );
}

export default TaskList;
