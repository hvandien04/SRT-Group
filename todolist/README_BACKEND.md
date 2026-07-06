# Todo List Backend

Backend của ứng dụng Todo List được xây dựng bằng Spring Boot 3, có xác thực JWT, PostgreSQL, Redis và tài liệu API bằng Swagger.

## 1. Mục tiêu của backend

Backend cung cấp các API để:
- Đăng ký và đăng nhập người dùng
- Tạo, xem, cập nhật, xóa công việc
- Đánh dấu công việc hoàn thành / chưa hoàn thành
- Lọc công việc theo trạng thái
- Phân trang danh sách công việc
- Trả về response thống nhất, dễ dùng cho frontend

## 2. Công nghệ sử dụng

- Java 21
- Spring Boot 3.5.16
- Spring Security
- OAuth2 Resource Server + JWT
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Swagger / OpenAPI
- Maven

## 3. Tính năng đã có

### Xác thực
- Đăng ký tài khoản
- Đăng nhập
- Làm mới token
- Đăng xuất
- Lưu token bằng cookie HTTP-only

### Quản lý công việc
- Thêm công việc mới
- Lấy danh sách công việc theo trang
- Lọc theo trạng thái `PENDING` hoặc `COMPLETED`
- Xem chi tiết một công việc
- Cập nhật công việc
- Xóa công việc
- Toggle trạng thái hoàn thành / chưa hoàn thành
- Lấy thống kê tổng số task

### Chất lượng dữ liệu
- Validate dữ liệu đầu vào
- Trả lỗi rõ ràng nếu request không hợp lệ
- Chỉ cho phép người dùng thao tác trên dữ liệu của chính họ

## 4. Cấu trúc thư mục chính

```text
src/main/java/com/example/todolist/
├── controller/      # REST controller
├── service/         # Business logic
├── entity/          # JPA entity
├── repository/      # Spring Data JPA repository
├── dto/             # Request / response DTO
├── config/          # Security, JWT, cookie, Redis config
├── exception/       # Mã lỗi nghiệp vụ
├── cache/           # Tương tác Redis
└── wapper/          # API response & exception handler
```

## 5. Cấu hình môi trường

Backend đọc cấu hình từ biến môi trường.

### File `.env`

Tạo file `todolist/.env`:

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

## 6. Chạy backend bằng Docker

Đây là cách được khuyến nghị.

### Bước 1: Chuẩn bị file môi trường

Từ thư mục gốc dự án:

```bash
cp todolist/.env.example todolist/.env
```

Nếu chưa có `.env.example`, có thể tạo theo nội dung ở phần trên.

### Bước 2: Build và chạy

Từ thư mục gốc dự án:

```bash
docker compose build
docker compose up -d
```

### Bước 3: Kiểm tra dịch vụ

- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API docs: http://localhost:8080/v3/api-docs

### Bước 4: Xem log

```bash
docker compose logs -f backend
```

### Bước 5: Dừng hệ thống

```bash
docker compose down
```

Xóa luôn volume dữ liệu nếu cần:

```bash
docker compose down -v
```

## 7. Chạy backend local không dùng Docker

### Yêu cầu
- Java 21
- Maven 3.8+
- PostgreSQL
- Redis

### Chạy

```bash
cd todolist
./mvnw spring-boot:run
```

Backend mặc định chạy tại `http://localhost:8080`.

## 8. Build backend

```bash
cd todolist
./mvnw clean package
```

File jar sẽ nằm trong thư mục `target/`.

## 9. API chính

### Auth
- `POST /auth` – đăng nhập
- `POST /auth/register` – đăng ký
- `POST /auth/logout` – đăng xuất
- `POST /auth/refresh` – làm mới token

### Task
- `GET /tasks` – lấy danh sách task theo trang
- `GET /tasks/filter?status=PENDING` – lọc theo trạng thái
- `GET /tasks/{id}` – xem chi tiết task
- `POST /tasks` – tạo task mới
- `PUT /tasks/{id}` – cập nhật task
- `DELETE /tasks/{id}` – xóa task
- `PATCH /tasks/{id}/toggle` – đổi trạng thái hoàn thành
- `GET /tasks/stats/overview` – thống kê task

## 10. Ví dụ response

```json
{
  "statusCode": 200,
  "message": "Success",
  "timestamp": "2026-07-05T12:00:00Z",
  "data": {
    "id": 1,
    "title": "Học bài",
    "status": "PENDING"
  }
}
```

## 11. Database

### Bảng `users`
- `id`
- `username`
- `password_hash`
- `email`
- `created_at`

### Bảng `tasks`
- `id`
- `user_id`
- `title`
- `description`
- `status`
- `created_at`
- `updated_at`

## 12. Lưu ý về bảo mật

- Mật khẩu được mã hóa bằng BCrypt
- API yêu cầu JWT cho các endpoint cần đăng nhập
- Access token và refresh token được lưu bằng cookie HTTP-only
- CORS đã được cấu hình để frontend gọi API

## 13. Kiểm thử

Hiện tại có thể chạy kiểm tra build bằng:

```bash
cd todolist
./mvnw test
```

## 14. Link GitHub

Nếu nộp kèm GitHub, điền vào đây:

```text
https://github.com/<your-account>/<your-repo>
```

## 15. Ghi chú nộp bài

- Source code đã được tổ chức theo từng lớp rõ ràng
- Có Docker Compose để chạy toàn bộ hệ thống
- Có Swagger để người chấm test nhanh API
- Nếu cần, có thể bổ sung unit test ở bước sau
