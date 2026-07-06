import { TaskResponse } from '../api/taskApi'
import TaskItem from './TaskItem'

interface TaskListProps {
  tasks: TaskResponse[]
  onToggle: (id: number) => Promise<void>
  onEdit: (task: TaskResponse) => void
  onDelete: (id: number) => Promise<void>
}

export default function TaskList({
  tasks,
  onToggle,
  onEdit,
  onDelete,
}: TaskListProps) {
  return (
    <div className="space-y-3">
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggle}
          onEdit={onEdit}
          onDelete={onDelete}
        />
      ))}
    </div>
  )
}
