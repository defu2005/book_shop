# Tài liệu API — Learn Spring Boot

Tài liệu này mô tả các endpoint CRUD cho Author, Category và Book.

Base path: `/api`

Tất cả endpoint trả về wrapper chung `ApiResponseDto<T>`:
- `status`: String (ví dụ "200 OK")
- `message`: String
- `response`: Dữ liệu thực tế (T)

## DTO (tóm tắt)

- AuthorRequestDto
  - name: String — required, max 256

- CategoryRequestDto
  - name: String — required, max 256

- BookRequestDto
  - name: String — required, max 256
  - quantity: Integer — optional, must be >= 0
  - price: Integer — optional, must be >= 0
  - authorId: Integer — required
  - categoryIds: Set<Integer> — optional

- AuthorResponseDto
  - id: int
  - name: String

- CategoryResponseDto
  - id: int
  - name: String

- BookResponseDto
  - id: long
  - name: String
  - quantity: Integer
  - price: Integer
  - author: AuthorResponseDto
  - categories: Set<CategoryResponseDto>

## Lỗi phổ biến

- 400 Bad Request: lỗi validate input — trả về `ApiResponseDto` với `response` là danh sách message lỗi.
- 404 Not Found: tài nguyên không tồn tại (ResourceNotFoundException) — trả về `ApiResponseDto` với message mô tả.

---

## Endpoints — Author

### GET /api/authors
- Mô tả: Lấy danh sách tất cả tác giả.
- Response 200: `ApiResponseDto.response` = `List<AuthorResponseDto>`

### GET /api/authors/{id}
- Mô tả: Lấy tác giả theo id.
- Response 200: `ApiResponseDto.response` = `AuthorResponseDto`
- Error 404 nếu không tìm thấy.

### POST /api/authors
- Mô tả: Tạo tác giả mới.
- Request body JSON:

```json
{
  "name": "Jane Austen"
}
```

- Validation: `name` required, max 256
- Response 201: created `AuthorResponseDto`
- Error 400: validation lỗi

### PUT /api/authors/{id}
- Mô tả: Cập nhật tác giả.
- Request body: same as POST
- Response 200: updated `AuthorResponseDto`
- Error 404: nếu id không tồn tại

### DELETE /api/authors/{id}
- Mô tả: Xoá tác giả.
- Response 200: message xác nhận
- Error 404: nếu id không tồn tại

---

## Endpoints — Category

Tương tự Author, đường dẫn base: `/api/categories`

Ví dụ POST body:

```json
{
  "name": "Fiction"
}
```

---

## Endpoints — Book

### GET /api/books
- Mô tả: Lấy danh sách sách (kèm author & categories).
- Response 200: `List<BookResponseDto>`

### GET /api/books/{id}
- Mô tả: Lấy sách theo id.
- Response 200: `BookResponseDto`
- Error 404 nếu không tìm thấy.

### POST /api/books
- Mô tả: Tạo sách mới.
- Request example:

```json
{
  "name": "The Great Gatsby",
  "quantity": 10,
  "price": 150,
  "authorId": 1,
  "categoryIds": [2, 3]
}
```

- Validation:
  - `name`: required, max 256
  - `authorId`: required
  - `quantity`, `price` nếu có phải >= 0
- Response 201: created `BookResponseDto` (kèm nested author/categories)
- Error 400: validation hoặc JSON không hợp lệ
- Error 404: nếu `authorId` hoặc bất kỳ category id nào không tồn tại (ResourceNotFoundException)

### PUT /api/books/{id}
- Mô tả: Cập nhật sách.
- Request body: same shape as POST (các field có thể optional trong update)
- Behavior:
  - cập nhật name/quantity/price nếu có
  - thay author nếu `authorId` được cung cấp
  - thay toàn bộ categories nếu `categoryIds` được cung cấp
- Response 200: updated `BookResponseDto`
- Error 404: nếu book/author/category không tồn tại

### DELETE /api/books/{id}
- Mô tả: Xoá sách.
- Response 200: message xác nhận
- Error 404: nếu id không tồn tại

---

## Ví dụ lỗi

Validation error (400):

```json
{
  "status": "400 BAD_REQUEST",
  "message": "Registration Failed: Please provide valid data.",
  "response": [
    "Book name is required!",
    "Author id is required!"
  ]
}
```

Not found (404):

```json
{
  "status": "404 NOT_FOUND",
  "message": "Author not found with id=5",
  "response": null
}
```

Invalid JSON (400):

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid JSON format: ...",
  "path": "/api/books"
}
```

---

## OpenAPI starter snippet (tuỳ chọn)

Bạn có thể dùng đoạn sau làm khởi đầu cho `openapi.yaml`:

```yaml
openapi: 3.0.3
info:
  title: Learn Spring Boot API
  version: 0.1.0
paths:
  /api/authors:
    get:
      summary: Get all authors
      responses:
        '200':
          description: OK
    post:
      summary: Create author
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/AuthorRequest'
      responses:
        '201': { description: Created }
  /api/authors/{id}:
    parameters:
      - $ref: '#/components/parameters/id'
    get:
      summary: Get author by id
      responses: { '200': { description: OK }, '404': { description: Not Found } }
components:
  parameters:
    id:
      name: id
      in: path
      required: true
      schema:
        type: integer
  schemas:
    AuthorRequest:
      type: object
      properties:
        name:
          type: string
          maxLength: 256
          example: "Jane Austen"
      required: [name]
    AuthorResponse:
      type: object
      properties:
        id: { type: integer }
        name: { type: string }

```

---

## Gợi ý tiếp theo

- Muốn mình tạo file `src/main/resources/openapi.yaml` từ snippet trên không? Hoặc thêm dependency `org.springdoc:springdoc-openapi-starter-webmvc-ui` để có Swagger UI tự động.

Nếu muốn lưu nội dung này vào file khác (ví dụ `docs/API.md`), cho mình biết tên file, mình sẽ di chuyển/đổi tên.

---

Tạo bởi automated assistant — file này chứa toàn bộ tài liệu API CRUD cho Author/Category/Book.
