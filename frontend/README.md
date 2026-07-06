# Todo List Frontend

Frontend của ứng dụng Todo List được xây dựng bằng React + TypeScript và Tailwind CSS.

## 1. Mục tiêu của frontend

Giao diện cho phép người dùng:
- Đăng nhập và đăng ký tài khoản
- Xem danh sách công việc
- Thêm công việc mới
- Chỉnh sửa công việc
- Xóa công việc
- Đánh dấu hoàn thành / chưa hoàn thành
- Lọc theo trạng thái
- Phân trang dữ liệu

## 2. Công nghệ sử dụng

- React 18
- TypeScript
- Tailwind CSS
- Vite
- React Router
- Axios
- jwt-decode

## 3. Tính năng đã có

### Xác thực
- Trang đăng nhập
- Trang đăng ký
- Lưu token trong `localStorage`
- Tự động gắn token vào request
- Đăng xuất

### Quản lý task
- Form thêm task mới
- Form sửa task
- Danh sách task dạng card
- Checkbox để đổi trạng thái task
- Xóa task có xác nhận
- Lọc task theo trạng thái `All / Pending / Completed`
- Phân trang
- Hiển thị thống kê tổng task

### Giao diện
- Responsive
- Có trạng thái loading / error
- Dùng Tailwind CSS để dễ bảo trì

## 4. Cấu trúc thư mục chính

```text
frontend/
├── src/
│   ├── api/          # Tầng gọi API
│   ├── components/   # Component tái sử dụng
│   ├── pages/        # Các trang chính
│   ├── utils/        # Hàm tiện ích
│   ├── App.tsx       # Router chính
│   └── main.tsx      # Điểm vào ứng dụng
├── index.html
├── package.json
├── vite.config.ts
├── tailwind.config.js
└── README.md
```

## 5. Cấu hình môi trường

Tạo file `frontend/.env.local`:

```env
VITE_API_URL=http://localhost:8080
```

Biến này là base URL của backend.

## 6. Chạy frontend local không dùng Docker

### Bước 1: Cài dependency

```bash
cd frontend
npm install
```

### Bước 2: Chạy dev server

```bash
npm run dev
```

Frontend mặc định chạy tại:

```text
http://localhost:3000
```

## 7. Build frontend

```bash
cd frontend
npm run build
```

## 8. Chạy frontend bằng Docker

Frontend đã có Dockerfile riêng và được ghép chung trong `docker-compose.yml`.

### Chạy cùng backend

Từ thư mục gốc dự án:

```bash
docker compose build
docker compose up -d
```

Sau đó mở:
- http://localhost:3000

### Dừng container

```bash
docker compose down
```

## 9. Luồng hoạt động

1. Người dùng mở frontend
2. Đăng nhập hoặc đăng ký
3. Frontend nhận token từ backend
4. Token được lưu vào `localStorage`
5. Frontend gọi các API task với token
6. Danh sách task được render theo trang và theo bộ lọc

## 10. Các màn hình chính

- Login
- Register
- Todo Dashboard
- Form thêm / sửa task

## 11. Kết nối với backend

Frontend gọi backend qua `VITE_API_URL`.

Các API chính:
- `/auth`
- `/auth/register`
- `/auth/logout`
- `/auth/refresh`
- `/tasks`
- `/tasks/filter`
- `/tasks/{id}`
- `/tasks/{id}/toggle`
- `/tasks/stats/overview`

## 12. Ghi chú kỹ thuật

- Dùng `axios` interceptor để tự động đính kèm `Authorization`
- Route được bảo vệ bằng logic kiểm tra token
- UI được tối ưu để dễ mở rộng thêm tính năng tìm kiếm hoặc sắp xếp sau này

## 13. Kiểm thử thủ công

Có thể test nhanh bằng cách:
1. Chạy backend
2. Chạy frontend
3. Đăng ký tài khoản mới
4. Tạo task
5. Sửa task
6. Toggle trạng thái
7. Lọc theo trạng thái
8. Xóa task

## 14. Link GitHub

Nếu cần nộp kèm link repository:

```text
https://github.com/<your-account>/<your-repo>
```

## 15. Ghi chú nộp bài

- Đây là frontend riêng của ứng dụng Todo List
- Giao diện đã làm theo hướng đơn giản, rõ ràng, dễ dùng
- Có hỗ trợ Docker nên có thể chạy cùng backend bằng một lệnh
