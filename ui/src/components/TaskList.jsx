import React, { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

const apiUrl = import.meta.env.VITE_TODO_API;

function TaskList({ onTasksUpdate }) {
  const [tasks, setTasks] = useState([]);
  const [completedTaskId, setCompletedTaskId] = useState(null);
  const [prevTasks, setPrevTasks] = useState([]);

  const fetchTasks = async () => {
    try {
      const res = await fetch(`${apiUrl}/api/tasks/recent`);
      if (!res.ok) throw new Error("Failed to fetch tasks");
      const data = await res.json();
      setPrevTasks(tasks);
      setTasks(data);


      if (onTasksUpdate) {
        onTasksUpdate(data.length > 0);
      }
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

  const completeTask = async (id) => {
    setCompletedTaskId(id);
    await fetch(`${apiUrl}/api/tasks/${id}/complete`, { method: "POST" });
    fetchTasks();
  };

  const getEnterAnimation = (taskId) => {
    if (!prevTasks.length || tasks.length === 0) return { opacity: 1, y: 0 };
    const wasMissingBefore = !prevTasks.some((t) => t.id === taskId);
    if (wasMissingBefore) return { opacity: 0, y: 100 };
    return { opacity: 1, y: 0 };
  };

  return (
    <ul className="space-y-4">
      <AnimatePresence>
        {tasks.map((task) => {
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
                onClick={() => completeTask(task.id)}
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
