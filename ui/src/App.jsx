import { useState } from 'react';
import AddTask from './components/AddTask';
import TaskList from './components/TaskList';

function App() {
  const [taskAdded, setTaskAdded] = useState(false);

  const handleTaskAdded = () => {
    setTaskAdded(!taskAdded); // Toggle to trigger re-fetch
  };

  return (
    <div className="app">
      <h1>To-Do List</h1>
      <AddTask onTaskAdded={handleTaskAdded} />
      <TaskList key={taskAdded} />
    </div>
  );
}

export default App;
