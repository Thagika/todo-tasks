import React, { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

function TaskList() {
  const [tasks, setTasks] = useState([]);
  const [completedTaskId, setCompletedTaskId] = useState(null); // Track the completed task for animation

  const fetchTasks = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/tasks");
      const data = await res.json();
      const incomplete = data.filter((t) => t.isCompleted === 0);
      setTasks(incomplete.sort((a, b) => b.id - a.id).slice(0, 5)); // Get the latest 5 tasks
    } catch (err) {
      console.error("Failed to fetch tasks", err);
    }
  };

  useEffect(() => {
    fetchTasks();

    const handleTaskAdded = () => {
      fetchTasks(); // Re-fetch tasks when a new task is added
    };

    window.addEventListener("taskAdded", handleTaskAdded);

    return () => window.removeEventListener("taskAdded", handleTaskAdded);
  }, []);

  const toggleDone = async (id) => {
    await fetch(`http://localhost:8080/api/tasks/${id}/toggle`, {
      method: "PUT",
    });
    setCompletedTaskId(id); // Set the completed task ID for animation
    fetchTasks(); // Fetch tasks after toggling
  };

  return (
    <ul className="space-y-4">
      <AnimatePresence>
        {tasks.map((task) => (
        <motion.li
          key={task.id}
          initial={{ opacity: 1, y: 0 }} // Initial state
          animate={
            completedTaskId === task.id
              ? { opacity: 0, y: -20 } // Animation for completed task
              : { opacity: 1, y: 0 } // Animation for remaining tasks
          }
          exit={{ opacity: 0, y: -20 }} // Exit animation for removed task
          transition={{ duration: 0.3 }} // Change this value to adjust speed
          onAnimationComplete={() => {
            if (completedTaskId === task.id) {
              setCompletedTaskId(null); // Reset completed task ID after animation
            }
          }}
          className={`bg-[#d6d6d6] rounded-xl p-4 shadow-[0_4px_30px_rgba(0,0,0,0.15)] flex items-center justify-between`}
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
          </motion.li>
        ))}
      </AnimatePresence>
    </ul>
  );
}

export default TaskList;
