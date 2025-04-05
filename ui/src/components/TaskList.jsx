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
    fetchTasks(); // Fetch tasks when the component mounts

    const handleTaskAdded = () => {
      fetchTasks(); // Re-fetch tasks when a new task is added
    };

    window.addEventListener("taskAdded", handleTaskAdded); // Listen for task added event

    return () => window.removeEventListener("taskAdded", handleTaskAdded); // Cleanup listener
  }, []);

  const toggleDone = async (id) => {
    await fetch(`http://localhost:8080/api/tasks/${id}/toggle`, {
      method: "PUT",
    });
    fetchTasks(); // Re-fetch tasks after toggling
  };

return (
  <ul className="space-y-4">
    {tasks.map((task) => (
      <li
        key={task.id}
        className="bg-[#d6d6d6] rounded-xl p-4 shadow-[0_4px_30px_rgba(0,0,0,0.15)] rounded-2xl flex items-center justify-between"
      >
        <div>
          <h3 className="font-bold text-lg">{task.title}</h3>
          <p className="text-sm">{task.description}</p>
        </div>
        <button
          onClick={() => toggleDone(task.id)}
          className="border border-gray-600 rounded px-4 py-1 hover:bg-gray-200 text-sm"
        >
          Done
        </button>
      </li>
    ))}
  </ul>
);

}

export default TaskList;
