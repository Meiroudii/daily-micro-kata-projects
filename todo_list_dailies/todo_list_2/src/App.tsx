import { useState, useEffect } from "react";

type Todo = {
  id: number;
  text: string;
  done: boolean;
}

function use_local_storage<T>(key: string, initial_value: T) {
  const [value, set_value] = useState<T>(() => {
    try {
      const saved = localStorage.getItem(key);
      return saved ? JSON.parse(saved) : initial_value;
    } catch {
      return initial_value;
    }
  });

  useEffect(() => {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch {
      alert("storage full");
    }
  }, [key, value]);
  return [value, set_value] as const;
}

export default function App() {
  const [todos, set_todos] = use_local_storage<Todo[]>("todos", []);
  const [text, set_text] = use_local_storage<string>("input", "");
  const add_todo = () => {
    if (!text.trim()) return;
  const new_todo: Todo = { id: Date.now(), text, done: false};
    set_todos([...todos, new_todo]);
    set_text("");
  };

  const toggle_todo = (id: number) => {
    set_todos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, done: !todo.done } : todo
      )
    );
  };

  const remove_todo = (id: number) => {
    set_todos(todos.filter((todo) => todo.id !== id));
  };
  return (
    <>
      <h2>Tasks</h2>
      <input 
        value={text}
        onChange={(e) => set_text(e.target.value)}
        placeholder="Write now..."
      />
      <button onClick={add_todo}>Add</button>
      <ul>
        {todos.map((todo) => (
          <li key={todo.id}>
            <input
            type="checkbox"
            checked={todo.done}
            onChange={() => toggle_todo(todo.id)}
            />
            {todo.done ? <s>{todo.text}</s> : todo.text}
            <button onClick={() => remove_todo(todo.id)}>「X]</button>
          </li>
        ))}
      </ul>
    </>
  );
}
