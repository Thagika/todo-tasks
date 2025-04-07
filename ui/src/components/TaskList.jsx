import React, { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

const apiUrl = import.meta.env.VITE_TODO_API;

function TaskList() {
  const [tasks, setTasks] = useState([]);
  const [completedTaskId, setCompletedTaskId] = useState(null);
  const [prevTasks, setPrevTasks] = useState([]);

  const fetchTasks = async () => {
    try {
      const res = await fetch(`${apiUrl}/api/tasks`);
      const data = await res.json();
      const incomplete = data.filter((t) => t.isCompleted === 0).sort((a, b) => b.id - a.id);
      const newList = incomplete.slice(0, 5);

      setPrevTasks(tasks); // Store previous list before setting new one
      setTasks(newList);
    } catch (err) {
      console.error("Failed to fetch tasks", err);
    }
  };

  useEffect(() => {
    fetchTasks();

    const handleTaskAdded = () => {
      fetchTasks();
    };

    window.addEventListener("taskAdded", handleTaskAdded);

    return () => window.removeEventListener("taskAdded", handleTaskAdded);
  }, []);

  const toggleDone = async (id) => {
    setCompletedTaskId(id);
    await fetch(`${apiUrl}/api/tasks/${id}/toggle`, { method: "PUT" });
    fetchTasks();
  };

  const getEnterAnimation = (taskId) => {
    if (!prevTasks.length || tasks.length === 0) return { opacity: 1, y: 0 };

    const wasMissingBefore = !prevTasks.some((t) => t.id === taskId);
    if (wasMissingBefore) {
      return {
        opacity: 0,
        y: 100, // from offscreen bottom
      };
    }

    return {
      opacity: 1,
      y: 0,
    };
  };

  return (
    <ul className="space-y-4">
      <AnimatePresence>
        {tasks.map((task, index) => {
          const isCompleted = completedTaskId === task.id;
          return (
            <motion.li
              key={task.id}
              layout
              initial={getEnterAnimation(task.id)}
              animate={isCompleted ? { opacity: 0, scale: 0.9, y: -20 } : { opacity: 1, y: 0 }}
              exit={{ opacity: 0, scale: 0.8, y: -20 }}
              transition={{ duration: 0.4 }}
              onAnimationComplete={() => {
                if (isCompleted) setCompletedTaskId(null);
              }}
              className="bg-[#d6d6d6] rounded-xl p-4 shadow-[0_4px_30px_rgba(0,0,0,0.15)] flex items-center justify-between"
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
          );
        })}
      </AnimatePresence>
    </ul>
  );
}

export default TaskList;
