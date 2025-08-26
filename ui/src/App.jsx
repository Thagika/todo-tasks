import React, { useState, useEffect } from 'react';
import AddTask from './components/AddTask';
import TaskList from './components/TaskList';

const apiUrl = import.meta.env.VITE_TODO_API;

const App = () => {
  const [hasTasks, setHasTasks] = useState(false);
  const [reloadFlag, setReloadFlag] = useState(false);

  useEffect(() => {
    const fetchRecent = async () => {
      try {
        const res = await fetch(`${apiUrl}/api/tasks/recent`);
        if (res.ok) {
          const data = await res.json();
          setHasTasks(data.length > 0);
        }
      } catch (err) {
        console.error('Failed to fetch recent tasks', err);
      }
    };

    fetchRecent();

    const handleTaskAdded = () => {
      setReloadFlag((prev) => !prev);
      setHasTasks(true);
    };
    window.addEventListener('taskAdded', handleTaskAdded);

    return () => window.removeEventListener('taskAdded', handleTaskAdded);
  }, []);

  return (
    <div className="flex mt-[3%] ml-[5%] mr-[5%] w-[95%] h-full overflow-hidden">
      <div className={hasTasks ? "w-[50%] h-full" : "w-full h-full pl-[30%] pr-[30%]"}>
        <AddTask />
      </div>

      {hasTasks && (
        <>
          <div className="border-l-2 border-gray-300 mx-[5%] overflow-hidden" style={{ height: 'auto' }}></div>
          <div className="w-[50%] p-6">
            <TaskList key={reloadFlag} onTasksUpdate={setHasTasks} />
          </div>
        </>
      )}
    </div>
  );
};

export default App;
