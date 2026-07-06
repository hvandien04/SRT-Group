import { useState } from 'react'

interface TaskFormProps {
  initialTitle?: string
  initialDescription?: string
  onSubmit: (title: string, description: string) => Promise<void>
  onCancel?: () => void
  isEditing?: boolean
}

export default function TaskForm({
  initialTitle = '',
  initialDescription = '',
  onSubmit,
  onCancel,
  isEditing = false,
}: TaskFormProps) {
  const [title, setTitle] = useState(initialTitle)
  const [description, setDescription] = useState(initialDescription)
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!title.trim()) return

    setLoading(true)
    try {
      await onSubmit(title.trim(), description.trim())
      if (!isEditing) {
        setTitle('')
        setDescription('')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="mb-8 rounded-2xl border border-white/80 bg-white/90 p-6 shadow-sm backdrop-blur-sm"
    >
      <div className="mb-4">
        <label className="mb-1.5 block text-sm font-medium text-slate-700">Tiêu đề</label>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Nhập tiêu đề công việc..."
          className="input-field"
          required
        />
      </div>

      <div className="mb-5">
        <label className="mb-1.5 block text-sm font-medium text-slate-700">Mô tả</label>
        <textarea
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Mô tả chi tiết (tùy chọn)..."
          className="input-field resize-none"
          rows={3}
        />
      </div>

      <div className="flex gap-3">
        <button
          type="submit"
          disabled={loading}
          className="rounded-xl bg-gradient-to-r from-indigo-600 to-purple-600 px-6 py-2.5 font-semibold text-white shadow-md transition-all hover:shadow-lg disabled:opacity-60"
        >
          {loading ? 'Đang lưu...' : isEditing ? 'Cập nhật' : 'Thêm công việc'}
        </button>
        {isEditing && (
          <button type="button" onClick={onCancel} className="btn-secondary">
            Hủy
          </button>
        )}
      </div>
    </form>
  )
}
