import React, { useState } from 'react';
import AddTask from './components/AddTask';
import TaskList from './components/TaskList';

const App = () => {
  const [taskAdded, setTaskAdded] = useState(false);

  const handleTaskAdded = () => {
    setTaskAdded(!taskAdded); // To trigger re-fetching in TaskList
  };

  return (
    <div className="flex m-[5%]"> {/* 5% margin on all sides */}
      <div className="w-[50%]"> {/* Left side for AddTask */}
        <AddTask onTaskAdded={handleTaskAdded} />
      </div>

      {/* Vertical Divider */}
      <div className="border-l-2 [0_4px_30px_rgba(0,0,0,0.1)] border-gray-300 mx-[5%]" style={{ height: 'auto' }}></div>

      <div className="w-[50%] p-6"> {/* Right side for TaskList */}
        <TaskList key={taskAdded} />
      </div>
    </div>
  );

};

export default App;
