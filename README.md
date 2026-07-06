# Todo List Application

Ứng dụng quản lý công việc đầy đủ chức năng gồm backend Spring Boot và frontend React TypeScript + Tailwind CSS.

## 1. Mô tả bài toán

Ứng dụng cho phép:
- Hiển thị danh sách công việc
- Thêm công việc mới
- Chỉnh sửa công việc
- Xóa công việc
- Đánh dấu hoàn thành / chưa hoàn thành
- Lọc công việc theo trạng thái
- Phân trang dữ liệu

## 2. Công nghệ sử dụng

### Backend
- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Swagger/OpenAPI

### Frontend
- React
- TypeScript
- Tailwind CSS
- Vite
- Axios
- React Router

### Hạ tầng
- Docker
- Docker Compose

## 3. Cấu trúc dự án

```text
SRT Group/
├── docker-compose.yml
├── README.md
├── frontend/
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── src/
└── todolist/
    ├── Dockerfile
    ├── pom.xml
    └── src/
```

## 4. Tính năng đã làm

### Backend
- Đăng nhập / đăng ký
- Bảo vệ API bằng JWT
- CRUD công việc
- Toggle trạng thái task
- Lọc theo `PENDING` / `COMPLETED`
- Phân trang
- Validation dữ liệu đầu vào
- API response format thống nhất
- Swagger UI

### Frontend
- Login / Register
- Trang quản lý todo
- Form thêm / sửa task
- Danh sách task dạng card
- Checkbox đổi trạng thái hoàn thành
- Lọc theo trạng thái
- Phân trang
- Responsive UI bằng Tailwind CSS

## 5. Yêu cầu của đề bài và trạng thái

| Yêu cầu | Trạng thái |
|---|---|
| Hiển thị danh sách công việc | Đã làm |
| Thêm công việc mới | Đã làm |
| Chỉnh sửa công việc | Đã làm |
| Xóa công việc | Đã làm |
| Đánh dấu hoàn thành/chưa hoàn thành | Đã làm |
| Tìm kiếm hoặc lọc theo trạng thái | Đã làm |
| Tổ chức mã nguồn rõ ràng | Đã làm |
| Xử lý dữ liệu không hợp lệ | Đã làm |
| Có README hướng dẫn chạy | Đã làm |
| Phân trang hoặc sắp xếp | Đã làm phân trang |
| Responsive giao diện | Đã làm |
| Docker | Đã làm |
| Unit test | Chưa bổ sung |
| Triển khai online | Chưa bổ sung |

## 6. Chạy bằng Docker Compose

Đây là cách khuyến nghị để chạy toàn bộ hệ thống.

### 6.1. Yêu cầu
- Docker
- Docker Compose v2

### 6.2. Chuẩn bị

Tạo file môi trường cho backend nếu chưa có:

```bash
cd todolist
```

Nội dung `.env` mẫu:

```env
POSTGRES_URL=jdbc:postgresql://postgres:5432/srtgroup
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=postgres
PRIVATE_KEY=LS0tLS1CRUdJTiBSU....
PUBLIC_KEY=LS0tLS1CRUdJTiBQVU....
REDIS_ADDR=redis
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_USER=
```

### 6.3. Build và chạy

Từ thư mục gốc của dự án:

```bash
docker compose build
docker compose up -d
```

### 6.4. Truy cập ứng dụng
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

### 6.5. Dừng hệ thống

```bash
docker compose down
```

Nếu muốn xóa luôn dữ liệu PostgreSQL:

```bash
docker compose down -v
```

## 7. Chạy local không dùng Docker

### 7.1. Backend

```bash
cd todolist
./mvnw spring-boot:run
```

Backend mặc định chạy tại `http://localhost:8080`.

### 7.2. Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend mặc định chạy tại `http://localhost:3000`.

## 8. Cấu hình môi trường

### Backend
File: `todolist/.env`

```env
POSTGRES_URL=jdbc:postgresql://postgres:5432/srtgroup
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=postgres
PRIVATE_KEY=LS0tLS1CRUdJTiBSU....
PUBLIC_KEY=LS0tLS1CRUdJTiBQVU....
REDIS_ADDR=redis
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_USER=
```

### Frontend
File: `frontend/.env.local`

```env
VITE_API_URL=http://localhost:8080
```

## 9. API chính

### Auth
- `POST /auth`
- `POST /auth/register`
- `POST /auth/logout`
- `POST /auth/refresh`

### Task
- `GET /tasks`
- `GET /tasks/filter?status=PENDING`
- `GET /tasks/{id}`
- `POST /tasks`
- `PUT /tasks/{id}`
- `DELETE /tasks/{id}`
- `PATCH /tasks/{id}/toggle`
- `GET /tasks/stats/overview`

## 10. Database

### users
- `id`
- `username`
- `password_hash`
- `email`
- `created_at`

### tasks
- `id`
- `user_id`
- `title`
- `description`
- `status`
- `created_at`
- `updated_at`

## 11. Ghi chú khi nộp bài

- Có thể nộp toàn bộ source code theo đúng cấu trúc hiện tại.
- Nếu có GitHub, thêm link repository vào phần mô tả nộp bài.
- Dự án ưu tiên cách tổ chức code rõ ràng, dễ bảo trì và dễ mở rộng.

## 12. Link GitHub


```text
https://github.com/hvandien04/SRT-Group
```

## 13. Kết luận

Dự án đáp ứng các yêu cầu chính của đề bài và đã có hướng dẫn chạy bằng Docker cũng như chạy local.
