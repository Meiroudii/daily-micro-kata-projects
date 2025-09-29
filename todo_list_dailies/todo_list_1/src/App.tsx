import { useState, useEffect } from "react";

type Todo = {
  id: number;
  text: string;
  done: boolean;
};

// Hook to sync any state with localStorage
function useLocalStorage<T>(key: string, initialValue: T) {
  const [value, setValue] = useState<T>(() => {
    try {
      const saved = localStorage.getItem(key);
      return saved ? JSON.parse(saved) : initialValue;
    } catch {
      return initialValue;
    }
  });

  useEffect(() => {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch {
      // storage might be full or blocked
    }
  }, [key, value]);

  return [value, setValue] as const;
}

export default function App() {
  const [todos, setTodos] = useLocalStorage<Todo[]>("todos", []);
  const [text, setText] = useLocalStorage<string>("input", ""); // even remembers the input box

  const addTodo = () => {
    if (!text.trim()) return;
    const newTodo: Todo = { id: Date.now(), text, done: false };
    setTodos([...todos, newTodo]);
    setText("");
  };

  const toggleTodo = (id: number) => {
    setTodos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, done: !todo.done } : todo
      )
    );
  };

  const removeTodo = (id: number) => {
    setTodos(todos.filter((todo) => todo.id !== id));
  };

  return (
    <div>
      <h1>Todo List</h1>
      <input
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="Add task..."
      />
      <button onClick={addTodo}>Add</button>
      <ul>
        {todos.map((todo) => (
          <li key={todo.id}>
            <input
              type="checkbox"
              checked={todo.done}
              onChange={() => toggleTodo(todo.id)}
            />
            {todo.done ? <s>{todo.text}</s> : todo.text}
            <button onClick={() => removeTodo(todo.id)}>X</button>
          </li>
        ))}
      </ul>
    </div>
  );
}
